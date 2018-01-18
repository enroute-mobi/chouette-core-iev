package mobi.chouette.exchange.netex_stif.exporter;

import java.io.IOException;

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
import mobi.chouette.dao.LineDAO;
import mobi.chouette.model.Line;

@Log4j
@Stateless(name = DaoNetexStifLineProducerCommand.COMMAND)
public class DaoNetexStifLineProducerCommand implements Command {

	public static final String COMMAND = "DaoNetexStifLineProducerCommand";
	
	@Resource 
	private SessionContext daoContext;
	
	@EJB 
	private LineDAO lineDAO;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean execute(Context context) throws Exception {

		boolean result = Constant.ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);

		try {

			Long lineId = (Long) context.get(Constant.LINE_ID);
			Line line = lineDAO.find(lineId);
			
			InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
			
			Command export = CommandFactory.create(initialContext, NetexStifLineProducerCommand.class.getName());
			
			context.put(Constant.LINE, line);
			result = export.execute(context);
			//daoContext.setRollbackOnly();
			
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
