package mobi.chouette.exchange.netex_stif.validator;

import java.util.HashSet;
import java.util.Set;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;

public abstract class AbstractValidator implements NetexCheckPoints, Constant {

	private static final String REGEX_ID_PREFIX = "^[\\w-]+:";
	private static final String REGEX_ID_SUFFIX = ":[\\w-]+:LOC$";

	protected static final String OBJECT_IDS = "encontered_ids";

	
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

		// prepare local chekpoints
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_4);

	}

	@SuppressWarnings("unchecked")
	protected static Context getObjectContext(Context context, String localContextName, String objectId) {
		Context validationContext = (Context) context.get(VALIDATION_CONTEXT);
		if (validationContext == null) {
			validationContext = new Context();
			context.put(VALIDATION_CONTEXT, validationContext);
			
		}

		// TODO à retirer si non utilisé (contrôle d'existence des objectIds) 
		Set<String> objectIds = (Set<String>) validationContext.get(OBJECT_IDS);
		if (objectIds == null)
		{
			objectIds = new HashSet<String>();
			validationContext.put(OBJECT_IDS, objectIds);
		}
		objectIds.add(objectId);

		Context localContext = (Context) validationContext.get(localContextName);
		if (localContext == null) {
			localContext = new Context();
			validationContext.put(localContextName, localContext);
		}
		Context objectContext = (Context) localContext.get(objectId);
		if (objectContext == null) {
			objectContext = new Context();
			localContext.put(objectId, objectContext);
		}
		return objectContext;

	}

	/**
	 * <a target="_blank" href="https://projects.af83.io/issues/2293" >Carte #2293</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-4
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'identifiant d'un objet NeTEx doit respecter la syntaxe
	 * définie et le type d'objet doit correspondre à la balise NeTEx de l'objet
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'identifiant
	 * {objectId} de l'objet {typeNeTEx} ne respecte pas la syntaxe
	 * [CODESPACE]:{typeNeTEx}:[identifiant Technique]:LOC
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 */
	public boolean checkNetexId(Context context, String type, String id, int lineNumber, int columnNumber) {

		String regex = REGEX_ID_PREFIX + type + REGEX_ID_SUFFIX;
		boolean result = id.matches(regex);

		if (!result) {

			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_4, location, id, regex);
		}
		return result;
	}
}
