package mobi.chouette.exchange;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

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
import mobi.chouette.common.JobData.ACTION;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.common.chain.ProgressionCommand;
import mobi.chouette.dao.ActionDAO;
import mobi.chouette.dao.ActionMessageDAO;
import mobi.chouette.dao.ActionResourceDAO;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.report.CheckedReport;
import mobi.chouette.exchange.report.FileReport;
import mobi.chouette.exchange.report.ObjectCollectionReport;
import mobi.chouette.exchange.report.ObjectReport;
import mobi.chouette.exchange.report.ProgressionReport;
import mobi.chouette.exchange.report.Report;
import mobi.chouette.exchange.report.StepProgression;
import mobi.chouette.exchange.report.StepProgression.STEP;
import mobi.chouette.exchange.validation.report.CheckPointErrorReport;
import mobi.chouette.exchange.validation.report.Location;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.ActionMessage;
import mobi.chouette.model.ActionResource;
import mobi.chouette.model.ActionTask;

@Log4j
@Stateless(name = DaoProgressionCommand.COMMAND)
public class DaoProgressionCommand implements ProgressionCommand {

	public static final String COMMAND = "ProgressionCommand";

	@EJB
	ActionDAO actionDAO;

	@EJB
	ActionResourceDAO actionResourceDAO;

	@EJB
	ActionMessageDAO actionMessageDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void initialize(Context context, int stepCount) {
		ProgressionReport report = (ProgressionReport) context.get(Constant.REPORT);
		report.getProgression().setCurrentStep(STEP.INITIALISATION.ordinal() + 1);
		report.getProgression().getSteps().get(STEP.INITIALISATION.ordinal()).setTotal(stepCount);
		saveProgression(context);
		saveReport(context, true);
		saveMainValidationReport(context, true);
	}

	private void saveProgression(Context context) {
		if (context.containsKey(Constant.TESTNG))
			return;
		ProgressionReport report = (ProgressionReport) context.get(Constant.REPORT);
		JobData job = (JobData) context.get(Constant.JOB_DATA);
		ActionTask task = actionDAO.getTask(job);
		task.setCurrentStepId(STEP.values()[report.getProgression().getCurrentStep() - 1].name());
		StepProgression step = report.getProgression().getSteps().get(report.getProgression().getCurrentStep() - 1);
		int count = step.getTotal();
		int value = step.getRealized();
		double currentStepProgress = count > 0 ? (double) value / (double) count : 0L;
		task.setCurrentStepProgress(currentStepProgress);
		task.setUpdatedAt(new Timestamp(new Date().getTime()));
		actionDAO.saveTask(task);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void start(Context context, int stepCount) {
		ProgressionReport report = (ProgressionReport) context.get(Constant.REPORT);
		report.getProgression().setCurrentStep(STEP.PROCESSING.ordinal() + 1);
		report.getProgression().getSteps().get(STEP.PROCESSING.ordinal()).setTotal(stepCount);
		saveProgression(context);
		saveReport(context, true);
		saveMainValidationReport(context, true);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void terminate(Context context, int stepCount) {
		ProgressionReport report = (ProgressionReport) context.get(Constant.REPORT);
		report.getProgression().setCurrentStep(STEP.FINALISATION.ordinal() + 1);
		report.getProgression().getSteps().get(STEP.FINALISATION.ordinal()).setTotal(stepCount);
		saveProgression(context);
		saveReport(context, true);
		saveMainValidationReport(context, true);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void dispose(Context context) {
		saveReport(context, true);
		saveMainValidationReport(context, true);

		Monitor monitor = MonitorFactory.getTimeMonitor("ActionReport");
		if (monitor != null)
			log.info(Color.LIGHT_GREEN + monitor.toString() + Color.NORMAL);
		monitor = MonitorFactory.getTimeMonitor("ValidationReport");
		if (monitor != null)
			log.info(Color.LIGHT_GREEN + monitor.toString() + Color.NORMAL);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void saveReport(Context context, boolean force) {
		if (context.containsKey(Constant.TESTNG))
			return;
		ActionReport report = (ActionReport) context.get(Constant.REPORT);
		ValidationReport valReport = (ValidationReport) context.get(Constant.VALIDATION_REPORT);
		JobData job = (JobData) context.get(Constant.JOB_DATA);
		if (log.isDebugEnabled()) {
			log.debug("report : \n" + report);
			log.debug("validation : \n " + valReport);
		}

		if (valReport != null)
			valReport.computeStats();
		for (FileReport zipReport : report.getZips()) {
			ActionResource actionResource = actionResourceDAO.createResource(job);
			actionResource.setType("zip");
			actionResource.setName(zipReport.getName());
			actionResource.setStatus(zipReport.getStatus().name());
			actionResourceDAO.saveResource(actionResource);
			addMessages(context, zipReport, actionResource);
		}
		report.getZips().clear();
		for (FileReport fileReport : report.getFiles()) {
			ActionResource actionResource = actionResourceDAO.createResource(job);
			actionResource.setType("file");
			actionResource.setName(fileReport.getName());
			actionResource.setStatus(fileReport.getStatus().name());
			if (valReport != null) {
				// just set warning and error messages count !
				actionResource.getMetrics().put("ok_count", "-1");
				actionResource.getMetrics().put("warning_count", Integer.toString(valReport.getWarningCount()));
				actionResource.getMetrics().put("error_count", Integer.toString(valReport.getErrorCount()));
				actionResource.getMetrics().put("uncheck_count", "-1");
			}
			actionResourceDAO.saveResource(actionResource);

			addMessages(context, fileReport, actionResource);
		}
		report.getFiles().clear();

		report.getObjects().clear();

		for (ObjectCollectionReport collection : report.getCollections().values()) {
			for (ObjectReport objectReport : collection.getObjectReports()) {
				ActionResource actionResource = actionResourceDAO.createResource(job);
				actionResource.setType(objectReport.getType().name().toLowerCase());
				actionResource.setName(objectReport.getDescription());
				actionResource.setStatus(objectReport.getStatus().name());
				actionResource.setReference(objectReport.getObjectId());
				for (Entry<OBJECT_TYPE, Integer> entry : objectReport.getStats().entrySet()) {
					actionResource.getMetrics().put(entry.getKey().name().toLowerCase(), entry.getValue().toString());
				}
				if (valReport != null) {
					// just set warning and error messages count !
					actionResource.getMetrics().put("ok_count", "-1");
					actionResource.getMetrics().put("warning_count", Integer.toString(valReport.getWarningCount()));
					actionResource.getMetrics().put("error_count", Integer.toString(valReport.getErrorCount()));
					actionResource.getMetrics().put("uncheck_count", "-1");
				}
				actionResourceDAO.saveResource(actionResource);
				if (actionResource.getAction().equals(ACTION.validator))
					addMessages(context, objectReport, actionResource);
			}
		}
		report.getCollections().clear();
		if (valReport != null) {
			valReport.getCheckPoints().clear();
			valReport.getCheckPointErrors().clear();
			context.put(Constant.VALIDATION_REPORT, new ValidationReport());
		}
	}

	private void addMessages(Context context, CheckedReport report, ActionResource actionResource) {
		ValidationReport valReport = (ValidationReport) context.get(Constant.VALIDATION_REPORT);
		if (valReport == null)
			return;
		if (report.getCheckPointErrorCount() > 0) {
			for (Integer key : report.getCheckPointErrorKeys()) {
				CheckPointErrorReport error = valReport.getCheckPointErrors().get(key.intValue());
				ActionMessage message = actionMessageDAO.createMessage(actionResource);
				message.setCriticity(ActionMessage.CRITICITY.error);
				populateMessage(message, error);
				actionMessageDAO.saveMessage(message);
			}
		}
		if (report.getCheckPointWarningCount() > 0) {
			for (Integer key : report.getCheckPointWarningKeys()) {
				CheckPointErrorReport error = valReport.getCheckPointErrors().get(key.intValue());
				ActionMessage message = actionMessageDAO.createMessage(actionResource);
				message.setCriticity(ActionMessage.CRITICITY.warning);
				populateMessage(message, error);
				actionMessageDAO.saveMessage(message);
			}
		}
	}

	private void populateMessage(ActionMessage message, CheckPointErrorReport error) {
		message.setMessageKey(error.getKey());
		message.setCheckPointId(error.getCheckPointDefId());
		Map<String, String> map = message.getMessageAttributs();
		Map<String, String> mapResource = message.getResourceAttributs();
		map.put("test_id", error.getTestId());
		addLocation(map, "source", error.getSource());
		addLocation(mapResource, "", error.getSource());
		for (int i = 0; i < error.getTargets().size(); i++) {
			addLocation(map, "target_" + i, error.getTargets().get(i));
		}
		map.put("error_value", asString(error.getValue()));
		map.put("reference_value", asString(error.getReferenceValue()));
	}

	private void addLocation(Map<String, String> map, String prefix, Location location) {
		if (!prefix.isEmpty())
			prefix = prefix + "_";
		if (location.getFile() != null) {
			map.put(prefix + "filename", asString(location.getFile().getFilename()));
			map.put(prefix + "line_number", asString(location.getFile().getLineNumber()));
			map.put(prefix + "column_number", asString(location.getFile().getColumnNumber()));
		}
		map.put(prefix + "label", asString(location.getName()));
		map.put(prefix + "objectid", asString(location.getObjectId()));
		if (!location.getObjectRefs().isEmpty()) {
			// TODO save path
		}

	}

	private String asString(Object o) {
		if (o == null)
			return "";
		return o.toString();
	}

	/**
	 * @param context
	 */
	public void saveMainValidationReport(Context context, boolean force) {
		if (context.containsKey(Constant.TESTNG))
			return;

		Report report = (Report) context.get(Constant.VALIDATION_REPORT);
		// ne pas sauver un rapport null ou vide
		if (report == null || report.isEmpty())
			return;
		Date date = new Date();
		Date delay = new Date(date.getTime() - 8000);
		if (force || report.getDate().before(delay)) {
			report.setDate(date);
			Monitor monitor = MonitorFactory.start("ValidationReport");
			JobData jobData = (JobData) context.get(Constant.JOB_DATA);
			Path path = Paths.get(jobData.getPathName(), Constant.VALIDATION_FILE);

			try {
				PrintStream stream = new PrintStream(path.toFile(), "UTF-8");
				report.print(stream);
				stream.close();
			} catch (Exception e) {
				log.error("failed to save validation report", e);
			}
			monitor.stop();
		}

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean execute(Context context) throws Exception {
		boolean result = Constant.SUCCESS;

		ProgressionReport report = (ProgressionReport) context.get(Constant.REPORT);
		StepProgression step = report.getProgression().getSteps().get(report.getProgression().getCurrentStep() - 1);
		step.setRealized(step.getRealized() + 1);
		boolean force = report.getProgression().getCurrentStep() != STEP.PROCESSING.ordinal() + 1;
		saveProgression(context);
		saveReport(context, force);
		if (force && context.containsKey(Constant.VALIDATION_REPORT)) {
			saveMainValidationReport(context, force);
		}
		if (context.containsKey(Constant.CANCEL_ASKED) || Thread.currentThread().isInterrupted()) {
			log.info("Command cancelled");
			throw new CommandCancelledException(Constant.COMMAND_CANCELLED);
		}
		AbstractParameter params = (AbstractParameter) context.get(Constant.CONFIGURATION);
		if (params.getTest() > 0) {
			long time = params.getTest() / 1000;
			log.info(Color.YELLOW + "Mode test on: waiting " + time + " s" + Color.NORMAL);
			Thread.sleep(params.getTest());
		}
		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = null;
			try {
				String name = "java:app/mobi.chouette.exchange/" + COMMAND;
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
		CommandFactory.register(DaoProgressionCommand.class.getName(), new DefaultCommandFactory());
	}
}
