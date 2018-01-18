package mobi.chouette.exchange.netex_stif.exporter.writer;

import java.util.Collection;

import org.apache.commons.lang.StringEscapeUtils;

import mobi.chouette.model.ChouetteIdentifiedObject;
import mobi.chouette.model.StopPoint;

public class AbstractWriter {


	public static boolean isSet(Object... objects) {
		for (Object val : objects) {
			if (val != null) {
				if (val instanceof String) {
					if (!((String) val).isEmpty())
						return true;
				} else {
					return true;
				}
			}
		}
		return false;

	}

	public static String toXml(Object source) {
		if (source == null)
			return "";
		return StringEscapeUtils.escapeXml(source.toString());
	}
	
	public static boolean nonEmpty(Collection<?> list)
	{
		return list != null && !list.isEmpty();
	}

	protected static String buildChildSequenceId(ChouetteIdentifiedObject object, String type, String childType,
			int rank) {
		return object.getObjectId().replace(":" + type + ":", ":" + childType + ":").replace(":LOC", rank + ":LOC");
	}

	protected static String buildScheduledStopPointId(StopPoint stopPoint) {
		return stopPoint.getRoute().getObjectId().replace(":Route:", ":ScheduledStopPoint:").replace(":LOC",
				stopPoint.getPosition() + ":LOC");
	}

	protected static String buildPassengerStopAssignmentId(StopPoint stopPoint) {
		return stopPoint.getRoute().getObjectId().replace(":Route:", ":PassengerStopAssignment:").replace(":LOC",
				stopPoint.getPosition() + ":LOC");
	}

}
