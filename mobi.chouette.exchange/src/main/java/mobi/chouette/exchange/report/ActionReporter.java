package mobi.chouette.exchange.report;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.CheckPointReport.SEVERITY;

public interface ActionReporter {

	public enum OBJECT_TYPE {
		NETWORK, COMPANY, STOP_AREA, CONNECTION_LINK, ACCESS_POINT, TIMETABLE, LINE, ROUTE, JOURNEY_PATTERN, VEHICLE_JOURNEY
	}

	public enum FILE_STATE {
		IGNORED, OK, WARNING, ERROR
	}

	public enum ERROR_CODE {
		INVALID_PARAMETERS, NO_DATA_FOUND, NO_DATA_PROCEEDED, NO_DATA_ON_PERIOD, INVALID_DATA, INVALID_FORMAT, INTERNAL_ERROR, WRITE_ERROR
	}

	public enum OBJECT_STATE {
		IGNORED, OK, WARNING, ERROR
	}

	public enum FILE_ERROR_CODE {
		FILE_NOT_FOUND, READ_ERROR, WRITE_ERROR, INVALID_FORMAT, INTERNAL_ERROR
	}

	/**
	 * create an entry for a zip file
	 * 
	 * @param context
	 * @param fileInfoName
	 *            zip file simple name
	 * @param ioType
	 *            access mode
	 */
	void addZipReport(Context context, String fileInfoName, IO_TYPE ioType);

	/**
	 * report an error on zip file
	 * 
	 * @param context
	 * @param fileInfoName
	 *            zip file simple name
	 * @param code
	 *            error code
	 * @param message
	 *            explanation message
	 */
	void addZipErrorInReport(Context context, String fileInfoName, FILE_ERROR_CODE code, String message);

	/**
	 * report an error on zip file with validation code
	 * 
	 * @param context
	 * @param fileInfoName
	 * @param code
	 * @param severity
	 * @return
	 */
	boolean addValidationErrorToZipReport(Context context, String fileInfoName, int code, SEVERITY severity);

	/**
	 * create an entry for a simple file
	 * 
	 * @param context
	 * @param fileInfoName
	 *            simple file name
	 * @param ioType
	 *            access mode
	 */
	void addFileReport(Context context, String fileInfoName, IO_TYPE ioType);

	/**
	 * fix file state
	 * 
	 * @param context
	 * @param fileInfoName
	 *            simple file name
	 * @param ioType
	 *            access mode
	 * @param state
	 *            state to fix
	 */
	void setFileState(Context context, String fileInfoName, IO_TYPE ioType, FILE_STATE state);

	/**
	 * add error on file
	 * 
	 * @param context
	 * @param fileInfoName
	 *            simple file name
	 * @param code
	 *            access mode
	 * @param message
	 *            message
	 */
	void addFileErrorInReport(Context context, String fileInfoName, FILE_ERROR_CODE code, String message);

	/**
	 * affect a validation error on a file
	 * 
	 * @param context
	 * @param fileInfoName
	 * @param code
	 * @param severity
	 * @return
	 */
	boolean addValidationErrorToFileReport(Context context, String fileInfoName, int code, SEVERITY severity);

	/**
	 * set final action error
	 * 
	 * @param context
	 * @param code
	 * @param description
	 */
	void setActionError(Context context, ERROR_CODE code, String description);

	/**
	 * check if action is in error
	 * 
	 * @param context
	 * @return
	 */
	boolean hasActionError(Context context);

	/**
	 * add object entry
	 * 
	 * @param context
	 * @param objectId
	 *            object id
	 * @param type
	 *            objet type
	 * @param description
	 *            object name
	 * @param status
	 *            object initial status
	 * @param ioType
	 *            access mode
	 */
	void addObjectReport(Context context, String objectId, OBJECT_TYPE type, String description, OBJECT_STATE status,
			IO_TYPE ioType);

	/**
	 * add error on object
	 * 
	 * @param context
	 * @param objectId
	 * @param type
	 * @param code
	 * @param descriptionError
	 */
	void addErrorToObjectReport(Context context, String objectId, OBJECT_TYPE type, ERROR_CODE code,
			String descriptionError);

	/**
	 * add validation error on object
	 * 
	 * @param context
	 * @param objectId
	 * @param type
	 * @param code
	 * @return
	 */
	boolean addValidationErrorToObjectReport(Context context, String objectId, OBJECT_TYPE type, int code,
			SEVERITY severity);

	/**
	 * add statistics value for object
	 * 
	 * @param context
	 * @param objectId
	 * @param type
	 * @param statType
	 * @param count
	 *            value to add
	 */
	void addStatToObjectReport(Context context, String objectId, OBJECT_TYPE type, OBJECT_TYPE statType, int count);

	/**
	 * set statistics value for object (force value)
	 * 
	 * @param context
	 * @param objectId
	 * @param type
	 * @param statType
	 * @param count
	 *            value to set
	 */
	void setStatToObjectReport(Context context, String objectId, OBJECT_TYPE type, OBJECT_TYPE statType, int count);

	/**
	 * @param context
	 * @param type
	 * @return
	 */
	boolean hasInfo(Context context, OBJECT_TYPE type);

	/**
	 * @param context
	 * @param filename
	 * @return
	 */
	boolean hasFileValidationErrors(Context context, String filename);

	/**
	 * @param context
	 * @param objectId
	 * @param type
	 * @param state
	 */
	void setObjectStatus(Context context, String objectId, OBJECT_TYPE type, OBJECT_STATE state);

	/**
	 * Factory for using action reporter instance
	 * 
	 * @author gjamot
	 *
	 */
	public class Factory {
		private static ActionReporter actionReporter;

		private Factory() {
		}

		public static synchronized ActionReporter getInstance() {
			if (actionReporter == null) {
				actionReporter = new ActionReporterImpl();
			}

			return actionReporter;
		}
	}

}
