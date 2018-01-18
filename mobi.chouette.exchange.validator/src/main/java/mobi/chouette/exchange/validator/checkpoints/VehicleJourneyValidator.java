package mobi.chouette.exchange.validator.checkpoints;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.exchange.validator.Constant;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.exchange.validator.ValidationException;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.util.Referential;

@Log4j
public class VehicleJourneyValidator extends GenericValidator<VehicleJourney> {

	private static final String MISSING_STOP_AREA_REFERENCE = " : missing StopArea reference";
	private static final String VEHICLE_JOURNEY = "VehicleJourney ";
	private static final String PASSING_TIME_FOR_MISSING_STOP_AREA = "passingTime for missing stop area ";
	private static final String[] codes = { 
			CheckPointConstant.L3_VehicleJourney_1, 
			CheckPointConstant.L3_VehicleJourney_2, 
			CheckPointConstant.L3_VehicleJourney_4,
			CheckPointConstant.L3_VehicleJourney_5 };

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
		validationReporter.prepareCheckPointReport(context, CheckPointConstant.L3_VehicleJourney_1);
		Referential r = (Referential) context.get(Constant.REFERENTIAL);

		long deltaMax = Long.parseLong(parameters.getMaximumValue()) * 60L;

		passingTimes.stream()
				.filter(passingTime -> passingTime.getArrivalTime() != null && passingTime.getDepartureTime() != null)
				.forEach(passingTime -> {
					long delta = this.diffTime(passingTime.getArrivalTime(), passingTime.getDepartureTime());

					if (delta > deltaMax) {
						StopAreaLite zdep = r.findStopArea(passingTime.getStopPoint().getStopAreaId());
						if (zdep == null) {
							log.error(PASSING_TIME_FOR_MISSING_STOP_AREA);
							throw new ValidationException(
									VEHICLE_JOURNEY + object.getId() + MISSING_STOP_AREA_REFERENCE);
						}
						DataLocation source = new DataLocation(object);
						DataLocation target = new DataLocation(zdep);
						validationReporter.addCheckPointReportError(context, parameters.getCheckId(), CheckPointConstant.L3_VehicleJourney_1, source,
								Long.toString(delta / 60L), parameters.getMaximumValue(), target);

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
		// prerequisites
		List<VehicleJourneyAtStop> passingTimes = object.getVehicleJourneyAtStops();
		if (passingTimes.size() < 2)
			return;

		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, CheckPointConstant.L3_VehicleJourney_2);
		Referential r = (Referential) context.get(Constant.REFERENTIAL);

		long speedMax = Long.parseLong(parameters.getMaximumValue());
		long speedMin = Long.parseLong(parameters.getMinimumValue());

		for (int i = 1; i < passingTimes.size(); i++) {
			VehicleJourneyAtStop passingTime1 = passingTimes.get(i - 1);
			VehicleJourneyAtStop passingTime2 = passingTimes.get(i);

			long speed = (long) getSpeedBetweenStops(context, passingTime1, passingTime2);
			if (speed > speedMax) {
				StopAreaLite zdep1 = r.findStopArea(passingTime1.getStopPoint().getStopAreaId());
				StopAreaLite zdep2 = r.findStopArea(passingTime2.getStopPoint().getStopAreaId());

				DataLocation source = new DataLocation(object);
				DataLocation target1 = new DataLocation(zdep1);
				DataLocation target2 = new DataLocation(zdep2);
				validationReporter.addCheckPointReportError(context, parameters.getCheckId(), CheckPointConstant.L3_VehicleJourney_2, "1", source,
						Long.toString(speed), parameters.getMaximumValue(), target1, target2);

			}
			if (speedMin > 0 && speed < speedMin) {
				StopAreaLite zdep1 = r.findStopArea(passingTime1.getStopPoint().getStopAreaId());
				StopAreaLite zdep2 = r.findStopArea(passingTime2.getStopPoint().getStopAreaId());

				DataLocation source = new DataLocation(object);
				DataLocation target1 = new DataLocation(zdep1);
				DataLocation target2 = new DataLocation(zdep2);
				validationReporter.addCheckPointReportError(context, parameters.getCheckId(), CheckPointConstant.L3_VehicleJourney_2, "2", source,
						Long.toString(speed), parameters.getMinimumValue(), target1, target2);
			}
		}
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
		// prerequisites

		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, CheckPointConstant.L3_VehicleJourney_4);

		if (object.getTimetables().isEmpty()) {
			DataLocation source = new DataLocation(object);
			validationReporter.addCheckPointReportError(context, parameters.getCheckId(), CheckPointConstant.L3_VehicleJourney_4, source);

		}
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
		// prerequisites
		List<VehicleJourneyAtStop> passingTimes = object.getVehicleJourneyAtStops();
		if (passingTimes.isEmpty())
			return;

		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, CheckPointConstant.L3_VehicleJourney_5);
		Referential r = (Referential) context.get(Constant.REFERENTIAL);
		DateFormat format = new SimpleDateFormat("HH:mm");
		// check first passingTime
		{
			VehicleJourneyAtStop passingTime = passingTimes.get(0);
			if (getArrivalTime(passingTime).after(passingTime.getDepartureTime())) {
				StopAreaLite zdep = r.findStopArea(passingTime.getStopPoint().getStopAreaId());
				if (zdep == null) {
					log.error(PASSING_TIME_FOR_MISSING_STOP_AREA);
					throw new ValidationException(VEHICLE_JOURNEY + object.getId() + MISSING_STOP_AREA_REFERENCE);
				}
				DataLocation source = new DataLocation(object);
				DataLocation target = new DataLocation(zdep);
				validationReporter.addCheckPointReportError(context, parameters.getCheckId(), CheckPointConstant.L3_VehicleJourney_5, "1", source,
						format.format(passingTime.getArrivalTime()), format.format(passingTime.getDepartureTime()),
						target);
			}
		}

		for (int i = 1; i < passingTimes.size(); i++) {
			VehicleJourneyAtStop passingTime1 = passingTimes.get(i - 1);
			VehicleJourneyAtStop passingTime2 = passingTimes.get(i);
			if (passingTime1.getDepartureTime().after(getArrivalTime(passingTime2))
					&& passingTime1.getDepartureDayOffset() == passingTime2.getDepartureDayOffset()) {
				StopAreaLite zdep1 = r.findStopArea(passingTime1.getStopPoint().getStopAreaId());

				StopAreaLite zdep2 = r.findStopArea(passingTime2.getStopPoint().getStopAreaId());
				if (zdep2 == null) {
					log.error(PASSING_TIME_FOR_MISSING_STOP_AREA);
					throw new ValidationException(VEHICLE_JOURNEY + object.getId() + MISSING_STOP_AREA_REFERENCE);
				}

				DataLocation source = new DataLocation(object);
				DataLocation target1 = new DataLocation(zdep1);
				DataLocation target2 = new DataLocation(zdep2);
				validationReporter.addCheckPointReportError(context, parameters.getCheckId(), CheckPointConstant.L3_VehicleJourney_5, "2", source,
						format.format(passingTime1.getDepartureTime()), format.format(getArrivalTime(passingTime2)),
						target1, target2);

			}
		}
	}

}
