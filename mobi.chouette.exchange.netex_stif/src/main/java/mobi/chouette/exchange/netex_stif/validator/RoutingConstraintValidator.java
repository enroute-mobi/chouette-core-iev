package mobi.chouette.exchange.netex_stif.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.RoutingConstraint;
import mobi.chouette.model.StopPoint;

@Log4j
public class RoutingConstraintValidator extends AbstractValidator {

	public static final String LOCAL_CONTEXT = NetexStifConstant.ROUTING_CONSTRAINT_ZONE;
	
	protected String getLocalContext() {return LOCAL_CONTEXT;}

	@Override
	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		// -- preset checkpoints to OK if uncheck
		validationReporter.prepareCheckPointReport(context, NetexCheckPoints.L2_NeTExSTIF_RoutingConstraintZone_3);
	}
	
	public boolean validate(Context context, RoutingConstraint routingConstraint, int lineNumber, int columnNumber)
	{
		//boolean result1 = checkNetexId(context, NetexStifConstant.ROUTING_CONSTRAINT_ZONE, zone.getObjectId(), lineNumber, columnNumber);
		addLocation(context, routingConstraint, lineNumber, columnNumber);
		checkChanged(context, NetexStifConstant.ROUTING_CONSTRAINT_ZONE, routingConstraint, lineNumber, columnNumber);
		boolean result2 = checkModification(context, NetexStifConstant.ROUTING_CONSTRAINT_ZONE, routingConstraint, lineNumber, columnNumber);
		boolean result3 = check2NeTExSTIFRoutingConstraintZone3(context, routingConstraint, lineNumber, columnNumber);
		return result2 && result3;
	}

	/**
	 * <b>Titre</b> :[ITL] Couverture de l'itinéraire
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2510">Cartes
	 * #2510</a>
	 * <p>
	 * <b>Code</b> :3-RoutingConstraint-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Une ITL ne peut pas couvrir l'ensemble des arrêts de
	 * l'itinéraire
	 * <p>
	 * <b>Message</b> : L'ITL {objectId} couvre tous les arrêts de l'itinéraire
	 * {ObjectId}.
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
	protected boolean check2NeTExSTIFRoutingConstraintZone3(Context context, RoutingConstraint routingConstaint,int lineNumber, int columnNumber) {
		
		List<String> spObjectIds = new ArrayList<String>();
		if (routingConstaint!=null && routingConstaint.getStopPoints()!=null) {
			for (StopPoint sp : routingConstaint.getStopPoints()) {
				spObjectIds.add(sp.getObjectId());
			}
		}
		
		boolean result = true;

		Route route = routingConstaint.getRoute();
		int spRouteCount = route.getStopPoints().size();
		if (spRouteCount == spObjectIds.size()) {
			List<StopPoint> intersectSet = route.getStopPoints().stream()
					.filter(p -> spObjectIds.contains(p.getObjectId())).collect(Collectors.toList());
			if (intersectSet.size() == spObjectIds.size()) {
				result = false;
			}
		}

		if (!result) {
			String value = routingConstaint.getRoute()!=null ? routingConstaint.getRoute().getObjectId() : "null";
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			LineLite line = (LineLite) context.get(Constant.LINE);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, line,  routingConstaint);
			location.setName(routingConstaint.getName());
			validationReporter.addCheckPointReportError(context, null, NetexCheckPoints.L2_NeTExSTIF_RoutingConstraintZone_3
					,NetexCheckPoints.L2_NeTExSTIF_RoutingConstraintZone_3, location, value);
		}
		return result;
	}
}
