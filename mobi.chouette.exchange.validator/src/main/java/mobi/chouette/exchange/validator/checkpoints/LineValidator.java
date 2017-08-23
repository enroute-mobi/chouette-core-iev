package mobi.chouette.exchange.validator.checkpoints;

import java.util.Collection;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.util.Referential;

@Log4j
public class LineValidator extends GenericValidator<LineLite> implements CheckPointConstant {

	private static final String[] codes = { L3_Line_1, L3_Route_4 };

	@Override
	public void validate(Context context, LineLite object, ValidateParameters parameters, String transportMode) {
		super.validate(context, object, parameters, transportMode, codes);
	}

	/**
	 * <b>Titre</b> :[Ligne] Appariement des itinéraires
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2121">Cartes #2121</a>
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
	 * 
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3Line1(Context context, LineLite object, CheckpointParameters parameters) {
		
		Referential ref = (Referential) context.get(REFERENTIAL);
		Collection<Route> routes = ref.getRoutes().values();

		if (routes.size() > 1) { // -- Prérequis : Ligne disposant de plusieurs itinéraires

			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			validationReporter.prepareCheckPointReport(context, L3_Line_1);
			for (Route r : routes) {
				Route opposite = r.getOppositeRoute();
				if (opposite == null ) {
					log.error("Route " + r.getObjectId()  + " of Line "+ object.getObjectId()+" has incorrect opposite Route");
					DataLocation source = new DataLocation(object);
					validationReporter.addCheckPointReportError(context, L3_Line_1, source);
				} 
			}
		} 

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
	 *            objet à contrôler (ligne conteneur)
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3Route4(Context context, LineLite object, CheckpointParameters parameters) {
		// TODO à déplacer dans LineValidator
	}

}
