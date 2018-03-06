package mobi.chouette.exchange.netex_stif.exporter.writer;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringEscapeUtils;

import mobi.chouette.model.ChouetteIdentifiedObject;
import mobi.chouette.model.type.DateRange;

public class AbstractWriter {

	public static final String EMPTY_STRING = "";
	public static final String COLUMN = ":";
	public static final String UTC = "UTC";
	public static final String LOC = ":LOC";
	public static final String ID_DATE_TIME_UTC = "yyyyMMdd'T'HHmmss'Z'";
	public static final String ID_DATE = "yyyyMMdd";
	public static final String XSD_DATE_TIME_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String XSD_DATE_00 = "yyyy-MM-dd'T'00:00:00";
	public static final String XSD_DATE_ONLY = "yyyy-MM-dd";
	public static final String INDENT = "    ";
	public static final String VERSION_NETEX = "1.04:FR100-NETEX-1.6-1.8";
	public static final String VERSION_PUBLICATION = "1.8";
	public static final String OFFRE_PARTICIPANT_REF = "FR100_OFFRE";
	public static final String FRAME_REF_PREFIX = "FR100:TypeOfFrame:";
	public static final String FRAME_DATASOURCE = "FR100-OFFRE_AUTO";
	
	public enum FILE_TYPE {FULL,LINE,CALENDRIERS,COMMUN,CODIFLIGNE,REFLEX} 

	public static void openPublicationDelivery(Writer writer, String participantRef, DateRange validityPeriod,
			String lineName, FILE_TYPE fileType) throws IOException {
		SimpleDateFormat utcDateFormat = new SimpleDateFormat(XSD_DATE_TIME_UTC);
		utcDateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
		SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<netex:PublicationDelivery xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		writer.write("                           xsi:schemaLocation=\"http://www.netex.org.uk/netex\"\n");
		writer.write("                           xmlns:netex=\"http://www.netex.org.uk/netex\"\n");
		writer.write("                           xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n");
		writer.write("                           xmlns:ifopt=\"http://www.ifopt.org.uk/ifopt\"\n");
		writer.write("                           xmlns:gml=\"http://www.opengis.net/gml/3.2\"\n");
		writer.write("                           xmlns:core=\"http://www.govtalk.gov.uk/core\"\n");
		writer.write("                           xmlns:siri=\"http://www.siri.org.uk/siri\"\n");
		writer.write("                           version=\"" + VERSION_NETEX + "\">\n");
		writer.write("    <netex:PublicationTimestamp>" + utcDateFormat.format(new Date())
				+ "</netex:PublicationTimestamp>\n");
		writer.write("    <netex:ParticipantRef>" + participantRef + "</netex:ParticipantRef>\n");
		String startDate = dayFormat.format(validityPeriod.getFirst());
		String endDate = dayFormat.format(validityPeriod.getLast());
		switch (fileType)
		{
		case FULL :
			writer.write("    <netex:PublicationRefreshInterval>P1D</netex:PublicationRefreshInterval>\n");
			writer.write("    <netex:Description>Offre complète IDF sur la période du " + startDate + " au " + endDate
					+ "</netex:Description>\n");
			break;
		case LINE : 
			writer.write("    <netex:Description>Offre pour la ligne " + toXml(lineName) + " sur la période du "
					+ startDate + " au " + endDate + "</netex:Description>\n");
			break;
		default :
		}
		writer.write("    <netex:dataObjects>\n");
	}

	public static void openGeneralFrame(Writer writer, String prefix, String frameType, List<DateRange> validityPeriods,
			boolean indent, boolean empty) throws IOException {
		SimpleDateFormat format = new SimpleDateFormat(XSD_DATE_00);
		SimpleDateFormat idDateFormat = new SimpleDateFormat(ID_DATE_TIME_UTC);
		idDateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
		String whiteSpaces = INDENT + INDENT;
		if (indent)
			whiteSpaces += INDENT;

		writer.write(whiteSpaces + "<netex:GeneralFrame id=\"" + prefix + ":GeneralFrame:" + frameType + "-"
				+ idDateFormat.format(new Date()) + ":LOC\" version=\"" + VERSION_PUBLICATION + "\" dataSourceRef=\""
				+ FRAME_DATASOURCE + (empty? " modification=\"delete\" " : "") +"\">\n");
		if (validityPeriods != null) {
			// NOTICE : don't use lambda because of exception management
			for (DateRange validityPeriod : validityPeriods) {
				writer.write(whiteSpaces + "    <netex:ValidBetween>\n");
				writer.write(whiteSpaces + "        <netex:FromDate>" + format.format(validityPeriod.getFirst())
						+ "</netex:FromDate>\n");
				writer.write(whiteSpaces + "        <netex:ToDate>" + format.format(validityPeriod.getLast())
						+ "</netex:ToDate>\n");
				writer.write(whiteSpaces + "    </netex:ValidBetween>\n");
			}
		}
		writer.write(whiteSpaces + "    <netex:TypeOfFrameRef ref=\"" + FRAME_REF_PREFIX + frameType + ":\"/>\n");
		if (!empty) {
			writer.write(whiteSpaces + "    <netex:members>\n");
		}

	}

	public static void closeGeneralFrame(Writer writer, boolean indent, boolean empty) throws IOException {
		String whiteSpaces = INDENT + INDENT;
		if (indent)
			whiteSpaces += INDENT;
		if (!empty) {
		writer.write(whiteSpaces + "    </netex:members>\n");
		}
		writer.write(whiteSpaces + "</netex:GeneralFrame>\n");
	}

	public static void openCompositeFrame(Writer writer, String prefix, String frameType, String name, boolean empty)
			throws IOException {
		SimpleDateFormat idDateFormat = new SimpleDateFormat(ID_DATE_TIME_UTC);
		idDateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
		String whiteSpaces = INDENT + INDENT;

		writer.write(whiteSpaces + "<netex:CompositeFrame id=\"" + prefix + ":CompositeFrame:" + frameType + "-"
				+ idDateFormat.format(new Date()) + ":LOC\" version=\"" + VERSION_PUBLICATION + "\" dataSourceRef=\""
				+ FRAME_DATASOURCE + "\">\n");
		writer.write(whiteSpaces + "    <netex:Name>" + toXml(name) + "</netex:Name>\n");
		writer.write(whiteSpaces + "    <netex:TypeOfFrameRef ref=\"" + FRAME_REF_PREFIX + frameType + ":\"/>\n");
		if (!empty) {
			writer.write(whiteSpaces + "    <netex:frames>\n");
		}

	}

	public static void closeCompositeFrame(Writer writer, boolean empty) throws IOException {
		String whiteSpaces = INDENT + INDENT;
		if (!empty) {
			writer.write(whiteSpaces + "    </netex:frames>\n");
		}
		writer.write(whiteSpaces + "</netex:CompositeFrame>\n");
	}

	public static void closePublicationDelivery(Writer writer) throws IOException {
		writer.write("    </netex:dataObjects>\n");
		writer.write("</netex:PublicationDelivery>\n");

	}

	public static boolean isSet(Object... objects) {
		for (Object val : objects) {
			if (val != null) {
				if (val instanceof String) {
					if (!((String) val).isEmpty())
						return true;
				} else {
					return true;
				}
			}
		}
		return false;

	}

	public static String toXml(Object source) {
		if (source == null)
			return EMPTY_STRING;
		return StringEscapeUtils.escapeXml(source.toString());
	}

	public static boolean nonEmpty(Collection<?> list) {
		return list != null && !list.isEmpty();
	}

	protected static String buildChildSequenceId(ChouetteIdentifiedObject object, String type, String childType,
			int rank) {
		return object.getObjectId().replace(COLUMN + type + COLUMN, COLUMN + childType + COLUMN).replace(LOC,
				rank + LOC);
	}

}
