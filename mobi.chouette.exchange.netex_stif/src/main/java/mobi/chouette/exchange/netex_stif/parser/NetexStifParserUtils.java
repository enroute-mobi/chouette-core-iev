package mobi.chouette.exchange.netex_stif.parser;

import mobi.chouette.exchange.netex_stif.model.ScheduledStopPoint;

/**
 * static method to help parsing
 * 
 * @author flabourot
 *
 */
public class NetexStifParserUtils {

	public final static String ID_SEPARATOR = ":";

	public static String genStopPointId(String id, String order) {
		return id + ID_SEPARATOR + order;
	}

	public static String genStopPointId(ScheduledStopPoint scheduledStopPoint) {
		return scheduledStopPoint.getId() + ID_SEPARATOR + scheduledStopPoint.getOrder();
	}

}
