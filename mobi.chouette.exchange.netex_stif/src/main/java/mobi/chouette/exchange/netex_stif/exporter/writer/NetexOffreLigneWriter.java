package mobi.chouette.exchange.netex_stif.exporter.writer;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;

import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.exporter.ExportableData;
import mobi.chouette.model.CompanyLite;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.RoutingConstraint;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.type.AlightingPossibilityEnum;
import mobi.chouette.model.type.BoardingPossibilityEnum;
import mobi.chouette.model.util.NamingUtil;

public class NetexOffreLigneWriter extends AbstractWriter {

	public static void write(Writer writer, ExportableData data) throws IOException {
		LineLite line = data.getLineLite();

		String participantRef = OFFRE_PARTICIPANT_REF;
		String prefix = ROOT_PREFIX;
		String lineName = NamingUtil.getName(line);

		// TODO : manage versions, RouteLinks and RoutePoints
		FILE_TYPE type = (lineName == null ? FILE_TYPE.FULL : FILE_TYPE.LINE);
		openPublicationDelivery(writer, participantRef, data.getGlobalValidityPeriod(), lineName, type);
		openCompositeFrame(writer, prefix, NetexStifConstant.NETEX_OFFRE_LIGNE, lineName, data.getRoutes().isEmpty(),data.getRoutes().isEmpty());
		if (!data.getRoutes().isEmpty()) {
			openGeneralFrame(writer, prefix, NetexStifConstant.NETEX_STRUCTURE, null, true, false);
			writeRoutes(writer, data);
			// writeRoutePoints(writer, data);
			// writeRouteLinks(writer, data);
			writeDirections(writer, data);
			writeServiceJourneyPatterns(writer, data);
			writDestinationDisplays(writer, data);
			writeScheduledStopPoints(writer, data);
			writePassengerStopAssignments(writer, data);
			writeRoutingConstraints(writer, data);
			closeGeneralFrame(writer, true, false);

			openGeneralFrame(writer, prefix, NetexStifConstant.NETEX_HORAIRE, null, true, false);
			writeServiceJourneys(writer, data);
			closeGeneralFrame(writer, true, false);
		}
		closeCompositeFrame(writer, data.getRoutes().isEmpty());
		closePublicationDelivery(writer);

	}

	private static void writeRoutes(Writer writer, ExportableData data) throws IOException {
		// TODO : manage versions, points on routes
		LineLite line = data.getLineLite();
		for (Route object : data.getRoutes()) {
			write(writer,6,"<Route "+buildDataSourceRef(data,object)+" id=\"" + object.getObjectId() + "\" version=\"any\">");
			write(writer,7,"<Name>" + toXml(object.getName()) + "</Name>");
			write(writer,7,"<LineRef ref=\"" + line.getObjectId()
					+ "\">version=\"any\"</LineRef>");
			write(writer,7,"<DirectionType>" + object.getWayback() + "</DirectionType>");
			write(writer,7,"<DirectionRef ref=\""
					+ object.getObjectId().replace(":Route:", ":Direction:") + "\" version=\"any\"/>");
			// only if inverse route exists and is exported  
			if (object.getOppositeRoute() != null && data.getRoutes().contains(object.getOppositeRoute()))
				write(writer,7,"<InverseRouteRef ref=\""
						+ object.getOppositeRoute().getObjectId() + "\" version=\"any\"/>");
			write(writer,6,"</Route>");
		}
	}

	private static void writeDirections(Writer writer, ExportableData data) throws IOException {
		// TODO Manage version and direction name
		for (Route object : data.getRoutes()) {
			write(writer,6,"<Direction "+buildDataSourceRef(data,object)+" id=\""
					+ object.getObjectId().replace(":Route:", ":Direction:") + "\" version=\"any\">");
			write(writer,7,"<Name>" + toXml(object.getPublishedName()) + "</Name>");
			write(writer,6,"</Direction>");
		}

	}

	private static void writeServiceJourneyPatterns(Writer writer, ExportableData data) throws IOException {
		// TODO Manage version, check existence of name and destination display
		for (JourneyPattern object : data.getJourneyPatterns()) {
			write(writer,6,"<ServiceJourneyPattern "+buildDataSourceRef(data,object)+" id=\"" + object.getObjectId()
					+ "\" version=\"any\">");
			write(writer,7,"<Name>" + toXml(object.getName()) + "</Name>");
			write(writer,7,"<RouteRef ref=\"" + object.getRoute().getObjectId()
					+ "\" version=\"any\"/>");
			if (object.getPublishedName() != null || object.getRegistrationNumber() != null) {
				write(writer,7,"<DestinationDisplayRef ref=\""
						+ object.getObjectId().replace(":ServiceJourneyPattern:", ":DestinationDisplay:")
						+ "\" version=\"any\"/>");
			}
			write(writer,7,"<pointsInSequence>");
			int rank = 1;
			for (StopPoint child : object.getStopPoints()) {
				write(writer,8,"<StopPointInJourneyPattern id=\""
						+ buildChildSequenceId(object, "ServiceJourneyPattern", "StopPointInJourneyPattern", rank++)
						+ "\" order=\"" + (child.getPosition() + 1) + "\" version=\"any\">");
				write(writer,9,"<ScheduledStopPointRef ref=\""
						+ buildScheduledStopPointId(child) + "\" version=\"any\"/>");
				write(writer,9,"<ForAlighting>"
						+ child.getForAlighting().equals(AlightingPossibilityEnum.normal) + "</ForAlighting>");
				write(writer,9,"<ForBoarding>"
						+ child.getForBoarding().equals(BoardingPossibilityEnum.normal) + "</ForBoarding>");
				write(writer,8,"</StopPointInJourneyPattern>");
			}
			write(writer,7,"</pointsInSequence>");
			write(writer,7,"<ServiceJourneyPatternType>passenger</ServiceJourneyPatternType>");
			write(writer,6,"</ServiceJourneyPattern>");
		}
	}

	private static void writDestinationDisplays(Writer writer, ExportableData data) throws IOException {
		// TODO Manage version and check existence of destination display

		for (JourneyPattern object : data.getJourneyPatterns()) {
			if (object.getPublishedName() != null || object.getRegistrationNumber() != null) {
				write(writer,6,"<DestinationDisplay "+buildDataSourceRef(data,object)+" id=\""
						+ object.getObjectId().replace(":ServiceJourneyPattern:", ":DestinationDisplay:")
						+ "\" version=\"any\">");
				if (object.getPublishedName() != null)
					write(writer,7,"<FrontText>" + toXml(object.getPublishedName())
							+ "</FrontText>");
				if (object.getRegistrationNumber() != null)
					write(writer,7,"<PublicCode>" + object.getRegistrationNumber()
							+ "</PublicCode>");
				write(writer,6,"</DestinationDisplay>");
			}
		}

	}

	private static void writeScheduledStopPoints(Writer writer, ExportableData data) throws IOException {
		// TODO Manage version
		for (Route object : data.getRoutes()) {
			for (StopPoint child : object.getStopPoints()) {
				write(writer,6,"<ScheduledStopPoint "+buildDataSourceRef(data,object)+" id=\"" + buildScheduledStopPointId(child)
						+ "\" version=\"any\" />");
			}
		}

	}

	private static void writePassengerStopAssignments(Writer writer, ExportableData data) throws IOException {
		// TODO Manage version
		for (Route object : data.getRoutes()) {
			for (StopPoint child : object.getStopPoints()) {
				StopAreaLite quay = data.getMappedStopAreas().get(child.getStopAreaId());
				write(writer,6,"<PassengerStopAssignment "+buildDataSourceRef(data,object)+" id=\""
						+ buildPassengerStopAssignmentId(child) + "\" version=\"any\" order=\"0\">");
				write(writer,7,"<ScheduledStopPointRef ref=\""
						+ buildScheduledStopPointId(child) + "\" version=\"any\" />");
				if (quay != null) {
					write(writer,7,"<QuayRef ref=\"" + quay.getObjectId()
							+ "\">version=\"any\"</QuayRef>");
				}
				write(writer,6,"</PassengerStopAssignment>");
			}
		}

	}

	private static void writeRoutingConstraints(Writer writer, ExportableData data) throws IOException {
		// TODO manage versions
		for (RoutingConstraint object : data.getRoutingConstraints()) {
			write(writer,6,"<RoutingConstraintZone "+buildDataSourceRef(data,object)+" id=\"" + object.getObjectId()
					+ "\" version=\"any\">");
			write(writer,7,"<Name>" + toXml(object.getName()) + "</Name>");
			write(writer,7,"<members>");
			for (StopPoint child : object.getStopPoints()) {
				write(writer,8,"<ScheduledStopPointRef ref=\""
						+ buildScheduledStopPointId(child) + "\" version=\"any\" />");
			}
			write(writer,7,"</members>");
			write(writer,7,"<ZoneUse>cannotBoardAndAlightInSameZone</ZoneUse>");
			write(writer,6,"</RoutingConstraintZone>");
		}
	}

	private static void writeServiceJourneys(Writer writer, ExportableData data) throws IOException {
		// TODO manage version and times
		for (VehicleJourney object : data.getVehicleJourneys()) {
			write(writer,6,"<ServiceJourney "+buildDataSourceRef(data,object)+" id=\"" + object.getObjectId() + "\" version=\"any\">");
			write(writer,7,"<Name>" + toXml(object.getPublishedJourneyName()) + "</Name>");
			if (!object.getFootnotes().isEmpty()) {
				write(writer,7,"<noticeAssignments>");
				int rank = 1;
				for (Footnote child : object.getFootnotes()) {
					write(writer,8,"<NoticeAssignment id=\""
							+ buildChildSequenceId(object, "ServiceJourney", "NoticeAssignment", rank++)
							+ "\" version=\"any\" order=\"0\">");
					write(writer,9,"<NoticeRef ref=\"" + child.getObjectId()
							+ "\">version=\"any\"</NoticeRef>");
					write(writer,8,"</NoticeAssignment>");
				}
				write(writer,7,"</noticeAssignments>");
			}
			write(writer,7,"<dayTypes>");
			for (Timetable child : object.getTimetables()) {
				write(writer,8,"<DayTypeRef ref=\"" + child.getObjectId()
						+ "\">version=\"any\"</DayTypeRef>");
			}
			write(writer,7,"</dayTypes>");
			write(writer,7,"<JourneyPatternRef ref=\""
					+ object.getJourneyPattern().getObjectId() + "\" version=\"any\"/>");
			if (object.getCompanyId() != null) {
				CompanyLite company = data.getMappedCompanies().get(object.getCompanyId());
				write(writer,7,"<OperatorRef ref=\"" + company.getObjectId()
						+ "\">version=\"any\"</OperatorRef>");
			}

			if (object.getPublishedJourneyIdentifier() != null) {
				write(writer,7,"<trainNumbers>");
				write(writer,8,"<TrainNumberRef ref=\"" + object.objectIdPrefix()
						+ ":TrainNumber:" + object.getPublishedJourneyIdentifier()
						+ ":LOC\">version=\"any\"</TrainNumberRef>");
				write(writer,7,"</trainNumbers>");
			}

			write(writer,7,"<passingTimes>");
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			for (VehicleJourneyAtStop child : object.getVehicleJourneyAtStops()) {
				write(writer,8,"<TimetabledPassingTime version=\"any\">");
				write(writer,9,"<ArrivalTime>" + format.format(child.getArrivalTime())
						+ "</ArrivalTime>");
				// write(writer,9,"<ArrivalDayOffset>" +
				// child.getArrivalDayOffset()+"</ArrivalDayOffset>");
				write(writer,9,"<DepartureTime>"
						+ format.format(child.getDepartureTime()) + "</DepartureTime>");
				write(writer,9,"<DepartureDayOffset>" + child.getDepartureDayOffset()
						+ "</DepartureDayOffset>");
				write(writer,8,"</TimetabledPassingTime>");
			}
			write(writer,7,"</passingTimes>");
			write(writer,6,"</ServiceJourney>");
		}
	}

	private static String buildScheduledStopPointId(StopPoint stopPoint) {
		return stopPoint.getRoute().getObjectId().replace(":Route:", ":ScheduledStopPoint:").replace(LOC,
				stopPoint.getPosition() + LOC);
	}

	private static String buildPassengerStopAssignmentId(StopPoint stopPoint) {
		return stopPoint.getRoute().getObjectId().replace(":Route:", ":PassengerStopAssignment:").replace(LOC,
				stopPoint.getPosition() + LOC);
	}

}
