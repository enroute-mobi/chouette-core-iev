package mobi.chouette.exchange.netex_stif.validator;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;

public abstract class AbstractValidator implements NetexCheckPoints {

	public AbstractValidator() {

	}

	public AbstractValidator(Context context) {
		init(context);
	}

	public void init(Context context) {
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_1, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_2, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_3, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_4, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_5, "W");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_6, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_7, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_8, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_9, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_10, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_11, "E");
		
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_Notice_1, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_Notice_2, "W");
		
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_DayTypeAssignment_1, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_DayTypeAssignment_2, "E");

		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_DayType_1, "W");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_DayType_2, "E");

		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_OperatingPeriod_1, "E");

		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_Route_1, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_Route_2, "W");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_Route_3, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_Route_4, "W");

		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_Direction_1, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_Direction_2, "E");

		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_ServiceJourneyPattern_1, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_ServiceJourneyPattern_2, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_ServiceJourneyPattern_3, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_ServiceJourneyPattern_4, "E");

		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_PassengerStopAssignment_1, "W");

		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_RoutingConstraintZone_1, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_RoutingConstraintZone_2, "E");

		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_ServiceJourney_1, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_ServiceJourney_2, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_ServiceJourney_3, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_ServiceJourney_4, "E");

		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_PassingTime_1, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_PassingTime_2, "E");
	}

	/*
	 * https://projects.af83.io/issues/2293 Description
	 * 
	 * 
	 * Code : 2-NeTExSTIF-4
	 * 
	 * Variables : néant
	 * 
	 * Prérequis : néant
	 * 
	 * Prédicat : L'identifiant d'un objet NeTEx doit respecter la syntaxe
	 * définie et le type d'objet doit correspondre à la balise NeTEx de l'objet
	 * 
	 * Message : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'identifiant
	 * {objectId} de l'objet {typeNeTEx} ne respecte pas la syntaxe
	 * [CODESPACE]:{typeNeTEx}:[identifiant Technique]:LOC
	 * 
	 * Criticité : error
	 */
	public boolean checkNetexId(Context context, String type, String id, int lineNumber, int columnNumber) {
		boolean result = true;

		String[] token = id.split(":");
		if (token.length != 4 || !token[1].equals(type) || !token[3].equals("LOC")) {
			result = false;
		}
		if (!result) {

			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_4, location, id);
		}
		return result;
	}
}
