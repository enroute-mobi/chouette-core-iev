package mobi.chouette.exchange.netex_stif.exporter.writer;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;

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
		String prefix = FRAME_REF_PREFIX;
		String lineName = NamingUtil.getName(line);

		// TODO : manage versions, RouteLinks and RoutePoints
		FILE_TYPE type = (lineName == null ? FILE_TYPE.FULL : FILE_TYPE.LINE);
		openPublicationDelivery(writer, participantRef, data.getGlobalValidityPeriod(), lineName, type);
		openCompositeFrame(writer, prefix, "NETEX_OFFRE_LIGNE", lineName,data.getRoutes().isEmpty());
		if (!data.getRoutes().isEmpty()) {
			openGeneralFrame(writer, prefix, "NETEX_STRUCTURE", null, true, false);
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
			
			openGeneralFrame(writer, prefix, "NETEX_HORAIRE", null, true, false);
			writeServiceJourneys(writer, data);
			closeGeneralFrame(writer, true, false);
		}
		closeCompositeFrame(writer,data.getRoutes().isEmpty());
		closePublicationDelivery(writer);

	}

	private static void writeRoutes(Writer writer, ExportableData data) throws IOException {
		// TODO : manage versions, points on routes
		LineLite line = data.getLineLite();
		for (Route object : data.getRoutes()) {
			writer.write("                   <netex:Route id=\"" + object.getObjectId() + "\" version=\"any\">\n");
			writer.write("                      <netex:Name>" + toXml(object.getName()) + "</netex:Name>\n");
			writer.write("                      <netex:LineRef ref=\"" + line.getObjectId()
					+ "\">version=\"any\"</netex:LineRef>\n");
			writer.write(
					"                      <netex:DirectionType>" + object.getWayback() + "</netex:DirectionType>\n");
			writer.write("                      <netex:DirectionRef ref=\""
					+ object.getObjectId().replace(":Route:", ":Direction:") + "\" version=\"any\"/>\n");
			if (object.getOppositeRoute() != null)
				writer.write("                      <netex:InverseRouteRef ref=\""
						+ object.getOppositeRoute().getObjectId() + "\" version=\"any\"/>\n");
			writer.write("                   </netex:Route>\n");
		}
	}

	private static void writeDirections(Writer writer, ExportableData data) throws IOException {
		// TODO Manage version and direction name
		for (Route object : data.getRoutes()) {
			writer.write("                   <netex:Direction id=\""
					+ object.getObjectId().replace(":Route:", ":Direction:") + "\" version=\"any\">\n");
			writer.write("                      <netex:Name>" + toXml(object.getPublishedName()) + "</netex:Name>\n");
			writer.write("                   </netex:Direction>\n");
		}

	}

	private static void writeServiceJourneyPatterns(Writer writer, ExportableData data) throws IOException {
		// TODO Manage version, check existence of name and destination display
		for (JourneyPattern object : data.getJourneyPatterns()) {
			writer.write("                   <netex:ServiceJourneyPattern id=\"" + object.getObjectId()
					+ "\" version=\"any\">\n");
			writer.write("                      <netex:Name>" + toXml(object.getName()) + "</netex:Name>\n");
			writer.write("                      <netex:RouteRef ref=\"" + object.getRoute().getObjectId()
					+ "\" version=\"any\"/>\n");
			if (object.getPublishedName() != null || object.getRegistrationNumber() != null) {
				writer.write("                      <netex:DestinationDisplayRef ref=\""
						+ object.getObjectId().replace(":ServiceJourneyPattern:", ":DestinationDisplay:")
						+ "\" version=\"any\"/>\n");
			}
			writer.write("                      <netex:pointsInSequence>\n");
			int rank = 1;
			for (StopPoint child : object.getStopPoints()) {
				writer.write("                         <netex:StopPointInJourneyPattern id=\""
						+ buildChildSequenceId(object, "ServiceJourneyPattern", "StopPointInJourneyPattern", rank++)
						+ "\" order=\"" + (child.getPosition() + 1) + "\" version=\"any\">\n");
				writer.write("                            <netex:ScheduledStopPointRef ref=\""
						+ buildScheduledStopPointId(child) + "\" version=\"any\"/>\n");
				writer.write("                            <netex:ForAlighting>"
						+ child.getForAlighting().equals(AlightingPossibilityEnum.normal) + "</netex:ForAlighting>\n");
				writer.write("                            <netex:ForBoarding>"
						+ child.getForBoarding().equals(BoardingPossibilityEnum.normal) + "</netex:ForBoarding>\n");
				writer.write("                         </netex:StopPointInJourneyPattern>\n");
			}
			writer.write("                      </netex:pointsInSequence>\n");
			writer.write(
					"                      <netex:ServiceJourneyPatternType>passenger</netex:ServiceJourneyPatternType>\n");
			writer.write("                   </netex:ServiceJourneyPattern>\n");
		}
	}

	private static void writDestinationDisplays(Writer writer, ExportableData data) throws IOException {
		// TODO Manage version and check existence of destination display

		for (JourneyPattern object : data.getJourneyPatterns()) {
			if (object.getPublishedName() != null || object.getRegistrationNumber() != null) {
				writer.write("                   <netex:DestinationDisplay id=\""
						+ object.getObjectId().replace(":ServiceJourneyPattern:", ":DestinationDisplay:")
						+ "\" version=\"any\">\n");
				if (object.getPublishedName() != null)
					writer.write("                      <netex:FrontText>" + toXml(object.getPublishedName())
							+ "</netex:FrontText>\n");
				if (object.getRegistrationNumber() != null)
					writer.write("                      <netex:PublicCode>" + object.getRegistrationNumber()
							+ "</netex:PublicCode>\n");
				writer.write("                   </netex:DestinationDisplay>\n");
			}
		}

	}

	private static void writeScheduledStopPoints(Writer writer, ExportableData data) throws IOException {
		// TODO Manage version
		for (Route object : data.getRoutes()) {
			for (StopPoint child : object.getStopPoints()) {
				writer.write("                   <netex:ScheduledStopPoint id=\"" + buildScheduledStopPointId(child)
						+ "\" version=\"any\" />\n");
			}
		}

	}

	private static void writePassengerStopAssignments(Writer writer, ExportableData data) throws IOException {
		// TODO Manage version
		for (Route object : data.getRoutes()) {
			for (StopPoint child : object.getStopPoints()) {
				StopAreaLite quay = data.getMappedStopAreas().get(child.getStopAreaId());
				writer.write("                   <netex:PassengerStopAssignment id=\""
						+ buildPassengerStopAssignmentId(child) + "\" version=\"any\" order=\"0\">\n");
				writer.write("                      <netex:ScheduledStopPointRef ref=\""
						+ buildScheduledStopPointId(child) + "\" version=\"any\" />\n");
				writer.write("                      <netex:QuayRef ref=\"" + quay.getObjectId()
						+ "\">version=\"any\"</netex:QuayRef>\n");
				writer.write("                   </netex:PassengerStopAssignment>\n");
			}
		}

	}

	private static void writeRoutingConstraints(Writer writer, ExportableData data) throws IOException {
		// TODO manage versions
		for (RoutingConstraint object : data.getRoutingConstraints()) {
			writer.write("                   <netex:RoutingConstraintZone id=\"" + object.getObjectId()
					+ "\" version=\"any\">\n");
			writer.write("                      <netex:Name>" + toXml(object.getName()) + "</netex:Name>\n");
			writer.write("                      <netex:members>\n");
			for (StopPoint child : object.getStopPoints()) {
				writer.write("                         <netex:ScheduledStopPointRef ref=\""
						+ buildScheduledStopPointId(child) + "\" version=\"any\" />\n");
			}
			writer.write("                      </netex:members>\n");
			writer.write("                      <netex:ZoneUse>cannotBoardAndAlightInSameZone</netex:ZoneUse>\n");
			writer.write("                   </netex:RoutingConstraintZone>\n");
		}
	}

	private static void writeServiceJourneys(Writer writer, ExportableData data) throws IOException {
		// TODO manage version and times
		for (VehicleJourney object : data.getVehicleJourneys()) {
			writer.write(
					"                   <netex:ServiceJourney id=\"" + object.getObjectId() + "\" version=\"any\">\n");
			writer.write(
					"                      <netex:Name>" + toXml(object.getPublishedJourneyName()) + "</netex:Name>\n");
			if (!object.getFootnotes().isEmpty()) {
				writer.write("                      <netex:noticeAssignments>\n");
				int rank = 1;
				for (Footnote child : object.getFootnotes()) {
					writer.write("                         <netex:NoticeAssignment id=\""
							+ buildChildSequenceId(object, "ServiceJourney", "NoticeAssignment", rank++)
							+ "\" version=\"any\" order=\"0\">\n");
					writer.write("                            <netex:NoticeRef ref=\"" + child.getObjectId()
							+ "\">version=\"any\"</netex:NoticeRef>\n");
					writer.write("                         </netex:NoticeAssignment>\n");
				}
				writer.write("                      </netex:noticeAssignments>\n");
			}
			writer.write("                      <netex:dayTypes>\n");
			for (Timetable child : object.getTimetables()) {
				writer.write("                         <netex:DayTypeRef ref=\"" + child.getObjectId()
						+ "\">version=\"any\"</netex:DayTypeRef>\n");
			}
			writer.write("                      </netex:dayTypes>\n");
			writer.write("                      <netex:JourneyPatternRef ref=\""
					+ object.getJourneyPattern().getObjectId() + "\" version=\"any\"/>\n");
			if (object.getCompanyId() != null) {
				CompanyLite company = data.getMappedCompanies().get(object.getCompanyId());
				writer.write("                      <netex:OperatorRef ref=\"" + company.getObjectId()
						+ "\">version=\"any\"</netex:OperatorRef>\n");
			}

			if (object.getPublishedJourneyIdentifier() != null) {
				writer.write("                      <netex:trainNumbers>\n");
				writer.write("                         <netex:TrainNumberRef ref=\"" + object.objectIdPrefix()
						+ ":TrainNumber:" + object.getPublishedJourneyIdentifier()
						+ ":LOC\">version=\"any\"</netex:TrainNumberRef>\n");
				writer.write("                      </netex:trainNumbers>\n");
			}

			writer.write("                      <netex:passingTimes>\n");
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			for (VehicleJourneyAtStop child : object.getVehicleJourneyAtStops()) {
				writer.write("                         <netex:TimetabledPassingTime version=\"any\">\n");
				writer.write("                            <netex:ArrivalTime>" + format.format(child.getArrivalTime())
						+ "</netex:ArrivalTime>\n");
				// writer.write(" <netex:ArrivalDayOffset>" +
				// child.getArrivalDayOffset()+"</netex:ArrivalDayOffset>\n");
				writer.write("                            <netex:DepartureTime>"
						+ format.format(child.getDepartureTime()) + "</netex:DepartureTime>\n");
				writer.write("                            <netex:DepartureDayOffset>" + child.getDepartureDayOffset()
						+ "</netex:DepartureDayOffset>\n");
				writer.write("                         </netex:TimetabledPassingTime>\n");
			}
			writer.write("                      </netex:passingTimes>\n");
			writer.write("                   </netex:ServiceJourney>\n");
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
