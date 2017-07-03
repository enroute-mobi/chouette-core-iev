package mobi.chouette.exchange.netex_stif.validator;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.ValidationReporter;

public class PassengerStopAssignmentValidator extends AbstractValidator {

	public static final String LOCAL_CONTEXT = PASSENGER_STOP_ASSIGNMENT;
	
	protected String getLocalContext()
	{
		return LOCAL_CONTEXT;
	}

	@Override
	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_PassengerStopAssignment_1);
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
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * 
 	 *
 	 * @param context
 	 * @return
 	 */
 	public boolean check2NeTExSTIFPassengerStopAssignment1(Context context, int lineNumber, int columnNumber) {
 		// TODO : [STIF] Implementation Controle  2-NeTExSTIF-PassengerStopAssignment-1 : [Netex] Contrôle de l'objet PassengerStopAssignment : complétude
 		boolean result = true;
 		return result;
 	}
 

}
