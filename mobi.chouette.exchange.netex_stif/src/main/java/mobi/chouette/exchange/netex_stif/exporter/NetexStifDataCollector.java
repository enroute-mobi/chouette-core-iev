package mobi.chouette.exchange.netex_stif.exporter;

import java.sql.Date;
import java.util.Collection;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.model.CompanyLite;
import mobi.chouette.model.CompanyLite;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.LineNotice;
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
		collection.getCompanies().clear();
		collection.getLineNotices().clear();

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
				StopAreaLite stopArea = referential.findStopAreaExtended(sp.getStopAreaId());
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
					if (vehicleJourney.getCompanyId()!=null && !collection.getMappedCompanies().containsKey(vehicleJourney.getCompanyId())) {
						CompanyLite company = referential.findCompany(vehicleJourney.getCompanyId());
						if (company == null) {
							log.error("NetexStifDataCollector # Company id =" + vehicleJourney.getCompanyId() + "not found ###");
						} else {
							collection.getMappedCompanies().put(vehicleJourney.getCompanyId(), company);
						}
					}

					if (vehicleJourney.getLineNoticeIds()!=null) {
						for (Long lineNoticeId : vehicleJourney.getLineNoticeIds()) {
							if (!collection.getMappedLineNotices().containsKey(lineNoticeId)) {
								LineNotice lineNotice = referential.findLineNotice(lineNoticeId);
								if (lineNotice == null) {
									log.error("NetexStifDataCollector # LineNotice id =" + lineNoticeId + "not found ###");
								} else {
									collection.getMappedLineNotices().put(lineNoticeId, lineNotice);
								}
							}
						}
					}

					boolean isValid = false;
					if (startDate == null && endDate == null) {
						for (Timetable timetable : vehicleJourney.getTimetables()) {
							if (collection.getTimetables().contains(timetable)) {
								isValid = true;
							} else if (!timetable.getPeriods().isEmpty() || !timetable.getCalendarDays().isEmpty()) {
								collection.getTimetables().add(timetable);
								isValid = true;
							}
						}
					} else {
						for (Timetable timetable : vehicleJourney.getTimetables()) {
							boolean matchingTimetable = false;
							if (collection.getTimetables().contains(timetable)) {
								isValid = true;
							} else {
								if (startDate == null) {
									if (timetable.isActiveBefore(endDate)) {matchingTimetable=true;}
								}
								else if (endDate == null) {
									if (timetable.isActiveAfter(startDate)) {matchingTimetable=true;}
								}
								else {
									if (timetable.isActiveOnPeriod(startDate, endDate)) {matchingTimetable=true;}
								}
								if (matchingTimetable) {
									isValid = true;
									log.info("NetexStifDataCollector # Timetable accepted " + timetable.getId());
									collection.getTimetables().add(timetable);
								} else {
									log.info("NetexStifDataCollector # Timetable excluded " + timetable.getId());
									collection.getExcludedTimetables().add(timetable);
								}
							}
						}
					}
					if (isValid) {
						if (vehicleJourney.getVehicleJourneyAtStops().isEmpty()) {
							log.error("NetexStifDataCollector # no passing times for vehicle journey " + vehicleJourney.getId());
							continue;
						}
						vehicleJourney.sortVjas();
						collection.getVehicleJourneys().add(vehicleJourney);
						if (vehicleJourney.getCompany() != null) {
							collection.getCompanies().add(vehicleJourney.getCompany());
						}
						if (vehicleJourney.getLineNoticeIds()!=null) {
							for (Long lineNoticeId : vehicleJourney.getLineNoticeIds()) {
								LineNotice lineNotice = collection.getMappedLineNotices().get(lineNoticeId);
								if (lineNotice != null) { collection.getLineNotices().add(lineNotice); }
							}
						}
						validJourneyPattern = true;
						validRoute = true;
						validLine = true;
						for (Footnote note : vehicleJourney.getFootnotes()) {
							if (!collection.getNotices().contains(note))
								collection.getNotices().add(note);
						}
					} else {
						log.info("no valid timetable for journey " + vehicleJourney.getId());
					}
				} // end vehiclejourney loop
				if (validJourneyPattern) {collection.getJourneyPatterns().add(jp);}
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
