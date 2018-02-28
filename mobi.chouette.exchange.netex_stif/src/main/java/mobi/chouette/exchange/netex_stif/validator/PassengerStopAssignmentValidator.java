package mobi.chouette.exchange.netex_stif.validator;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.model.PassengerStopAssignment;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.model.LineLite;

public class PassengerStopAssignmentValidator extends AbstractValidator {

	public static final String LOCAL_CONTEXT = NetexStifConstant.PASSENGER_STOP_ASSIGNMENT;
	
	protected String getLocalContext()
	{
		return LOCAL_CONTEXT;
	}

	@Override
	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck
		validationReporter.prepareCheckPointReport(context, NetexCheckPoints.L2_NeTExSTIF_PassengerStopAssignment_1);
	}

	public boolean validate(Context context, PassengerStopAssignment stopAssignment, int lineNumber, int columnNumber)
	{
		boolean result = checkModification(context, NetexStifConstant.PASSENGER_STOP_ASSIGNMENT, stopAssignment, lineNumber, columnNumber);
		return check2NeTExSTIFPassengerStopAssignment1(context, stopAssignment, lineNumber, columnNumber) && result;
	}
 	/** 
 	 * <b>Titre</b> :[Netex] Contrôle de l'objet PassengerStopAssignment : complétude
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2318">Cartes #2318</a>
 	 * <p>
 	 * <b>Code</b> : 2-NeTExSTIF-PassengerStopAssignment-1
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  Les attributs ScheduledStopPointRef et QuayRef doivent être renseignés
 	 * <p>
 	 * <b>Message</b> :  {fichier}-Ligne {ligne}-Colonne {Colonne}, l'attribut {attribut requis} de l'objet PassengerStopAssignment {ObjectId} doit être renseigné 
 	 * <p>
 	 * <b>Criticité</b> :  error
 	 * <p>
 	 * 
 	 *
 	 * @param context
 	 * @return
 	 */
 	public boolean check2NeTExSTIFPassengerStopAssignment1(Context context, PassengerStopAssignment stopAssignment, int lineNumber, int columnNumber) {
 		boolean result1 = stopAssignment.getScheduledStopPointRef() != null && !stopAssignment.getScheduledStopPointRef().isEmpty();
 		boolean result2 = stopAssignment.getQuayRef() != null && !stopAssignment.getQuayRef().isEmpty();
 		if (!result1)
 		{
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(Constant.LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, stopAssignment);
			validationReporter.addCheckPointReportError(context, null, NetexCheckPoints.L2_NeTExSTIF_PassengerStopAssignment_1,NetexCheckPoints.L2_NeTExSTIF_PassengerStopAssignment_1, location, "ScheduledStopPointRef");

 		}
 		if (!result2)
 		{
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(Constant.LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, stopAssignment);
			validationReporter.addCheckPointReportError(context, null, NetexCheckPoints.L2_NeTExSTIF_PassengerStopAssignment_1,NetexCheckPoints.L2_NeTExSTIF_PassengerStopAssignment_1, location, "QuayRef");

 		}

 		return result1 && result2;
 	}
 

}
