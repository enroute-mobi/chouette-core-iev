package mobi.chouette.exchange.validator.checkpoints;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.JourneyPattern;

public class JourneyPatternValidator extends GenericValidator<JourneyPattern> implements CheckPointConstant {

	private static final String[] codes = { L3_JourneyPattern_1, L3_JourneyPattern_2 };

	@Override
	public void validate(Context context, JourneyPattern object, ValidateParameters parameters, String transportMode) {
		super.validate(context, object, parameters, transportMode, codes);
	} 

	/**
	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de
	 * l'Organisation
	 * <p>
	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
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
	 * Note :
	 *
	 * @param context
	 * @return
	 */
	private void check3JourneyPattern1(Context context, JourneyPattern object, CheckpointParameters parameters) {
		// TODO
	}

	/**
	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de
	 * l'Organisation
	 * <p>
	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
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
	 * Note :
	 *
	 * @param context
	 * @return
	 */
	private void check3JourneyPattern2(Context context, JourneyPattern object, CheckpointParameters parameters) {
		// TODO
	}

}
