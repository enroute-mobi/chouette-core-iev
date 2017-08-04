package mobi.chouette.exchange.netex_stif.validator;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.VehicleJourneyAtStop;

public class PassingTimeValidator extends AbstractValidator {


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
 	 * <b>Titre</b> :[Netex] Contrôle de l'objet PassingTime : complétude
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2325">Cartes #2325</a>
 	 * <p>
 	 * <b>Code</b> : 2-NeTExSTIF-PassingTime-1
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  L'attribut DepartureTime de l'objet PassingTime doit être renseigné.
 	 * <p>
 	 * <b>Message</b> :  {fichier}-Ligne {ligne}-Colonne {Colonne} , objet ServiceJourney d'identifiant {objectId} : le passingTime de rang {rang} ne dispose pas de DepartureTime
 	 * <p>
 	 * <b>Criticité</b> :  error
 	 * <p>
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
			LineLite line = (LineLite) context.get(LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, 
					passingTime.getVehicleJourney());
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_PassingTime_1, location,
					Integer.toString(rank));

		}
		return result;
	}

 	/** 
 	 * <b>Titre</b> :[Netex] Contrôle de l'objet PassingTime : chronologie
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2326">Cartes #2326</a>
 	 * <p>
 	 * <b>Code</b> : 2-NeTExSTIF-PassingTime-2
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  Atrribut ArrivalTime renseigné
 	 * <p>
 	 * <b>Prédicat</b> :  l'Attribut DepartureTime de l'objet PassingTime doit être supérieur ou égal à l'attribut ArrivalTIme
 	 * <p>
 	 * <b>Message</b> :  {fichier}-Ligne {ligne}-Colonne {Colonne} , objet ServiceJourney d'identifiant {objectId} : le passingTime de rang {rang} fournit un ArrivalTime {arrival} supérieur à son DepartureTime {departure}
 	 * <p>
 	 * <b>Criticité</b> :  error
 	 * <p>
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
				LineLite line = (LineLite) context.get(LINE);
				DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, 
						passingTime.getVehicleJourney());
				validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_PassingTime_2, location,
						Integer.toString(rank));
			}

		}
		return result;
	}

	@Override
	protected String getLocalContext() {
		return TIMETABLED_PASSING_TIME;
	}

}
