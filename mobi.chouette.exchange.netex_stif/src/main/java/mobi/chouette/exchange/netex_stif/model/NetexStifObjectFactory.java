package mobi.chouette.exchange.netex_stif.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.Period;
import mobi.chouette.model.StopPoint;

@Log4j
public class NetexStifObjectFactory {

	private Map<String, Direction> direction = new HashMap<>();

	private Map<String, Footnote> footnote = new HashMap<>();

	private Map<String, DestinationDisplay> destinationDisplay = new HashMap<>();

	private Map<String, ScheduledStopPoint> scheduledStopPoint = new HashMap<>();

	private Map<String, Period> operatingPeriods = new HashMap<>();

	@Getter
	private Map<String, String> routeDirections = new HashMap<>();

	@Getter
	private Map<String, String> JourneyPatternDestinations = new HashMap<>();

	@Getter
	private Map<String, List<StopPoint>> stopPointInitIdToStopPoints = new HashMap<>();

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

	public Period getOperatingPeriod(String objectId) {
		Period result = operatingPeriods.get(objectId);
		if (result == null) {
			result = new Period();
			operatingPeriods.put(objectId, result);
		}
		return result;
	}

	public void addRouteDirection(String routeId, String directionId) {
		this.routeDirections.put(routeId, directionId);
	}

	public void addJourneyPatternDestination(String journeyPatternId, String destinationId) {
		this.JourneyPatternDestinations.put(journeyPatternId, destinationId);
	}

	public void addStopPoint(String initId, StopPoint stopPoint) {
		List<StopPoint> list = stopPointInitIdToStopPoints.get(initId);
		log.info("id:" + initId + " List:" + list);
		if (list == null) {
			list = new ArrayList<StopPoint>();
			stopPointInitIdToStopPoints.put(initId, list);
		}
		log.info("list:" + list);
		list.add(stopPoint);
		log.info("list:" + list);
	}

	public List<StopPoint> getStopPoints(String initId) {
		return stopPointInitIdToStopPoints.get(initId);
	}

	public void clear() {
		direction.clear();
		footnote.clear();
	}

	public void dispose() {
		clear();
	}
}
