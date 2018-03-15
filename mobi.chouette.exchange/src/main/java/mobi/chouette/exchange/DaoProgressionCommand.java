package mobi.chouette.exchange;

import java.io.IOException;
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

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean execute(Context context) throws Exception {
		boolean result = Constant.SUCCESS;

		ProgressionReport report = (ProgressionReport) context.get(Constant.REPORT);
		StepProgression step = report.getProgression().getSteps().get(report.getProgression().getCurrentStep() - 1);
		step.setRealized(step.getRealized() + 1);
		// boolean force = report.getProgression().getCurrentStep() !=
		// STEP.PROCESSING.ordinal() + 1;
		saveProgression(context);
		saveReport(context);
		// if (force && context.containsKey(Constant.VALIDATION_REPORT)) {
		// saveMainValidationReport(context, force);
		// }
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

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void initialize(Context context, int stepCount) {
		ProgressionReport report = (ProgressionReport) context.get(Constant.REPORT);
		report.getProgression().setCurrentStep(STEP.INITIALISATION.ordinal() + 1);
		report.getProgression().getSteps().get(STEP.INITIALISATION.ordinal()).setTotal(stepCount);
		saveProgression(context);
		saveReport(context);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void start(Context context, int stepCount) {
		ProgressionReport report = (ProgressionReport) context.get(Constant.REPORT);
		report.getProgression().setCurrentStep(STEP.PROCESSING.ordinal() + 1);
		report.getProgression().getSteps().get(STEP.PROCESSING.ordinal()).setTotal(stepCount);
		saveProgression(context);
		saveReport(context);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void terminate(Context context, int stepCount) {
		ProgressionReport report = (ProgressionReport) context.get(Constant.REPORT);
		report.getProgression().setCurrentStep(STEP.FINALISATION.ordinal() + 1);
		report.getProgression().getSteps().get(STEP.FINALISATION.ordinal()).setTotal(stepCount);
		saveProgression(context);
		saveReport(context);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void dispose(Context context) {
		saveReport(context);

		Monitor monitor = MonitorFactory.getTimeMonitor("ActionReport");
		if (monitor != null)
			log.info(Color.LIGHT_GREEN + monitor.toString() + Color.NORMAL);
		monitor = MonitorFactory.getTimeMonitor("ValidationReport");
		if (monitor != null)
			log.info(Color.LIGHT_GREEN + monitor.toString() + Color.NORMAL);

	}

	/**
	 * @param context
	 */
	private void saveReport(Context context) {
		if (context.containsKey(Constant.LOG_REPORTS)) {
			// log information for tests
			logReport(context);
		}
		if (context.containsKey(Constant.TESTNG)) {
			// don't save reports
			return;
		}
		ActionReport report = (ActionReport) context.get(Constant.REPORT);
		ValidationReport valReport = (ValidationReport) context.get(Constant.VALIDATION_REPORT);
		JobData job = (JobData) context.get(Constant.JOB_DATA);
		AbstractParameter parameters = (AbstractParameter) context.get(Constant.CONFIGURATION);
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
			addMessages(context, zipReport, actionResource, parameters.getReferentialId());
		}
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

			addMessages(context, fileReport, actionResource, parameters.getReferentialId());
		}

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
					addMessages(context, objectReport, actionResource, parameters.getReferentialId());
			}
		}
		for (ObjectReport objectReport : report.getObjects().values()) {
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
				addMessages(context, objectReport, actionResource, parameters.getReferentialId());

		}

		report.clear();
		if (valReport != null) {
			valReport.clear();
			context.put(Constant.VALIDATION_REPORT, new ValidationReport());
		}
	}

	/**
	 * @param context
	 */
	private void saveProgression(Context context) {
		ProgressionReport report = (ProgressionReport) context.get(Constant.REPORT);
		StepProgression step = report.getProgression().getSteps().get(report.getProgression().getCurrentStep() - 1);
		int count = step.getTotal();
		int value = step.getRealized();
		double currentStepProgress = count > 0 ? (double) value / (double) count : 0L;
		if (context.containsKey(Constant.LOG_REPORTS)) {
			// log information for tests
			StringBuilder b = new StringBuilder();
			b.append("CurrentStep=").append(STEP.values()[report.getProgression().getCurrentStep() - 1].name());
			b.append(", progress = ").append(currentStepProgress);
			log.info(Color.BROWN + b.toString() + Color.NORMAL);
		}
		if (context.containsKey(Constant.TESTNG)) {
			// don't save progression
			return;
		}
		JobData job = (JobData) context.get(Constant.JOB_DATA);
		ActionTask task = actionDAO.getTask(job);
		task.setCurrentStepId(STEP.values()[report.getProgression().getCurrentStep() - 1].name());
		task.setCurrentStepProgress(currentStepProgress);
		task.setUpdatedAt(new Timestamp(new Date().getTime()));
		actionDAO.saveTask(task);
	}

	/**
	 * @param context
	 * @param report
	 * @param actionResource
	 * @param referentialId
	 */
	private void addMessages(Context context, CheckedReport report, ActionResource actionResource, Long referentialId) {
		ValidationReport valReport = (ValidationReport) context.get(Constant.VALIDATION_REPORT);
		if (valReport == null)
			return;
		if (report.getCheckPointErrorCount() > 0) {
			for (Integer key : report.getCheckPointErrorKeys()) {
				CheckPointErrorReport error = valReport.getCheckPointErrors().get(key.intValue());
				ActionMessage message = actionMessageDAO.createMessage(actionResource);
				message.setCriticity(ActionMessage.CRITICITY.error);
				populateMessage(message, error, referentialId);
				actionMessageDAO.saveMessage(message);
			}
		}
		if (report.getCheckPointWarningCount() > 0) {
			for (Integer key : report.getCheckPointWarningKeys()) {
				CheckPointErrorReport error = valReport.getCheckPointErrors().get(key.intValue());
				ActionMessage message = actionMessageDAO.createMessage(actionResource);
				message.setCriticity(ActionMessage.CRITICITY.warning);
				populateMessage(message, error, referentialId);
				actionMessageDAO.saveMessage(message);
			}
		}
	}

	/**
	 * @param message
	 * @param error
	 * @param referentialId
	 */
	private void populateMessage(ActionMessage message, CheckPointErrorReport error, Long referentialId) {
		message.setMessageKey(error.getKey());
		message.setCheckPointId(error.getCheckPointDefId());
		Map<String, String> map = message.getMessageAttributs();
		Map<String, String> mapResource = message.getResourceAttributs();
		map.put("test_id", error.getTestId());
		addLocation(map, "source", error.getSource(), referentialId);
		addLocation(mapResource, "", error.getSource(), referentialId);
		for (int i = 0; i < error.getTargets().size(); i++) {
			addLocation(map, "target_" + i, error.getTargets().get(i), referentialId);
		}
		map.put("error_value", asString(error.getValue()));
		map.put("reference_value", asString(error.getReferenceValue()));
	}

	/**
	 * @param map
	 * @param prefix
	 * @param location
	 * @param referentialId
	 */
	private void addLocation(Map<String, String> map, String prefix, Location location, Long referentialId) {
		if (!prefix.isEmpty())
			prefix = prefix + "_";
		if (location.getFile() != null) {
			map.put(prefix + "filename", asString(location.getFile().getFilename()));
			map.put(prefix + "line_number", asString(location.getFile().getLineNumber()));
			map.put(prefix + "column_number", asString(location.getFile().getColumnNumber()));
		}
		map.put(prefix + "label", asString(location.getName()));
		map.put(prefix + "objectid", asString(location.getObjectId()));
		map.put(prefix + "attribute", asString(location.getAttribute()));
		if (!location.getObjectRefs().isEmpty()) {
			map.put(prefix + "object_path", location.getGuiPath(referentialId));
		}

	}

	/**
	 * @param o
	 * @return
	 */
	private String asString(Object o) {
		if (o == null)
			return "";
		return o.toString();
	}

	/**
	 * called only for testing purpose
	 * 
	 * @param context
	 * @param report
	 * @param referentialId
	 */
	private void logMessages(Context context, CheckedReport report, Long referentialId) {
		ValidationReport valReport = (ValidationReport) context.get(Constant.VALIDATION_REPORT);
		if (valReport == null)
			return;
		if (report.getCheckPointErrorCount() > 0) {
			for (Integer key : report.getCheckPointErrorKeys()) {
				CheckPointErrorReport error = valReport.getCheckPointErrors().get(key.intValue());
				StringBuilder b = new StringBuilder("   ");
				b.append("Criticty = ").append(ActionMessage.CRITICITY.error).append(getMessage(error, referentialId));
				log.info(Color.BROWN + b.toString() + Color.NORMAL);
			}
		}
		if (report.getCheckPointWarningCount() > 0) {
			for (Integer key : report.getCheckPointWarningKeys()) {
				CheckPointErrorReport error = valReport.getCheckPointErrors().get(key.intValue());
				StringBuilder b = new StringBuilder("   ");
				b.append("Criticty = ").append(ActionMessage.CRITICITY.warning)
						.append(getMessage(error, referentialId));
				log.info(Color.BROWN + b.toString() + Color.NORMAL);
			}
		}

	}

	/**
	 * called only for testing purpose
	 * 
	 * @param error
	 * @param referentialId
	 * @return
	 */
	private String getMessage(CheckPointErrorReport error, Long referentialId) {
		StringBuilder b = new StringBuilder("msg=");
		b.append(error.getKey());
		b.append(", CheckPointId = ").append(error.getCheckPointDefId());
		b.append(", MessageAttributs (");
		b.append("test_id =").append(error.getTestId());
		b.append(", ").append(getLocation("source", error.getSource(), referentialId));
		for (int i = 0; i < error.getTargets().size(); i++) {
			b.append(", ").append(getLocation("target_" + i, error.getTargets().get(i), referentialId));
		}
		b.append(", error_value :").append(asString(error.getValue()));
		b.append(", reference_value :").append(asString(error.getReferenceValue()));
		b.append("), ResourceAttributs (").append(getLocation("", error.getSource(), referentialId)).append(")");
		return b.toString();
	}

	/**
	 * called only for testing purpose
	 * 
	 * @param prefix
	 * @param location
	 * @param referentialId
	 * @return
	 */
	private String getLocation(String prefix, Location location, Long referentialId) {
		StringBuilder b = new StringBuilder();
		if (!prefix.isEmpty())
			prefix = prefix + "_";
		if (location.getFile() != null) {
			b.append(prefix).append("filename = ").append(asString(location.getFile().getFilename()));
			b.append(", ").append(prefix).append("line_number = ").append(asString(location.getFile().getLineNumber()));
			b.append(", ").append(prefix).append("column_number = ")
					.append(asString(location.getFile().getColumnNumber())).append(", ");
		}
		b.append(prefix).append("label = ").append(asString(location.getName()));
		b.append(", ").append(prefix).append("objectid = ").append(asString(location.getObjectId()));
		b.append(", ").append(prefix).append("attribute = ").append(asString(location.getAttribute()));
		if (!location.getObjectRefs().isEmpty()) {
			b.append(", ").append(prefix + "object_path = ").append(location.getGuiPath(referentialId));
		}
		return b.toString();
	}

	/**
	 * called only for testing purpose
	 * 
	 * @param context
	 */
	private void logReport(Context context) {
		ActionReport report = (ActionReport) context.get(Constant.REPORT);
		ValidationReport valReport = (ValidationReport) context.get(Constant.VALIDATION_REPORT);
		JobData job = (JobData) context.get(Constant.JOB_DATA);
		AbstractParameter parameters = (AbstractParameter) context.get(Constant.CONFIGURATION);
		if (log.isDebugEnabled()) {
			log.debug("report : \n" + report);
			log.debug("validation : \n " + valReport);
		}

		if (valReport != null)
			valReport.computeStats();
		for (FileReport zipReport : report.getZips()) {
			StringBuilder b = new StringBuilder();
			b.append("type=zip, name = ").append(zipReport.getName()).append(", status=")
					.append(zipReport.getStatus().name());
			log.info(Color.BROWN + b.toString() + Color.NORMAL);
			logMessages(context, zipReport, parameters.getReferentialId());
		}
		for (FileReport fileReport : report.getFiles()) {
			StringBuilder b = new StringBuilder();
			b.append("type=file, name = ").append(fileReport.getName()).append(", status=")
					.append(fileReport.getStatus().name());
			log.info(Color.BROWN + b.toString() + Color.NORMAL);
			if (valReport != null) {
				// just set warning and error messages count !
				b = new StringBuilder("   metrics : (");
				b.append("ok_count = -1, warning_count =").append(valReport.getWarningCount())
						.append(", error_count = ").append(valReport.getErrorCount()).append(", uncheck_count = -1 )");
				log.info(Color.BROWN + b.toString() + Color.NORMAL);
			}
			logMessages(context, fileReport, parameters.getReferentialId());
		}

		for (ObjectCollectionReport collection : report.getCollections().values()) {
			for (ObjectReport objectReport : collection.getObjectReports()) {
				StringBuilder b = new StringBuilder();
				b.append("type=").append(objectReport.getType().name().toLowerCase()).append(", name = ")
						.append(objectReport.getDescription()).append(", status=")
						.append(objectReport.getStatus().name()).append(", reference=")
						.append(objectReport.getObjectId());
				log.info(Color.BROWN + b.toString() + Color.NORMAL);
				// for (Entry<OBJECT_TYPE, Integer> entry :
				// objectReport.getStats().entrySet()) {
				// actionResource.getMetrics().put(entry.getKey().name().toLowerCase(),
				// entry.getValue().toString());
				// }
				if (valReport != null) {
					// just set warning and error messages count !
					b = new StringBuilder("   metrics : (");
					b.append("ok_count = -1, warning_count =").append(valReport.getWarningCount())
							.append(", error_count = ").append(valReport.getErrorCount())
							.append(", uncheck_count = -1 )");
					log.info(Color.BROWN + b.toString() + Color.NORMAL);
				}
				if (job.getAction().equals(ACTION.validator))
					logMessages(context, objectReport, parameters.getReferentialId());
			}
		}
		for (ObjectReport objectReport : report.getObjects().values()) {
			StringBuilder b = new StringBuilder();
			b.append("type=").append(objectReport.getType().name().toLowerCase()).append(", name = ")
					.append(objectReport.getDescription()).append(", status=").append(objectReport.getStatus().name())
					.append(", reference=").append(objectReport.getObjectId());
			log.info(Color.BROWN + b.toString() + Color.NORMAL);
			// for (Entry<OBJECT_TYPE, Integer> entry :
			// objectReport.getStats().entrySet()) {
			// actionResource.getMetrics().put(entry.getKey().name().toLowerCase(),
			// entry.getValue().toString());
			// }
			if (valReport != null) {
				// just set warning and error messages count !
				b = new StringBuilder("   metrics : (");
				b.append("ok_count = -1, warning_count =").append(valReport.getWarningCount())
						.append(", error_count = ").append(valReport.getErrorCount()).append(", uncheck_count = -1 )");
				log.info(Color.BROWN + b.toString() + Color.NORMAL);
			}
			if (job.getAction().equals(ACTION.validator))
				logMessages(context, objectReport, parameters.getReferentialId());

		}

		// report.clear();
		// if (valReport != null) {
		// valReport.clear();
		// context.put(Constant.VALIDATION_REPORT, new ValidationReport());
		// }

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
