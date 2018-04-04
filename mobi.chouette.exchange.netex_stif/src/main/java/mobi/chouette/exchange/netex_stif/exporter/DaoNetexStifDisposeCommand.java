package mobi.chouette.exchange.netex_stif.exporter;

import java.io.IOException;

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
import mobi.chouette.common.JobData;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.ExportTaskDAO;
import mobi.chouette.model.exporter.ExportTask;

@Log4j
@Stateless(name = DaoNetexStifDisposeCommand.COMMAND)
public class DaoNetexStifDisposeCommand implements Command {

	public static final String COMMAND = "DaoNetexStifDisposeCommand";

	@EJB
	private ExportTaskDAO exportDAO;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean execute(Context context) throws Exception {
		// check if test mode test without task managing
		if (context.containsKey(Constant.TESTNG))
			return true;

		boolean result = Constant.ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);

		JobData jobData = (JobData) context.get(Constant.JOB_DATA);
		try {
			if (jobData.getOutputFilename() != null)
			{
				ExportTask task = exportDAO.find(jobData.getId());
				task.setFile(jobData.getOutputFilename());
				exportDAO.update(task);
			}
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
		CommandFactory.register(DaoNetexStifDisposeCommand.class.getName(), new DefaultCommandFactory());
	}
}
