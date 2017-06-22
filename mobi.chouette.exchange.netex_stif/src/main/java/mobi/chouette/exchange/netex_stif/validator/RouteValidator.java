package mobi.chouette.exchange.netex_stif.validator;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.model.Route;

public class RouteValidator extends AbstractValidator {

	public RouteValidator(Context context) {
		init(context);
		
	}

	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_Route_1);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_Route_2);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_Route_3);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_Route_4);
	}
	
	
	/*
	 * https://projects.af83.io/issues/2308
	 * 
	 * Code : 2-NeTExSTIF-Route-1
	 * 
	 * Variables : néant
	 * 
	 * Prérequis : Attribut DirectionType renseigné
	 * 
	 * Prédicat : L'attribut DirectionType doit prendre l'une des 2 valeurs
	 * 'outbound' ou 'inbound'
	 * 
	 * Message : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet Route
	 * d'identifiant {objectId} a une valeur de l'attribut DirectionType
	 * interdite : {directionType}
	 * 
	 * Criticité : error
	 * 
	 */

	public boolean check2NeTExSTIFRoute1(Context context, String attribute, int lineNumber, int columnNumber) {
		boolean result = true;
		if (attribute == null || (!attribute.equals("outbound") && !attribute.equals("inbound"))) {
			result = false;
		}
		return result;
	}

	/*
	 * https://projects.af83.io/issues/2309
	 * 
	 * Code : 2-NeTExSTIF-Route-2
	 * 
	 * Variables : néant
	 * 
	 * Prérequis : Attribut InverseRouteRef renseigné
	 * 
	 * Prédicat : Les Routes associées comme routes inverses doivent se
	 * référencer mutuellement. Les DirectionType des Routes en sens opposés
	 * doivent être différent (Note : DirectionType non renseigné = outbound)
	 * 
	 * Message : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet Route
	 * d'identifiant {objectId} référence un objet Route inverse
	 * {InverseRouteRef.ref} qui ne le référence pas {fichier}-Ligne
	 * {ligne}-Colonne {Colonne} : l'objet Route d'identifiant {objectId}
	 * référence un objet Route inverse {InverseRouteRef.ref} de même
	 * DirectionType
	 * 
	 * Criticité : warning
	 * 
	 */
	public boolean check2NeTExSTIFRoute2(Context context, Route route, int lineNumber, int columnNumber) {
		throw new RuntimeException("Not yet implemented");
	}

	/*
	 * https://projects.af83.io/issues/2310
	 *
	 * Code : 2-NeTExSTIF-Route-3
	 * 
	 * Variables : néant
	 * 
	 * Prérequis : néant
	 * 
	 * Prédicat : L'ordre des arrêts (ScheduledStopPoint) issus des
	 * StopPointInJourneyPattern des ServiceJourneyPattern de la Route doit être
	 * croissant
	 * 
	 * Message : {fichier}-Ligne {ligne}-Colonne {Colonne} : Les
	 * ServiceJourneyPattern de l'objet Route d'identifiant {objectId} ne
	 * permettent pas de reconstituer la séquence des arrêts de celui-ci
	 * 
	 * Criticité : error
	 * 
	 */
	public boolean check2NeTExSTIFRoute3(Context context, String zzz, int lineNumber, int columnNumber) {
		throw new RuntimeException("Not yet implemented");
	}
}
