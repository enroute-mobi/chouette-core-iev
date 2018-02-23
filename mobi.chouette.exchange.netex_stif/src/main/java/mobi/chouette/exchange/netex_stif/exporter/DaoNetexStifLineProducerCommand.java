package mobi.chouette.exchange.netex_stif.exporter;

import java.io.IOException;
import java.util.List;

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
import mobi.chouette.dao.RouteDAO;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.util.NamingUtil;
import mobi.chouette.model.util.Referential;

@Log4j
@Stateless(name = DaoNetexStifLineProducerCommand.COMMAND)
public class DaoNetexStifLineProducerCommand implements Command {

	public static final String COMMAND = "DaoNetexStifLineProducerCommand";
	
	@Resource 
	private SessionContext daoContext;
	
	@EJB 
	private RouteDAO routeDAO;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean execute(Context context) throws Exception {

		boolean result = Constant.ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);

		InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		try {

			Command lineProducerCommand = CommandFactory.create(initialContext,
					NetexStifLineProducerCommand.class.getName());

			Long lineId = (Long) context.get(Constant.LINE_ID);
			Referential r = (Referential) context.get(Constant.REFERENTIAL);
            r.clear(false);
			LineLite line = r.findLine(lineId);
			r.setCurrentLine(line);
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, NamingUtil.getName(line), OBJECT_STATE.OK, IO_TYPE.INPUT);
			List<Route> routes = routeDAO.findByLineId(lineId);
			routes.forEach(route -> {
				route.setLineLite(line);
				r.getRoutes().put(route.getObjectId(), route);
				route.getRoutingConstraints().forEach(rc -> r.getRoutingConstraints().put(rc.getObjectId(), rc));
				route.getJourneyPatterns().forEach(jp -> {
					r.getJourneyPatterns().put(jp.getObjectId(), jp);
					jp.getVehicleJourneys().forEach(vj -> r.getVehicleJourneys().put(vj.getObjectId(), vj));
				});
			});
			result = lineProducerCommand.execute(context);
			daoContext.setRollbackOnly();
			
		} catch (Exception e) {
			log.error(e.getMessage(),e);
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
