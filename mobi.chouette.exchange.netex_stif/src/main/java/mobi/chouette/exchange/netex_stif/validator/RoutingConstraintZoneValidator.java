package mobi.chouette.exchange.netex_stif.validator;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.RoutingConstraintZone;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;

public class RoutingConstraintZoneValidator extends AbstractValidator {

	public static final String LOCAL_CONTEXT = ROUTING_CONSTRAINT_ZONE;
	private final static String VALID_ZONE_USE = "cannotBoardAndAlightInSameZone";

	
	protected String getLocalContext()
	{
		return LOCAL_CONTEXT;
	}

	@Override
	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_RoutingConstraintZone_1);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_RoutingConstraintZone_2);
	}
	
	public boolean validate(Context context, RoutingConstraintZone zone, int lineNumber, int columnNumber)
	{
		boolean result1 = checkNetexId(context, ROUTING_CONSTRAINT_ZONE, zone.getObjectId(), lineNumber, columnNumber);
		checkChanged(context, ROUTING_CONSTRAINT_ZONE, zone, lineNumber, columnNumber);
		boolean result2 = checkModification(context, ROUTING_CONSTRAINT_ZONE, zone, lineNumber, columnNumber);
		boolean result3 = check2NeTExSTIFRoutingConstraintZone1(context, zone, lineNumber, columnNumber);
		if (result3)
			result3 = check2NeTExSTIFRoutingConstraintZone2(context, zone, lineNumber, columnNumber);
		return result1 && result2 && result3;
		
	}

 	/** 
 	 * <b>Titre</b> :[Netex] Contrôle de l'objet RoutingConstraintZone : complétude
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2319">Cartes #2319</a>
 	 * <p>
 	 * <b>Code</b> : 2-NeTExSTIF-RoutingConstraintZone-1
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  Au moins deux ScheduledStopPointRef doivent être renseignés
 	 * <p>
 	 * <b>Message</b> :  {fichier}-Ligne {ligne}-Colonne {Colonne}, l'objet RoutingConstraintZone {ObjectId} doit référencer au moins deux ScheduledStopPoint
 	 * <p>
 	 * <b>Criticité</b> :  error
 	 * <p>
 	 * 
 	 *
 	 * @param context
 	 * @param zone 
 	 * @return
 	 */
 	public boolean check2NeTExSTIFRoutingConstraintZone1(Context context, RoutingConstraintZone zone, int lineNumber, int columnNumber) {
 		boolean result = zone.getStopPointsRef().size() > 1;
 		if (!result)
 		{
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, zone);
			location.setName(zone.getName());
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_RoutingConstraintZone_1, location);
 		}
 		return result;
 	}
 
 
 
 	/** 
 	 * <b>Titre</b> :[Netex] Contrôle de l'objet RoutingConstraintZone : attribut ZoneUse
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2320">Cartes #2320</a>
 	 * <p>
 	 * <b>Code</b> : 2-NeTExSTIF-RoutingConstraintZone-2
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  Attribut ZoneUse renseigné
 	 * <p>
 	 * <b>Prédicat</b> :  L'attribut ZoneUse ne peut prendre pour valeur que 'cannotBoardAndAlightInSameZone'
 	 * <p>
 	 * <b>Message</b> :  {fichier}-Ligne {ligne}-Colonne {Colonne}, l'objet RoutingConstraintZone {ObjectId} a une valeur interdite pour l'attribut ZoneUse : {zoneUse}
 	 * <p>
 	 * <b>Criticité</b> :  error
 	 * <p>
 	 * 
 	 *
 	 * @param context
 	 * @param zone 
 	 * @return
 	 */
 	public boolean check2NeTExSTIFRoutingConstraintZone2(Context context, RoutingConstraintZone zone, int lineNumber, int columnNumber) {
 		boolean result = VALID_ZONE_USE.equals(zone.getZoneUse());
 		if (!result)
 		{
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, zone);
			location.setName(zone.getName());
			String zoneUse = zone.getZoneUse();
			if (zoneUse == null) zoneUse = "null";
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_RoutingConstraintZone_2, location, zoneUse);
 		} 				
 		return result;
 	}
 


}
