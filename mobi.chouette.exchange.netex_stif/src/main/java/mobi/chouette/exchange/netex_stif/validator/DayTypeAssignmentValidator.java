package mobi.chouette.exchange.netex_stif.validator;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.model.DayTypeAssignment;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;

public class DayTypeAssignmentValidator extends AbstractValidator {

	public static final String LOCAL_CONTEXT = DAY_TYPE_ASSIGNMENT;

	protected String getLocalContext() {
		return LOCAL_CONTEXT;
	}

	@Override
	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_DayTypeAssignment_1);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_DayTypeAssignment_2);
	}

	public boolean validate(Context context, DayTypeAssignment dayTypeAssignment, int lineNumber, int columnNumber) {
		checkChanged(context, DAY_TYPE_ASSIGNMENT, dayTypeAssignment, lineNumber, columnNumber);
		boolean result3 = checkModification(context, DAY_TYPE_ASSIGNMENT, dayTypeAssignment, lineNumber, columnNumber);
		boolean result1 = check2NeTExSTIFDayTypeAssignment1(context, dayTypeAssignment, lineNumber, columnNumber);
		boolean result2 = check2NeTExSTIFDayTypeAssignment2(context, dayTypeAssignment, lineNumber, columnNumber);
		return result1 && result2 && result3;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet DayTypeAssignment :
	 * OperatingDayRef
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2303">Cartes
	 * #2303</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-DayTypeAssignment-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : La référence OperationDayRef ne doit pas être
	 * renseignée
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * DayTypeAssignment d'identifiant {objectId} ne peut référencer un
	 * OperatingDay
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check2NeTExSTIFDayTypeAssignment1(Context context, DayTypeAssignment dayTypeAssignment,
			int lineNumber, int columnNumber) {
		boolean result = dayTypeAssignment.getOperationDayRef() == null;
		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, dayTypeAssignment);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_DayTypeAssignment_1, location);

		}
		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet DayTypeAssignment : IsAvailable
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2304">Cartes
	 * #2304</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-DayTypeAssignment-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'attribut IsAvailable ne peut pas être renseigné à
	 * 'false' si la référence OperatingPeriodRef est renseignée
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * DayTypeAssignment d'identifiant {objectId} ne peut référencer un
	 * OperatingPeriod sur la condition IsAvailable à faux.
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check2NeTExSTIFDayTypeAssignment2(Context context, DayTypeAssignment dayTypeAssignment,
			int lineNumber, int columnNumber) {
		boolean result = true;
		if (dayTypeAssignment.getIsAvailable() != null)
		{
			if (dayTypeAssignment.getIsAvailable() == Boolean.FALSE)
				result = dayTypeAssignment.getOperatingPeriodRef() == null;
		}
		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, dayTypeAssignment);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_DayTypeAssignment_2, location);

		}
		return result;
	}

}
