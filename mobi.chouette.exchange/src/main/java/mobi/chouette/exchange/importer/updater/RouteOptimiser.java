package mobi.chouette.exchange.importer.updater;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import mobi.chouette.dao.JourneyPatternDAO;
import mobi.chouette.dao.RouteDAO;
import mobi.chouette.dao.RoutingConstraintDAO;
import mobi.chouette.dao.StopPointDAO;
import mobi.chouette.dao.TimetableDAO;
import mobi.chouette.dao.VehicleJourneyDAO;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Route;
import mobi.chouette.model.RoutingConstraint;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Stateless
public class RouteOptimiser {

	@EJB
	private TimetableDAO timetableDAO;

	@EJB
	private RouteDAO routeDAO;

	@EJB
	private JourneyPatternDAO journeyPatternDAO;

	@EJB
	private VehicleJourneyDAO vehicleJourneyDAO;

	@EJB
	private StopPointDAO stopPointDAO;

    @EJB 
    private RoutingConstraintDAO routingConstraintDAO;
    
	public void initialize(Referential cache, Referential referential) {

//		Monitor monitor = MonitorFactory.start("RouteOptimiser");
		initializeTimetable(cache, referential.getTimetables().values());
		initializeRoute(cache, referential.getRoutes().values());
		initializeStopPoint(cache, referential.getStopPoints().values());
		initializeJourneyPattern(cache, referential.getJourneyPatterns().values());
		initializeVehicleJourney(cache, referential.getVehicleJourneys().values());
		initializeRouingConstraints(cache, referential.getRoutingConstraints().values());
//		monitor.stop();
	}


	private void initializeTimetable(Referential cache, Collection<Timetable> list) {
		if (list != null && !list.isEmpty()) {
			Collection<String> objectIds = UpdaterUtils.getObjectIds(list);
			List<Timetable> objects = timetableDAO.findByObjectId(objectIds);
			for (Timetable object : objects) {
				cache.getTimetables().put(object.getObjectId(), object);
			}

			for (Timetable item : list) {
				if (!cache.getTimetables().containsKey(item.getObjectId())) {
					ObjectFactory.getTimetable(cache, item.getObjectId());
				}
			}
		}
	}



	private void initializeRoute(Referential cache, Collection<Route> list) {
		if (list != null && !list.isEmpty()) {
			Collection<String> objectIds = UpdaterUtils.getObjectIds(list);
			List<Route> objects = routeDAO.findByObjectId(objectIds);
			for (Route object : objects) {
				cache.getRoutes().put(object.getObjectId(), object);
			}

			for (Route item : list) {
				if (!cache.getRoutes().containsKey(item.getObjectId())) {
					ObjectFactory.getRoute(cache, item.getObjectId());
				}
			}
		}
	}

	private void initializeStopPoint(Referential cache, Collection<StopPoint> list) {
		if (list != null && !list.isEmpty()) {
			Collection<String> objectIds = UpdaterUtils.getObjectIds(list);
			List<StopPoint> objects = stopPointDAO.findByObjectId(objectIds);
			for (StopPoint object : objects) {
				cache.getStopPoints().put(object.getObjectId(), object);
			}

			for (StopPoint item : list) {
				if (!cache.getStopPoints().containsKey(item.getObjectId())) {
					ObjectFactory.getStopPoint(cache, item.getObjectId());
				}
			}
		}
	}

	private void initializeJourneyPattern(Referential cache, Collection<JourneyPattern> list) {
		if (list != null && !list.isEmpty()) {
			Collection<String> objectIds = UpdaterUtils.getObjectIds(list);
			List<JourneyPattern> objects = journeyPatternDAO.findByObjectId(objectIds);
			for (JourneyPattern object : objects) {
				cache.getJourneyPatterns().put(object.getObjectId(), object);
			}

			for (JourneyPattern item : list) {
				if (!cache.getJourneyPatterns().containsKey(item.getObjectId())) {
					ObjectFactory.getJourneyPattern(cache, item.getObjectId());
				}
			}
		}
	}

	private void initializeVehicleJourney(Referential cache, Collection<VehicleJourney> list) {
		if (list != null && !list.isEmpty()) {
			Collection<String> objectIds = UpdaterUtils.getObjectIds(list);
			List<VehicleJourney> objects = vehicleJourneyDAO.findByObjectId(objectIds);
			for (VehicleJourney object : objects) {
				cache.getVehicleJourneys().put(object.getObjectId(), object);
			}

			for (VehicleJourney item : list) {
				if (!cache.getVehicleJourneys().containsKey(item.getObjectId())) {
					ObjectFactory.getVehicleJourney(cache, item.getObjectId());
				}
			}
		}
	}

	private void initializeRouingConstraints(Referential cache, Collection<RoutingConstraint> list) {
		if (list != null && !list.isEmpty()) {
			Collection<String> objectIds = UpdaterUtils.getObjectIds(list);
			List<RoutingConstraint> objects = routingConstraintDAO.findByObjectId(objectIds);
			for (RoutingConstraint object : objects) {
				cache.getRoutingConstraints().put(object.getObjectId(), object);
			}

			for (RoutingConstraint item : list) {
				if (!cache.getRoutingConstraints().containsKey(item.getObjectId())) {
					ObjectFactory.getRoutingConstraint(cache, item.getObjectId());
				}
			}
		}
	}
}
