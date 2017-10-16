package mobi.chouette.exchange.netex_stif.exporter.writer;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;

import mobi.chouette.exchange.netex_stif.exporter.ExportableData;

public class DeliveryWriter extends AbstractWriter {

	public static void write(Writer writer, ExportableData data) throws IOException, DatatypeConfigurationException {
		// TODO version Netex
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Calendar now = Calendar.getInstance();
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<netex:PublicationDelivery xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		writer.write(
				"    xsi:schemaLocation=\"http://www.netex.org.uk/netex ../../xsd/NeTEx_publication.xsd\" xmlns:netex=\"http://www.netex.org.uk/netex\"\n");
		writer.write(
				"    xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:ifopt=\"http://www.ifopt.org.uk/ifopt\"\n");
		writer.write(
				"    xmlns:gml=\"http://www.opengis.net/gml/3.2\" xmlns:core=\"http://www.govtalk.gov.uk/core\"\n");
		writer.write("    xmlns:siri=\"http://www.siri.org.uk/siri\" version=\"1.04:FR100-NETEX-1.7-1.8\">\n");
		writer.write("    <netex:PublicationTimestamp>" + dateFormat.format(now.getTime())
				+ "</netex:PublicationTimestamp>\n");
		// TODO ParticipantRef
		writer.write("    <netex:ParticipantRef>FR100-OFFRE</netex:ParticipantRef>\n");
		// TODO if (offre globale)
		{
			writer.write("    <netex:PublicationRefreshInterval>P1D</netex:PublicationRefreshInterval>\n");
			writer.write("    <netex:Description>Offre complète IDF sur la période du dddd au ffff</netex:Description>\n");

		}
		// else
//		{
//			writer.write("    <netex:Description>Offre pour la ligne xxx sur la période du dddd au ffff</netex:Description>\n");
//		}
		writer.write("    <netex:dataObjects>\n");
		if (data.getLineLite() != null) {
			// produce offre file
			NetexOffreLigneWriter.write(writer, data);
		} else if (!data.getNotices().isEmpty()) {
			// produce commun.xml
			NetexCommunWriter.write(writer, data);
		} else if (!data.getTimetables().isEmpty()) {
			// produce calendrier.xml
			NetexCalendrierWriter.write(writer, data);
		} else if (!data.getLines().isEmpty()) {
			// produce ilico data
			NetexIlicoWriter.write(writer, data);
		} else if (!data.getStopAreas().isEmpty()) {
			// produce icar data
			NetexIcarWriter.write(writer, data);
		}
		
			
		writer.write("    </netex:dataObjects>\n");
		writer.write("</netex:PublicationDelivery>\n");

	}

}
