package mobi.chouette.exchange.validator.checkpoints;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.Referential;

@Log4j
public class LineValidator extends GenericValidator<LineLite> {

	private static final String HAS_NO_ROUTE = " has no route !";
	private static final String LINE_ID = "Line ID = ";
	private static final String[] codes = { CheckPointConstant.L3_Line_1, CheckPointConstant.L3_Route_4,
			CheckPointConstant.L3_JourneyPattern_1 };

	@Override
	public void validate(Context context, LineLite object, ValidateParameters parameters, String transportMode) {
		super.validate(context, object, parameters, transportMode, codes);
	}

	/**
	 * <b>Titre</b> :[Ligne] Appariement des itinéraires
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2121">Cartes
	 * #2121</a>
	 * <p>
	 * <b>Code</b> :3-Line-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : Ligne disposant de plusieurs itinéraires
	 * <p>
	 * <b>Prédicat</b> : Les itinéraires d'une ligne doivent être associés en
	 * aller/retour
	 * <p>
	 * <b>Message</b> : Sur la ligne {nomLigne} ({objectId}), aucun itinéraire
	 * n'a d'itinéraire inverse
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

		Referential ref = (Referential) context.get(Constant.REFERENTIAL);
		Collection<Route> routes = ref.getRoutes().values();
		if (routes.size() > 1) { // -- Prérequis : Ligne disposant de plusieurs
									// itinéraires

			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			validationReporter.prepareCheckPointReport(context, parameters.getSpecificCode());
			for (Route r : routes) {
				Route opposite = r.getOppositeRoute();
				if (opposite == null) {
					DataLocation source = new DataLocation(object);
					validationReporter.addCheckPointReportError(context, parameters.getCheckId(),
							parameters.getSpecificCode(), CheckPointConstant.L3_Line_1, source);
				}
			}
		} else {
			log.error(LINE_ID + object.getId() + HAS_NO_ROUTE);
			// throw new ValidationException(LINE_ID + object.getId() +
			// HAS_NO_ROUTE);
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
		Referential ref = (Referential) context.get(Constant.REFERENTIAL);
		Collection<Route> routes = ref.getRoutes().values();

		String checkPointName = CheckPointConstant.L3_Route_4;
		if (routes.size() > 1) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			validationReporter.prepareCheckPointReport(context, parameters.getSpecificCode());
			Map<String, Route> map = new HashMap<>();
			for (Route r : routes) {
				List<StopPoint> stoppoints = r.getStopPoints();
				StringBuilder b = new StringBuilder();
				for (StopPoint sp : stoppoints) {
					if (b.length() > 0) {
						b.append(",");
					}
					b.append("[");
					b.append(sp.getStopAreaId());
					b.append("/fa:");
					b.append(sp.getForAlighting());
					b.append("/fb:");
					b.append(sp.getForBoarding());
					b.append("]");
				}
				String key = b.toString();
				Route sameExist = map.get(key);
				if (sameExist != null) {
					DataLocation source = new DataLocation(object);
					DataLocation target = new DataLocation(sameExist);
					validationReporter.addCheckPointReportError(context, parameters.getCheckId(),
							parameters.getSpecificCode(), checkPointName, source, null, null, target);
				} else {
					map.put(key, r);
				}
			}
		} else {
			log.error(LINE_ID + object.getId() + HAS_NO_ROUTE);
			// throw new ValidationException(LINE_ID + object.getId() +
			// HAS_NO_ROUTE);
		}

	}

	/**
	 * <b>Titre</b> :[Mission] Doublon de missions dans une ligne
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2102">Cartes
	 * #2102</a>
	 * <p>
	 * <b>Code</b> :3-JourneyPattern-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Deux missions de la même ligne ne doivent pas desservir
	 * les mêmes arrêts dans le même ordre
	 * <p>
	 * <b>Message</b> : La mission {objectId} est identique à la mission
	 * {objectId}
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
	protected void check3JourneyPattern1(Context context, LineLite object, CheckpointParameters parameters) {
		Referential ref = (Referential) context.get(Constant.REFERENTIAL);
		String checkPointName = CheckPointConstant.L3_JourneyPattern_1;
		Collection<JourneyPattern> journeyPatterns = ref.getJourneyPatterns().values();
		Map<String, JourneyPattern> map = new HashMap<>();
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, parameters.getSpecificCode());
		journeyPatterns.stream().forEach(jp -> {
			List<StopPoint> stoppoints = jp.getStopPoints();
			StringBuilder b = new StringBuilder();
			for (StopPoint sp : stoppoints) {
				if (b.length() > 0) {
					b.append(",");
				}
				b.append("[");
				b.append(sp.getObjectId());
				b.append("/fa:");
				b.append(sp.getForAlighting());
				b.append("/fb:");
				b.append(sp.getForBoarding());
				b.append("]");
			}
			String key = b.toString();
			JourneyPattern sameExist = map.get(key);
			if (sameExist != null) {
				DataLocation source = new DataLocation(object);
				DataLocation target = new DataLocation(sameExist);
				validationReporter.addCheckPointReportError(context, parameters.getCheckId(),
						parameters.getSpecificCode(), checkPointName, source, null, null, target);
			} else {
				map.put(key, jp);
			}

		});
	}

}
