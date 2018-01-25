package mobi.chouette.exchange.netex_stif.exporter.writer;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.exporter.ExportableData;
import mobi.chouette.model.CalendarDay;
import mobi.chouette.model.Period;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.type.DayTypeEnum;

public class NetexCalendrierWriter extends AbstractWriter {


	public static void write(Context context, Writer writer, ExportableData data) throws IOException {
		String participantRef = OFFRE_PARTICIPANT_REF;
		String prefix = FRAME_REF_PREFIX;
		String lineName = ""; // TODO 

		openPublicationDelivery(writer, participantRef, data.getGlobalValidityPeriod(), lineName);
		openGeneralFrame(writer, prefix, "NETEX_CALENDRIER", data.getValidityPeriods(), false);
		writeDayTypes(writer, data);
		writeDayTypeAssignments(writer, data);
		writeOperatingPeriods(writer, data);
		closeGeneralFrame(writer, false);
		closePublicationDelivery(writer);

	}

	private static void writeDayTypes(Writer writer, ExportableData data) throws IOException {
		for (Timetable object : data.getTimetables()) {
			writer.write("                   <netex:DayType id=\"" + object.getObjectId() + "\" version=\"any\">\n");
			writer.write("                      <netex:Name>" + toXml(object.getComment()) + "</netex:Name>\n");
			if (!object.getDayTypes().isEmpty()) {
				writer.write("                      <netex:properties>\n");
				for (DayTypeEnum child : object.getDayTypes()) {
					writer.write("                         <netex:PropertyOfDay>\n");
					writer.write("                            <netex:DaysOfWeek>" + child.toString()
							+ "</netex:DaysOfWeek>\n");
					writer.write("                         </netex:PropertyOfDay>\n");

				}
				writer.write("                      </netex:properties>\n");
			}
			writer.write("                   <netex:DayType>\n");
		}
	}

	//@SuppressWarnings("unused")
	private static void writeDayTypeAssignments(Writer writer, ExportableData data) throws IOException {
		int rank = 1;
		SimpleDateFormat format = new SimpleDateFormat(DATE_00);
		for (Timetable object : data.getTimetables()) {
			String prefix = object.objectIdPrefix();
			for (int periodRank = 1; periodRank <= object.getPeriods().size(); periodRank++) {
				writer.write("                   <netex:DayTypeAssignment id=\"" + prefix + ":DayTypeAssignment:" + rank
						+ ":LOC\" version=\"any\" order=\"0\" >\n");
				writer.write("                      <netex:OperatingPeriodRef ref=\""
						+ buildChildSequenceId(object, "DayType", "OperatingPeriod", periodRank)
						+ "\" version=\"any\"/>\n");
				writer.write("                      <netex:DayTypeRef ref=\"" + object.getObjectId()
						+ "\" version=\"any\"/>\n");
				writer.write("                   </netex:DayTypeAssignment>\n");
				periodRank++;
				rank++;
			}
			for (CalendarDay child : object.getCalendarDays()) {
				writer.write("                   <netex:DayTypeAssignment id=\"" + prefix + ":DayTypeAssignment:" + rank
						+ ":LOC\" version=\"any\" order=\"0\" >\n");
				writer.write("                      <netex:Date>" + format.format(child.getDate()) + "</netex:Date\n");
				writer.write("                      <netex:DayTypeRef ref=\"" + object.getObjectId()
						+ "\" version=\"any\"/>\n");
				writer.write(
						"                      <netex:isAvailable>" + child.getIncluded() + "</netex:isAvailable\n");
				writer.write("                   </netex:DayTypeAssignment>\n");
				rank++;
			}
		}

	}

	private static void writeOperatingPeriods(Writer writer, ExportableData data) throws IOException {
		SimpleDateFormat format = new SimpleDateFormat(DATE_00);
		for (Timetable object : data.getTimetables()) {
			int periodRank = 1;
			for (Period child : object.getPeriods()) {
				writer.write("                   <netex:OperatingPeriod id=\"" + buildChildSequenceId(object, "DayType", "OperatingPeriod", periodRank)+"\" version=\"any\" >\n");
				writer.write("                      <netex:FromDate>"+format.format(child.getStartDate())+"</netex:FromDate\n");
				writer.write("                      <netex:ToDate>"+format.format(child.getEndDate())+"</netex:ToDate\n");
				writer.write("                   </netex:OperatingPeriod>\n");
				periodRank++;
			}
		}
	}

}
