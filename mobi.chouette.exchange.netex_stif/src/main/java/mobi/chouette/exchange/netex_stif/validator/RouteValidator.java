package mobi.chouette.exchange.netex_stif.validator;

import java.util.List;
import java.util.Optional;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopPoint;

public class RouteValidator extends AbstractValidator {

	public static final String LOCAL_CONTEXT = "Route";


	@Override
	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_Route_1);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_Route_2);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_Route_3);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_Route_4);
	}

	
	public void addInverseRouteRef(Context context, String objectId, String inverseRouteRef)
	{
		Context objectContext = getObjectContext(context, LOCAL_CONTEXT, objectId);
		objectContext.put(INVERSE_ROUTE_REF, inverseRouteRef);
	}
	/**
	 * <a target="_blank" href="https://projects.af83.io/issues/2308" >Carte #2308</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-Route-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : Attribut DirectionType renseigné
	 * <p>
	 * <b>Prédicat</b> : L'attribut DirectionType doit prendre l'une des 2 valeurs 'outbound' ou 'inbound'
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet Route d'identifiant {objectId} a une valeur
	 * de l'attribut DirectionType interdite : {directionType}
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 * @param context
	 * @param route
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	public boolean check2NeTExSTIFRoute1(Context context, Route route, int lineNumber, int columnNumber) {
		boolean result = true;
		String wayback = route.getWayBack();
		if (wayback == null || (!wayback.equals("outbound") && !wayback.equals("inbound"))) {
			result = false;
		}

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, route);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_Route_1, location, wayback);
		}
		return result;
	}

	/**
	 * <a target="_blank" href="https://projects.af83.io/issues/2309">Cartes #2309</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-Route-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : Attribut InverseRouteRef renseigné
	 * <p>
	 * <b>Prédicat</b> : Les Routes associées comme routes inverses doivent se référencer mutuellement. Les
	 * DirectionType des Routes en sens opposés doivent être différent (Note : DirectionType non renseigné = outbound)
	 * <p>
	 * <b>Message</b> :
	 * <ul>
	 * <li>1. {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet Route d'identifiant {objectId} référence un objet
	 * Route inverse {InverseRouteRef.ref} qui ne le référence pas</li>
	 * <li>2. {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet Route d'identifiant {objectId} référence un objet
	 * Route inverse {InverseRouteRef.ref} de même DirectionType</li>
	 * </ul>
	 * Criticité : warning
	 * <p>
	 * @see #addInverseRouteRef(Context, String, String) to preserve InverseRouteRef value
	 * 
	 * @param context
	 * @param route
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	public boolean check2NeTExSTIFRoute2_1(Context context, Route route, int lineNumber, int columnNumber) {
		boolean result = true;

		if (route.getOppositeRoute() != null) 
		{
			// récupération du contexte de validation
			Context validationContext = (Context) context.get(VALIDATION_CONTEXT);
			// récupération du sous contexte de validation des Routes (LOCAL_CONTEXT) 
			Context localContext = (Context) validationContext.get(LOCAL_CONTEXT);
			// récupération du contexte de l'objet Route 'wayback'
            Context waybackContext = (Context) localContext.get(route.getOppositeRoute().getObjectId());
            // récupération de la valeur d'attribut sauvegardée
            String wayBackInverseRouteRef = (String) waybackContext.get(INVERSE_ROUTE_REF);
            result = !wayBackInverseRouteRef.equals(route.getObjectId());
		}

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, route);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_Route_2, "1", location,
					route.getOppositeRoute().getObjectId());
		}

		return result;
	}

	public boolean check2NeTExSTIFRoute2_2(Context context, Route route, int lineNumber, int columnNumber) {
		boolean result = true;

		String wayback = "outbound";
		String oppositeWayback = "outbound";

		if (route.getWayBack() != null) {
			wayback = route.getWayBack();
		}
		if (route.getOppositeRoute() != null && route.getOppositeRoute().getWayBack() != null) {
			oppositeWayback = route.getOppositeRoute().getWayBack();
		}

		if (wayback.equals(oppositeWayback)) {
			result = false;
		}

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, route);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_Route_2, "2", location,
					route.getOppositeRoute().getObjectId());
		}

		return result;
	}

	/**
	 * <a target="_blank" href="https://projects.af83.io/issues/2310">Cartes #2310</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-Route-3
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'ordre des arrêts (ScheduledStopPoint) issus des StopPointInJourneyPattern des
	 * ServiceJourneyPattern de la Route doit être croissant
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : Les ServiceJourneyPattern de l'objet Route
	 * d'identifiant {objectId} ne permettent pas de reconstituer la séquence des arrêts de celui-ci
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 * @param context
	 * @param route
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	public boolean check2NeTExSTIFRoute3(Context context, Route route, int lineNumber, int columnNumber) {
		boolean result = true;

		List<StopPoint> list = route.getStopPoints();
		Integer lastPosition = 0;
		for (StopPoint p : list) {
			if (lastPosition > p.getPosition()) {
				result = false;
				break;
			}
			lastPosition = p.getPosition();
		}

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, route);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_Route_3, location, lastPosition.toString());
		}

		return result;
	}

	/**
	 * <a target="_blank" href="https://projects.af83.io/issues/2317">Cartes #2317</a>
	 * 
	 * <b>Code</b> : 2-NeTExSTIF-Route-4
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Les attributs ForAlighting et ForBoarding d'un StopPointInJourneyPattern doivent être
	 * identiques pour des arrêts partagés entre plusieurs ServiceJourneyPattern de la même Route
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne}, Les informations de montée/Descente à l'arrêt {id
	 * arrêt} de la Route {objectId} diffèrent sur plusieurs ServiceJourneyPattern, ces informations ne sont pas
	 * importées
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * Note : si plusieurs ServiceJourneyPattern sont concernées, le message n'apparait qu'une seule fois.
	 *
	 * @param context
	 * @param route
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	public boolean check2NeTExSTIFRoute4(Context context, Route route, int lineNumber, int columnNumber) {
		Boolean result = true;

		result = route.getStopPoints().stream().anyMatch(sp -> {

			Optional<JourneyPattern> incorrectJourneyPattern = route.getJourneyPatterns().stream().filter(
					jp -> (!sp.getForAlighting().equals(jp.getStopPoints().get(sp.getPosition()).getForAlighting()))
							|| (!sp.getForBoarding().equals(jp.getStopPoints().get(sp.getPosition()).getForBoarding())))
					.findFirst();

			return incorrectJourneyPattern.isPresent();

		});

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_Route_4, location, route.getObjectId());
		}

		return result;
	}
}
