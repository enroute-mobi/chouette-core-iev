package mobi.chouette.exchange.netex_stif.validator;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.model.ScheduledStopPoint;

public class ScheduledStopPointValidator extends AbstractValidator {

	public static final String LOCAL_CONTEXT = NetexStifConstant.SCHEDULED_STOP_POINT;

	protected String getLocalContext() {
		return LOCAL_CONTEXT;
	}

	@Override
	public void init(Context context) {
		super.init(context);
	}

	public boolean validate(Context context, ScheduledStopPoint object, int lineNumber, int columnNumber) {
		boolean result1 = checkNetexId(context, NetexStifConstant.SCHEDULED_STOP_POINT, object.getObjectId(), lineNumber, columnNumber);
        boolean result2 = checkModification(context, NetexStifConstant.SCHEDULED_STOP_POINT, object, lineNumber, columnNumber);
		return result1 && result2;
	}


}
