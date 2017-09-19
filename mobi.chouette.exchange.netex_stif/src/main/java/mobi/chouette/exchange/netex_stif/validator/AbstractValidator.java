package mobi.chouette.exchange.netex_stif.validator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.validation.ValidationData;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.model.ChouetteIdentifiedObject;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.util.Referential;

public abstract class AbstractValidator implements NetexCheckPoints, Constant {

	private static final String REGEX_ID_PREFIX = "^[\\w-]+:";
	private static final String REGEX_ID_SUFFIX = ":[\\w-]+:LOC$";
	// ATTENTION, regex: adapté aux 2 versions de CodifLigne
	private static final String REGEX_CODIFLIGNE_PREFIX = "^(STIF:CODIFLIGNE|STIF-LIGNE):";
	private static final String REGEX_CODIFLIGNE_SUFFIX = ":[\\w-]+(:STIF|)$";

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

		String fileName = (String) context.get(FILE_NAME);

		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_4, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_5, "W");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_6, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_7, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_8, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_9, "E");
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_10, "E");

		if (fileName.equals(COMMUN_FILE_NAME)) {
			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_1, "E");
			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_Notice_1, "E");
			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_Notice_2, "W");
		} else if (fileName.equals(CALENDRIER_FILE_NAME)) {
			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_2, "E");
			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_DayTypeAssignment_1, "E");
			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_DayTypeAssignment_2, "E");

			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_DayType_1, "W");
			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_DayType_2, "E");

			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_OperatingPeriod_1, "E");
		} else {
			// OFFRE FILE NAME
			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_3, "E");
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

			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_PassengerStopAssignment_1, "E");

			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_RoutingConstraintZone_1, "E");
			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_RoutingConstraintZone_2, "E");

			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_ServiceJourney_1, "E");
			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_ServiceJourney_2, "E");
			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_ServiceJourney_3, "E");
			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_ServiceJourney_4, "E");

			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_PassingTime_1, "E");
			validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_PassingTime_2, "E");

		}
		// prepare local checkpoints
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_4);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_5);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_7);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_8);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_9);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_10);

	}

	protected abstract String getLocalContext();

	public void addLocation(Context context, ChouetteIdentifiedObject object, int lineNumber, int columnNumber) {
		addLocation(context, getLocalContext(), object, lineNumber, columnNumber);
	}

	/**
	 * add location for local validation (level 1 and 2) and for general
	 * validation (level 3 and more)
	 * 
	 * @param context
	 * @param localContext
	 * @param objectId
	 * @param lineNumber
	 * @param columnNumber
	 */
	protected void addLocation(Context context, String localContext, ChouetteIdentifiedObject object, int lineNumber,
			int columnNumber) {
		String objectId = object.getObjectId();
		ValidationData data = (ValidationData) context.get(VALIDATION_DATA);
		if (data == null) {
			data = new ValidationData();
			context.put(VALIDATION_DATA, data);
		}
		LineLite line = (LineLite) context.get(LINE);
		String fileName = (String) context.get(FILE_NAME);
		if (data != null && fileName != null) {
			DataLocation loc = new DataLocation(fileName, lineNumber, columnNumber, line, object);
			loc.setName(DataLocation.buildName(object));
			data.getDataLocations().put(objectId, loc);
		}

	}

	public DataLocation getLocation(Context context, String objectId) {
		ValidationData data = (ValidationData) context.get(VALIDATION_DATA);
		if (data == null)
			return null;
		return data.getDataLocations().get(objectId);
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

	public void addModification(Context context, String objectId, String modification) {
		if (modification != null) {
			Context objectContext = getObjectContext(context, getLocalContext(), objectId);
			objectContext.put(MODIFICATION, modification);
		}

	}
	
	public void addXmlId(Context context, String objectId, String xmlId) {
		Context objectContext = getObjectContext(context, getLocalContext(), objectId);
		objectContext.put(ID, xmlId);
	}

	protected  String getXmlId(Context context, String objectId) {
		Context objectContext = getObjectContext(context, getLocalContext(), objectId);
		return (String) objectContext.get(ID);
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de la syntaxe des identifiants
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2293">Cartes
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
	 * 
	 * @param context
	 * @param type
	 * @param id
	 * @param lineNumber
	 * @param columnNumber
	 * @return
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
	 * <b>Titre</b> :[Netex] Contrôle de l'attribut 'changed'
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2294">Cartes
	 * #2294</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-5
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : attribut 'changed' renseigné
	 * <p>
	 * <b>Prédicat</b> : La date de mise à jour d'un objet NeTEx ne doit pas
	 * être dans le futur (J+n (n >0) par rapport à la date d'import)
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * {typeNeTEx} d'identifiant {objectId} a une date de mise à jour dans le
	 * futur
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 * @param context
	 * @param type
	 * @param object
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	public boolean checkChanged(Context context, String type, ChouetteIdentifiedObject object, int lineNumber,
			int columnNumber) {

		Calendar c = Calendar.getInstance();
		c.add(1, Calendar.DATE);

		boolean result = object.getCreationTime().before(c.getTime());

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_5, location, type);
			// reset creation time to now
			object.setCreationTime(Calendar.getInstance().getTime());
		}
		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'attribut 'modification'
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2295">Cartes
	 * #2295</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-6
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : attribut modification renseigné
	 * <p>
	 * <b>Prédicat</b> : la valeur 'delete' de l'indicateur de modification est
	 * interdite
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * {typeNeTEx} d'identifiant {objectId} a un état de modification interdit :
	 * 'delete'
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 * @param context
	 * @param type
	 * @param object
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	public boolean checkModification(Context context, String type, ChouetteIdentifiedObject object, int lineNumber,
			int columnNumber) {

		boolean result = true;

		Context objectContext = getObjectContext(context, getLocalContext(), object.getObjectId());
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		if (objectContext.containsKey(MODIFICATION)) {
			validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_6);
			String modification = (String) objectContext.get(MODIFICATION);
			result = !modification.equals("delete");
		}
		if (!result) {
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_6, location, type);
		}
		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de la syntaxe des références
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2296">Cartes
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
	 * 
	 * @param context
	 * @param object
	 * @param type
	 * @param ref
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	public boolean checkNetexRef(Context context, ChouetteIdentifiedObject object, String typeRef, String ref,
			int lineNumber, int columnNumber) {

		String type = typeRef;
		if (type.endsWith("Ref"))
			type = type.substring(0, type.length() - 3);
		if (type.equals("JourneyPattern"))
			type = "ServiceJourneyPattern";
		String regex = REGEX_ID_PREFIX + type + REGEX_ID_SUFFIX;
		if (CodifLigneTypes.contains(type)) {
			regex = REGEX_CODIFLIGNE_PREFIX + type + REGEX_CODIFLIGNE_SUFFIX;
		} else if (ReflexTypes.contains(type)) {
			String code = "ZDE"; // todo manage when StopPlaceRef should be
									// checked
			regex = REGEX_REFLEX_PREFIX + code + REGEX_REFLEX_SUFFIX;

		}
		boolean result = ref.matches(regex);

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_7, location, ref, typeRef);
		}
		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de la syntaxe des références internes
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2297">Cartes
	 * #2297</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-8
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'attribut 'version' doit être renseigné pour une
	 * référence interne<br>
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
	 *
	 * @param context
	 * @param object
	 * @param type
	 * @param id
	 * @param versionAttribute
	 * @param content
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	public boolean checkInternalRef(Context context, ChouetteIdentifiedObject object, String type, String id,
			String versionAttribute, String content, int lineNumber, int columnNumber) {

		boolean result1 = versionAttribute != null && !versionAttribute.isEmpty();
		boolean result2 = content == null || content.trim().isEmpty();

		if (!result1) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_8, "1", location, id, type);
		}
		if (!result2) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_8, "2", location, id, type);
		}
		return result1 && result2;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de la syntaxe des références externes
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2298">Cartes
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
	 * 
	 * @param context
	 * @param object
	 * @param type
	 * @param ref
	 * @param versionAttribute
	 * @param content
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	public boolean checkExternalRef(Context context, ChouetteIdentifiedObject object, String type, String ref,
			String versionAttribute, String content, int lineNumber, int columnNumber) {

		boolean result1 = versionAttribute == null || versionAttribute.isEmpty();
		boolean result2 = content != null && content.trim().matches("^version=\".+\"$");

		if (!result1) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_9, "1", location, ref, type);
		}
		if (!result2) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_9, "2", location, ref, type);
		}
		return result1 && result2;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de la syntaxe des références externes
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2300">Cartes
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
	 * fichiers cohérents.Les références CODIFLIGNE et REFLEX doivent
	 * correspondre à des objets existants dans le BOIV
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * {typeNeTEx} d'identifiant {objectId} définit une référence {objectRef} de
	 * type externe inconnue
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 * @param context
	 * @param object
	 * @param type
	 * @param ref
	 * @param versionAttribute
	 * @param content
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	public boolean checkExistsRef(Context context, ChouetteIdentifiedObject object, String type, String ref,
			String versionAttribute, String content, int lineNumber, int columnNumber) {

		Referential referential = (Referential) context.get(REFERENTIAL);
		boolean result = false;
		switch (type) {
		case DAY_TYPE_REF:
			result = referential.getSharedTimetableTemplates().containsKey(ref);
			break;
		case NOTICE_REF:
			result = referential.getSharedFootnotes().containsKey(ref);
			break;
		case LINE_REF:
			result = referential.getSharedReadOnlyLines().containsKey(ref);
			break;
		case OPERATOR_REF:
			result = referential.getSharedReadOnlyCompanies().containsKey(ref);
			break;
		case QUAY_REF:
			result = referential.getSharedReadOnlyStopAreas().containsKey(ref);
			break;
		}
		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, object);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_10, location, ref, type);
		}
		return result;
	}

}
