package mobi.chouette.exchange.netex_stif.model;

import java.util.HashMap;
import java.util.Map;

import mobi.chouette.model.Footnote;

public class NetexStifObjectFactory {

	private Map<String, Direction> direction = new HashMap<>();

	private Map<String, Footnote> footnote = new HashMap<>();

	private Map<String, DestinationDisplay> destinationDisplay = new HashMap<>();

	private Map<String, ScheduledStopPoint> scheduledStopPoint = new HashMap<>();

	public Direction getDirection(String objectId) {
		Direction result = direction.get(objectId);
		if (result == null) {
			result = new Direction();
			result.setObjectId(objectId);
			direction.put(objectId, result);
		}
		return result;
	}

	public Footnote getFootnote(String objectId) {
		Footnote result = footnote.get(objectId);
		if (result == null) {
			result = new Footnote();
			result.setObjectId(objectId);
			footnote.put(objectId, result);
		}
		return result;
	}

	public DestinationDisplay getDestinationDisplay(String objectId) {
		DestinationDisplay result = destinationDisplay.get(objectId);
		if (result == null) {
			result = new DestinationDisplay();
			result.setObjectId(objectId);
			destinationDisplay.put(objectId, result);
		}
		return result;
	}

	public ScheduledStopPoint getScheduledStopPoint(String objectId) {
		ScheduledStopPoint result = scheduledStopPoint.get(objectId);
		if (result == null) {
			result = new ScheduledStopPoint();
			result.setObjectId(objectId);
			scheduledStopPoint.put(objectId, result);
		}
		return result;
	}

	public void clear() {
		direction.clear();
		footnote.clear();
	}

	public void dispose() {
		clear();
	}
}
