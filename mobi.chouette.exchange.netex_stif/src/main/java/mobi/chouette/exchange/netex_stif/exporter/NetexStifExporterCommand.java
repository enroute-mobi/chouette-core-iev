package mobi.chouette.exchange.netex_stif.exporter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

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
import mobi.chouette.common.chain.ProgressionCommand;
import mobi.chouette.exchange.CommandCancelledException;
import mobi.chouette.exchange.DaoProgressionCommand;
import mobi.chouette.exchange.ProcessingCommands;
import mobi.chouette.exchange.ProcessingCommandsFactory;
import mobi.chouette.exchange.exporter.AbstractExporterCommand;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.exporter.writer.AbstractWriter;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.ERROR_CODE;
import mobi.chouette.model.CompanyLite;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.util.Referential;

@Log4j
@Stateless(name = NetexStifExporterCommand.COMMAND)
public class NetexStifExporterCommand extends AbstractExporterCommand implements Command {

	public static final String COMMAND = "NetexStifExporterCommand";

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean execute(Context context) throws Exception {
		boolean result = Constant.ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);

		InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
		ActionReporter reporter = ActionReporter.Factory.getInstance();

		// initialize reporting and progression
		ProgressionCommand progression = (ProgressionCommand) CommandFactory.create(initialContext,
				DaoProgressionCommand.class.getName());

		try {

			// read parameters
			Object configuration = context.get(Constant.CONFIGURATION);
			if (!(configuration instanceof NetexStifExportParameters)) {
				// fatal wrong parameters
				log.error("invalid parameters for netex export " + configuration.getClass().getName());
				reporter.setActionError(context, ERROR_CODE.INVALID_PARAMETERS,
						"invalid parameters for netex export " + configuration.getClass().getName());
				return Constant.ERROR;
			}

			NetexStifExportParameters parameters = (NetexStifExportParameters) configuration;
			if (parameters.getStartDate() != null && parameters.getEndDate() != null) {
				if (parameters.getStartDate().after(parameters.getEndDate())) {
					reporter.setActionError(context, ERROR_CODE.INVALID_PARAMETERS, "end date before start date");
					return Constant.ERROR;

				}
			}

			// no validation available for this export
			parameters.setValidateAfterExport(false);

			ProcessingCommands commands = ProcessingCommandsFactory
					.create(NetexStifExporterProcessingCommands.class.getName());

			result = process(context, commands, progression, true, Mode.line);

		} catch (CommandCancelledException e) {
			reporter.setActionError(context, ERROR_CODE.INTERNAL_ERROR, "Command cancelled");
			log.error(e.getMessage());
		} catch (Exception e) {
			if (!Constant.COMMAND_CANCELLED.equals(e.getMessage())) {
				reporter.setActionError(context, ERROR_CODE.INTERNAL_ERROR, "Fatal :" + e);
				log.error(e.getMessage(), e);
			}
		} finally {
			progression.dispose(context);
			log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
		}

		return result;
	}

	@Override
	public boolean process(Context context, ProcessingCommands commands, ProgressionCommand progression,
			boolean continueLineProcesingOnError, Mode mode) throws Exception {
		boolean result = Constant.ERROR;
		NetexStifExportParameters parameters = (NetexStifExportParameters) context.get(Constant.CONFIGURATION);
		ActionReporter reporter = ActionReporter.Factory.getInstance();

		result = processInit(context, commands, progression, mode);

		if (mode.equals(Mode.line)) {
			// get lines
			String type = parameters.getReferencesType();
			// set default type
			if (type == null || type.isEmpty()) {
				// all lines
				type = "line";
				parameters.setIds(null);
			}
			type = type.toLowerCase();

			List<Long> ids = new ArrayList<>();
			if (parameters.getIds() != null) {
				ids = new ArrayList<>(parameters.getIds());
			}

			if (ids.size() == 1) {
				result = processOneLine(context, commands, progression, continueLineProcesingOnError, ids);
			} else {

				Map<Long, Set<Long>> linesByOp = getReader().loadLinesByCompanies(parameters.getLineReferentialId(),
						ids);
				if (linesByOp.isEmpty()) {
					reporter.setActionError(context, ActionReporter.ERROR_CODE.NO_DATA_FOUND, "no data selected");
					return Constant.ERROR;
				}
				// set exportFileName
				JobData jobData = (JobData) context.get(Constant.JOB_DATA);
				SimpleDateFormat format = new SimpleDateFormat(AbstractWriter.ID_DATE);
				jobData.setOutputFilename("OFFRE_STIF_" + format.format(parameters.getStartDate()) + "_"
						+ format.format(parameters.getEndDate()) + ".zip");

				progression.execute(context);

				// process lines loop
				Referential r = (Referential) context.get(Constant.REFERENTIAL);
				// cannot use lambda cause may be return in loop
				int size = linesByOp.values().stream().collect(Collectors.summingInt(Set::size));
				progression.start(context, size + 2 * linesByOp.keySet().size());
				
				for (Long opId : linesByOp.keySet()) {

					ExportableData collection = (ExportableData) context.computeIfAbsent(Constant.EXPORTABLE_DATA,
							c -> new ExportableData());
					// TODO manage validity period ; reset after each operator
					collection.addPeriods(parameters.getValidityPeriods());

					// create Company directory
					CompanyLite company = r.findCompany(opId);
					String companyName = company.getName().replaceAll("[^\\w-]", "_");
					String rootDirectory = jobData.getPathName();
					String dirName = "OFFRE_" + company.objectIdSuffix() + "_" + companyName;
					context.put(NetexStifConstant.OUTPUT_SUB_PATH, dirName);
					Path path = Paths.get(rootDirectory, Constant.OUTPUT, dirName);
					if (!Files.exists(path)) {
						try {
							Files.createDirectories(path);
						} catch (IOException ex) {
							reporter.setActionError(context, ActionReporter.ERROR_CODE.INTERNAL_ERROR,
									"cannot create working directory");
							return Constant.ERROR;
						}
					}

					// process lines for directory
					Set<Long> lines = linesByOp.get(opId);
					result = processAllLines(context, commands, progression,
					   continueLineProcesingOnError, lines);

					// process post line data
					result = processPostLines(context, (NetexStifExporterProcessingCommands)commands, progression);

				}
				// end of loop : process lines and stopareas
				context.remove(NetexStifConstant.OUTPUT_SUB_PATH);
			}
		} else // stopareas
		{
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
		result = processTermination(context, commands, progression);
		return result;
	}
	
	protected boolean processPostLines(Context context, NetexStifExporterProcessingCommands commands, ProgressionCommand progression) throws Exception {
		boolean result = Constant.SUCCESS;
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		List<? extends Command> postProcessingCommands = commands.getPostLineProcessingCommands(context, true);
		for (Command exportCommand : postProcessingCommands) {
			result = exportCommand.execute(context);
			if (!result) {
				if (!reporter.hasActionError(context))
					reporter.setActionError(context, ActionReporter.ERROR_CODE.NO_DATA_PROCEEDED, "no data exported");
				return Constant.ERROR;
			}
			progression.execute(context);
		}
		return result;
	}

	
	protected boolean processAllLines(Context context, ProcessingCommands commands, ProgressionCommand progression,
			boolean continueLineProcesingOnError, Set<Long> lines) throws Exception {
		boolean result = Constant.SUCCESS;
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		List<? extends Command> lineProcessingCommands = commands.getLineProcessingCommands(context, true);
		int lineCount = 0;
		// export each line
		for (Long line : lines) {
			context.put(Constant.LINE_ID, line);
			boolean exportFailed = false;
			for (Command exportCommand : lineProcessingCommands) {
				result = exportCommand.execute(context);
				if (!result) {
					exportFailed = true;
					break;
				}
			}
			progression.execute(context);
			if (!exportFailed) {
				lineCount++;
			} else if (!continueLineProcesingOnError) {
				reporter.setActionError(context, ActionReporter.ERROR_CODE.INVALID_DATA, "unable to export data");
				return Constant.ERROR;
			}
		}
		// check if data where exported
		if (lineCount == 0) {
			progression.terminate(context, 1);
			reporter.setActionError(context, ActionReporter.ERROR_CODE.NO_DATA_PROCEEDED, "no data exported");
			progression.execute(context);
			return Constant.ERROR;
		}
		return result;
	}


	protected boolean processOneLine(Context context, ProcessingCommands commands, ProgressionCommand progression,
			boolean continueLineProcesingOnError, List<Long> ids)
			throws Exception {
		boolean result;
		NetexStifExportParameters parameters = (NetexStifExportParameters) context.get(Constant.CONFIGURATION);
		// process one line
		Set<Long> lines = new HashSet<>(ids);

		ExportableData collection = (ExportableData) context.computeIfAbsent(Constant.EXPORTABLE_DATA,
				c -> new ExportableData());
		// TODO manage validity period
		collection.addPeriods(parameters.getValidityPeriods());
		JobData jobData = (JobData) context.get(Constant.JOB_DATA);
		LineLite line = collection.getLineLite();
		String lineName = line.getName().replaceAll("[^\\w-]", "_");
		SimpleDateFormat format = new SimpleDateFormat(AbstractWriter.ID_DATE_TIME_UTC);
		format.setTimeZone(TimeZone.getTimeZone(AbstractWriter.UTC));
		jobData.setOutputFilename(
				"OFFRE_LIGNE_" + line.getNumber() + "_" + lineName + "_" + format.format(new Date()) + ".zip");

		progression.execute(context);
		result = processLines(context, commands, progression, continueLineProcesingOnError, lines);
		
		result = processPostLines(context, (NetexStifExporterProcessingCommands) commands, progression);
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
		CommandFactory.register(NetexStifExporterCommand.class.getName(), new DefaultCommandFactory());
	}
}
