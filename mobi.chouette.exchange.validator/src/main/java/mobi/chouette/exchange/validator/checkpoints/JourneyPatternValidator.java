package mobi.chouette.exchange.validator.checkpoints;


import java.util.List;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.VehicleJourney;

@Log4j
public class JourneyPatternValidator extends GenericValidator<JourneyPattern> implements CheckPointConstant {

	private static final String[] codes = { L3_JourneyPattern_1, L3_JourneyPattern_2,  L3_VehicleJourney_3 };

	@Override
	public void validate(Context context, JourneyPattern object, ValidateParameters parameters, String transportMode) {
		super.validate(context, object, parameters, transportMode, codes);
	}

	/**
	 * <b>Titre</b> :[Mission] Doublon de missions dans une ligne
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2102">Cartes #2102</a>
	 * <p>
	 * <b>Code</b> :3-JourneyPattern-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Deux missions de la même ligne ne doivent pas desservir les mêmes arrêts dans le même ordre
	 * <p>
	 * <b>Message</b> : La mission {objectId} est identique à la mission {objectId}
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
	protected void check3JourneyPattern1(Context context, JourneyPattern object, CheckpointParameters parameters) {
		// TODO
	}

	/**
	 * <b>Titre</b> :[Mission] Présence de courses
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2625">Cartes #2625</a>
	 * <p>
	 * <b>Code</b> :3-JourneyPattern-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Une mission doit avoir au moins une course
	 * <p>
	 * <b>Message</b> : La mission {objectId} n'a pas de course
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
	protected void check3JourneyPattern2(Context context, JourneyPattern object, CheckpointParameters parameters) {
		List<VehicleJourney> vjs = object.getVehicleJourneys();
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, L3_JourneyPattern_2);
		if (vjs == null || vjs.size() < 1) {
			log.error("JourneyPattern " + object.getObjectId() + " has less than 1 VehicleJourney");
			DataLocation source = new DataLocation(object);
			validationReporter.addCheckPointReportError(context, L3_JourneyPattern_2, source);
		}
	}
	
	/**
	 * <b>Titre</b> :[Course] Les vitesses entre 2 arrêts doivent être similaires pour toutes les courses d'une même
	 * mission
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2105">Cartes #2105</a>
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
	 * Note : Le calcul se fait à l'aide d'un temps de parcours moyen calculé sur toutes les courses de la mission puis
	 * d'une mesure de l'écart entre les temps de parcours de chaque course avec ce temps moyen
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3VehicleJourney3(Context context, JourneyPattern object, CheckpointParameters parameters) {
		// TODO
	}


}
