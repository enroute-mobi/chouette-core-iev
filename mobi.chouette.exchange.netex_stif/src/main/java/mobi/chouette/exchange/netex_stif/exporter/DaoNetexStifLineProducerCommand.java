package mobi.chouette.exchange.netex_stif.exporter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
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
import mobi.chouette.dao.FootnoteDAO;
import mobi.chouette.dao.RouteDAO;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.Referential;

@Log4j
@Stateless(name = DaoNetexStifLineProducerCommand.COMMAND)
public class DaoNetexStifLineProducerCommand implements Command {

	public static final String COMMAND = "DaoNetexStifLineProducerCommand";

	@Resource
	private SessionContext daoContext;

	@EJB
	private RouteDAO routeDAO;
	@EJB
	private FootnoteDAO footnoteDAO;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean execute(Context context) throws Exception {

		boolean result = Constant.ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);

		InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
		try {

			Command lineProducerCommand = CommandFactory.create(initialContext,
					NetexStifLineProducerCommand.class.getName());

			Long lineId = (Long) context.get(Constant.LINE_ID);
			Referential r = (Referential) context.get(Constant.REFERENTIAL);
			r.clear(false);
			LineLite line = r.findLine(lineId);
			r.setCurrentLine(line);
			List<Route> routes = routeDAO.findByLineId(lineId);
			routes.forEach(route -> {
				Map<Long, StopPoint> mappedStops = new HashMap<>();
				route.setLineLite(line);
				r.getRoutes().put(route.getObjectId(), route);
				route.getStopPoints().forEach(sp -> mappedStops.put(sp.getId(), sp));
				route.getRoutingConstraints().forEach(rc -> {
					rc.getStopPoints().clear();
					r.getRoutingConstraints().put(rc.getObjectId(), rc);
					Arrays.stream(rc.getStopPointIds()).forEach(id -> {
						StopPoint sp = mappedStops.get(id);
						rc.getStopPoints().add(sp);
					});
				});
				route.getJourneyPatterns().forEach(jp -> {
					r.getJourneyPatterns().put(jp.getObjectId(), jp);
					jp.getVehicleJourneys().forEach(vj -> r.getVehicleJourneys().put(vj.getObjectId(), vj));
				});
			});
			List<Footnote> notes = footnoteDAO.findByLineId(lineId);
			notes.forEach(note -> {
				note.setLineLite(line);
				r.getFootnotes().put(note.getObjectId(), note);
			});
//			log.info("Routes count = " + r.getRoutes().size());
//			log.info("RoutingConstraints count = " + r.getRoutingConstraints().size());
//			log.info("JourneyPatterns count = " + r.getJourneyPatterns().size());
//			log.info("VehicleJourneys count = " + r.getVehicleJourneys().size());
//			log.info("Footnotes count = " + r.getFootnotes().size());
			result = lineProducerCommand.execute(context);
			daoContext.setRollbackOnly();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}

		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = null;
			try {
				String name = "java:app/mobi.chouette.exchange.netex_stif/" + COMMAND;
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
		CommandFactory.register(DaoNetexStifLineProducerCommand.class.getName(), new DefaultCommandFactory());
	}
}
