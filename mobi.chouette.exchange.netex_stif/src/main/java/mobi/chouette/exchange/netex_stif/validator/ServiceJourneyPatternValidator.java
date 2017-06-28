package mobi.chouette.exchange.netex_stif.validator;

import java.util.HashMap;
import java.util.Map;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.ValidationReporter;

public class ServiceJourneyPatternValidator extends AbstractValidator {

	public static final String LOCAL_CONTEXT = SERVICE_JOURNEY_PATTERN;

	@Override
	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck
		// validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_ServiceJourney_1);
		// validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_ServiceJourney_2);
		// validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_ServiceJourney_3);
		// validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_ServiceJourney_4);
	}

	@SuppressWarnings("unchecked")
	public void addStopPointAlighting(Context context, String objectId, Integer position, Boolean alighting) {
		Context objectContext = getObjectContext(context, LOCAL_CONTEXT, objectId);
		Map<Integer, Boolean> map = (Map<Integer, Boolean>) objectContext.get(FOR_ALIGHTING);
		if (map == null) {
			map = new HashMap<Integer, Boolean>();
			objectContext.put(FOR_ALIGHTING, map);
		}
		map.put(position, alighting);
	}

	@SuppressWarnings("unchecked")
	public void addStopPointBoarding(Context context, String objectId, Integer position, Boolean boarding) {
		Context objectContext = getObjectContext(context, LOCAL_CONTEXT, objectId);
		Map<Integer, Boolean> map = (Map<Integer, Boolean>) objectContext.get(FOR_BOARDING);
		if (map == null) {
			map = new HashMap<Integer, Boolean>();
			objectContext.put(FOR_BOARDING, map);
		}
		map.put(position, boarding);
	}

}
