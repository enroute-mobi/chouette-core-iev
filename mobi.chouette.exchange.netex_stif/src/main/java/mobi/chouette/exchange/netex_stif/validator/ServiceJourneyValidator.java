package mobi.chouette.exchange.netex_stif.validator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;

public class ServiceJourneyValidator extends AbstractValidator {

	public static final String LOCAL_CONTEXT = NetexStifConstant.SERVICE_JOURNEY;
	
	protected String getLocalContext()
	{
		return LOCAL_CONTEXT;
	}

	@Override
	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck
		validationReporter.prepareCheckPointReport(context, NetexCheckPoints.L2_NeTExSTIF_ServiceJourney_1);
		validationReporter.prepareCheckPointReport(context, NetexCheckPoints.L2_NeTExSTIF_ServiceJourney_2);
		validationReporter.prepareCheckPointReport(context, NetexCheckPoints.L2_NeTExSTIF_ServiceJourney_3);
		validationReporter.prepareCheckPointReport(context, NetexCheckPoints.L2_NeTExSTIF_ServiceJourney_4);
	}

	@SuppressWarnings("unchecked")
	public void addTrainNumberRef(Context context, String objectId, String trainNumberRef) {
		Context objectContext = getObjectContext(context, LOCAL_CONTEXT, objectId);
		List<String> list = (List<String>) objectContext.get(NetexStifConstant.TRAIN_NUMBER_REF);
		if (list == null) {
			list = new ArrayList<String>();
			objectContext.put(NetexStifConstant.TRAIN_NUMBER_REF, list);
		}
		list.add(trainNumberRef);
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
	public boolean validate(Context context, VehicleJourney journey, int lineNumber, int columnNumber) {
		boolean result1 = checkNetexId(context, NetexStifConstant.SERVICE_JOURNEY, journey.getObjectId(), lineNumber, columnNumber);
		checkChanged(context, NetexStifConstant.SERVICE_JOURNEY, journey, lineNumber, columnNumber);
		boolean result2 = checkModification(context, NetexStifConstant.SERVICE_JOURNEY, journey, lineNumber, columnNumber);
		boolean result3 = check2NeTExSTIFServiceJourney1(context, journey, lineNumber, columnNumber);
		if (result3)
			result3 = check2NeTExSTIFServiceJourney2(context, journey, lineNumber, columnNumber);
		if (result3)
			result3 = check2NeTExSTIFServiceJourney3(context, journey, lineNumber, columnNumber);
		if (result3)
			result3 = check2NeTExSTIFServiceJourney4(context, journey, lineNumber, columnNumber);
		return result1 && result2 && result3;
	}

	/**
 	 * <b>Titre</b> :[Netex] Contrôle de l'objet ServiceJourney : JourneyPatternRef
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2321">Cartes #2321</a>
 	 * <p>
 	 * <b>Code</b> : 2-NeTExSTIF-ServiceJourney-1
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  L'attribut JourneyPatternRef de l'objet ServiceJourney doit être renseigné.
 	 * <p>
 	 * <b>Message</b> :  {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet ServiceJourney d'identifiant {objectId} ne référence pas de ServiceJourneyPattern
 	 * <p>
 	 * <b>Criticité</b> :  error
 	 * <p>
 	 * 
	 * @see #addJourneyPatternRef(Context, String, String)
	 * 
	 * @param context
	 * @param journey
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	public boolean check2NeTExSTIFServiceJourney1(Context context, VehicleJourney journey, int lineNumber,
			int columnNumber) {
		boolean result = true;

		if (journey.getJourneyPattern() == null) {
			result = false;
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(Constant.LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, journey);
			validationReporter.addCheckPointReportError(context, null, NetexCheckPoints.L2_NeTExSTIF_ServiceJourney_1, NetexCheckPoints.L2_NeTExSTIF_ServiceJourney_1, location);

		}
		return result;
	}

	/**
 	 * <b>Titre</b> :[Netex] Contrôle de l'objet ServiceJourney : trainNumbers
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2322">Cartes #2322</a>
 	 * <p>
 	 * <b>Code</b> : 2-NeTExSTIF-ServiceJourney-2
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  L'attribut trainNumbers de l'objet ServiceJourney ne peut pas être définit plusieurs fois
 	 * <p>
 	 * <b>Message</b> :  {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet ServiceJourney d'identifiant {objectId} fournit plus d'un trainNumber
 	 * <p>
 	 * <b>Criticité</b> :  error
 	 * <p>
 	 * 
	 * @see #addTrainNumberRef(Context, String, String)
	 * @param context
	 * @param journey
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean check2NeTExSTIFServiceJourney2(Context context, VehicleJourney journey, int lineNumber,
			int columnNumber) {
		boolean result = true;
		Context objectContext = getObjectContext(context, LOCAL_CONTEXT, journey.getObjectId());
		List<String> list = (List<String>) objectContext.get(NetexStifConstant.TRAIN_NUMBER_REF);
		if (list != null && list.size() > 1) {
			result = false;
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(Constant.LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, journey);
			validationReporter.addCheckPointReportError(context, null, NetexCheckPoints.L2_NeTExSTIF_ServiceJourney_2,NetexCheckPoints.L2_NeTExSTIF_ServiceJourney_2, location);
		}
		return result;
	}

	/**
 	 * <b>Titre</b> :[Netex] Contrôle de l'objet ServiceJourney : complétude des passingTimes
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2323">Cartes #2323</a>
 	 * <p>
 	 * <b>Code</b> : 2-NeTExSTIF-ServiceJourney-3
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  La liste des PassingTime du ServiceJourney doit contenir le même nombre d'éléments que la liste des StopPointInJourneyPattern du ServiceJourneyPattern associé.
 	 * <p>
 	 * <b>Message</b> :  {fichier}-Ligne {ligne}-Colonne {Colonne} : Le nombre d'horaires (passingTimes) de l'objet ServiceJourney d'identifiant {objectId} n'est pas cohérent avec le ServiceJourneyPattern associé.
 	 * <p>
 	 * <b>Criticité</b> :  error
 	 * <p>
	 * 
	 * @param context
	 * @param journey
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	public boolean check2NeTExSTIFServiceJourney3(Context context, VehicleJourney journey, int lineNumber,
			int columnNumber) {
		boolean result = true;
		if (journey.getVehicleJourneyAtStops().size() != journey.getJourneyPattern().getStopPoints().size()) {
			result = false;
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(Constant.LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, journey);
			validationReporter.addCheckPointReportError(context, null, NetexCheckPoints.L2_NeTExSTIF_ServiceJourney_3,NetexCheckPoints.L2_NeTExSTIF_ServiceJourney_3, location,Integer.toString(journey.getVehicleJourneyAtStops().size()));
		}
		return result;
	}

	/**
 	 * <b>Titre</b> :[Netex] Contrôle de l'objet ServiceJourney : chronologie des passingTimes
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2324">Cartes #2324</a>
 	 * <p>
 	 * <b>Code</b> : 2-NeTExSTIF-ServiceJourney-4
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  La chronologie horaires des PassingTime du ServiceJourney doit être croissante.. Pour chaque PassingTime de rang > 1, la valeur ArrivalTime doit être supérieure ou égale à la valeur DepartureTime du PassingTime précédent.. Note : lorsque ArrivalTime n'est pas renseigné, on considère ArrivalTime=DepartureTime
 	 * <p>
 	 * <b>Message</b> :  {fichier}-Ligne {ligne}-Colonne {Colonne} , objet ServiceJourney d'identifiant {objectId} : le passingTime de rang {rang} fournit des horaires antérieurs au passingTime précédent.
 	 * <p>
 	 * <b>Criticité</b> :  error
 	 * <p>
	 * 
	 * @param context
	 * @param journey
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	public boolean check2NeTExSTIFServiceJourney4(Context context, VehicleJourney journey, int lineNumber,
			int columnNumber) {
		boolean result = true;
		int res = 0;
		int rank = 0;
		Comparator<VehicleJourneyAtStop> cmp = new PassingTimeComparator();
		while (res <= 0 && rank < journey.getVehicleJourneyAtStops().size() -1)
		{
			rank ++;
			res = cmp.compare(journey.getVehicleJourneyAtStops().get(rank -1), journey.getVehicleJourneyAtStops().get(rank));
		}
		if (res > 0)
		{
			result = false;
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(Constant.LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line, journey);
			validationReporter.addCheckPointReportError(context, null, NetexCheckPoints.L2_NeTExSTIF_ServiceJourney_4,NetexCheckPoints.L2_NeTExSTIF_ServiceJourney_4, location, Integer.toString(rank));			
		}
		return result;
	}

	public class PassingTimeComparator implements Comparator<VehicleJourneyAtStop>
	{
		@Override
		public int compare(VehicleJourneyAtStop o1, VehicleJourneyAtStop o2) {
			int res = o1.getDepartureDayOffset()-o2.getArrivalDayOffset();
			if (res == 0)
			{
				if (o2.getArrivalTime() == null) res= 1;
				else if (o1.getDepartureTime() == null) res = -1;
				else if (o2.getArrivalTime().after(o1.getDepartureTime())) res = -1;
				else if (o2.getArrivalTime().before(o1.getDepartureTime())) res = 1;
			}
			return res;
		}
	}
}
