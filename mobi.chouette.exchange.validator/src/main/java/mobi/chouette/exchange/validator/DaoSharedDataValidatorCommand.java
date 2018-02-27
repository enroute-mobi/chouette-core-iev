/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */

package mobi.chouette.exchange.validator;

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
import mobi.chouette.dao.TimetableDAO;
import mobi.chouette.exchange.validation.ValidationData;

/**
 *
 */
@Log4j
@Stateless(name = DaoSharedDataValidatorCommand.COMMAND)
public class DaoSharedDataValidatorCommand implements Command {
	public static final String COMMAND = "DaoSharedDataValidatorCommand";

	@Resource
	private SessionContext daoContext;

	@EJB
	private TimetableDAO timetableDAO;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean execute(Context context) throws Exception {
		boolean result = Constant.ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);
		ValidationData data = (ValidationData) context.get(Constant.VALIDATION_DATA);
		InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
		if (!context.containsKey(Constant.SOURCE)) {
			context.put(Constant.SOURCE, Constant.SOURCE_DATABASE);
		}

		try {

			data.getTimetables().clear();
			data.getTimetables().addAll(timetableDAO.findAll());

			Command validateSharedData = CommandFactory.create(initialContext,
					SharedDataValidatorCommand.class.getName());
			result = validateSharedData.execute(context);
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
		CommandFactory.register(DaoSharedDataValidatorCommand.class.getName(), new DefaultCommandFactory());
	}

}
