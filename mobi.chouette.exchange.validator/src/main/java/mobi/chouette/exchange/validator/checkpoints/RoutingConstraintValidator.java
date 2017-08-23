package mobi.chouette.exchange.validator.checkpoints;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.exchange.validator.ValidationException;
import mobi.chouette.model.Route;
import mobi.chouette.model.RoutingConstraint;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.Referential;

@Log4j
public class RoutingConstraintValidator extends GenericValidator<RoutingConstraint> implements CheckPointConstant {

	private static final String[] codes = { L3_ITL_1, L3_ITL_2, L3_ITL_3 };

	@Override
	public void validate(Context context, RoutingConstraint object, ValidateParameters parameters,
			String transportMode) {
		super.validate(context, object, parameters, transportMode, codes);
	}

	/**
	 * <b>Titre</b> :[ITL] ITL & arret désactivé
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2109">Cartes #2109</a>
	 * <p>
	 * <b>Code</b> :3-ITL-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Les arrêts d'une ITL ne doivent pas être désactivés
	 * <p>
	 * <b>Message</b> : L'ITL {objectId} référence un arrêt (ZDEp) désactivé {nomArrêt} ({idArret})
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
	protected void check3ITL1(Context context, RoutingConstraint object, CheckpointParameters parameters) {
		Long[] spIds = object.getStopPointIds();
		Referential ref = (Referential) context.get(REFERENTIAL);

		Route route = object.getRoute();
		if (route == null) {
			// -- RoutingConstraint has no Route !
			throw new ValidationException("RoutingConstraint id = " + object.getId() + " : has no Route !");
		}

		List<StopPoint> spList = route.getStopPoints();
		Map<Long, StopPoint> spMap = spList.stream().collect(Collectors.toMap(StopPoint::getId, Function.identity()));
		for (Long id : spIds) {
			StopPoint p = spMap.get(id);
			if (p == null) {
				// -- RoutingConstraint references a point not present in Route !
				throw new ValidationException("RoutingConstraint id = " + object.getId() + " : stop ID = " + id
						+ " not found in Route  id = " + object.getRoute().getId());

			}
			StopAreaLite sa = ref.findStopArea(p.getStopAreaId());
			if (sa.isDesactivated()) {
				ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
				validationReporter.prepareCheckPointReport(context, L3_ITL_1);
				// log.error("ITL " + object.getObjectId() + " contains disabled stop " + sa.getObjectId());
				DataLocation source = new DataLocation(object);
				DataLocation target = new DataLocation(sa);
				validationReporter.addCheckPointReportError(context, L3_ITL_1, source, null, null, target);
			}

		}
	}

	/**
	 * <b>Titre</b> :[ITL] Couverture de l'itinéraire
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2510">Cartes #2510</a>
	 * <p>
	 * <b>Code</b> :3-ITL-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Une ITL ne peut pas couvrir l'ensemble des arrêts de l'itinéraire
	 * <p>
	 * <b>Message</b> : L'ITL {objectId} couvre tous les arrêts de l'itinéraire {ObjectId}.
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
	protected void check3ITL2(Context context, RoutingConstraint object, CheckpointParameters parameters) {
		Long[] spIds = object.getStopPointIds();
		boolean result = true;

		Route route = object.getRoute();
		int spRouteCount = route.getStopPoints().size();
		if (spRouteCount == spIds.length) {
			List<Long> rcStopPointlist = Arrays.asList(spIds);

			List<StopPoint> intersectSet = route.getStopPoints().stream()
					.filter(p -> rcStopPointlist.contains(p.getId())).collect(Collectors.toList());
			if (intersectSet.size() == rcStopPointlist.size()) {
				result = false;
			}
		}

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			validationReporter.prepareCheckPointReport(context, L3_ITL_2);
			log.error("ITL " + object.getObjectId() + " contains all route stops");
			DataLocation source = new DataLocation(object);
			validationReporter.addCheckPointReportError(context, L3_ITL_2, source);
		}
	}

	/**
	 * <b>Titre</b> :[ITL] Définition minimale d'une ITL
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2511">Cartes #2511</a>
	 * <p>
	 * <b>Code</b> :3-ITL-3
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Une ITL doit référencer au moins 2 arrêts
	 * <p>
	 * <b>Message</b> : L'ITL {ObjectId} n'a pas suffisament d'arrêts (minimum 2 arrêts requis)
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
	protected void check3ITL3(Context context, RoutingConstraint object, CheckpointParameters parameters) {
		Long[] spIds = object.getStopPointIds();
		boolean result = true;
		if (spIds == null) {
			// -- RoutingConstaint has no StopPoint
			result = false;
		} else {
			Set<Long> mySet = new HashSet<Long>(Arrays.asList(spIds));
			if (mySet.size() < 2) {
				// -- RoutingConstaint has less than 2 StopPoints
				result = false;
			} else {
				Route route = object.getRoute();
				if (route == null) {
					// -- RoutingConstraint has no Route !
					throw new ValidationException("RoutingConstraint id = " + object.getId() + " : has no Route !");
				}

				List<StopPoint> spList = route.getStopPoints();
				Map<Long, StopPoint> spMap = spList.stream()
						.collect(Collectors.toMap(StopPoint::getId, Function.identity()));
				for (Long id : spIds) {
					StopPoint p = spMap.get(id);
					if (p == null) {
						// -- RoutingConstraint references a point not present in Route !
						throw new ValidationException("RoutingConstraint id = " + object.getId() + " : stop ID = " + id
								+ " not found in Route  id = " + object.getRoute().getId());

					}
				}
			}
		}

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			validationReporter.prepareCheckPointReport(context, L3_ITL_3);
			log.error("ITL " + object.getObjectId() + " has not enough stops");
			DataLocation source = new DataLocation(object);
			validationReporter.addCheckPointReportError(context, L3_ITL_3, source);
		}
	}

}
