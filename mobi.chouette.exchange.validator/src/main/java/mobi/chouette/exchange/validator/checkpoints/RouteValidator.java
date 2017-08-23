package mobi.chouette.exchange.validator.checkpoints;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.Referential;

@Log4j
public class RouteValidator extends GenericValidator<Route> implements CheckPointConstant {

	private static final String[] codes = { L3_Route_1, L3_Route_2, L3_Route_3, L3_Route_5, L3_Route_6, L3_Route_8,
			L3_Route_9, L3_Route_10, L3_Route_11 };

	@Override
	public void validate(Context context, Route object, ValidateParameters parameters, String transportMode) {
		super.validate(context, object, parameters, transportMode, codes);
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Deux arrêts d'une même ZDL ne peuvent pas se
	 * succéder dans un itinéraire
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2092">Cartes
	 * #2092</a>
	 * <p>
	 * <b>Code</b> :3-Route-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Deux arrêts d'une même ZDL ne peuvent pas se succéder
	 * dans un itinéraire
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} dessert successivement les
	 * arrêts {nomArrêt1} ({idArrêt1}) et {nomArrêt2} ({idArrêt2}) de la même
	 * zone de lieu
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3Route1(Context context, Route object, CheckpointParameters parameters) {
		// prerequisites
		List<StopPoint> points = object.getStopPoints();
		if (points.size() < 2)
			return;

		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, L3_Route_1);
		Referential r = (Referential) context.get(REFERENTIAL);
		Long stopId = points.get(0).getStopAreaId();
		StopAreaLite zdep1 = r.findStopArea(stopId);
		if (zdep1 == null) {
			log.error("stop Area ID = " + stopId + " not found for stopPoint rank 0");
			return;
		}
		for (int i = 1; i < points.size(); i++) {
			stopId = points.get(i).getStopAreaId();
			StopAreaLite zdep2 = r.findStopArea(stopId);
			if (zdep2 == null) {
				log.error("stop Area ID = " + stopId + " not found for stopPoint rank " + i);
				return;
			}
			if (zdep2.getParentId() == null) {
				log.error("stop Area ID = " + stopId + " has no parent");
				return;

			}
			if (zdep2.getParentId().equals(zdep1.getParentId())) {
				// error
				DataLocation source = new DataLocation(object);
				DataLocation target1 = new DataLocation(zdep1);
				DataLocation target2 = new DataLocation(zdep2);
				validationReporter.addCheckPointReportError(context, L3_Route_1, source, null, null, target1, target2);
			}
			// prepare next control step
			zdep1 = zdep2;

		}
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Vérification de l'itinéraire inverse
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2093">Cartes
	 * #2093</a>
	 * <p>
	 * <b>Code</b> :3-Route-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : présence d'itinéraire référençant un itinéraire
	 * inverse
	 * <p>
	 * <b>Prédicat</b> : Si l'itinéraire référence un itinéraire inverse,
	 * celui-ci doit :<br>
	 * * référencer l'itinéraire inverse<br>
	 * * avoir un sens opposé à l'itinéraire testé
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} référence un itinéraire retour
	 * {objectId} incohérent
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3Route2(Context context, Route object, CheckpointParameters parameters) {
		// prerequisite
		if (object.getOppositeRoute() == null)
			return;

		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, L3_Route_2);
		Route opposite = object.getOppositeRoute();
		if (opposite.getOppositeRoute() == null || !opposite.getOppositeRoute().equals(object)) {
			// routes pair mismatch
			DataLocation source = new DataLocation(object);
			DataLocation target = new DataLocation(opposite);
			validationReporter.addCheckPointReportError(context, L3_Route_2, source, null, null, target);
		} else if (opposite.getWayBack() == null || object.getWayBack() == null) {
			log.error("wayback is null for route");
		} else if (opposite.getWayBack().equals(object.getWayBack())) {
			// routes direction are same
			DataLocation source = new DataLocation(object);
			DataLocation target = new DataLocation(opposite);
			validationReporter.addCheckPointReportError(context, L3_Route_2, source, null, null, target);
		}

	}

	/**
	 * <b>Titre</b> :[Itinéraire] Présence de missions
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2624">Cartes
	 * #2624</a>
	 * <p>
	 * <b>Code</b> :3-Route-3
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : Néant
	 * <p>
	 * <b>Prédicat</b> : Un itinéraire doit avoir au moins une mission
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} n'a pas de mission
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3Route3(Context context, Route object, CheckpointParameters parameters) {
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, L3_Route_3);

		if (object.getJourneyPatterns().isEmpty()) {
			// route has no journey pattern
			DataLocation source = new DataLocation(object);
			validationReporter.addCheckPointReportError(context, L3_Route_3, source);
		}
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Vérification des terminus de l'itinéraire
	 * inverse
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2120">Cartes
	 * #2120</a>
	 * <p>
	 * <b>Code</b> :3-Route-5
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : présence d'itinéraire référençant un itinéraire
	 * inverse
	 * <p>
	 * <b>Prédicat</b> : Deux itinéraires en aller/retour doivent desservir les
	 * mêmes terminus
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} dessert au départ un arrêt de la
	 * ZDL {nomZDL1} alors que l'itinéraire inverse dessert à l'arrivée un arrêt
	 * de la ZDL {nomZDL2}
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * Note : le test à l'arrivée est équivalent au test au départ sur
	 * l'itinéraire inverse, il est inutile de le faire 2 fois.
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3Route5(Context context, Route object, CheckpointParameters parameters) {
		// Prerequisites
		if (object.getOppositeRoute() == null)
			return;
		List<StopPoint> points = object.getStopPoints();
		if (points.size() < 2)
			return;
		Route opposite = object.getOppositeRoute();
		List<StopPoint> oppositePoints = opposite.getStopPoints();
		if (oppositePoints.size() < 2)
			return;

		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, L3_Route_5);
		Referential r = (Referential) context.get(REFERENTIAL);

		// check only first stop ; last will be checked with opposite route as
		// input
		Long quayRef1 = points.get(0).getStopAreaId();
		Long quayRef2 = oppositePoints.get(oppositePoints.size() - 1).getStopAreaId();
		StopAreaLite zdep1 = r.findStopArea(quayRef1);
		StopAreaLite zdep2 = r.findStopArea(quayRef2);
		if (zdep1 == null || zdep2 == null) {
			log.error("unknown stoparea for route " + object.getId());
		}
		if (!isEqual(zdep1.getParentId(), zdep2.getParentId())) {
			// zdl mismatch
			DataLocation source = new DataLocation(object);
			StopAreaLite zdl1 = r.findStopArea(zdep1.getParentId());
			if (zdl1 == null) {
				zdl1 = zdep1;
				log.error("unknown parent for zdep " + zdep1.getName() + " (" + zdep1.getObjectId() + ")");
			}
			StopAreaLite zdl2 = r.findStopArea(zdep2.getParentId());
			if (zdl2 == null) {
				zdl2 = zdep2;
				log.error("unknown parent for zdep " + zdep2.getName() + " (" + zdep2.getObjectId() + ")");
			}
			DataLocation target1 = new DataLocation(zdl1);
			DataLocation target2 = new DataLocation(zdl2);
			validationReporter.addCheckPointReportError(context, L3_Route_5, source, null, null, target1, target2);
		}

	}

	/**
	 * <b>Titre</b> :[Itinéraire] Un itinéraire doit contenir au moins 2 arrêts
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2097">Cartes
	 * #2097</a>
	 * <p>
	 * <b>Code</b> :3-Route-6
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Un itinéraire doit référencer au moins 2 arrêts
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} ne dessert pas assez d'arrêts
	 * (minimum 2 requis)
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3Route6(Context context, Route object, CheckpointParameters parameters) {
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, L3_Route_6);

		if (object.getStopPoints().size() < 2) {
			// route has not enough stopPoints
			DataLocation source = new DataLocation(object);
			validationReporter.addCheckPointReportError(context, L3_Route_6, source);
		}
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Utilisation des arrêts par les missions
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2099">Cartes
	 * #2099</a>
	 * <p>
	 * <b>Code</b> :3-Route-8
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Les arrêts de l'itinéraire doivent être desservis par
	 * au moins une mission
	 * <p>
	 * <b>Message</b> : l'arrêt {nomArrêt} ({idArrêt}) de l'itinéraire
	 * {objectId} n'est desservi par aucune mission
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3Route8(Context context, Route object, CheckpointParameters parameters) {
		// prerequisites
		if (object.getJourneyPatterns().isEmpty())
			return;
		if (object.getStopPoints().size() < 2)
			return;

		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, L3_Route_8);

		Set<StopPoint> undeservedStops = new HashSet<>(object.getStopPoints());
		for (JourneyPattern journeyPattern : object.getJourneyPatterns()) {
			undeservedStops.removeAll(journeyPattern.getStopPoints());
		}
		if (undeservedStops.size() > 0) {
			Referential r = (Referential) context.get(REFERENTIAL);
			DataLocation source = new DataLocation(object);
			for (StopPoint stopPoint : undeservedStops) {
				StopAreaLite zdep = r.findStopArea(stopPoint.getStopAreaId());
				DataLocation target = new DataLocation(zdep);
				validationReporter.addCheckPointReportError(context, L3_Route_8, source, null, null, target);
			}
		}

	}

	/**
	 * <b>Titre</b> :[Itinéraire] Existence d'une mission passant par tous les
	 * arrêts de l'itinéraire
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2100">Cartes
	 * #2100</a>
	 * <p>
	 * <b>Code</b> :3-Route-9
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Une mission de l'itinéraire devrait desservir
	 * l'ensemble des arrêts de celui-ci
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} n'a aucune mission desservant
	 * l'ensemble de ses arrêts
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3Route9(Context context, Route object, CheckpointParameters parameters) {
		// prerequisites
		if (object.getJourneyPatterns().isEmpty())
			return;
		if (object.getStopPoints().size() < 2)
			return;

		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, L3_Route_9);

		boolean found = false;
		for (JourneyPattern journeyPattern : object.getJourneyPatterns()) {
			if (journeyPattern.getStopPoints().size() == object.getStopPoints().size()) {
				found = true;
				break;
			}
		}
		if (!found) {
			// no journeypattern for complete route stops found
			DataLocation source = new DataLocation(object);
			validationReporter.addCheckPointReportError(context, L3_Route_9, source);
		}

	}

	/**
	 * <b>Titre</b> :[Itinéraire] Itinéraire & arrêt désactivé
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2101">Cartes
	 * #2101</a>
	 * <p>
	 * <b>Code</b> :3-Route-10
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Les arrêts d'un itinéraire ne doivent pas être
	 * désactivés
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} référence un arrêt (ZDEp)
	 * désactivé {nomArrêt} ({idArret})
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3Route10(Context context, Route object, CheckpointParameters parameters) {
		// prerequisites
		if (object.getJourneyPatterns().isEmpty())
			return;
		if (object.getStopPoints().isEmpty())
			return;

		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, L3_Route_10);
		Referential r = (Referential) context.get(REFERENTIAL);
		
		for (StopPoint stopPoint : object.getStopPoints()) {
			StopAreaLite zdep = r.findStopArea(stopPoint.getStopAreaId());
			if (zdep != null && zdep.getDeletedTime() != null)
			{
				// deleted stopArea
				DataLocation source = new DataLocation(object);
				DataLocation target = new DataLocation(zdep);
				validationReporter.addCheckPointReportError(context, L3_Route_10, source,null,null,target);
			}
		}
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Présence de tracé
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2419">Cartes
	 * #2419</a>
	 * <p>
	 * <b>Code</b> :3-Route-11
	 * <p>
	 * <b>Variables</b> : un ou plusieurs mode(s) de transport
	 * <p>
	 * <b>Prérequis</b> : Le mode de transport de la ligne correspond à celui
	 * demandé par le contrôle
	 * <p>
	 * <b>Prédicat</b> : Un itinéraire doit avoir un tracé
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} n'a pas de tracé associé
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3Route11(Context context, Route object, CheckpointParameters parameters) {
		// TODO : waiting for data model
		
		return;
	}

}
