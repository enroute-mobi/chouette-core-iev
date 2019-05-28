package mobi.chouette.exchange.netex_stif.exporter.writer;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringEscapeUtils;

import mobi.chouette.exchange.netex_stif.exporter.ExportableData;
import mobi.chouette.model.ChouetteIdentifiedObject;
import mobi.chouette.model.ChouetteObject;
import mobi.chouette.model.DataSourceRefObject;
import mobi.chouette.model.type.DateRange;
import lombok.extern.log4j.Log4j;

@Log4j
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
	public static final String INDENT = "   ";
	public static final String VERSION_NETEX = "1.04:FR100-NETEX-1.6-1.8";
	public static final String VERSION_PUBLICATION = "1.8";
	public static final String OFFRE_PARTICIPANT_REF = "FR100_OFFRE";
	public static final String FRAME_REF_PREFIX = "FR100:TypeOfFrame:";
	public static final String FRAME_DATASOURCE = "FR100-OFFRE_AUTO";
	public static final String ROOT_PREFIX = "FR100";

	public enum FILE_TYPE {
		FULL, LINE, CALENDRIERS, COMMUN, CODIFLIGNE, REFLEX
	}

	public static void openPublicationDelivery(Writer writer, String participantRef, DateRange validityPeriod,
			String lineName, FILE_TYPE fileType) throws IOException {
		SimpleDateFormat utcDateFormat = new SimpleDateFormat(XSD_DATE_TIME_UTC);
		utcDateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
		SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");
		write(writer, 0, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		write(writer, 0, "<PublicationDelivery xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		write(writer, 0, "                     xsi:schemaLocation=\"http://www.netex.org.uk/netex\"");
		write(writer, 0, "                     xmlns=\"http://www.netex.org.uk/netex\"");
		write(writer, 0, "                     xmlns:xlink=\"http://www.w3.org/1999/xlink\"");
		write(writer, 0, "                     xmlns:ifopt=\"http://www.ifopt.org.uk/ifopt\"");
		write(writer, 0, "                     xmlns:gml=\"http://www.opengis.net/gml/3.2\"");
		write(writer, 0, "                     xmlns:core=\"http://www.govtalk.gov.uk/core\"");
		write(writer, 0, "                     xmlns:siri=\"http://www.siri.org.uk/siri\"");
		write(writer, 0, "                     version=\"" + VERSION_NETEX + "\">");
		write(writer, 1, "<PublicationTimestamp>" + utcDateFormat.format(new Date()) + "</PublicationTimestamp>");
		write(writer, 1, "<ParticipantRef>" + participantRef + "</ParticipantRef>");
		switch (fileType) {
		case FULL:
			write(writer, 1, "<PublicationRefreshInterval>P1D</PublicationRefreshInterval>");
			write(writer, 1,
					"<Description>Offre complète IDF sur la période du " + dayFormat.format(validityPeriod.getFirst())
					+ " au " + dayFormat.format(validityPeriod.getLast()) + "</Description>");
			break;
		case LINE:
			write(writer, 1,
					"<Description>Offre pour la ligne " + toXml(lineName) + " sur la période du "
							+ dayFormat.format(validityPeriod.getFirst()) + " au "
							+ dayFormat.format(validityPeriod.getLast()) + "</Description>");
			break;
		default:
		}
		write(writer, 1, "<dataObjects>");
	}

	public static void openGeneralFrame(Writer writer, String prefix, String frameType, List<DateRange> validityPeriods,
			boolean indent, boolean empty) throws IOException {
		SimpleDateFormat format = new SimpleDateFormat(XSD_DATE_00);
		SimpleDateFormat idDateFormat = new SimpleDateFormat(ID_DATE_TIME_UTC);
		idDateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
		int whiteSpaces = 2;
		if (indent)
			whiteSpaces += 2;

		write(writer, whiteSpaces,
				"<GeneralFrame id=\"" + prefix + ":GeneralFrame:" + frameType + "-" + idDateFormat.format(new Date())
				+ ":LOC\" version=\"" + VERSION_PUBLICATION + "\" dataSourceRef=\"" + FRAME_DATASOURCE + "\">");
		if (validityPeriods != null) {
			// NOTICE : don't use lambda because of exception management
			for (DateRange validityPeriod : validityPeriods) {
				write(writer, whiteSpaces + 1, "<ValidBetween>");
				write(writer, whiteSpaces + 2, "<FromDate>" + format.format(validityPeriod.getFirst()) + "</FromDate>");
				write(writer, whiteSpaces + 2, "<ToDate>" + format.format(validityPeriod.getLast()) + "</ToDate>");
				write(writer, whiteSpaces + 1, "</ValidBetween>");
			}
		}
		write(writer, whiteSpaces + 1, "<TypeOfFrameRef ref=\"" + FRAME_REF_PREFIX + frameType + ":\"/>");
		if (!empty) {
			write(writer, whiteSpaces + 1, "<members>");
		}

	}

	public static void closeGeneralFrame(Writer writer, boolean indent, boolean empty) throws IOException {
		int whiteSpaces = 2;
		if (indent)
			whiteSpaces += 2;
		if (!empty) {
			write(writer, whiteSpaces + 1, "</members>");
		}
		write(writer, whiteSpaces, "</GeneralFrame>");
	}

	public static void openCompositeFrame(Writer writer, String prefix, String frameType, String name, boolean empty,
			boolean delete) throws IOException {
		SimpleDateFormat idDateFormat = new SimpleDateFormat(ID_DATE_TIME_UTC);
		idDateFormat.setTimeZone(TimeZone.getTimeZone(UTC));

		write(writer, 2, "<CompositeFrame id=\"" + prefix + ":CompositeFrame:" + frameType + "-"
				+ idDateFormat.format(new Date()) + ":LOC\" version=\"" + VERSION_PUBLICATION + "\" dataSourceRef=\""
				+ FRAME_DATASOURCE + "\"" + (delete ? " modification=\"delete\"" : "") + ">");
		if (!name.isEmpty()) {
			write(writer, 3, "<Name>" + toXml(name) + "</Name>");
		}
		write(writer, 3, "<TypeOfFrameRef ref=\"" + FRAME_REF_PREFIX + frameType + ":\"/>");
		if (!empty) {
			write(writer, 3, "<frames>");
		}

	}

	public static void closeCompositeFrame(Writer writer, boolean empty) throws IOException {
		if (!empty) {
			write(writer, 3, "</frames>");
		}
		write(writer, 2, "</CompositeFrame>");
	}

	public static void closePublicationDelivery(Writer writer) throws IOException {
		write(writer, 1, "</dataObjects>");
		write(writer, 0, "</PublicationDelivery>");

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

	public static String buildDataSourceRef(ExportableData data, ChouetteObject object) {

		StringBuilder b = new StringBuilder("dataSourceRef=\"");
		if (object != null && object instanceof DataSourceRefObject) {
			DataSourceRefObject dsObject = (DataSourceRefObject) object;
			if (isSet(dsObject.getDataSourceRef())) {
				b.append(dsObject.getDataSourceRef());
				// collect for OrganisationalUnit export
				if (data != null)
					data.getDataSourceRefs().add(dsObject.getDataSourceRef());
			} else {
				b.append(FRAME_DATASOURCE);
			}
		} else {
			b.append(FRAME_DATASOURCE);
		}
		b.append('"');
		return b.toString();
	}

	public static String buildChildSequenceId(ChouetteIdentifiedObject object, String type, String childType, int rank) {
		return object.getObjectId().replace(COLUMN + type + COLUMN, COLUMN + childType + COLUMN).replace(LOC, rank + LOC);
	}

	public static void writeXml(Writer writer, String xml, int indent) throws IOException {
		if (xml!=null) {
			String[] source = xml.replaceAll("> <", ">\n<").split("\n");
			int rank = indent;
			for (String line : source) {
				line = line.trim();
				if (line.startsWith("</"))
					rank--;
				write(writer, rank, line);
				if (!line.contains("</") && !line.endsWith("/>") && !line.startsWith("<!"))
					rank++;
			}
		}
	}

	private static final String[] indentation = { "", INDENT, INDENT + INDENT, INDENT + INDENT + INDENT,
			INDENT + INDENT + INDENT + INDENT, INDENT + INDENT + INDENT + INDENT + INDENT,
			INDENT + INDENT + INDENT + INDENT + INDENT + INDENT,
			INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT,
			INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT,
			INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT,
			INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT + INDENT };

	public static void write(Writer writer, int indent, String text) throws IOException {
		if (indent >= indentation.length)
			indent = indentation.length - 1;
		writer.write(indentation[indent] + text + "\n");
	}

}
