package mobi.chouette.exchange.netex_stif.validator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.model.ChouetteIdentifiedObject;

public abstract class AbstractValidator implements NetexCheckPoints, Constant {

	private static final String REGEX_ID_PREFIX = "^[\\w-]+:";
	private static final String REGEX_ID_SUFFIX = ":[\\w-]+:LOC$";
	private static final String REGEX_CODIFLIGNE_PREFIX = "^[STIF:LIGNE|STIF-LIGNE]:"; // ATTENTION,
																						// code
																						// adapté
																						// aux
																						// 2
																						// versions
																						// de
																						// CodifLigne
	private static final String REGEX_CODIFLIGNE_SUFFIX = ":[\\w-]+:STIF$";
	private static final String REGEX_REFLEX_PREFIX = "^FR:[\\d]{5}:";
	private static final String REGEX_REFLEX_SUFFIX = ":[\\w-]+:STIF$";

	protected static final String OBJECT_IDS = "encontered_ids";

	private static final Collection<String> CodifLigneTypes;
	private static final Collection<String> ReflexTypes;

	static {
		CodifLigneTypes = new ArrayList<>();
		CodifLigneTypes.add("Line");
		CodifLigneTypes.add("Operator");
		ReflexTypes = new ArrayList<>();
		ReflexTypes.add("Quay");
	}

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

	protected abstract String getLocalContext();

	@SuppressWarnings("unchecked")
	protected static Context getObjectContext(Context context, String localContextName, String objectId) {
		Context validationContext = (Context) context.get(VALIDATION_CONTEXT);
		if (validationContext == null) {
			validationContext = new Context();
			context.put(VALIDATION_CONTEXT, validationContext);

		}

		// TODO à retirer si non utilisé (contrôle d'existence des objectIds)
		Set<String> objectIds = (Set<String>) validationContext.get(OBJECT_IDS);
		if (objectIds == null) {
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

	public void addModificationf(Context context, String objectId, String modification) {
		if (modification != null) {
			Context objectContext = getObjectContext(context, getLocalContext(), objectId);
			objectContext.put(MODIFICATION, modification);
		}

	}

	/**
	 * <a target="_blank" href="https://projects.af83.io/issues/2293" >Carte
	 * #2293</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-4
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'identifiant d'un objet NeTEx doit respecter la
	 * syntaxe définie et le type d'objet doit correspondre à la balise NeTEx de
	 * l'objet
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} :
	 * l'identifiant {objectId} de l'objet {typeNeTEx} ne respecte pas la
	 * syntaxe [CODESPACE]:{typeNeTEx}:[identifiant Technique]:LOC
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

	/**
	 * <a target="_blank" href="https://projects.af83.io/issues/2294" >Carte
	 * #2294</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-5
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : attribut 'changed' renseigné
	 * <p>
	 * <b>Prédicat</b> :La date de mise à jour d'un objet NeTEx ne doit pas être
	 * dans le futur (J+n (n >0) par rapport à la date d'import)
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * {typeNeTEx} d'identifiant {objectId} a une date de mise à jour dans le
	 * futur
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 */
	public boolean checkChanged(Context context, String type, ChouetteIdentifiedObject object, int lineNumber,
			int columnNumber) {

		Calendar c = Calendar.getInstance();
		c.add(1, Calendar.DATE);

		boolean result = object.getCreationTime().after(c.getTime());

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_5, location, type);
			object.setCreationTime(Calendar.getInstance().getTime()); // reset
																		// to
																		// now
		}
		return result;
	}

	/**
	 * <a target="_blank" href="https://projects.af83.io/issues/2295" >Carte
	 * #2295</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-6
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : attribut 'modification' renseigné
	 * <p>
	 * <b>Prédicat</b> :la valeur 'delete' de l'indicateur de modification est
	 * interdite
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * {typeNeTEx} d'identifiant {objectId} a un état de modification interdit :
	 * 'delete'
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 */
	public boolean checkModification(Context context, String type, ChouetteIdentifiedObject object, int lineNumber,
			int columnNumber) {

		boolean result = true;

		Context objectContext = getObjectContext(context, getLocalContext(), object.getObjectId());
		if (objectContext.containsKey(MODIFICATION)) {
			String modification = (String) objectContext.get(MODIFICATION);
			result = !modification.equals("delete");
		}
		if (!result) {

			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_6, location, type);
		}
		return result;
	}

	/**
	 * <a target="_blank" href="https://projects.af83.io/issues/2296" >Carte
	 * #2296</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-7
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : La référence (attribut 'ref') doit respecter le motif
	 * [CODESPACE]:[type d'objet]:[identifiant Technique]:LOC pour un objet
	 * local à l'import ou l'un des motifs REFLEX ou CODIFLIGNE pour les
	 * références à ces types d'objets.
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * {typeNeTEx} d'identifiant {objectId} définit une référence {objectRef} de
	 * syntaxe invalide : {ref}
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 */
	public boolean checkNetexRef(Context context, ChouetteIdentifiedObject object, String type, String id,
			int lineNumber, int columnNumber) {

		String regex = REGEX_ID_PREFIX + type + REGEX_ID_SUFFIX;
		if (CodifLigneTypes.contains(type)) {
			regex = REGEX_CODIFLIGNE_PREFIX + type + REGEX_CODIFLIGNE_SUFFIX;
		} else if (ReflexTypes.contains(type)) {
			regex = REGEX_REFLEX_PREFIX + type + REGEX_REFLEX_SUFFIX;
		}
		boolean result = id.matches(regex);

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_7, location, id, type);
		}
		return result;
	}

	/**
	 * <a target="_blank" href="https://projects.af83.io/issues/2297" >Carte
	 * #2297</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-8
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'attribut 'version' doit être renseigné pour une
	 * référence interne <br>
	 * La balise ne doit pas avoir de contenu
	 * <p>
	 * <b>Message</b> :
	 * <ol>
	 * <li>{fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet {typeNeTEx}
	 * d'identifiant {objectId} définit une référence {objectRef} de type
	 * externe : référence interne attendue</li>
	 * <li>{fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet {typeNeTEx}
	 * d'identifiant {objectId} définit une référence {objectRef} de type
	 * interne mais disposant d'un contenu (version externe possible)</li>
	 * </ol>
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 */
	public boolean checkInternalRef(Context context, ChouetteIdentifiedObject object, String type, String id,
			String versionAttribute, String content, int lineNumber, int columnNumber) {

		boolean result1 = versionAttribute != null && !versionAttribute.isEmpty();
		boolean result2 = content == null || content.isEmpty();

		if (!result1) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_8, "1", location, id, type);
		}
		if (!result2) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_8, "2", location, id, type);
		}
		return result1 && result2;
	}

	/**
	 * <a target="_blank" href="https://projects.af83.io/issues/2298" >Carte
	 * #2298</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-9
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'attribut 'version' ne doit pas être renseigné pour
	 * une référence externe, la version est fournie dans le contenu de la
	 * balise sous la forme 'version="[VERSION de l'objet]"'
	 * <p>
	 * <b>Message</b> :
	 * <ol>
	 * <li>{fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet {typeNeTEx}
	 * d'identifiant {objectId} définit une référence {objectRef} de type
	 * interne : référence externe attendue</li>
	 * <li>{fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet {typeNeTEx}
	 * d'identifiant {objectId} définit une référence {objectRef} de type
	 * externe sans information de version</li>
	 * </ol>
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 */
	public boolean checkExternalRef(Context context, ChouetteIdentifiedObject object, String type, String id,
			String versionAttribute, String content, int lineNumber, int columnNumber) {

		boolean result1 = versionAttribute == null || versionAttribute.isEmpty();
		boolean result2 = content != null && content.matches("^version=\".+\"$");

		if (!result1) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_9, "1", location, id, type);
		}
		if (!result2) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_9, "2", location, id, type);
		}
		return result1 && result2;
	}

	/**
	 * <a target="_blank" href="https://projects.af83.io/issues/2300" >Carte
	 * #2300</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-10
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : En dehors des références CODIFLIGNE et REFLEX, l'objet
	 * référencé par une référence externe doit exister au sein d'un lot de
	 * fichiers cohérents. Les références CODIFLIGNE et REFLEX doivent
	 * correspondre à des objets existants dans le BOIV
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * {typeNeTEx} d'identifiant {objectId} définit une référence {objectRef} de
	 * type externe inconnue
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 */
	public boolean checkExistsRef(Context context, ChouetteIdentifiedObject object, String type, String id,
			String versionAttribute, String content, int lineNumber, int columnNumber) {

		// TODO !
		boolean result1 = versionAttribute == null || versionAttribute.isEmpty();
		boolean result2 = content != null && content.matches("^version=\".+\"$");

		if (!result1) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_9, "1", location, id, type);
		}
		if (!result2) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_9, "2", location, id, type);
		}
		return result1 && result2;
	}

}
