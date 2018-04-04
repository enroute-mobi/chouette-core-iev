package mobi.chouette.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import lombok.Data;
import lombok.ToString;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.JobData;
import mobi.chouette.common.PropertyNames;
import mobi.chouette.exchange.InputValidator;
import mobi.chouette.exchange.InputValidatorFactory;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.model.ActionTask;
import mobi.chouette.model.exporter.ExportTask;
import mobi.chouette.model.importer.ImportTask;

@Log4j
@Data
@ToString(exclude = { "inputValidator" })
public class JobService implements JobData {

	public enum STATUS {
		NEW, PENDING, RUNNING, SUCCESSFUL, WARNING, FAILED, CANCELLED, ABORTED
	}

	private Long id;
	private String inputFilename;
	private String outputFilename;
	private String referential;
	private ACTION action;
	private String type;
	private Date startedAt;
	private Date endedAt;
	private Date updatedAt;

	private AbstractParameter actionParameter;

	private String rootDirectory;

	private InputValidator inputValidator;
	private STATUS status;

	/**
	 * create a jobService on existing job
	 * 
	 * @param job
	 */
	public JobService(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	/**
	 * create a new jobService
	 * 
	 */
	public JobService(String application, String rootDirectory, ActionTask actionTask) throws ServiceException {
		this.rootDirectory = rootDirectory;
		this.id = actionTask.getId();
		if (actionTask.getReferential() == null) {
			throw new RequestServiceException(RequestExceptionCode.INVALID_PARAMETERS, "no referential");
		}
		this.referential = actionTask.getReferential().getSchemaName();
		this.action = actionTask.getAction();
		switch (this.action) {
		case importer:
			initImportJob(actionTask);
			break;
		case exporter:
			initExportJob(actionTask);
			break;
		case validator:
			break;
		default:
			break;
		}
		this.updatedAt = actionTask.getUpdatedAt();
		this.startedAt = actionTask.getStartedAt();
		this.endedAt = actionTask.getEndedAt();
		if (actionTask.getStatus() == null) {
			throw new RequestServiceException(RequestExceptionCode.INVALID_PARAMETERS, "Status is null");
		}
		this.status = STATUS.valueOf(actionTask.getStatus().toUpperCase());

		if (!commandExists()) {
			throw new RequestServiceException(RequestExceptionCode.UNKNOWN_ACTION, getCommandName() + " not found");
		}
		try {
			inputValidator = getCommandInputValidator(action.name(), type);
		} catch (ClassNotFoundException | IOException e) {
			throw new RequestServiceException(RequestExceptionCode.UNKNOWN_ACTION,
					getCommandName() + " has no input validator", e);
		}
		try {
			actionParameter = inputValidator.toActionParameter(actionTask);
		} catch (Exception ex) {
			log.error("problem while reading parameters : " + ex.getMessage(),ex);
			throw new RequestServiceException(RequestExceptionCode.INVALID_PARAMETERS,
					getCommandName() + " cannot read action parameters", ex);
		}
		if (System.getProperty(application + PropertyNames.PROGRESSION_WAIT_BETWEEN_STEPS) != null) {
			long stepWait = Long
					.parseLong(System.getProperty(application + PropertyNames.PROGRESSION_WAIT_BETWEEN_STEPS));
			if (stepWait > 0) {
				actionParameter.setTest(stepWait);
			}
		}
	}

	private void initImportJob(ActionTask actionTask) {
		ImportTask importTask = (ImportTask) actionTask;
		this.type = importTask.getFormat();
		if (this.type == null)
			this.type = "netex_stif";
		if (importTask.getFile() != null && !importTask.getFile().isEmpty()) {
			File f = new File(importTask.getFile());
			this.inputFilename = f.getName();
		} else {
			this.inputFilename = ACTION.importer.name() + ".zip";
		}
	}

	private void initExportJob(ActionTask actionTask) {
		ExportTask exportTask = (ExportTask) actionTask;
		this.type = exportTask.getFormat();
		if (this.type == null)
			this.type = "netex_stif";

		// this.outputFilename = ACTION.exporter.name() + ".zip";

	}

	/**
	 * return job file path <br/>
	 * build it if not set and job saved
	 * 
	 * @return path or null if job not saved
	 */
	public String getPathName() {

		return Paths.get(rootDirectory, Constant.ROOT_PATH, action.name(), id.toString()).toString();
	}

	public static String getRootPathName(String rootDirectory) {
		return Paths.get(rootDirectory, Constant.ROOT_PATH).toString();
	}

	public java.nio.file.Path getPath() {
		return java.nio.file.Paths.get(getPathName());
	}

	private boolean commandExists() {
		try {
			Class.forName(getCommandName());
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	public String getCommandName() {
		String computeType = getType() == null ? "" : getType();
		return "mobi.chouette.exchange." + (computeType.isEmpty() ? "" : computeType + ".") + getAction() + "."
				+ capitalize(computeType) + StringUtils.capitalize(getAction().name()) + "Command";
	}
	
	public boolean notFailed()
	{
		return status.equals(STATUS.SUCCESSFUL) || status.equals(STATUS.WARNING);
	}

	private static String capitalize(String text) {
		return WordUtils.capitalize(text.replaceAll("_", " ")).replaceAll(" ", "");
	}

	public static InputValidator getCommandInputValidator(String actionParam, String typeParam)
			throws ClassNotFoundException, IOException {
		String computeType = typeParam == null ? "" : typeParam;
		return InputValidatorFactory.create("mobi.chouette.exchange." + (computeType.isEmpty() ? "" : computeType + ".")
				+ actionParam + "." + capitalize(computeType) + StringUtils.capitalize(actionParam) + "InputValidator");
	}
}
