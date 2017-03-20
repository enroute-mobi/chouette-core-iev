package mobi.chouette.exchange.importer.inserter;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import mobi.chouette.common.Context;
import mobi.chouette.dao.StopPointDAO;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.util.Referential;

@Stateless(name = VehicleJourneyAtStopInserter.BEAN_NAME)
public class VehicleJourneyAtStopInserter implements
		Inserter<VehicleJourneyAtStop> {

	public static final String BEAN_NAME = "VehicleJourneyAtStopInserter";

	@EJB 
	private StopPointDAO stopPointDAO;

	
	@Override
	public void insert(Context context, VehicleJourneyAtStop oldValue,
			VehicleJourneyAtStop newValue) { 
		// The list of fields to synchronize with LineRegisterCommand.write(StringWriter buffer, VehicleJourney vehicleJourney, StopPoint stopPoint,
		//    VehicleJourneyAtStop vehicleJourneyAtStop)
		Referential cache = (Referential) context.get(CACHE);
		

		if (newValue.getArrivalTime() != null
				&& !newValue.getArrivalTime().equals(oldValue.getArrivalTime())) {
			oldValue.setArrivalTime(newValue.getArrivalTime());
		}
		if (newValue.getDepartureTime() != null
				&& !newValue.getDepartureTime().equals(
						oldValue.getDepartureTime())) {
			oldValue.setDepartureTime(newValue.getDepartureTime());
		}
		
		if (newValue.getArrivalDayOffset() != oldValue.getArrivalDayOffset()) {
			oldValue.setArrivalDayOffset(newValue.getArrivalDayOffset());
		}
		if (newValue.getDepartureDayOffset() != oldValue.getDepartureDayOffset()) {
			oldValue.setDepartureDayOffset(newValue.getDepartureDayOffset());
		}
		
		// if (newValue.getElapseDuration() != null
		// 		&& !newValue.getElapseDuration().equals(
		// 				oldValue.getElapseDuration())) {
		// 	oldValue.setElapseDuration(newValue.getElapseDuration());
		// }
		// if (newValue.getHeadwayFrequency() != null
		// 		&& !newValue.getHeadwayFrequency().equals(
		// 				oldValue.getHeadwayFrequency())) {
		// 	oldValue.setHeadwayFrequency(newValue.getHeadwayFrequency());
		// }

		// VehicleJourney
		if (oldValue.getVehicleJourney() == null
				|| !oldValue.getVehicleJourney().equals(newValue.getVehicleJourney())) {
			VehicleJourney vj = cache.getVehicleJourneys().get(newValue.getVehicleJourney().getObjectId());
			if (vj != null) {
				oldValue.setVehicleJourney(vj);
			}
		}
		// StopPoint
		if (oldValue.getStopPoint() == null
				|| !oldValue.getStopPoint().equals(newValue.getStopPoint())) {
			StopPoint stopPoint = cache.getStopPoints().get(newValue.getStopPoint().getObjectId());
			if (stopPoint != null) {
				oldValue.setStopPoint(stopPoint);
			}
		}
	}

}
