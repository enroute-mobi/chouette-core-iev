package mobi.chouette.exchange.importer;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.naming.InitialContext;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.ChainCommand;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.common.chain.ProgressionCommand;
import mobi.chouette.exchange.ProcessingCommands;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.model.util.Referential;

@Log4j
public class AbstractImporterCommand {

	protected enum Mode {
		line, stopareas
	}

	@SuppressWarnings("unchecked")
	public boolean process(Context context, ProcessingCommands commands, ProgressionCommand progression,
			boolean continueProcesingOnError, Mode mode) throws Exception {
		boolean result = Constant.ERROR;
		boolean disposeResult = Constant.SUCCESS;
		InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
		ActionReporter reporter = ActionReporter.Factory.getInstance();

		try {
			// Initialization
			List<? extends Command> preProcessingCommands = commands.getPreProcessingCommands(context, true);
			progression.initialize(context, preProcessingCommands.size() + 1);
			for (Command importCommand : preProcessingCommands) {
				result = importCommand.execute(context);
				if (!result) {
					if (!reporter.hasActionError(context))
					   reporter.setActionError(context, ActionReporter.ERROR_CODE.NO_DATA_FOUND, "no data to import");
					progression.execute(context);
					return Constant.ERROR;
				}
				progression.execute(context);
			}

			if (mode.equals(Mode.line)) {
				// get lines info
				List<? extends Command> lineProcessingCommands = commands.getLineProcessingCommands(context, true);

				ChainCommand master = (ChainCommand) CommandFactory
						.create(initialContext, ChainCommand.class.getName());
				master.setIgnored(continueProcesingOnError);

				for (Command command : lineProcessingCommands) {
					master.add(progression);
					master.add(command);
				}
				progression.execute(context);

				Referential referential = (Referential) context.get(Constant.REFERENTIAL);
				if (referential != null) {
					referential.clear(true);
				}
				if (!lineProcessingCommands.isEmpty()) {
					progression.start(context, lineProcessingCommands.size());
					if (master.execute(context) == Constant.ERROR && !continueProcesingOnError) {
						return Constant.ERROR;
					}
				}

				// check if CopyCommands ended (with timeout to 5 minutes >
				// transaction timeout)
				if (context.containsKey(Constant.COPY_IN_PROGRESS)) {
					long timeout = 5;
					TimeUnit unit = TimeUnit.MINUTES;
					List<Future<Void>> futures = (List<Future<Void>>) context.get(Constant.COPY_IN_PROGRESS);
					for (Future<Void> future : futures) {
						if (!future.isDone()) {
							log.info("waiting for CopyCommand");
							future.get(timeout, unit);
						}
					}
				}

			} else {
				// get stop info
				List<? extends Command> stopProcessingCommands = commands.getStopAreaProcessingCommands(context, true);
				progression.start(context, stopProcessingCommands.size());
				for (Command command : stopProcessingCommands) {
					result = command.execute(context);
					if (!result) {
						return Constant.ERROR;
					}
					progression.execute(context);
				}

			}
			// post processing
			List<? extends Command> postProcessingCommands = commands.getPostProcessingCommands(context, true);
			if (postProcessingCommands.isEmpty()) {
				progression.terminate(context, 1);
				progression.execute(context);
			} else {
				progression.terminate(context, postProcessingCommands.size());
				for (Command command : postProcessingCommands) {
					result = command.execute(context);
					if (!result) {
						return Constant.ERROR;
					}
					progression.execute(context);
				}
			}

//			if (mode.equals(Mode.line) && !reporter.hasInfo(context, OBJECT_TYPE.LINE)) {
//				if (!reporter.hasActionError(context))
//					reporter.setActionError(context, ActionReporter.ERROR_CODE.NO_DATA_FOUND, "no data");
//			}
		} finally {
			// call dispose commmands
			try {
				List<? extends Command> disposeCommands = commands.getDisposeCommands(context, true);
				log.info("Call dispose command exporter");
				for (Command command : disposeCommands) {
					disposeResult = command.execute(context);
					if (!disposeResult) {
						break;
					}
				}
			} catch (Exception e) {
				log.warn("problem on dispose commands " + e.getMessage());
			}
			context.remove(Constant.CACHE);
		}
		return result ;
	}

}
