package mobi.chouette.exchange.netex_stif.importer;

import java.io.IOException;
import java.util.Date;

import javax.naming.InitialContext;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.common.chain.ProgressionCommand;
import mobi.chouette.exchange.CommandCancelledException;
import mobi.chouette.exchange.DaoProgressionCommand;
import mobi.chouette.exchange.ProcessingCommands;
import mobi.chouette.exchange.ProcessingCommandsFactory;
import mobi.chouette.exchange.importer.AbstractImporterCommand;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.ERROR_CODE;
import mobi.chouette.model.util.Referential;

@Log4j
public class NetexStifImporterCommand extends AbstractImporterCommand implements Command {

	public static final String COMMAND = "NetexStifImporterCommand";

	@Override
	public boolean execute(Context context) throws Exception {
		boolean result = Constant.SUCCESS;
		Monitor monitor = MonitorFactory.start(COMMAND);

		InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		Date date = new Date();
		long version = date.getTime();
		context.put(NetexStifConstant.VERSION, version);
		context.put(Constant.REFERENTIAL, new Referential());

		// initialize reporting and progression
		ProgressionCommand progression = (ProgressionCommand) CommandFactory.create(initialContext,
				DaoProgressionCommand.class.getName());

		try {

		Object configuration = context.get(Constant.CONFIGURATION);
		if (!(configuration instanceof NetexStifImportParameters)) {
			// fatal wrong parameters
			log.error("invalid parameters for netex import " + configuration.getClass().getName());
			reporter.setActionError(context, ERROR_CODE.INVALID_PARAMETERS,"invalid parameters for netex import " + configuration.getClass().getName());
			return false;
		}

		ProcessingCommands commands = ProcessingCommandsFactory.create(NetexStifImporterProcessingCommands.class.getName());
		result = process(context, commands, progression, true, Mode.line);

		} catch (CommandCancelledException e) {
			reporter.setActionError(context, ERROR_CODE.INTERNAL_ERROR, "Command cancelled");
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			reporter.setActionError(context, ERROR_CODE.INTERNAL_ERROR,"Fatal :" + e);

		} finally {
			progression.dispose(context);
			log.info(Color.YELLOW + monitor.stop() +(result? " OK":" FAILED")+ Color.NORMAL);
		}

		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = new NetexStifImporterCommand();
			return result;
		}
	}

	static {
		CommandFactory.register(NetexStifImporterCommand.class.getName(), new DefaultCommandFactory());
	}
}
