package mobi.chouette.exchange.importer;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.LineDAO;
import mobi.chouette.dao.VehicleJourneyDAO;
import mobi.chouette.exchange.importer.updater.LineOptimiser;
import mobi.chouette.exchange.importer.updater.LineUpdater;
import mobi.chouette.exchange.importer.updater.Updater;
import mobi.chouette.exchange.parameters.AbstractImportParameter;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.ERROR_CODE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Line;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.util.NamingUtil;
import mobi.chouette.model.util.Referential;

@Log4j
@Stateless(name = LineRegisterCommand.COMMAND)
public class LineRegisterCommand implements Command {

	public static final String COMMAND = "LineRegisterCommand";

	@EJB
	private LineOptimiser optimiser;

	@EJB
	private LineDAO lineDAO;

	@EJB
	private VehicleJourneyDAO vehicleJourneyDAO;

	@EJB(beanName = LineUpdater.BEAN_NAME)
	private Updater<Line> lineUpdater;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean execute(Context context) throws Exception {

		boolean result = Constant.ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);

		if (!context.containsKey(Constant.OPTIMIZED)) {
			context.put(Constant.OPTIMIZED, Boolean.TRUE);
		}
		Boolean optimized = (Boolean) context.get(Constant.OPTIMIZED);
		Referential cache = new Referential();
		context.put(Constant.CACHE, cache);

		AbstractImportParameter importParameter = (AbstractImportParameter) context.get(Constant.CONFIGURATION);

		Referential referential = (Referential) context.get(Constant.REFERENTIAL);

		Line newValue = referential.getLines().values().iterator().next();
		
		if(importParameter.isKeepObsoleteLines() || isLineIsValidInFuture(newValue)) {
		
			log.info("register line : " + newValue.getObjectId() + " " + newValue.getName() + " vehicleJourney count = "
					+ referential.getVehicleJourneys().size());
			try {
	
				optimiser.initialize(importParameter,cache, referential);
	
				Line oldValue = cache.getLines().get(newValue.getObjectId());
				lineUpdater.update(context, oldValue, newValue);
				lineDAO.create(oldValue);
				lineDAO.flush(); // to prevent SQL error outside method
	
				if (optimized) {
					Monitor wMonitor = MonitorFactory.start("prepareCopy");
					StringWriter buffer = new StringWriter(1024);
					final List<String> list = new ArrayList<>(referential.getVehicleJourneys().keySet());
					for (VehicleJourney item : referential.getVehicleJourneys().values()) {
						VehicleJourney vehicleJourney = cache.getVehicleJourneys().get(item.getObjectId());
	
						List<VehicleJourneyAtStop> vehicleJourneyAtStops = item.getVehicleJourneyAtStops();
						for (VehicleJourneyAtStop vehicleJourneyAtStop : vehicleJourneyAtStops) {
	
							StopPoint stopPoint = cache.getStopPoints().get(
									vehicleJourneyAtStop.getStopPoint().getObjectId());
	
							write(buffer, vehicleJourney, stopPoint, vehicleJourneyAtStop);
						}
					}
					vehicleJourneyDAO.deleteChildren(list);
					context.put(Constant.BUFFER, buffer.toString());
					wMonitor.stop();
				}
	
				result = Constant.SUCCESS;
			} catch (Exception ex) {
				log.error(ex.getMessage());
				ActionReporter reporter = ActionReporter.Factory.getInstance();
				reporter.addObjectReport(context, newValue.getObjectId(), 
						OBJECT_TYPE.LINE, NamingUtil.getName(newValue), OBJECT_STATE.ERROR, IO_TYPE.INPUT);
				if (ex.getCause() != null) {
					Throwable e = ex.getCause();
					while (e.getCause() != null) {
						log.error(e.getMessage());
						e = e.getCause();
					}
					if (e instanceof SQLException) {
						e = ((SQLException) e).getNextException();
						reporter.addErrorToObjectReport(context, newValue.getObjectId(), OBJECT_TYPE.LINE, ERROR_CODE.WRITE_ERROR,  e.getMessage());
						
					} else {
						reporter.addErrorToObjectReport(context, newValue.getObjectId(), OBJECT_TYPE.LINE, ERROR_CODE.INTERNAL_ERROR,  e.getMessage());
					}
				} else {
					reporter.addErrorToObjectReport(context, newValue.getObjectId(), OBJECT_TYPE.LINE, ERROR_CODE.INTERNAL_ERROR,  ex.getMessage());
				}
				throw ex;
			} finally {
				log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
				
			}
		} else {
			log.info("skipping obsolete line : " + newValue.getObjectId());
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}
		return result;
	}

	private boolean isLineIsValidInFuture(Line line) {

		Date now = new Date();
		
		for(Route r : line.getRoutes()) {
			for(JourneyPattern jp : r.getJourneyPatterns()) {
				for(VehicleJourney vj : jp.getVehicleJourneys()) {
					for(Timetable t : vj.getTimetables()) {
						t.computeLimitOfPeriods();
						log.info("Checking "+t.getEndOfPeriod()+ " against "+now);
						if(!t.getEndOfPeriod().before(now)) {
							return true;
						}
					}
				}
			}
		}
		
		
		return false;
	}
	
	
	protected void write(StringWriter buffer, VehicleJourney vehicleJourney, StopPoint stopPoint,
			VehicleJourneyAtStop vehicleJourneyAtStop) throws IOException {
		// The list of fields to synchronize with
		// VehicleJourneyAtStopUpdater.update(Context context,
		// VehicleJourneyAtStop oldValue,
		// VehicleJourneyAtStop newValue)

		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		buffer.write(vehicleJourney.getId().toString());
		buffer.append(Constant.COPY_SEP);
		buffer.write(stopPoint.getId().toString());
		buffer.append(Constant.COPY_SEP);
		if (vehicleJourneyAtStop.getArrivalTime() != null)
			buffer.write(timeFormat.format(vehicleJourneyAtStop.getArrivalTime()));
		else
			buffer.write(Constant.NULL);
		buffer.append(Constant.COPY_SEP);
		if (vehicleJourneyAtStop.getDepartureTime() != null)
			buffer.write(timeFormat.format(vehicleJourneyAtStop.getDepartureTime()));
		else
			buffer.write(Constant.NULL);
		buffer.append(Constant.COPY_SEP);
		buffer.write(Integer.toString(vehicleJourneyAtStop.getArrivalDayOffset()));
		buffer.append(Constant.COPY_SEP);
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
		CommandFactory.register(LineRegisterCommand.class.getName(), new DefaultCommandFactory());
	}
}
