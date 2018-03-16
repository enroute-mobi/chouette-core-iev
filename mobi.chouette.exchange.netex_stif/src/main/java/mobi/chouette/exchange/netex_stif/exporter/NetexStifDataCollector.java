package mobi.chouette.exchange.netex_stif.exporter;

import java.sql.Date;
import java.util.Collection;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.util.ChouetteModelUtil;
import mobi.chouette.model.util.Referential;

@Log4j
public class NetexStifDataCollector {
	public boolean collect(Context context, ExportableData collection, Referential referential, Date startDate,
			Date endDate) {
		boolean res = false;
		Collection<Route> routes = referential.getRoutes().values();
		LineLite line = referential.getCurrentLine();
		// check for each route if vehiclejourney is in DateRange scope
		boolean validLine = false;
		boolean validStops = true;
		collection.setLineLite(line);
		collection.getMappedLines().put(line.getId(), line);
		collection.getRoutes().clear();
		collection.getJourneyPatterns().clear();
		collection.getStopPoints().clear();
		collection.getVehicleJourneys().clear();
		if (routes.isEmpty()) {
			log.info("no route for line" + line.getObjectId());
		}
		for (Route route : routes) {
			boolean validRoute = false;
			if (route.getStopPoints().size() < 2) {
				log.error("no stops for route " + route.getObjectId());
				continue;
			}
			for (StopPoint sp : route.getStopPoints()) {
				if (collection.getMappedStopAreas().containsKey(sp.getStopAreaId()))
					continue;
				StopAreaLite stopArea = referential.findStopArea(sp.getStopAreaId());
				if (stopArea == null) {
					log.error("stoparea id =" + sp.getStopAreaId() + "not found");
					validStops= false;
				} else {
					collection.getMappedStopAreas().put(sp.getStopAreaId(), stopArea);
				}
			}
			for (JourneyPattern jp : route.getJourneyPatterns()) {
				boolean validJourneyPattern = false;
				if (jp.getStopPoints().size() < 2) {
					log.error("no stops for journeyPattern " + jp.getObjectId());
					continue; // no stops
				}
				if (jp.getDepartureStopPoint() == null || jp.getArrivalStopPoint() == null) {
					ChouetteModelUtil.refreshDepartureArrivals(jp);
				}
				for (VehicleJourney vehicleJourney : jp.getVehicleJourneys()) {
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
							if (vehicleJourney.getVehicleJourneyAtStops().isEmpty()) {
								log.error("no passing times for journey " + vehicleJourney.getObjectId());
								continue;
							}
							vehicleJourney.sortVjas();
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
								if (isValid) {
									log.info("timetable accepted " + timetable.getObjectId());
									collection.getTimetables().add(timetable);
								} else {
									log.info("timetable excluded " + timetable.getObjectId());
									collection.getExcludedTimetables().add(timetable);
								}
							}
						}
						if (isValid) {
							if (vehicleJourney.getVehicleJourneyAtStops().isEmpty()) {
								log.error("no passing times for journey " + vehicleJourney.getObjectId());
								continue;
							}							
							vehicleJourney.sortVjas();
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
						} else {
							log.info("no valid timetable for journey " + vehicleJourney.getObjectId());
						}
					}
				} // end vehiclejourney loop
				if (validJourneyPattern)
					collection.getJourneyPatterns().add(jp);
			} // end journeyPattern loop
			if (validRoute) {
				collection.getRoutes().add(route);
			    collection.getRoutingConstraints().addAll(route.getRoutingConstraints());
				route.getOppositeRoute(); // to avoid lazy loading afterward
				for (StopPoint stopPoint : route.getStopPoints()) {
					if (stopPoint == null)
						continue; // protection from missing stopPoint ranks
					collection.getStopPoints().add(stopPoint);
				}
			}
		} // end route loop
		if (validLine && validStops) {
			res = true;
		}
		return res;
	}

}
