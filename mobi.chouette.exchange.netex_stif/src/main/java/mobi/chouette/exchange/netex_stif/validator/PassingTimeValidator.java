package mobi.chouette.exchange.netex_stif.validator;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.model.VehicleJourneyAtStop;

public class PassingTimeValidator extends AbstractValidator {

	public PassingTimeValidator(Context context) {
		init(context);

	}

	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_PassingTime_1);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_PassingTime_2);
	}

	/**
	 * 
	 * @param context
	 * @param passingTime
	 * @param lineNumber
	 * @param columnNumber
	 * @param rank
	 * @return
	 */
	public boolean validate(Context context, VehicleJourneyAtStop passingTime, int lineNumber, int columnNumber,
			int rank) {
		boolean result = check2NeTExSTIFPassingTime1(context, passingTime, lineNumber, columnNumber, rank);
		if (result)
			result = check2NeTExSTIFPassingTime2(context, passingTime, lineNumber, columnNumber, rank);
		return result;
	}

	/**
	 * https://projects.af83.io/issues/2325
	 * 
	 * Code : 2-NeTExSTIF-PassingTime-1
	 * 
	 * Variables : néant
	 * 
	 * Prérequis : L'attribut DepartureTime de l'objet PassingTime doit être
	 * renseigné.
	 * 
	 * Prédicat : L'attribut DirectionType doit prendre l'une des 2 valeurs
	 * 'outbound' ou 'inbound'
	 * 
	 * Message : {fichier}-Ligne {ligne}-Colonne {Colonne} , objet
	 * ServiceJourney d'identifiant {objectId} : le passingTime de rang {rang}
	 * ne dispose pas de DepartureTime
	 * 
	 * Criticité : error
	 * 
	 * @param context
	 * @param passingTime
	 * @param lineNumber
	 * @param columnNumber
	 * @param rank
	 * @return
	 */
	public boolean check2NeTExSTIFPassingTime1(Context context, VehicleJourneyAtStop passingTime, int lineNumber,
			int columnNumber, int rank) {
		boolean result = true;
		if (passingTime.getDepartureTime() == null) {
			result = false;
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber,
					passingTime.getVehicleJourney());
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_PassingTime_1, location,
					Integer.toString(rank));

		}
		return result;
	}

	/**
	 * https://projects.af83.io/issues/2326
	 * 
	 * Code : 2-NeTExSTIF-PassingTime-2
	 * 
	 * Variables : néant
	 * 
	 * Prérequis : Attribut ArrivalTime renseigné
	 * 
	 * Prédicat : l'Attribut DepartureTime de l'objet PassingTime doit être
	 * supérieur ou égal à l'attribut ArrivalTIme
	 * 
	 * Message : {fichier}-Ligne {ligne}-Colonne {Colonne} , objet
	 * ServiceJourney d'identifiant {objectId} : le passingTime de rang {rang}
	 * fournit un ArrivalTime supérieur à son DepartureTime
	 * 
	 * Criticité : error
	 * 
	 * @param context
	 * @param passingTime
	 * @param lineNumber
	 * @param columnNumber
	 * @param rank
	 * @return
	 */
	public boolean check2NeTExSTIFPassingTime2(Context context, VehicleJourneyAtStop passingTime, int lineNumber,
			int columnNumber, int rank) {
		boolean result = true;
		if (passingTime.getArrivalTime() != null) {
			if (passingTime.getArrivalDayOffset() == passingTime.getDepartureDayOffset()) {
				if (passingTime.getArrivalTime().after(passingTime.getDepartureTime())) {
					result = false;
				}
			} else if (passingTime.getArrivalDayOffset() > passingTime.getDepartureDayOffset()) {
				result = false;
			}
			if (!result) {
				ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
				String fileName = (String) context.get(Constant.FILE_NAME);
				DataLocation location = new DataLocation(fileName, lineNumber, columnNumber,
						passingTime.getVehicleJourney());
				validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_PassingTime_2, location,
						Integer.toString(rank));
			}

		}
		return result;
	}

}
