package mobi.chouette.exchange.netex_stif.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.model.StopPointInJourneyPattern;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.model.JourneyPattern;

public class ServiceJourneyPatternValidator extends AbstractValidator {

	public static final String LOCAL_CONTEXT = NetexStifConstant.SERVICE_JOURNEY_PATTERN;

	protected String getLocalContext() {
		return LOCAL_CONTEXT;
	}

	@Override
	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck
		validationReporter.prepareCheckPointReport(context, NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_1);
		validationReporter.prepareCheckPointReport(context, NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_2);
		validationReporter.prepareCheckPointReport(context, NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_3);
		validationReporter.prepareCheckPointReport(context, NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_4);
	}

	public boolean validate(Context context, JourneyPattern journeyPattern, int lineNumber, int columnNumber) {
		boolean result1 = checkNetexId(context, NetexStifConstant.SERVICE_JOURNEY_PATTERN, journeyPattern.getObjectId(),
				lineNumber, columnNumber);
		checkChanged(context, NetexStifConstant.SERVICE_JOURNEY_PATTERN, journeyPattern, lineNumber, columnNumber);
		boolean result2 = checkModification(context, NetexStifConstant.SERVICE_JOURNEY_PATTERN, journeyPattern,
				lineNumber, columnNumber);
		boolean result3 = check2NeTExSTIFServiceJourneyPattern1(context, journeyPattern, lineNumber, columnNumber);
		result3 &= check2NeTExSTIFServiceJourneyPattern2(context, journeyPattern, lineNumber, columnNumber);
		result3 &= check2NeTExSTIFServiceJourneyPattern3(context, journeyPattern, lineNumber, columnNumber);
		result3 &= check2NeTExSTIFServiceJourneyPattern4(context, journeyPattern, lineNumber, columnNumber);
		return result1 && result2 && result3;
	}

	public void addPatternType(Context context, String objectId, String value) {
		Context objectContext = getObjectContext(context, LOCAL_CONTEXT, objectId);
		objectContext.put(NetexStifConstant.SERVICE_JOURNEY_PATTERN_TYPE, value);

	}

	/**
	 * preserve StopPointInJourneyPattern data for test
	 * 2-NeTExSTIF-ServiceJourneyPattern-4
	 * 
	 * @param context
	 * @param objectId
	 * @param spijp
	 */
	@SuppressWarnings("unchecked")
	public void addStopPointInJourneyPattern(Context context, String objectId, StopPointInJourneyPattern spijp) {
		Context objectContext = getObjectContext(context, LOCAL_CONTEXT, objectId);
		List<StopPointInJourneyPattern> list = (List<StopPointInJourneyPattern>) objectContext.computeIfAbsent(
				NetexStifConstant.STOP_POINT_IN_JOURNEY_PATTERN, l -> new ArrayList<StopPointInJourneyPattern>());
		list.add(spijp);
	}

	/**
	 * preserve StopPoint order for test 2-NeTExSTIF-Route-3
	 * 
	 * @param context
	 * @param objectId
	 * @param order
	 * @param spId
	 */
	@SuppressWarnings("unchecked")
	public void addStopPointOrder(Context context, String objectId, Integer order, String spId) {
		Context objectContext = getObjectContext(context, LOCAL_CONTEXT, objectId);
		Map<Integer, String> map = (Map<Integer, String>) objectContext.computeIfAbsent(NetexStifConstant.ORDER,
				m -> new HashMap<Integer, String>());
		map.put(order, spId);
	}

	@SuppressWarnings("unchecked")
	public void addStopPointAlighting(Context context, String objectId, Integer position, Boolean alighting) {
		Context objectContext = getObjectContext(context, LOCAL_CONTEXT, objectId);
		Map<Integer, Boolean> map = (Map<Integer, Boolean>) objectContext
				.computeIfAbsent(NetexStifConstant.FOR_ALIGHTING, m -> new HashMap<Integer, Boolean>());
		map.put(position, alighting);
	}

	@SuppressWarnings("unchecked")
	public void addStopPointBoarding(Context context, String objectId, Integer position, Boolean boarding) {
		Context objectContext = getObjectContext(context, LOCAL_CONTEXT, objectId);
		Map<Integer, Boolean> map = (Map<Integer, Boolean>) objectContext
				.computeIfAbsent(NetexStifConstant.FOR_BOARDING, m -> new HashMap<Integer, Boolean>());
		map.put(position, boarding);
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet ServiceJourneyPattern :
	 * RouteRef
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2313">Cartes
	 * #2313</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-ServiceJourneyPattern-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'attribut RouteRef de l'objet ServiceJourneyPattern
	 * doit être renseigné.
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * ServiceJourneyPattern d'identifiant {objectId} ne référence pas de Route
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check2NeTExSTIFServiceJourneyPattern1(Context context, JourneyPattern journeyPattern, int lineNumber,
			int columnNumber) {
		boolean result = true;
		result = journeyPattern.getRoute() != null;
		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			location.setObjectId(journeyPattern.getObjectId());
			validationReporter.addCheckPointReportError(context, null,
					NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_1,NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_1, location);
		}

		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet ServiceJourneyPattern :
	 * pointsInSequence
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2314">Cartes
	 * #2314</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-ServiceJourneyPattern-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'attribut pointsInSequence de l'objet
	 * ServiceJourneyPattern doit contenir au moins 2 StopPointInJourneyPattern
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * ServiceJourneyPattern d'identifiant {objectId} doit contenir au moins 2
	 * StopPointInJourneyPattern
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 * note, la xsd surveille que pointsInJourneyPattern contient au moins 2
	 * items, mais ceux-ci peuvent être de différente nature : TimingPoint par
	 * exemple
	 *
	 * @param context
	 * @return
	 */
	public boolean check2NeTExSTIFServiceJourneyPattern2(Context context, JourneyPattern journeyPattern, int lineNumber,
			int columnNumber) {
		boolean result = true;
		result = journeyPattern.getStopPoints() != null && journeyPattern.getStopPoints().size() > 1;
		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			location.setObjectId(journeyPattern.getObjectId());
			validationReporter.addCheckPointReportError(context, null,
					NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_2,NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_2, location);
		}
		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet ServiceJourneyPattern :
	 * ServiceJourneyPatternType
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2315">Cartes
	 * #2315</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-ServiceJourneyPattern-3
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'objet ServiceJourneyPattern doit avoir son attribut
	 * ServiceJourneyPatternType renseigné et à la valeur passenger.
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * ServiceJourneyPattern d'identifiant {objectId} n'a pas de valeur pour
	 * l'attribut ServiceJourneyPatternType <br>
	 * {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet ServiceJourneyPattern
	 * d'identifiant {objectId} a une valeur interdite {valeur} pour l'attribut
	 * ServiceJourneyPatternType; différente de 'passenger'
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check2NeTExSTIFServiceJourneyPattern3(Context context, JourneyPattern journeyPattern, int lineNumber,
			int columnNumber) {
		boolean result = true;
		Context objectContext = getObjectContext(context, LOCAL_CONTEXT, journeyPattern.getObjectId());
		// récupération de la valeur d'attribut sauvegardée
		String patternType = (String) objectContext.get(NetexStifConstant.SERVICE_JOURNEY_PATTERN_TYPE);
		result = patternType != null && patternType.equals("passenger");
		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			location.setObjectId(journeyPattern.getObjectId());
			if (patternType == null || patternType.isEmpty())
				validationReporter.addCheckPointReportError(context, null,
						NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_3,NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_3, "1", location);
			else
				validationReporter.addCheckPointReportError(context, null,
						NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_3,NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_3, "2", location, patternType);

		}
		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet ServiceJourneyPattern : ordre
	 * des StopPointInJourneyPattern
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2316">Cartes
	 * #2316</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-ServiceJourneyPattern-4
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Les "order" des StopPointInJourneyPattern peuvent être
	 * discontinus mais ils doivent toujours croissants
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne}, objet
	 * ServiceJourneyPattern d'identifiant {objectId} : les attributs 'order'
	 * des StopPointInJourneyPattern ne sont pas croissants.
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean check2NeTExSTIFServiceJourneyPattern4(Context context, JourneyPattern journeyPattern, int lineNumber,
			int columnNumber) {
		boolean result = true;
		int lastOrder = -1;

		Context objectContext = getObjectContext(context, LOCAL_CONTEXT, journeyPattern.getObjectId());
		List<StopPointInJourneyPattern> list = (List<StopPointInJourneyPattern>) objectContext
				.get(NetexStifConstant.STOP_POINT_IN_JOURNEY_PATTERN);

		if (list != null) {
			for (StopPointInJourneyPattern stopPoint : list) {
				if (stopPoint.getOrder() <= lastOrder) {
					result = false;
					break;
				}
				lastOrder = stopPoint.getOrder();
			}
		}
		// List<StopPoint> stopPoints = journeyPattern.getStopPoints();
		// for (StopPoint stopPoint : stopPoints) {
		// if (stopPoint.getPosition() <= lastOrder) {
		// result = false;
		// break;
		// }
		// lastOrder = stopPoint.getPosition();
		// }

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			location.setObjectId(journeyPattern.getObjectId());
			validationReporter.addCheckPointReportError(context, null,
					NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_4,NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_4, location);
		}
		return result;
	}

}
