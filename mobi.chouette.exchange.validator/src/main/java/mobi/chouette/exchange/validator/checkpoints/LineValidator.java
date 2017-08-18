package mobi.chouette.exchange.validator.checkpoints;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.Line;

public class LineValidator extends GenericValidator<Line> implements CheckPointConstant {

	private static final String[] codes = { L3_Line_1 };

	@Override
	public void validate(Context context, Line object, ValidateParameters parameters, String transportMode) {
		super.validate(context, object, parameters, transportMode, codes);
	}

	/**
	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de
	 * l'Organisation
	 * <p>
	 * <b>Réference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
	 * <p>
	 * <b>Code</b> :3-Line-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : Ligne disposant de plusieurs itinéraires
	 * <p>
	 * <b>Prédicat</b> : Les itinéraires d'une ligne doivent être associés en aller/retour
	 * <p>
	 * <b>Message</b> : Sur la ligne {nomLigne} ({objectId}), aucun itinéraire n'a d'itinéraire inverse
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
	protected void check3Line1(Context context, Line object, CheckpointParameters parameters) {
		// TODO
	}

}
