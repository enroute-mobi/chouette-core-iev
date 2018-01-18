package mobi.chouette.exchange.importer.updater;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import mobi.chouette.common.Context;
import mobi.chouette.dao.StopPointDAO;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.util.ChecksumUtil;

@Stateless(name = VehicleJourneyAtStopUpdater.BEAN_NAME)
public class VehicleJourneyAtStopUpdater implements Updater<VehicleJourneyAtStop> {

	public static final String BEAN_NAME = "VehicleJourneyAtStopUpdater";

	@EJB
	private StopPointDAO stopPointDAO;

	@Override
	public void update(Context context, VehicleJourneyAtStop oldValue, VehicleJourneyAtStop newValue) {
		// The list of fields to sunchronize with
		// LineRegisterCommand.write(StringWriter buffer, VehicleJourney
		// vehicleJourney, StopPoint stopPoint,
		// VehicleJourneyAtStop vehicleJourneyAtStop)

		if (newValue.getArrivalTime() != null && !newValue.getArrivalTime().equals(oldValue.getArrivalTime())) {
			oldValue.setArrivalTime(newValue.getArrivalTime());
		}
		if (newValue.getDepartureTime() != null && !newValue.getDepartureTime().equals(oldValue.getDepartureTime())) {
			oldValue.setDepartureTime(newValue.getDepartureTime());
		}

		if (newValue.getArrivalDayOffset() != oldValue.getArrivalDayOffset()) {
			oldValue.setArrivalDayOffset(newValue.getArrivalDayOffset());
		}
		if (newValue.getDepartureDayOffset() != oldValue.getDepartureDayOffset()) {
			oldValue.setDepartureDayOffset(newValue.getDepartureDayOffset());
		}

		// StopPoint
		if (oldValue.getStopPoint() == null || !oldValue.getStopPoint().equals(newValue.getStopPoint())) {
			StopPoint stopPoint = stopPointDAO.findByObjectId(newValue.getStopPoint().getObjectId());
			if (stopPoint != null) {
				oldValue.setStopPoint(stopPoint);
			}
		}
		
		ChecksumUtil.checksum(context, oldValue);
	}

}
