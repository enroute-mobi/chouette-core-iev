package mobi.chouette.exchange.validator.checkpoints;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.exchange.validator.ValidationException;
import mobi.chouette.model.StopArea;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.util.Referential;

@Log4j
public class VehicleJourneyValidator extends GenericValidator<VehicleJourney> implements CheckPointConstant {

	private static final String[] codes = { L3_VehicleJourney_1, L3_VehicleJourney_2, L3_VehicleJourney_4,
			L3_VehicleJourney_5 };

	@Override
	public void validate(Context context, VehicleJourney object, ValidateParameters parameters, String transportMode) {
		super.validate(context, object, parameters, transportMode, codes);
	}

	/**
	 * <b>Titre</b> :[Course] La durée d'attente à un arrêt ne doit pas être
	 * trop grande
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2103">Cartes
	 * #2103</a>
	 * <p>
	 * <b>Code</b> :3-VehicleJourney-1
	 * <p>
	 * <b>Variables</b> : * durée d'attente maximale à un arrêt en minutes, <br>
	 * 
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : La durée d?attente à un arrêt ne doit pas être trop
	 * grande
	 * <p>
	 * <b>Message</b> : Sur la course {ObjectId}, le temps d'attente {valeur} à
	 * l'arrêt {nomArrêt} ({objectId}) est supérieur au seuil toléré
	 * ({tempsMax})
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
	protected void check3VehicleJourney1(Context context, VehicleJourney object, CheckpointParameters parameters) {
		// prerequisites
		List<VehicleJourneyAtStop> passingTimes = object.getVehicleJourneyAtStops();
		if (passingTimes.isEmpty())
			return;

		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, L3_VehicleJourney_1);
		Referential r = (Referential) context.get(REFERENTIAL);

		long deltaMax = Long.parseLong(parameters.getFirstValue()) * 60L;

		passingTimes.stream()
				.filter(passingTime -> passingTime.getArrivalTime() != null && passingTime.getDepartureTime() != null)
				.forEach(passingTime -> {
					long delta = (passingTime.getDepartureTime().getTime() - passingTime.getArrivalTime().getTime())
							/ 1000L;
					if (passingTime.getDepartureTime().before(passingTime.getArrivalTime())) {
						delta += 86400L;
					}
					if (delta > deltaMax) {
						StopAreaLite zdep = r.findStopArea(passingTime.getStopPoint().getStopAreaId());
						if (zdep == null) {
							log.error("passingTime for missing stop area ");
							throw new ValidationException(
									"VehicleJourney " + object.getId() + " : missing StopArea reference");
						}
						DataLocation source = new DataLocation(object);
						DataLocation target = new DataLocation(zdep);
						validationReporter.addCheckPointReportError(context, L3_VehicleJourney_1, source,
								Long.toString(delta / 60L), parameters.getFirstValue(), target);

					}
				});
	}

	/**
	 * <b>Titre</b> :[Course] La vitesse entre deux arrêts doit être dans une
	 * fourchette paramétrable
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2104">Cartes
	 * #2104</a>
	 * <p>
	 * <b>Code</b> :3-VehicleJourney-2
	 * <p>
	 * <b>Variables</b> : * vitesse minimale (km/h), <br>
	 * * vitesse maximale (km/h),<br>
	 * 
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : La vitesse entre deux arrêts doit être dans une
	 * fourchette paramétrable
	 * <p>
	 * <b>Message</b> : Sur la course {ObjectId}, la vitesse calculée {valeur}
	 * entre les arrêts {nomArrêt1} ({objectId}) et {nomArrêt2} ({objectId}) est
	 * supérieur au seuil toléré ({vitesseMax})<br>
	 * Sur la course {ObjectId}, la vitesse calculée {valeur} entre les arrêts
	 * {nomArrêt1} ({objectId}) et {nomArrêt2} ({objectId}) est inférieure au
	 * seuil toléré ({vitesseMin})
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
	protected void check3VehicleJourney2(Context context, VehicleJourney object, CheckpointParameters parameters) {
		// TODO
	}

	/**
	 * <b>Titre</b> :[Course] Une course doit avoir au moins un calendrier
	 * d'application
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2106">Cartes
	 * #2106</a>
	 * <p>
	 * <b>Code</b> :3-VehicleJourney-4
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Une course doit avoir au moins un calendrier
	 * d?application
	 * <p>
	 * <b>Message</b> : La course {objecId} n'a pas de calendrier d'application
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
	protected void check3VehicleJourney4(Context context, VehicleJourney object, CheckpointParameters parameters) {
		// TODO
	}

	/**
	 * <b>Titre</b> :[Course] Chronologie croissante des horaires
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2107">Cartes
	 * #2107</a>
	 * <p>
	 * <b>Code</b> :3-VehicleJourney-5
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'horaire d'arrivée à un arrêt doit être antérieur à
	 * l'horaire de départ de cet arrêt ET les horaires de départ aux arrêts
	 * doivent être dans l'ordre chronologique croissant.
	 * <p>
	 * <b>Message</b> : La course {objectId} a un horaire d'arrivé {hh:mm}
	 * supérieur à l'horaire de départ {hh:mm} à l'arrêt {nomArrêt} ({objectId})
	 * <br>
	 * La course {objectId} a un horaire de départ {hh:mm} à l'arrêt {nomArrêt}
	 * ({objectId}) supérieur à l'horaire d'arrivé {hh:mm} à l'arrêt suivant
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
	protected void check3VehicleJourney5(Context context, VehicleJourney object, CheckpointParameters parameters) {
		// TODO
	}
	private Map<String,Double> distances = new HashMap<>();
	
	private double getDistance(StopArea stop1, StopArea stop2)
	{
		String key = stop1.getObjectId()+"#"+stop2.getObjectId();
		if (distances.containsKey(key))
		{
			return distances.get(key).doubleValue();
		}
		key = stop2.getObjectId()+"#"+stop1.getObjectId();
		if (distances.containsKey(key))
		{
			return distances.get(key).doubleValue();
		}
		double distance = distance(stop1, stop2);
		distances.put(key,Double.valueOf(distance));
		return distance;
	}

	/**
	 * calculate duration in second beetween times <br>
	 * if last time before first time, assume a change of day
	 * 
	 * @param first first time (earlyer one)
	 * @param last last time 
	 * @return difference in seconds
	 */
	protected long diffTime(Time first,  Time last) {
		if (first == null || last == null)
			return Long.MIN_VALUE; // TODO

		

		long lastTime = (last.getTime() / 1000L) ;
		long firstTime = (first.getTime() / 1000L) ;

		long delta = lastTime - firstTime;
		if (last.before(first)) {
			delta += 86400L;
		}

		return delta;
	}

}
