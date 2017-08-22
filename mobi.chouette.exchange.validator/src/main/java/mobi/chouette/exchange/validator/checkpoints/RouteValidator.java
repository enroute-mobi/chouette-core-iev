package mobi.chouette.exchange.validator.checkpoints;

import java.util.List;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.Referential;

@Log4j
public class RouteValidator extends GenericValidator<Route> implements CheckPointConstant {

	private static final String[] codes = { L3_Route_1, L3_Route_2, L3_Route_3, L3_Route_4, L3_Route_5, L3_Route_6,
			L3_Route_7, L3_Route_8, L3_Route_9, L3_Route_10, L3_Route_11 };

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
		List<StopPoint> points = object.getStopPoints();
		if (points.size() > 1) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			validationReporter.prepareCheckPointReport(context, L3_Route_1);
			Referential r =(Referential) context.get(REFERENTIAL);
			Long stopId = points.get(0).getStopAreaId();
			StopAreaLite zdep1 = r.findStopArea(stopId);
			if (zdep1 == null)
			{
				log.error("stop Area ID = "+stopId+ " not found for stopPoint rank 0");
				return;
			}
			for (int i = 1; i < points.size(); i++)
			{
				stopId = points.get(i).getStopAreaId();
				StopAreaLite zdep2 = r.findStopArea(stopId);
				if (zdep2 == null)
				{
					log.error("stop Area ID = "+stopId+ " not found for stopPoint rank "+i);
					return;
				}
				if (zdep2.getParentId() == null)
				{
					log.error("stop Area ID = "+stopId+ " has no parent");
					return;
					
				}
				if (zdep2.getParentId().equals(zdep1.getParentId()))
				{
					// error
					DataLocation source = new DataLocation(object);
					DataLocation target1 = new DataLocation(zdep1);
					DataLocation target2 = new DataLocation(zdep2);
					validationReporter.addCheckPointReportError(context, L3_Route_1, source,null,null,target1,target2);
				}
				// prepare next control step
				zdep1 = zdep2;
			}
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
		// TODO
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
		// TODO
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Détection de double définition d'itinéraire
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2095">Cartes
	 * #2095</a>
	 * <p>
	 * <b>Code</b> :3-Route-4
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : 2 itinéraires ne doivent pas desservir strictement les
	 * mêmes arrêts dans le même ordre avec les mêmes critères de monté/descente
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} est identique à l'itinéraire
	 * {objectid}
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
	protected void check3Route4(Context context, Route object, CheckpointParameters parameters) {
		// TODO
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
		// TODO
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
		// TODO
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Un itinéraire doit contenir au moins 1 mission
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2098">Cartes
	 * #2098</a>
	 * <p>
	 * <b>Code</b> :3-Route-7
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Un itinéraire doit posséder au moins une mission
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} n'a aucune mission
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
	protected void check3Route7(Context context, Route object, CheckpointParameters parameters) {
		// TODO
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
		// TODO
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
		// TODO
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
		// TODO
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
		// TODO
	}

}
