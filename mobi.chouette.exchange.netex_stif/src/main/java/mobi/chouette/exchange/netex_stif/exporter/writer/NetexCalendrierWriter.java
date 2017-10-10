package mobi.chouette.exchange.netex_stif.exporter.writer;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;

import mobi.chouette.exchange.netex_stif.exporter.ExportableData;
import mobi.chouette.model.CalendarDay;
import mobi.chouette.model.Period;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.type.DateRange;
import mobi.chouette.model.type.DayTypeEnum;

public class NetexCalendrierWriter extends AbstractWriter {

	public static void write(Writer writer, ExportableData data) throws IOException {
		// TODO : manage frames ID and versions
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddT00:00:00");
		writer.write(
				"        <netex:GeneralFrame id=\"CITYWAY:GeneralFrame:NETEX_CALENDRIER-20170214090012:LOC\" version=\"any\">\n");
		// valid between :
		for (DateRange period : data.getPeriods()) {
			writer.write("            <netex:ValidBetween>\n");
			writer.write("                <netex:FromDate>" + format.format(period.getFirst()) + "</netex:FromDate>\n");
			writer.write("                <netex:ToDate>" + format.format(period.getLast()) + "</netex:ToDate>\n");
			writer.write("            </netex:ValidBetween>\n");
		}
		writer.write("            <netex:TypeOfFrameRef ref=\"NETEX_CALENDRIER\"/>\n");
		writer.write("            <netex:members>\n");
		writeDayTypes(writer, data);
		writeDayTypeAssignments(writer, data);
		writeOperatingPeriods(writer, data);
		writer.write("            </netex:members>\n");
		writer.write("        </netex:GeneralFrame>\n");

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
							+ "</netex:PropertyOfDay>\n");
					writer.write("                         </netex:PropertyOfDay>\n");

				}
				writer.write("                      </netex:properties>\n");
			}
			writer.write("                   <netex:DayType>\n");
		}
	}

	@SuppressWarnings("unused")
	private static void writeDayTypeAssignments(Writer writer, ExportableData data) throws IOException {
		int rank = 1;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddT00:00:00");
		for (Timetable object : data.getTimetables()) {
			String prefix = object.objectIdPrefix();
			int periodRank = 1;
			for (Period child : object.getPeriods()) {
				writer.write("                   <netex:DayTypeAssignment id=\"" + prefix + ":DayTypeAssignment:" + rank
						+ ":LOC\" version=\"any\" order=\"0\" >\n");
				writer.write("                      <netex:OperatingPeriodRef ref=\""
						+ buildChildSequenceId(object, "DayType", "OperatingPeriod", periodRank)
						+ "\" version=\"any\">\n");
				writer.write("                      <netex:DayTypeRef ref=\"" + object.getObjectId()
						+ "\" version=\"any\">\n");
				writer.write("                   </netex:DayTypeAssignment>\n");
				periodRank++;
				rank++;
			}
			for (CalendarDay child : object.getCalendarDays()) {
				writer.write("                   <netex:DayTypeAssignment id=\"" + prefix + ":DayTypeAssignment:" + rank
						+ ":LOC\" version=\"any\" order=\"0\" >\n");
				writer.write("                      <netex:Date>" + format.format(child.getDate()) + "</netex:Date\n");
				writer.write("                      <netex:DayTypeRef ref=\"" + object.getObjectId()
						+ "\" version=\"any\">\n");
				writer.write(
						"                      <netex:isAvailable>" + child.getIncluded() + "</netex:isAvailable\n");
				writer.write("                   </netex:DayTypeAssignment>\n");
				rank++;
			}
		}

	}

	private static void writeOperatingPeriods(Writer writer, ExportableData data) throws IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddT00:00:00");
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
