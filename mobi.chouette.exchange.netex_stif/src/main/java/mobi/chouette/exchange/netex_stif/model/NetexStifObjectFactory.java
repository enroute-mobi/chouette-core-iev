package mobi.chouette.exchange.netex_stif.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.Period;
import mobi.chouette.model.StopPoint;

public class NetexStifObjectFactory {

	private Map<String, Direction> direction = new HashMap<>();

	private Map<String, Footnote> footnote = new HashMap<>();

	private Map<String, DestinationDisplay> destinationDisplay = new HashMap<>();

	private Map<String, ScheduledStopPoint> scheduledStopPoint = new HashMap<>();

	private Map<String, OperatingPeriod> operatingPeriods = new HashMap<>();

	@Getter
	private Map<String, String> routeDirections = new HashMap<>();

	@Getter
	private Map<String, String> journeyPatternDestinations = new HashMap<>();

	@Getter
	private Map<String, List<StopPoint>> stopPointInitIdToStopPoints = new HashMap<>();
	
	// singleton for local parsing use
	private PassengerStopAssignment stopAssignment = new PassengerStopAssignment();
	
	// singleton for local parsing use
	private RoutingConstraintZone zone = new RoutingConstraintZone();
		
	public PassengerStopAssignment getPassengerStopAssignment(String objectId)
	{
		stopAssignment.clear();
		stopAssignment.setObjectId(objectId);
		return stopAssignment;
	}

	public RoutingConstraintZone getRoutingConstraintZone(String objectId)
	{
		zone.clear();
		zone.setObjectId(objectId);
		return zone;
	}
	
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

	public OperatingPeriod getOperatingPeriod(String objectId) {
		OperatingPeriod result = operatingPeriods.get(objectId);
		if (result == null) {
			result = new OperatingPeriod();
			operatingPeriods.put(objectId, result);
		}
		return result;
	}

	public void addRouteDirection(String routeId, String directionId) {
		this.routeDirections.put(routeId, directionId);
	}

	public void addJourneyPatternDestination(String journeyPatternId, String destinationId) {
		this.journeyPatternDestinations.put(journeyPatternId, destinationId);
	}

	public void addStopPoint(String initId, StopPoint stopPoint) {
		List<StopPoint> list = stopPointInitIdToStopPoints.get(initId);
		if (list == null) {
			list = new ArrayList<StopPoint>();
			stopPointInitIdToStopPoints.put(initId, list);
		}
		list.add(stopPoint);
	}

	public List<StopPoint> getStopPoints(String initId) {
		return stopPointInitIdToStopPoints.get(initId);
	}

	public void clear() {
		direction.clear();
		footnote.clear();
		destinationDisplay.clear();
		scheduledStopPoint.clear();
		operatingPeriods.clear();
		routeDirections.clear();
		journeyPatternDestinations.clear();
		stopPointInitIdToStopPoints.clear();
	}

	public void dispose() {
		clear();
	}
}
