package mobi.chouette.exchange.importer;

import java.io.IOException;
import java.io.StringWriter;
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
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.RouteDAO;
import mobi.chouette.dao.RoutingConstraintDAO;
import mobi.chouette.dao.TimetableDAO;
import mobi.chouette.exchange.importer.inserter.Inserter;
import mobi.chouette.exchange.importer.inserter.RouteInserter;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Route;
import mobi.chouette.model.RoutingConstraint;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.util.Referential;

@Log4j
@Stateless(name = RouteRegisterCommand.COMMAND)
public class RouteRegisterCommand implements Command {

	public static final String COMMAND = "RouteRegisterCommand";

	@EJB
	private RouteDAO routeDAO;

	@EJB
	private TimetableDAO timetableDAO;

	@EJB
	private RoutingConstraintDAO routingConstraintDAO;

	// @EJB(beanName = RouteInserter.BEAN_NAME)
	// private Inserter<Route> routeInserter;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean execute(Context context) throws Exception {

		if (!context.containsKey(OPTIMIZED)) {
			context.put(OPTIMIZED, Boolean.TRUE);
		}
		Referential cache = new Referential();
		context.put(CACHE, cache);

		Referential referential = (Referential) context.get(REFERENTIAL);
		Map<String, Timetable> timetables = referential.getTimetables();
		for (Timetable timetable : timetables.values()) {
			if (timetable.getId() != null) {
				log.info("timetable " + timetable.getObjectId() + " already saved ");
				Timetable saved = timetableDAO.findByObjectId(timetable.getObjectId());
				if (saved != null) {
					for (VehicleJourney vj : timetable.getVehicleJourneys()) {
						vj.getTimetables().remove(timetable);
						vj.getTimetables().add(saved);
					}
				}
			}
		}
		Map<String, Route> routes = referential.getRoutes();
		Iterator<Route> iterator = routes.values().iterator();
		while (iterator.hasNext()) {
			Route route = iterator.next();
			for (JourneyPattern jp : route.getJourneyPatterns()) {
				if (jp.getStopPoints().size() > 0)
				{
					jp.setDepartureStopPoint(jp.getStopPoints().get(0));
					jp.setArrivalStopPoint(jp.getStopPoints().get(jp.getStopPoints().size()-1));
				}
			}
			routeDAO.create(route);
			routeDAO.flush();
		}
		// RoutingConstraint routingConstraint = (RoutingConstraint) entity;
		Map<String, RoutingConstraint> routingConstraints = referential.getRoutingConstraints();
		log.info("list of rc size : " + routingConstraints.size());
		Iterator<RoutingConstraint> iterator2 = routingConstraints.values().iterator();
		while (iterator2.hasNext()) {
			RoutingConstraint routingConstraint = iterator2.next();

			List<StopPoint> stopPoints = routingConstraint.getStopPoints();
			Long ids[] = new Long[stopPoints.size()];
			log.info("RC nb stop point: " + stopPoints.size());
			int c = 0;
			for (StopPoint stopPoint : stopPoints) {
				ids[c++] = stopPoint.getId();
			}
			routingConstraint.setStopPointIds(ids);
			routingConstraintDAO.update(routingConstraint);
			routingConstraintDAO.flush();
		}

		return true;
	}

	protected void write(StringWriter buffer, VehicleJourney vehicleJourney, StopPoint stopPoint,
			VehicleJourneyAtStop vehicleJourneyAtStop) throws IOException {

		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		buffer.write(vehicleJourney.getId().toString());
		buffer.append(SEP);
		buffer.write(stopPoint.getId().toString());
		buffer.append(SEP);
		if (vehicleJourneyAtStop.getArrivalTime() != null)
			buffer.write(timeFormat.format(vehicleJourneyAtStop.getArrivalTime()));
		else
			buffer.write(NULL);
		buffer.append(SEP);
		if (vehicleJourneyAtStop.getDepartureTime() != null)
			buffer.write(timeFormat.format(vehicleJourneyAtStop.getDepartureTime()));
		else
			buffer.write(NULL);
		buffer.append(SEP);
		buffer.write(Integer.toString(vehicleJourneyAtStop.getArrivalDayOffset()));
		buffer.append(SEP);
		buffer.write(Integer.toString(vehicleJourneyAtStop.getDepartureDayOffset()));

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
		CommandFactory.factories.put(RouteRegisterCommand.class.getName(), new DefaultCommandFactory());
	}
}
