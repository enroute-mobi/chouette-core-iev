package mobi.chouette.exchange.netex_stif.exporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
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
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.exchange.report.ObjectReport;
import mobi.chouette.exchange.report.ActionReporter.ERROR_CODE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.model.CompanyLite;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.type.DateRange;
import mobi.chouette.model.util.NamingUtil;
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
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		actionReport.getCollectionTypes().add(ActionReporter.OBJECT_TYPE.COMPANY);

		// initialize reporting and progression
		ProgressionCommand progression = (ProgressionCommand) CommandFactory.create(initialContext,
				DaoProgressionCommand.class.getName());

		try {

			// read parameters
			Object configuration = context.get(Constant.CONFIGURATION);
			if (!(configuration instanceof NetexStifExportParameters)) {
				// fatal wrong parameters
				log.error(Color.RED +"NetexStifExporter : invalid parameters for netex export " + configuration.getClass().getName()+ Color.NORMAL);
				reporter.setActionError(context, ERROR_CODE.INVALID_PARAMETERS,
						"invalid parameters for netex export " + configuration.getClass().getName());
				return Constant.ERROR;
			}

			NetexStifExportParameters parameters = (NetexStifExportParameters) configuration;
			if (parameters.getStartDate() != null && parameters.getEndDate() != null) {
				if (parameters.getStartDate().after(parameters.getEndDate())) {
					log.error(Color.ORANGE +"NetexStifExporter : end date before start date "+ Color.NORMAL);
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
			log.info(Color.ORANGE +"NetexStifExporter : Command cancelled :"+ Color.NORMAL);
			log.error(Color.RED + e.getMessage()+ Color.NORMAL);
			reporter.setActionError(context, ERROR_CODE.INTERNAL_ERROR, "Command cancelled");
		} catch (Exception e) {
			if (!Constant.COMMAND_CANCELLED.equals(e.getMessage())) {
				reporter.setActionError(context, ERROR_CODE.INTERNAL_ERROR, "Fatal :" + e);
				log.error(Color.ORANGE + e.getMessage() + Color.NORMAL, e);
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
		boolean disposeResult = Constant.SUCCESS;

		NetexStifExportParameters parameters = (NetexStifExportParameters) context.get(Constant.CONFIGURATION);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		try {
		result = processInit(context, commands, progression, mode);

		if (mode.equals(Mode.line)) {
			// get lines
			String type = parameters.getReferencesType();
			// set default type
			if (type == null || type.isEmpty()) {
				// all lines
				type = "line";
			}
			type = type.toLowerCase();

			List<Long> ids = new ArrayList<>();
			if (parameters.getIds() != null) {
				ids = new ArrayList<>(parameters.getIds());
			}

			if (ids.size() == 1 && parameters.getMode().equals("line")) {
				result = processOneLine(context, commands, progression, continueLineProcesingOnError, ids);
			} else {

				Map<Long, Set<Long>> linesByOp = getReader().loadLinesByCompanies(parameters.getLineReferentialId(),
						ids);
				if (linesByOp.isEmpty()) {
					log.error(Color.MAGENTA + "=== Netex Export Error => loadLinesByCompanies empty result set  ===" + Color.NORMAL);
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
					collection.addPeriods(parameters.getValidityPeriods());
					collection.setGlobalValidityPeriod(new DateRange(new Date(parameters.getStartDate().getTime()), new Date(parameters.getEndDate().getTime())));

					// create Company directory
					CompanyLite company = r.findCompany(opId);
					context.put(NetexStifConstant.OPERATOR_OBJECT_ID, company.getObjectId());

					ObjectReport companyReport = new ObjectReport(company.getObjectId(), OBJECT_TYPE.COMPANY,
							NamingUtil.getName(company), OBJECT_STATE.OK, IO_TYPE.INPUT);
					context.put(NetexStifConstant.SHARED_REPORT, companyReport);

					String companyName = company.getName().replaceAll("[^\\w-]", "_");
					String rootDirectory = jobData.getPathName();
					String dirName = "OFFRE_" + company.objectIdSuffix() + "_" + companyName;
					context.put(NetexStifConstant.OUTPUT_SUB_PATH, dirName);
					Path path = Paths.get(rootDirectory, Constant.OUTPUT, dirName);
					if (!Files.exists(path)) {
						try {
							Files.createDirectories(path);
						} catch (IOException ex) {
							log.error(Color.RED +"NetexStifExporter : Cannot create working directory."+ Color.NORMAL);
							reporter.setActionError(context, ActionReporter.ERROR_CODE.INTERNAL_ERROR,
									"cannot create working directory");
							return Constant.ERROR;
						}
					}

					// process lines for directory
					Set<Long> lines = linesByOp.get(opId);
					result = processAllLines(context, commands, progression, continueLineProcesingOnError, lines);

					// process post line data
					result = processPostLines(context, (NetexStifExporterProcessingCommands) commands, progression);

					// put company report in reporting
					reporter.addObjectReport(context, companyReport.getObjectId(), companyReport.getType(),
							companyReport.getDescription(), companyReport.getStatus(), companyReport.getIoType());
					companyReport.getStats().entrySet().stream().forEach((e) -> {
						reporter.setStatToObjectReport(context, companyReport.getObjectId(), companyReport.getType(),
								e.getKey(), e.getValue());
					});
					context.remove(NetexStifConstant.SHARED_REPORT);
					context.remove(NetexStifConstant.OPERATOR_OBJECT_ID);

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
		return result;
	}

	protected boolean processPostLines(Context context, NetexStifExporterProcessingCommands commands,
			ProgressionCommand progression) throws Exception {
		boolean result = Constant.SUCCESS;
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		List<? extends Command> postProcessingCommands = commands.getPostLineProcessingCommands(context, true);
		for (Command exportCommand : postProcessingCommands) {
			result = exportCommand.execute(context);
			if (!result) {
				if (!reporter.hasActionError(context)) {
					log.error(Color.RED  +"NetexStifExporter -W processPostLines ->  Error processing this postProcessingCommand -> "+exportCommand.toString()+ Color.NORMAL);
					reporter.setActionError(context, ActionReporter.ERROR_CODE.NO_DATA_PROCEEDED, "no data exported");
				}
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
		// int lineCount = 0;
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
				// lineCount++;
			} else if (!continueLineProcesingOnError) {
				log.error(Color.RED +"NetexStifExporter : Unable to export data."+ Color.NORMAL);
				reporter.setActionError(context, ActionReporter.ERROR_CODE.INVALID_DATA, "unable to export data");
				return Constant.ERROR;
			}
		}
		return result;
	}

	protected boolean processOneLine(Context context, ProcessingCommands commands, ProgressionCommand progression,
			boolean continueLineProcesingOnError, List<Long> ids) throws Exception {
		boolean result;
		NetexStifExportParameters parameters = (NetexStifExportParameters) context.get(Constant.CONFIGURATION);
		// process one line
		Set<Long> lines = new HashSet<>(ids);

		JobData jobData = (JobData) context.get(Constant.JOB_DATA);
		Referential r = (Referential) context.get(Constant.REFERENTIAL);
		LineLite line = r.findLine(ids.get(0));
		context.put(NetexStifConstant.LINE_OBJECT_ID, line.getObjectId());
		String lineName = line.getName().replaceAll("[^\\w-]", "_");
		SimpleDateFormat format = new SimpleDateFormat(AbstractWriter.ID_DATE_TIME_UTC);
		format.setTimeZone(TimeZone.getTimeZone(AbstractWriter.UTC));
		jobData.setOutputFilename(
				"OFFRE_LIGNE_" + line.getNumber() + "_" + lineName + "_" + format.format(new java.util.Date()) + ".zip");

		ExportableData collection = (ExportableData) context.computeIfAbsent(Constant.EXPORTABLE_DATA,
				c -> new ExportableData());
		collection.addPeriods(parameters.getValidityPeriods());
		collection.setGlobalValidityPeriod(new DateRange(new Date(parameters.getStartDate().getTime()), new Date(parameters.getEndDate().getTime())));

		progression.execute(context);
		result = processLines(context, commands, progression, continueLineProcesingOnError, lines);

		result = processPostLines(context, (NetexStifExporterProcessingCommands) commands, progression);

		// put line report in reporting
		if (context.containsKey(NetexStifConstant.SHARED_REPORT)) {
			ActionReporter reporter = ActionReporter.Factory.getInstance();
			ObjectReport lineReport = (ObjectReport) context.remove(NetexStifConstant.SHARED_REPORT);
			reporter.addObjectReport(context, lineReport.getObjectId(), lineReport.getType(),
					lineReport.getDescription(), lineReport.getStatus(), lineReport.getIoType());
			lineReport.getStats().entrySet().stream().forEach((e) -> {
				reporter.setStatToObjectReport(context, lineReport.getObjectId(), lineReport.getType(), e.getKey(),
						e.getValue());
			});
		}
		context.remove(NetexStifConstant.LINE_OBJECT_ID);

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
