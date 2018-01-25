package mobi.chouette.exchange.netex_stif.exporter;

import java.sql.Date;
import java.util.Collection;

import lombok.extern.log4j.Log4j;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.util.ChouetteModelUtil;
import mobi.chouette.model.util.Referential;

@Log4j
public class NetexStifDataCollector {
	public boolean collect(ExportableData collection, Referential referential, Date startDate, Date endDate) {
       boolean res = false;
		Collection<Route> routes = referential.getRoutes().values();
		LineLite line = referential.getCurrentLine();
		// check for each route if vehiclejourney is in DateRange scope  
		boolean validLine = false;
		collection.setLineLite(null);
		collection.getRoutes().clear();
		collection.getJourneyPatterns().clear();
		collection.getStopPoints().clear();
		collection.getVehicleJourneys().clear();
		
		
		for (Route route : routes) {
			boolean validRoute = false;
			if (route.getStopPoints().size() < 2)
				continue;
			for (JourneyPattern jp : route.getJourneyPatterns()) {
				boolean validJourneyPattern = false;
				if (jp.getStopPoints().size() < 2)
					continue; // no stops
				if (jp.getDepartureStopPoint() == null || jp.getArrivalStopPoint() == null) {
					ChouetteModelUtil.refreshDepartureArrivals(jp);
				}
				for (VehicleJourney vehicleJourney : jp.getVehicleJourneys()) {
					if (vehicleJourney.getVehicleJourneyAtStops().isEmpty()) {
						continue;
					}
					if (startDate == null && endDate == null) {
						boolean isValid = false;
						for (Timetable timetable : vehicleJourney.getTimetables()) {

							if (collection.getTimetables().contains(timetable)) {
								isValid = true;
							} else {
								if (!timetable.getPeriods().isEmpty() || !timetable.getCalendarDays().isEmpty()) {
									collection.getTimetables().add(timetable);
									isValid = true;
								}
							}
						}
						if (isValid) {
							collection.getTimetables().addAll(vehicleJourney.getTimetables());
							collection.getVehicleJourneys().add(vehicleJourney);
							validJourneyPattern = true;
							validRoute = true;
							validLine = true;
						}
					} else {
						boolean isValid = false;
						for (Timetable timetable : vehicleJourney.getTimetables()) {
							if (collection.getTimetables().contains(timetable)) {
								isValid = true;
							} else if (collection.getExcludedTimetables().contains(timetable)) {
								isValid = false;
							} else {

								if (startDate == null)
									isValid = timetable.isActiveBefore(endDate);
								else if (endDate == null)
									isValid = timetable.isActiveAfter(startDate);
								else
									isValid = timetable.isActiveOnPeriod(startDate, endDate);
								if (isValid)
									collection.getTimetables().add(timetable);
								else
									collection.getExcludedTimetables().add(timetable);
							}
						}
						if (isValid) {
							collection.getVehicleJourneys().add(vehicleJourney);
							if (vehicleJourney.getCompany() != null) {
								collection.getCompanies().add(vehicleJourney.getCompany());
							}
							validJourneyPattern = true;
							validRoute = true;
							validLine = true;
							for (Footnote note : vehicleJourney.getFootnotes()) {
								if (!collection.getNotices().contains(note))
									collection.getNotices().add(note);
							}
						}
					}
				} // end vehiclejourney loop
				if (validJourneyPattern)
					collection.getJourneyPatterns().add(jp);
			} // end journeyPattern loop
			if (validRoute) {
				collection.getRoutes().add(route);
				route.getOppositeRoute(); // to avoid lazy loading afterward
				for (StopPoint stopPoint : route.getStopPoints()) {
					if (stopPoint == null)
						continue; // protection from missing stopPoint ranks
					collection.getStopPoints().add(stopPoint);
				}
			}
		} // end route loop
		if (validLine) {
			collection.setLineLite(line);
		}
		return res;
	}


}
