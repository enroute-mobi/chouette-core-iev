package mobi.chouette.model.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.AccessLink;
import mobi.chouette.model.AccessPoint;
import mobi.chouette.model.Company;
import mobi.chouette.model.CompanyLite;
import mobi.chouette.model.LineNotice;
import mobi.chouette.model.ConnectionLink;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.GroupOfLine;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Line;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Network;
import mobi.chouette.model.Route;
import mobi.chouette.model.RoutingConstraint;
import mobi.chouette.model.StopArea;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Timeband;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;

@NoArgsConstructor
@ToString()
public class Referential implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private LineLite currentLine = null;

	@Getter
	@Setter
	private Map<String, AccessLink> sharedAccessLinks = new HashMap<>();

	@Getter
	@Setter
	private Map<String, AccessPoint> sharedAccessPoints = new HashMap<>();

	@Getter
	@Setter
	private Map<String, Network> sharedPTNetworks = new HashMap<>();

	@Getter
	@Setter
	private Map<String, Company> sharedCompanies = new HashMap<>();

	@Getter
	@Setter
	private Map<String, CompanyLite> sharedReadOnlyCompanies = new HashMap<>();

	@Getter
	@Setter
	private Map<String, LineNotice> sharedLineNotices = new HashMap<>();

	@Getter
	@Setter
	private Map<String, LineNotice> sharedReadOnlyLineNotices = new HashMap<>();

	@Getter
	@Setter
	private Map<String, ConnectionLink> sharedConnectionLinks = new HashMap<>();

	@Getter
	@Setter
	private Map<String, StopArea> sharedStopAreas = new HashMap<>();

	@Getter
	@Setter
	private Map<String, StopAreaLite> sharedReadOnlyStopAreas = new HashMap<>();

	@Getter
	@Setter
	private Map<String, StopAreaLite> nonCommercialStopAreas = new HashMap<>();

	@Getter
	@Setter
	private Map<String, GroupOfLine> sharedGroupOfLines = new HashMap<>();

	@Getter
	@Setter
	private Map<String, Line> sharedLines = new HashMap<>();

	@Getter
	@Setter
	private Map<String, LineLite> sharedReadOnlyLines = new HashMap<>();

	@Getter
	@Setter
	private Map<String, Timetable> sharedTimetables = new HashMap<>();

	@Getter
	@Setter
	private Map<String, Timetable> sharedTimetableTemplates = new HashMap<>();

	@Getter
	@Setter
	private Map<String, Footnote> sharedFootnotes = new HashMap<>();

	@Getter
	@Setter
	private Map<String, Timeband> sharedTimebands = new HashMap<>();

	@Getter
	@Setter
	private Map<String, Route> routes = new HashMap<>();

	@Getter
	@Setter
	private Map<String, Line> lines = new HashMap<>();

	@Getter
	@Setter
	private Map<String, JourneyPattern> journeyPatterns = new HashMap<>();

	@Getter
	@Setter
	private Map<String, StopPoint> stopPoints = new HashMap<>();

	@Getter
	@Setter
	private Map<String, VehicleJourney> vehicleJourneys = new HashMap<>();

	@Getter
	@Setter
	private Map<String, AccessLink> accessLinks = new HashMap<>();

	@Getter
	@Setter
	private Map<String, AccessPoint> accessPoints = new HashMap<>();

	@Getter
	@Setter
	private Map<String, Network> ptNetworks = new HashMap<>();

	@Getter
	@Setter
	private Map<String, Company> companies = new HashMap<>();

	@Getter
	@Setter
	private Map<String, LineNotice> lineNotices = new HashMap<>();

	@Getter
	@Setter
	private Map<String, ConnectionLink> connectionLinks = new HashMap<>();

	@Getter
	@Setter
	private Map<String, StopArea> stopAreas = new HashMap<>();

	@Getter
	@Setter
	private Map<String, GroupOfLine> groupOfLines = new HashMap<>();

	@Getter
	@Setter
	private Map<String, Timetable> timetables = new HashMap<>();

	@Getter
	@Setter
	private Map<String, Timeband> timebands = new HashMap<>();


	@Getter
	@Setter
	private Map<String, Footnote> footnotes = new HashMap<>();

	@Getter
	@Setter
	private Map<String, RoutingConstraint> routingConstraints = new HashMap<>();

	public LineLite findLine(Long id) {
		Optional<LineLite> result = sharedReadOnlyLines.values().stream().filter(line -> line.getId().equals(id))
				.findFirst();
		return result.orElse(null);
	}

	public StopAreaLite findStopArea(Long id) {
		Optional<StopAreaLite> result = sharedReadOnlyStopAreas.values().stream()
				.filter(stop -> stop.getId().equals(id)).findFirst();
		return result.orElse(null);
	}

	// Find the stopArea among all the available stopAreas, independently of their area_type value
	public StopAreaLite findStopAreaExtended(Long id) {
		Optional<StopAreaLite> result = Stream.concat(sharedReadOnlyStopAreas.values().stream(), nonCommercialStopAreas.values().stream())
		.filter(stop -> stop.getId().equals(id)).findFirst();
		return result.orElse(null);
	}

	public CompanyLite findCompany(Long id) {
		Optional<CompanyLite> result = sharedReadOnlyCompanies.values().stream()
				.filter(company->company.getId().equals(id)).findFirst();
		return result.orElse(null);
	}

	public LineNotice findLineNotice(Long id) {
		Optional<LineNotice> result = sharedReadOnlyLineNotices.values().stream()
				.filter(lineNotice->lineNotice.getId().equals(id)).findFirst();
		return result.orElse(null);
	}

	public void clear(boolean cascade) {
		if (cascade) {
			lines.values().stream().forEach(line -> {
				line.getRoutes().clear();
				line.getFootnotes().clear();
				line.getGroupOfLines().clear();
			});
			routes.values().stream().forEach(route -> {
				route.getStopPoints().clear();
				route.getJourneyPatterns().clear();
			});
			journeyPatterns.values().stream().forEach(jp -> {
				jp.getStopPoints().clear();
				jp.getVehicleJourneys().clear();
			});
			vehicleJourneys.values().stream().forEach(vj -> {
				vj.getVehicleJourneyAtStops().clear();
				vj.getTimetables().clear();
				vj.getJourneyFrequencies().clear();
				vj.getFootnotes().clear();
			});
			timetables.values().stream().forEach(timetable -> {
				timetable.getVehicleJourneys().clear();
			});
			sharedTimetables.values().stream().forEach(timetable -> {
				timetable.getVehicleJourneys().clear();
			});
			sharedTimebands.values().stream().forEach(timeband -> {
				timeband.getJourneyFrequencies().clear();
			});
			timebands.values().stream().forEach(timeband -> {
				timeband.getJourneyFrequencies().clear();
			});
			sharedGroupOfLines.values().stream().forEach(group -> {
				group.getLines().clear();
			});

		}
		accessLinks.clear();
		accessPoints.clear();
		companies.clear();
		lineNotices.clear();
		connectionLinks.clear();
		footnotes.clear();
		groupOfLines.clear();
		journeyPatterns.clear();
		lines.clear();
		ptNetworks.clear();
		routes.clear();
		stopAreas.clear();
		stopPoints.clear();
		timebands.clear();
		timetables.clear();
		vehicleJourneys.clear();
		routingConstraints.clear();
		currentLine = null;
	}

	public void dispose() {
		// clear(false);
		sharedAccessLinks.clear();
		sharedAccessPoints.clear();
		sharedCompanies.clear();
		sharedLineNotices.clear();
		sharedConnectionLinks.clear();
		sharedFootnotes.clear();
		sharedGroupOfLines.clear();
		sharedLines.clear();
		sharedPTNetworks.clear();
		sharedStopAreas.clear();
		sharedTimebands.clear();
		sharedTimetables.clear();
		sharedTimetableTemplates.clear();
		sharedReadOnlyLines.clear();
		sharedReadOnlyCompanies.clear();
		sharedReadOnlyLineNotices.clear();
		sharedReadOnlyStopAreas.clear();
		nonCommercialStopAreas.clear();
	}

}
