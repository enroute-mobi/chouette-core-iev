package mobi.chouette.exchange.netex_stif.exporter.writer;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;

import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.exporter.ExportableData;
import mobi.chouette.model.CalendarDay;
import mobi.chouette.model.Period;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.type.DayTypeEnum;

public class NetexCalendrierWriter extends AbstractWriter {


	public static void write(Writer writer, ExportableData data) throws IOException {
		String participantRef = OFFRE_PARTICIPANT_REF;
		String prefix = ROOT_PREFIX;
		String lineName = ""; // TODO 

		openPublicationDelivery(writer, participantRef, data.getGlobalValidityPeriod(), lineName, FILE_TYPE.CALENDRIERS);
		openGeneralFrame(writer, prefix, NetexStifConstant.NETEX_CALENDRIER, data.getValidityPeriods(), false, data.getTimetables().isEmpty());
		writeDayTypes(writer, data);
		writeDayTypeAssignments(writer, data);
		writeOperatingPeriods(writer, data);
		closeGeneralFrame(writer, false, data.getTimetables().isEmpty());
		closePublicationDelivery(writer);

	}

	private static void writeDayTypes(Writer writer, ExportableData data) throws IOException {
		for (Timetable object : data.getTimetables()) {
			write(writer,4,"<DayType id=\"" + object.getObjectId() + "\" version=\"any\">");
			write(writer,5,"<Name>" + toXml(object.getComment()) + "</Name>");
			if (!object.getDayTypes().isEmpty()) {
				write(writer,5,"<properties>");
				for (DayTypeEnum child : object.getDayTypes()) {
					write(writer,6,"<PropertyOfDay>");
					write(writer,7,"<DaysOfWeek>" + child.toString()
							+ "</DaysOfWeek>");
					write(writer,6,"</PropertyOfDay>");

				}
				write(writer,5,"</properties>");
			}
			write(writer,4,"</DayType>");
		}
	}

	//@SuppressWarnings("unused")
	private static void writeDayTypeAssignments(Writer writer, ExportableData data) throws IOException {
		int rank = 1;
		SimpleDateFormat format = new SimpleDateFormat(XSD_DATE_ONLY);
		for (Timetable object : data.getTimetables()) {
			String prefix = object.objectIdPrefix();
			for (int periodRank = 1; periodRank <= object.getPeriods().size(); periodRank++) {
				write(writer,4,"<DayTypeAssignment id=\"" + prefix + ":DayTypeAssignment:" + rank
						+ ":LOC\" version=\"any\" order=\"0\" >");
				write(writer,5,"<OperatingPeriodRef ref=\""
						+ buildChildSequenceId(object, "DayType", "OperatingPeriod", periodRank)
						+ "\" version=\"any\"/>");
				write(writer,5,"<DayTypeRef ref=\"" + object.getObjectId()
						+ "\" version=\"any\"/>");
				write(writer,4,"</DayTypeAssignment>");
				periodRank++;
				rank++;
			}
			for (CalendarDay child : object.getCalendarDays()) {
				write(writer,4,"<DayTypeAssignment id=\"" + prefix + ":DayTypeAssignment:" + rank
						+ ":LOC\" version=\"any\" order=\"0\" >");
				write(writer,5,"<Date>" + format.format(child.getDate()) + "</Date>");
				write(writer,5,"<DayTypeRef ref=\"" + object.getObjectId()
						+ "\" version=\"any\"/>");
				write(writer,5,"<isAvailable>" + child.getIncluded() + "</isAvailable>");
				write(writer,4,"</DayTypeAssignment>");
				rank++;
			}
		}

	}

	private static void writeOperatingPeriods(Writer writer, ExportableData data) throws IOException {
		SimpleDateFormat format = new SimpleDateFormat(XSD_DATE_00);
		for (Timetable object : data.getTimetables()) {
			int periodRank = 1;
			for (Period child : object.getPeriods()) {
				write(writer,4,"<OperatingPeriod id=\"" + buildChildSequenceId(object, "DayType", "OperatingPeriod", periodRank)+"\" version=\"any\" >");
				write(writer,5,"<FromDate>"+format.format(child.getStartDate())+"</FromDate>");
				write(writer,5,"<ToDate>"+format.format(child.getEndDate())+"</ToDate>");
				write(writer,4,"</OperatingPeriod>");
				periodRank++;
			}
		}
	}

}
