package mobi.chouette.exchange.exporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.model.AccessLink;
import mobi.chouette.model.AccessPoint;
import mobi.chouette.model.Company;
import mobi.chouette.model.CompanyLite;
import mobi.chouette.model.ConnectionLink;
import mobi.chouette.model.GroupOfLine;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Line;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Network;
import mobi.chouette.model.NetworkLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.RoutingConstraint;
import mobi.chouette.model.StopArea;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;

public class ExportableData {

	@Getter
	@Setter
	private Set<Network> networks = new HashSet<>();
	@Getter
	@Setter
	private Line line;
	@Getter
	@Setter
	private LineLite lineLite;
	@Getter
	@Setter
	private Set<Company> companies = new HashSet<>();
	@Getter
	@Setter
	private Set<Line> lines = new HashSet<>();
	@Getter
	@Setter
	private Set<GroupOfLine> groupOfLines = new HashSet<>();
	@Getter
	@Setter
	private Set<StopArea> stopAreas = new HashSet<>();
	@Getter
	@Setter
	private Set<StopArea> quays = new HashSet<>();
	@Getter
	@Setter
	private Set<StopArea> boardingPositions = new HashSet<>();
	@Getter
	@Setter
	private Set<StopArea> physicalStops = new HashSet<>();
	@Getter
	@Setter
	private Set<StopArea> commercialStops = new HashSet<>();
	@Getter
	@Setter
	private Set<StopArea> stopPlaces = new HashSet<>();
	@Getter
	@Setter
	private Set<ConnectionLink> connectionLinks = new HashSet<>();
	@Getter
	@Setter
	private Set<AccessLink> accessLinks = new HashSet<>();
	@Getter
	@Setter
	private Set<AccessPoint> accessPoints = new HashSet<>();
	@Getter
	@Setter
	private Set<Timetable> timetables = new HashSet<>();
	@Getter
	@Setter
	private Set<Timetable> excludedTimetables = new HashSet<>();
	@Getter
	@Setter
	private Set<RoutingConstraint> routingConstraints = new HashSet<>();
	@Getter
	@Setter
	private Map<String, List<Timetable>> timetableMap = new HashMap<>();
	@Getter
	@Setter
	private List<VehicleJourney> vehicleJourneys = new ArrayList<>();
	@Getter
	@Setter
	private List<JourneyPattern> journeyPatterns = new ArrayList<>();
	@Getter
	@Setter
	private List<Route> routes = new ArrayList<>();
	@Getter
	@Setter
	private List<StopPoint> stopPoints = new ArrayList<>();

	// prevent lazy loading for non complete connectionlinks
	@Getter
	@Setter
	private Set<StopArea> sharedStops = new HashSet<>();

	@Getter
	@Setter
	private Map<Long, LineLite> mappedLines = new HashMap<>();

	@Getter
	@Setter
	private Map<Long, StopAreaLite> mappedStopAreas = new HashMap<>();

	@Getter
	@Setter
	private Map<Long, CompanyLite> mappedCompanies = new HashMap<>();

	// public Timetable findTimetable(String objectId) {
	// for (Timetable tm : timetables) {
	// if (tm.getObjectId().equals(objectId))
	// return tm;
	// }
	// return null;
	// }
    public void clearAll()
    {
    	clearCompany();
    	clearLineReferential();
    	clearStopAreaReferential();
		mappedStopAreas.clear();
		mappedCompanies.clear();
		mappedLines.clear();
    }
	
	public void clearCompany() {
		clearLine();
		timetables.clear();
		excludedTimetables.clear();
	}
	
	public void clearLineReferential()
	{
		networks.clear();
		companies.clear();
		groupOfLines.clear();
		lines.clear();
	}
	
	public void clearStopAreaReferential()
	{
		stopAreas.clear();
	}

	public void clearLine() {
		line = null;
		lineLite = null;
		routingConstraints.clear();
		timetableMap.clear();
		vehicleJourneys.clear();
		journeyPatterns.clear();
		routes.clear();
		stopPoints.clear();
		sharedStops.clear();
	}
}
