package mobi.chouette.exchange.report;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.CheckPointReport.SEVERITY;
import mobi.chouette.model.LineLite;

public class ActionReporterImpl implements ActionReporter {

	@Override
	public void addZipReport(Context context, String fileInfoName, IO_TYPE ioType) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		FileReport fileReport = actionReport.findZipReport(fileInfoName);

		if (fileReport == null) {
			actionReport.addZipReport(new FileReport(fileInfoName, FILE_STATE.OK, ioType));
		}
	}

	@Override
	public void addZipErrorInReport(Context context, String fileInfoName, FILE_ERROR_CODE code, String message) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		FileReport fileReport = actionReport.findZipReport(fileInfoName);
		// if (fileReport == null)
		// actionReport.setZip(new FileReport(fileInfoName, FILE_STATE.ERROR));

		fileReport.addError(new FileError(code, message));

	}

	@Override
	public void addFileReport(Context context, String fileInfoName, IO_TYPE ioType) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		FileReport fileReport = actionReport.findFileReport(fileInfoName);

		if (fileReport == null) {
			actionReport.addFileReport(new FileReport(fileInfoName, FILE_STATE.OK, ioType));
		}
	}

	@Override
	public void setFileState(Context context, String fileInfoName, IO_TYPE ioType, FILE_STATE state) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		FileReport fileReport = actionReport.findFileReport(fileInfoName);

		if (fileReport == null) {
			actionReport.addFileReport(new FileReport(fileInfoName, state, ioType));
		} else {
			fileReport.setStatus(state);
		}

	}

	@Override
	public void addFileErrorInReport(Context context, String fileInfoName, FILE_ERROR_CODE code, String message) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		FileReport fileReport = actionReport.findFileReport(fileInfoName);

		// if (fileReport == null)
		// actionReport.addFileReport(new FileReport(fileInfoName,
		// FILE_STATE.ERROR));
		//
		fileReport.addError(new FileError(code, message));
		setActionError(context, ERROR_CODE.INVALID_DATA, "File error for " + fileInfoName);

	}

	@Override
	public void setActionError(Context context, ERROR_CODE code, String description) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		ActionError actionError = new ActionError(code, description);

		if (actionReport != null)
			actionReport.setFailure(actionError);

	}

	@Override
	public boolean hasActionError(Context context) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		return actionReport.getFailure() != null;
	}

	@Override
	public void addObjectReport(Context context, String objectId, OBJECT_TYPE type, String description,
			OBJECT_STATE status, IO_TYPE ioType) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		if (actionReport != null) {
			ObjectReport old = actionReport.findObjectReport(objectId, type);
			if (old != null) {
				// objectreport exists, set
				old.setDescription(description);
				// if (old.getStatus().ordinal() < status.ordinal())
				// old.setStatTus(status);
			} else {
				if (actionReport.getCollectionTypes().contains(type)) {
					// type to be reported separatedly in a collection

					actionReport.addObjectReportToSpecificCollection(
							new ObjectReport(objectId, type, description, status, ioType));
				} else {
					actionReport.addObjectReport(new ObjectReport(objectId, type, description, status, ioType));
				}
			}
		}
	}

	@Override
	public void addErrorToObjectReport(Context context, String objectId, OBJECT_TYPE type, ERROR_CODE code,
			String descriptionError) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		if (actionReport != null) {
			if (actionReport.findObjectReport(objectId, type) == null) {
				addObjectReport(context, objectId, type, "", OBJECT_STATE.OK, IO_TYPE.INPUT);
			}
			ObjectReport object = actionReport.findObjectReport(objectId, type);
			if (object != null) {
				object.addError(new ObjectError(code, descriptionError));
			}
		}
	}

	@Override
	public void setObjectStatus(Context context, String objectId, OBJECT_TYPE type, OBJECT_STATE state) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		if (actionReport != null) {

			ObjectReport object = actionReport.findObjectReport(objectId, type);
			if (object != null) {
				object.setStatus(state);
			}
		}

	}

	@Override
	public void addStatToObjectReport(Context context, String objectId, OBJECT_TYPE type, OBJECT_TYPE statType,
			int count) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		if (actionReport != null) {
			if (actionReport.getObjects().containsKey(type)) {
				ObjectReport object = actionReport.getObjects().get(type);
				object.addStatTypeToObject(statType, count);
			} else if (actionReport.getCollections().containsKey(type)) {
				ObjectReport object = actionReport.getCollections().get(type).findObjectReport(objectId);
				if (object != null) {
					object.addStatTypeToObject(statType, count);
					actionReport.getCollections().get(type).addStatTypeToObject(statType, count);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mobi.chouette.exchange.report.ActionReporter#setStatToObjectReport(mobi
	 * .chouette.common.Context, java.lang.String,
	 * mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE,
	 * mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE, int)
	 */
	@Override
	public void setStatToObjectReport(Context context, String objectId, OBJECT_TYPE type, OBJECT_TYPE statType,
			int count) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		if (actionReport != null) {
			if (actionReport.getObjects().containsKey(type)) {
				ObjectReport object = actionReport.getObjects().get(type);
				object.setStatTypeToObject(statType, count);
			} else if (actionReport.getCollections().containsKey(type)) {
				ObjectReport object = actionReport.getCollections().get(type).findObjectReport(objectId);
				if (object != null) {
					int previous = object.setStatTypeToObject(statType, count);
					// refresh stats on collection
					actionReport.getCollections().get(type).addStatTypeToObject(statType, count - previous);
				}
			}
		}
	}

	@Override
	public boolean hasInfo(Context context, OBJECT_TYPE type) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		if (actionReport != null) {
			if (actionReport.getObjects().containsKey(type)) {
				return true;
			} else if (actionReport.getCollections().containsKey(type)) {
				return !actionReport.getCollections().get(type).getObjectReports().isEmpty();
			}
		}
		return false;

	}

	@Override
	public boolean addValidationErrorToZipReport(Context context, String fileInfoName, int code, SEVERITY severity) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		boolean ret = false;
		if (actionReport != null) {
			FileReport fileReport = actionReport.findZipReport(fileInfoName);
			if (fileReport != null) {
				ret = fileReport.addCheckPointError(code, severity);
				if (severity.equals(SEVERITY.WARNING))
					actionReport.setWarning(true);
			}
		}
		return ret;
	}

	@Override
	public boolean addValidationErrorToFileReport(Context context, String fileInfoName, int code, SEVERITY severity) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		boolean ret = false;
		if (actionReport != null) {
			FileReport fileReport = actionReport.findFileReport(fileInfoName);
			if (fileReport != null) {
				ret = fileReport.addCheckPointError(code, severity);
				if (severity.equals(SEVERITY.WARNING))
					actionReport.setWarning(true);
			}
		}
		return ret;
	}

	@Override
	public boolean addValidationErrorToObjectReport(Context context, String objectId, OBJECT_TYPE type, int code,
			SEVERITY severity) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		boolean ret = false;
		if (actionReport != null) {
			if (actionReport.findObjectReport(objectId, type) == null) {
				addObjectReport(context, objectId, type, "", OBJECT_STATE.OK, IO_TYPE.INPUT);
			}
			ObjectReport objectReport = actionReport.findObjectReport(objectId, type);
			if (objectReport != null) {
				ret = objectReport.addCheckPointError(code, severity);
				if (severity.equals(SEVERITY.WARNING))
					actionReport.setWarning(true);
			}
		}
		return ret;
	}

	@Override
	public boolean hasFileValidationErrors(Context context, String filename) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		if (actionReport == null)
			return false;
		FileReport fileReport = actionReport.findFileReport(filename);
		if (fileReport == null)
			return false;

		return fileReport.getCheckPointErrorCount() > 0;
	}

	@Override
	public boolean hasLineValidationErrors(Context context, LineLite line) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		if (actionReport == null)
			return false;
		ObjectReport objectReport = actionReport.findObjectReport(line.getObjectId(), OBJECT_TYPE.LINE);
		if (objectReport == null)
			return false;

		return objectReport.getCheckPointErrorCount() > 0;
	}

	@Override
	public boolean hasObjectValidationErrors(Context context, OBJECT_TYPE type) {
		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);
		if (actionReport == null)
			return false;
		ObjectReport objectReport = actionReport.findObjectReport(null, type);
		if (objectReport == null)
			return false;

		return objectReport.getCheckPointErrorCount() > 0;
	}
}
