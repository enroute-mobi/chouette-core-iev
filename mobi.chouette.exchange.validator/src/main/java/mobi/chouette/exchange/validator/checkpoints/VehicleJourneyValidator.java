package mobi.chouette.exchange.validator.checkpoints;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.VehicleJourney;

public class VehicleJourneyValidator extends GenericValidator<VehicleJourney> implements CheckPointConstant {

	private static final String[] codes = { L3_VehicleJourney_1, L3_VehicleJourney_2, L3_VehicleJourney_3,
			L3_VehicleJourney_4, L3_VehicleJourney_5 };

	@Override
	public void validate(Context context, VehicleJourney object, ValidateParameters parameters, String transportMode) {
		super.validate(context, object, parameters, transportMode, codes);
	}

	/**
	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de
	 * l'Organisation
	 * <p>
	 * <b>Réference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
	 * <p>
	 * <b>Code</b> :3-VehicleJourney-1
	 * <p>
	 * <b>Variables</b> : * durée d'attente maximale à un arrêt en minutes, <br>
	 * 
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : La durée d?attente à un arrêt ne doit pas être trop grande
	 * <p>
	 * <b>Message</b> : Sur la course {ObjectId}, le temps d'attente {valeur} à l'arrêt {nomArrêt} ({objectId}) est
	 * supérieur au seuil toléré ({tempsMax})
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * Note :
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3VehicleJourney1(Context context, VehicleJourney object, CheckpointParameters parameters) {
		// TODO
	}

	/**
	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de
	 * l'Organisation
	 * <p>
	 * <b>Réference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
	 * <p>
	 * <b>Code</b> :3-VehicleJourney-2
	 * <p>
	 * <b>Variables</b> : * vitesse minimale (km/h), <br>
	 * * vitesse maximale (km/h),<br>
	 * 
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : La vitesse entre deux arrêts doit être dans une fourchette paramétrable
	 * <p>
	 * <b>Message</b> : Sur la course {ObjectId}, la vitesse calculée {valeur} entre les arrêts {nomArrêt1} ({objectId})
	 * et {nomArrêt2} ({objectId}) est supérieur au seuil toléré ({vitesseMax})<br>
	 * Sur la course {ObjectId}, la vitesse calculée {valeur} entre les arrêts {nomArrêt1} ({objectId}) et {nomArrêt2}
	 * ({objectId}) est inférieure au seuil toléré ({vitesseMin})
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * Note :
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3VehicleJourney2(Context context, VehicleJourney object, CheckpointParameters parameters) {
		// TODO
	}

	/**
	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de
	 * l'Organisation
	 * <p>
	 * <b>Réference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
	 * <p>
	 * <b>Code</b> :3-VehicleJourney-3
	 * <p>
	 * <b>Variables</b> : * écart maximal en minutes
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Les temps de parcours entre 2 arrêts successifs doivent être similaires pour toutes les courses
	 * d?une même mission
	 * <p>
	 * <b>Message</b> : Le temps de parcours sur la course {objectId} entre les arrêts {nomArrêt1} ({objectId}) et
	 * {nomArrêt2} ({objectId}) s'écarte de {écart} du temps moyen constaté sur la mission
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * Note :
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3VehicleJourney3(Context context, VehicleJourney object, CheckpointParameters parameters) {
		// TODO
	}

	/**
	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de
	 * l'Organisation
	 * <p>
	 * <b>Réference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
	 * <p>
	 * <b>Code</b> :3-VehicleJourney-4
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Une course doit avoir au moins un calendrier d?application
	 * <p>
	 * <b>Message</b> : La course {objecId} n'a pas de calendrier d'application
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * Note :
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3VehicleJourney4(Context context, VehicleJourney object, CheckpointParameters parameters) {
		// TODO
	}

	/**
	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de
	 * l'Organisation
	 * <p>
	 * <b>Réference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
	 * <p>
	 * <b>Code</b> :3-VehicleJourney-5
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'horaire d'arrivée à un arrêt doit être antérieur à l'horaire de départ de cet arrêt ET les
	 * horaires de départ aux arrêts doivent être dans l'ordre chronologique croissant.
	 * <p>
	 * <b>Message</b> : La course {objectId} a un horaire d'arrivé {hh:mm} supérieur à l'horaire de départ {hh:mm} à
	 * l'arrêt {nomArrêt} ({objectId}) <br>
	 * La course {objectId} a un horaire de départ {hh:mm} à l'arrêt {nomArrêt} ({objectId}) supérieur à l'horaire
	 * d'arrivé {hh:mm} à l'arrêt suivant
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * Note :
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3VehicleJourney5(Context context, VehicleJourney object, CheckpointParameters parameters) {
		// TODO
	}

}
