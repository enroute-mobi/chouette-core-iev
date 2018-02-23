package mobi.chouette.exchange.netex_stif.validator;

import java.util.Map;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.type.AlightingPossibilityEnum;
import mobi.chouette.model.type.BoardingPossibilityEnum;
import mobi.chouette.model.util.Referential;

public class RouteValidator extends AbstractValidator {

	public static final String LOCAL_CONTEXT = NetexStifConstant.ROUTE;

	protected String getLocalContext() {
		return LOCAL_CONTEXT;
	}

	@Override
	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck
		validationReporter.prepareCheckPointReport(context, NetexCheckPoints.L2_NeTExSTIF_Route_1);
		validationReporter.prepareCheckPointReport(context, NetexCheckPoints.L2_NeTExSTIF_Route_2);
		validationReporter.prepareCheckPointReport(context, NetexCheckPoints.L2_NeTExSTIF_Route_3);
		validationReporter.prepareCheckPointReport(context, NetexCheckPoints.L2_NeTExSTIF_Route_4);
	}

	public void addInverseRouteRef(Context context, String objectId, String inverseRouteRef) {
		Context objectContext = getObjectContext(context, LOCAL_CONTEXT, objectId);
		objectContext.put(NetexStifConstant.INVERSE_ROUTE_REF, inverseRouteRef);
	}

	/**
	 * @param context
	 * @param route
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	public boolean validate(Context context, Route route, int lineNumber, int columnNumber) {
		addLocation(context, route, lineNumber, columnNumber);
		checkChanged(context, NetexStifConstant.ROUTE, route, lineNumber, columnNumber);
		boolean result2 = checkModification(context, NetexStifConstant.ROUTE, route, lineNumber, columnNumber);
		boolean result3 = check2NeTExSTIFRoute1(context, route, lineNumber, columnNumber);
		if (result3)
			check2NeTExSTIFRoute2_1(context, route, lineNumber, columnNumber);
		if (result3)
			check2NeTExSTIFRoute2_2(context, route, lineNumber, columnNumber);
		return result2 && result3;

	}

	public void validateAll(Context context) {
		Referential referential = (Referential) context.get(Constant.REFERENTIAL);
		referential.getRoutes().values().stream().forEach(r -> {
			if (r.isFilled()) {
				DataLocation d = getLocation(context, r.getObjectId());
				if (check2NeTExSTIFRoute3(context, r, d.getLineNumber(), d.getColumnNumber())) {
					check2NeTExSTIFRoute4(context, r, d.getLineNumber(), d.getColumnNumber());
				}
			}
		});
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet Route : DirectionType
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2308">Cartes
	 * #2308</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-Route-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : Attribut DirectionType renseigné
	 * <p>
	 * <b>Prédicat</b> : L'attribut DirectionType doit prendre l'une des 2
	 * valeurs 'outbound' ou 'inbound'
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * Route d'identifiant {objectId} a une valeur de l'attribut DirectionType
	 * interdite : {directionType}
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check2NeTExSTIFRoute1(Context context, Route route, int lineNumber, int columnNumber) {
		boolean result = true;
		String wayback = route.getWayBack();
		if (wayback == null || (!wayback.equals(NetexStifConstant.DIRECTION_OUTBOUND) && !wayback.equals(NetexStifConstant.DIRECTION_INBOUND))) {
			result = false;
		}

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(Constant.LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, route);
			validationReporter.addCheckPointReportError(context, null, NetexCheckPoints.L2_NeTExSTIF_Route_1, NetexCheckPoints.L2_NeTExSTIF_Route_1,location, "" + wayback);
		}
		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet Route : cohérence des routes
	 * inverses
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2309">Cartes
	 * #2309</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-Route-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : Attribut InverseRouteRef renseigné
	 * <p>
	 * <b>Prédicat</b> : Les Routes associées comme routes inverses doivent se
	 * référencer mutuellement.Les DirectionType des Routes en sens opposés
	 * doivent être différent (Note : DirectionType non renseigné = outbound)
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * Route d'identifiant {objectId} référence un objet Route inverse
	 * {InverseRouteRef.ref} qui ne le référence pas. {fichier}-Ligne
	 * {ligne}-Colonne {Colonne} : l'objet Route d'identifiant {objectId}
	 * référence un objet Route inverse {InverseRouteRef.ref} de même
	 * DirectionType
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check2NeTExSTIFRoute2_1(Context context, Route route, int lineNumber, int columnNumber) {
		boolean result = true;

		if (route.getOppositeRoute() == null || !route.getOppositeRoute().isFilled()) {
			// log.info("2-NeTExSTIF-Route-2-1 : Route " + route.getObjectId() +
			// (route.getOppositeRoute() == null ? " pas de route inverse" : "
			// route inverse non lue :
			// "+route.getOppositeRoute().getObjectId()));
			return result;
		}
		// log.info("2-NeTExSTIF-Route-2-1 : check Route " + route.getObjectId()
		// );

		Context routeContext = getObjectContext(context, LOCAL_CONTEXT, route.getObjectId());
		Context waybackContext = getObjectContext(context, LOCAL_CONTEXT, route.getOppositeRoute().getObjectId());
		// récupération de la valeur d'attribut sauvegardée
		String wayBackInverseRouteRef = (String) waybackContext.get(NetexStifConstant.INVERSE_ROUTE_REF);
		String inverseRouteRef = (String) routeContext.get(NetexStifConstant.INVERSE_ROUTE_REF);

		result = route.getObjectId().equals(wayBackInverseRouteRef)
				&& route.getOppositeRoute().getObjectId().equals(inverseRouteRef);

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(Constant.LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, route);
			validationReporter.addCheckPointReportError(context, null, NetexCheckPoints.L2_NeTExSTIF_Route_2,NetexCheckPoints.L2_NeTExSTIF_Route_2, "1", location,
					route.getOppositeRoute().getObjectId());
		}

		return result;
	}

	public boolean check2NeTExSTIFRoute2_2(Context context, Route route, int lineNumber, int columnNumber) {
		boolean result = true;

		if (route.getOppositeRoute() == null || !route.getOppositeRoute().isFilled())
			return result;
		String wayback = "null";
		String oppositeWayback = "null";

		if (route.getWayBack() != null) {
			wayback = route.getWayBack();
		}
		if (route.getOppositeRoute().getWayBack() != null) {
			oppositeWayback = route.getOppositeRoute().getWayBack();
		}
		// log.warn("wayback = " + wayback + "; oppositeWayback = " +
		// oppositeWayback);
		if (wayback.equals(oppositeWayback)) {
			result = false;
		}

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(Constant.LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, route);
			validationReporter.addCheckPointReportError(context, null, NetexCheckPoints.L2_NeTExSTIF_Route_2,NetexCheckPoints.L2_NeTExSTIF_Route_2, "2", location,
					route.getOppositeRoute().getObjectId());
		}

		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet Route : Séquence des arrêts
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2310">Cartes
	 * #2310</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-Route-3
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'ordre des arrêts (ScheduledStopPoint) issus des
	 * StopPointInJourneyPattern des ServiceJourneyPattern de la Route doit être
	 * croissant
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : Les
	 * ServiceJourneyPattern de l'objet Route d'identifiant {objectId} ne
	 * permettent pas de reconstituer la séquence des arrêts de celui-ci
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean check2NeTExSTIFRoute3(Context context, Route route, int lineNumber, int columnNumber) {
		boolean result = true;

		StopPoint stopPointOnError = null;

		for (JourneyPattern jp : route.getJourneyPatterns()) {
			// log.warn("JP  "+ jp.getObjectId() );
			Context objectContext = getObjectContext(context, ServiceJourneyPatternValidator.LOCAL_CONTEXT,
					jp.getObjectId());
			Map<Integer, String> mapOrder = (Map<Integer, String>) objectContext.get(NetexStifConstant.ORDER);
			if (mapOrder != null) {
				Integer position = 1;
				for (StopPoint sp : jp.getStopPoints()) {
					// log.warn("stopPoint("+position+") position "+ sp.getPosition() +", objectId = "+sp.getObjectId());

					String objectId = mapOrder.get(sp.getPosition());
					// log.warn(" check "+ sp.getObjectId() + " with "+ objectId);
					if (!sp.getObjectId().equals(objectId)) {
						// log.warn(" error found");
						result = false;
						stopPointOnError = sp;
                        break;
					}
					position++;
				}
			}
			if (!result) {
				break;
			}
		}


		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(Constant.LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, route);
			validationReporter.addCheckPointReportError(context, null, NetexCheckPoints.L2_NeTExSTIF_Route_3,NetexCheckPoints.L2_NeTExSTIF_Route_3, location,
					stopPointOnError.getPosition().toString());
		}

		return result;
	}

	private boolean compare(Boolean boolAlighting, AlightingPossibilityEnum enumAlighting) {

		if (enumAlighting == null) {
			return true;
		} else if (enumAlighting.equals(AlightingPossibilityEnum.normal)) {
			return boolAlighting.booleanValue();
		} else if (enumAlighting.equals(AlightingPossibilityEnum.forbidden)) {
			return !(boolAlighting.booleanValue());
		} else {
			return false;
		}
	}

	private boolean compare(Boolean boolBoarding, BoardingPossibilityEnum enumBoarding) {

		if (enumBoarding == null) {
			return true;
		} else if (enumBoarding.equals(BoardingPossibilityEnum.normal)) {
			return boolBoarding.booleanValue();
		} else if (enumBoarding.equals(BoardingPossibilityEnum.forbidden)) {
			return !(boolBoarding.booleanValue());
		} else {
			return false;
		}

	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet ServiceJourneyPattern :
	 * Interdictions de montée et descente
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2317">Cartes
	 * #2317</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-Route-4
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Les attributs ForAlighting et ForBoarding d'un
	 * StopPointInJourneyPattern doivent être identiques pour des arrêts
	 * partagés entre plusieurs ServiceJourneyPattern de la même Route
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne}, Les
	 * informations de montée/Descente à l'arrêt {id arrêt} de la Route
	 * {objectId} diffèrent sur plusieurs ServiceJourneyPattern, ces
	 * informations ne sont pas importées
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * Note : si plusieurs ServiceJourneyPattern sont concernées, le message
	 * n'apparait qu'une seule fois
	 *
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean check2NeTExSTIFRoute4(Context context, Route route, int lineNumber, int columnNumber) {
		boolean result = true;

		StopPoint stopPointOnError = null;

		for (JourneyPattern jp : route.getJourneyPatterns()) {
			// log.warn("check2NeTExSTIFRoute4 : jp "+ jp.getObjectId() );

			Context objectContext = getObjectContext(context, ServiceJourneyPatternValidator.LOCAL_CONTEXT,
					jp.getObjectId());
			Map<Integer, Boolean> mapAlighting = (Map<Integer, Boolean>) objectContext.get(NetexStifConstant.FOR_ALIGHTING);
			Map<Integer, Boolean> mapBoarding = (Map<Integer, Boolean>) objectContext.get(NetexStifConstant.FOR_BOARDING);
			for (StopPoint sp : route.getStopPoints()) {
				// log.warn("stopPoint position "+ sp.getPosition() );
				if (mapAlighting != null && mapAlighting.containsKey(sp.getPosition())) {
					Boolean alighting = mapAlighting.get(sp.getPosition());
					// log.warn(" check "+ sp.getForAlighting() + " with "+
					// alighting);
					if (!compare(alighting, sp.getForAlighting())) {
						// log.warn(" error found");
						result = false;
						stopPointOnError = sp;
						break;
					}
				}
				if (mapBoarding != null && mapBoarding.containsKey(sp.getPosition())) {
					Boolean boarding = mapBoarding.get(sp.getPosition());
					// log.warn(" check "+ sp.getForBoarding() + " with "+
					// boarding);
					if (!compare(boarding, sp.getForBoarding())) {
						// log.warn(" error found");
						result = false;
						stopPointOnError = sp;
						break;
					}
				}
			}

			if (!result) {
				break;
			}

		}

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(Constant.LINE);
			Referential referential = (Referential) context.get(Constant.REFERENTIAL);
			String refId = stopPointOnError.getObjectId();
			if (referential != null && !referential.getSharedReadOnlyStopAreas().isEmpty()) {
				// protection from testng conditions
				StopAreaLite stopArea = referential.findStopArea(stopPointOnError.getStopAreaId());
				refId = stopArea.getObjectId();
			}
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, route);
			validationReporter.addCheckPointReportError(context, null, NetexCheckPoints.L2_NeTExSTIF_Route_4,NetexCheckPoints.L2_NeTExSTIF_Route_4, location, refId);

			// clean boarding and alighting data
			for (StopPoint sp : route.getStopPoints()) {
				sp.setForBoarding(null);
				sp.setForAlighting(null);
			}
		}

		return result;
	}

}
