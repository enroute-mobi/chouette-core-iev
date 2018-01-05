package mobi.chouette.exchange.validator.checkpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.util.Referential;

public class JourneyPatternValidator extends GenericValidator<JourneyPattern>  {

	private static final String[] codes = { 
			CheckPointConstant.L3_JourneyPattern_2, 
			CheckPointConstant.L3_VehicleJourney_3 };

	@Override
	public void validate(Context context, JourneyPattern object, ValidateParameters parameters, String transportMode) {
		super.validate(context, object, parameters, transportMode, codes);
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
		validationReporter.prepareCheckPointReport(context, CheckPointConstant.L3_JourneyPattern_2);
		if (vjs == null || vjs.size() < 1) {
			DataLocation source = new DataLocation(object);
			validationReporter.addCheckPointReportError(context, parameters.getCheckId(), CheckPointConstant.L3_JourneyPattern_2, source);
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
		List<VehicleJourney> vj = object.getVehicleJourneys();
		Referential ref = (Referential) context.get(Constant.REFERENTIAL);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, CheckPointConstant.L3_VehicleJourney_3);
		Map<String, List<TravelTime>> travelTimeMap = new HashMap<String, List<TravelTime>>();

		vj.stream().forEach(v -> {
			VehicleJourneyAtStop previous = null;
			List<VehicleJourneyAtStop> list = v.getVehicleJourneyAtStops();
			for (VehicleJourneyAtStop vjs : list) {
				if (previous != null) {
					updateSpeed(v, previous, vjs, travelTimeMap);
				}
				previous = vjs;
			}
		});
		String tmp = parameters.getMaximumValue();
		Long threshold = Long.parseLong(tmp);

		travelTimeMap.keySet().stream().forEach(s -> {
			final Long average = (long) travelTimeMap.get(s).stream().mapToLong(t -> t.getDelta()).average()
					.getAsDouble();
			List<TravelTime> list = travelTimeMap.get(s).stream()
					.filter(tt -> (tt.getDelta() > (average + threshold)) || (tt.getDelta() < (average - threshold)))
					.collect(Collectors.toList());
			list.stream().forEach(tt -> {
				DataLocation source = new DataLocation(tt.getVehicleJourney());
				StopAreaLite sa1 = ref.findStopArea(tt.getVehicleJourneyAtStop1().getStopPoint().getStopAreaId());
				StopAreaLite sa2 = ref.findStopArea(tt.getVehicleJourneyAtStop2().getStopPoint().getStopAreaId());
				long delta = Math.abs(tt.getDelta() - average);
				DataLocation target1 = new DataLocation(sa1);
				DataLocation target2 = new DataLocation(sa2);
				validationReporter.addCheckPointReportError(context, parameters.getCheckId(), CheckPointConstant.L3_VehicleJourney_3, source, Long.toString(delta), null, target1,
						target2);
			});
		});
	}

	private void updateSpeed(VehicleJourney vj, VehicleJourneyAtStop vjs1, VehicleJourneyAtStop vjs2,
			Map<String, List<TravelTime>> travelTimeMap) {

		TravelTime tt = new TravelTime();
		tt.setVehicleJourney(vj);
		String key = new StringBuilder().append("(").append(vjs1.getStopPoint().getObjectId()).append("->")
				.append(vjs1.getStopPoint().getObjectId()).append(")").toString();
		Long delta = diffTime(vjs1.getDepartureTime(), vjs2.getArrivalTime());
		tt.setVehicleJourneyAtStop1(vjs1);
		tt.setVehicleJourneyAtStop2(vjs2);
		tt.setDelta(delta);

		List<TravelTime> ttList = travelTimeMap.get(key);
		if (ttList == null) {
			ttList = new ArrayList<TravelTime>();
			travelTimeMap.put(key, ttList);
		}
		ttList.add(tt);

	}

	
	class TravelTime {
		@Getter
		@Setter
		private Long delta;
		@Getter
		@Setter
		private VehicleJourney vehicleJourney;
		@Getter
		@Setter
		private VehicleJourneyAtStop vehicleJourneyAtStop1;
		@Getter
		@Setter
		private VehicleJourneyAtStop vehicleJourneyAtStop2;

	}

}
