package mobi.chouette.exchange.importer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.RouteDAO;
import mobi.chouette.dao.RoutingConstraintDAO;
import mobi.chouette.dao.TimetableDAO;
import mobi.chouette.exchange.importer.updater.RouteOptimiser;
import mobi.chouette.exchange.importer.updater.RouteUpdater;
import mobi.chouette.exchange.importer.updater.Updater;
import mobi.chouette.exchange.parameters.AbstractImportParameter;
import mobi.chouette.model.Route;
import mobi.chouette.model.RoutingConstraint;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.util.Referential;

@Log4j
@Stateless(name = RouteRegisterCommand.COMMAND)
public class RouteRegisterCommand implements Command {

	public static final String COMMAND = "RouteRegisterCommand";

	@EJB
	private RouteOptimiser optimiser;

	@EJB
	private RouteDAO routeDAO;

	@EJB
	private TimetableDAO timetableDAO;

	@EJB
	private RoutingConstraintDAO routingConstraintDAO;

	@EJB(beanName = RouteUpdater.BEAN_NAME)
	private Updater<Route> routeUpdater;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean execute(Context context) throws Exception {
		// save has been abandoned
		AbstractImportParameter parameters = (AbstractImportParameter) context.get(Constant.CONFIGURATION);
		if (parameters.isNoSave())
			return false;

		if (!context.containsKey(Constant.OPTIMIZED)) {
			context.put(Constant.OPTIMIZED, Boolean.TRUE);
		}
		Referential referential = (Referential) context.get(Constant.REFERENTIAL);

		if (referential.getRoutes().isEmpty()) {
			log.info("no data to save");
			return Constant.SUCCESS;
		}
		Referential cache = new Referential();
		context.put(Constant.CACHE, cache);

		boolean optimized = (Boolean) context.get(Constant.OPTIMIZED);
		optimiser.initialize(cache, referential);

		for (Route newValue : referential.getRoutes().values()) {
			Route oldValue = cache.getRoutes().get(newValue.getObjectId());
			routeUpdater.update(context, oldValue, newValue);
			routeDAO.create(oldValue);
		}

		Map<String, RoutingConstraint> routingConstraints = referential.getRoutingConstraints();
		Iterator<RoutingConstraint> iterator2 = routingConstraints.values().iterator();
		while (iterator2.hasNext()) {
			RoutingConstraint routingConstraint = iterator2.next();

			List<StopPoint> stopPoints = routingConstraint.getStopPoints();
			Long ids[] = new Long[stopPoints.size()];
			int c = 0;
			for (StopPoint stopPoint : stopPoints) {
				StopPoint cached = cache.getStopPoints().get(stopPoint.getObjectId());
				ids[c++] = cached.getId();
			}
			routingConstraint.setStopPointIds(ids);
			routingConstraintDAO.update(routingConstraint);
			routingConstraintDAO.flush();
		}

		if (optimized) {
			// prepare copy for saved vjas
			StringBuilder buffer = new StringBuilder(1024);
			for (VehicleJourney item : referential.getVehicleJourneys().values()) {
				VehicleJourney vehicleJourney = cache.getVehicleJourneys().get(item.getObjectId());

				List<VehicleJourneyAtStop> vehicleJourneyAtStops = item.getVehicleJourneyAtStops();
				for (VehicleJourneyAtStop vehicleJourneyAtStop : vehicleJourneyAtStops) {

					StopPoint stopPoint = cache.getStopPoints().get(vehicleJourneyAtStop.getStopPoint().getObjectId());

					write(buffer, vehicleJourney, stopPoint, vehicleJourneyAtStop);
				}
			}
			context.put(Constant.BUFFER, buffer.toString());
			// log.warn(context.get(Constant.BUFFER));
		}

		return true;
	}

	protected void write(StringBuilder buffer, VehicleJourney vehicleJourney, StopPoint stopPoint,
			VehicleJourneyAtStop vehicleJourneyAtStop) throws IOException {

		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		buffer.append(vehicleJourney.getId().toString());
		buffer.append(Constant.COPY_SEP);
		buffer.append(stopPoint.getId().toString());
		buffer.append(Constant.COPY_SEP);
		if (vehicleJourneyAtStop.getArrivalTime() != null)
			buffer.append(timeFormat.format(vehicleJourneyAtStop.getArrivalTime()));
		else
			buffer.append(Constant.NULL);
		buffer.append(Constant.COPY_SEP);
		if (vehicleJourneyAtStop.getDepartureTime() != null)
			buffer.append(timeFormat.format(vehicleJourneyAtStop.getDepartureTime()));
		else
			buffer.append(Constant.NULL);
		buffer.append(Constant.COPY_SEP);
		buffer.append(Integer.toString(vehicleJourneyAtStop.getArrivalDayOffset()));
		buffer.append(Constant.COPY_SEP);
		buffer.append(Integer.toString(vehicleJourneyAtStop.getDepartureDayOffset()));
		buffer.append(Constant.COPY_SEP);
		buffer.append(vehicleJourneyAtStop.getChecksum());
		buffer.append(Constant.COPY_SEP);
		buffer.append(vehicleJourneyAtStop.getChecksumSource());

		buffer.append('\n');

	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = null;
			try {
				String name = "java:app/mobi.chouette.exchange/" + COMMAND;
				result = (Command) context.lookup(name);
			} catch (NamingException e) {
				// try another way on test context
				String name = "java:module/" + COMMAND;
				try {
					result = (Command) context.lookup(name);
				} catch (NamingException e1) {
					log.error(e);
				}
			}
			return result;
		}
	}

	static {
		CommandFactory.register(RouteRegisterCommand.class.getName(), new DefaultCommandFactory());
	}
}
