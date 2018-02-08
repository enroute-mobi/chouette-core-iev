package mobi.chouette.exchange.netex_stif.validator;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.model.DestinationDisplay;

public class DestinationDisplayValidator extends AbstractValidator {

	public static final String LOCAL_CONTEXT = NetexStifConstant.DESTINATION_DISPLAY;

	protected String getLocalContext() {
		return LOCAL_CONTEXT;
	}

	@Override
	public void init(Context context) {
		super.init(context);
	}
	/**
	 * @param context
	 * @param destination
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	public boolean validate(Context context, DestinationDisplay destination, int lineNumber, int columnNumber) {
		boolean result = checkModification(context, NetexStifConstant.DESTINATION_DISPLAY, destination, lineNumber, columnNumber);
		return result;

	}

}
