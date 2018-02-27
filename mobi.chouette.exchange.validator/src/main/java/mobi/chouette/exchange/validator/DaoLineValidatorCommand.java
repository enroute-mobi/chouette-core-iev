/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */

package mobi.chouette.exchange.validator;

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
import mobi.chouette.dao.FootnoteDAO;
import mobi.chouette.dao.RouteDAO;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.util.NamingUtil;
import mobi.chouette.model.util.Referential;

/**
 *
 */
@Log4j 
@Stateless(name = DaoLineValidatorCommand.COMMAND)
public class DaoLineValidatorCommand implements Command {
	public static final String COMMAND = "DaoLineValidatorCommand";

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
		if (!context.containsKey(Constant.SOURCE))
		{
			context.put(Constant.SOURCE, Constant.SOURCE_DATABASE);
		}
		ActionReporter reporter = ActionReporter.Factory.getInstance();

		try {
			Command lineValidatorCommand = CommandFactory.create(initialContext,
					LineValidatorCommand.class.getName());

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
            List<Footnote> notes = footnoteDAO.findByLineId(lineId);
            notes.forEach(note -> {
            	note.setLineLite(line);
            	r.getFootnotes().put(note.getObjectId(),note);
            });
			
			result = lineValidatorCommand.execute(context);
			daoContext.setRollbackOnly();
			
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
				String name = "java:app/mobi.chouette.exchange.validator/" + COMMAND;
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
		CommandFactory.register(DaoLineValidatorCommand.class.getName(), new DefaultCommandFactory());
	}

}
