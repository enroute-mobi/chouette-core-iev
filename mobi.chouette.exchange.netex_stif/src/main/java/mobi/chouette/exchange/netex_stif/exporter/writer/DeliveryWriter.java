package mobi.chouette.exchange.netex_stif.exporter.writer;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.datatype.DatatypeConfigurationException;

import mobi.chouette.exchange.netex_stif.exporter.ExportableData;
import mobi.chouette.model.Line;

public class DeliveryWriter extends AbstractWriter{
	
	public static void write(Writer writer, ExportableData data ) throws IOException, DatatypeConfigurationException 
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Line line = data.getLine();
		Calendar now = Calendar.getInstance();
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<netex:PublicationDelivery xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		writer.write("    xsi:schemaLocation=\"http://www.netex.org.uk/netex ../../xsd/NeTEx_publication.xsd\" xmlns:netex=\"http://www.netex.org.uk/netex\"\n");
		writer.write("    xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:ifopt=\"http://www.ifopt.org.uk/ifopt\"\n");
		writer.write("    xmlns:gml=\"http://www.opengis.net/gml/3.2\" xmlns:core=\"http://www.govtalk.gov.uk/core\"\n");
		writer.write("    xmlns:siri=\"http://www.siri.org.uk/siri\" version=\"1.04\">\n");
		writer.write("  <PublicationTimestamp>"+dateFormat.format(now.getTime())+"</PublicationTimestamp>\n");
		// TODO ParticipantRef
		writer.write("  <ParticipantRef>FR100</ParticipantRef>\n");
		// TODO ?? 
		writer.write("  <Description>Line export in Netex Format by IBOO systeme</Description>\n");
		writer.write("  <dataObjects>\n");
		writer.write("    <!-- =========================================== -->    \n");   
		if (data.getLineLite() != null)
		{
			// produce offre file
		}
		else if (data)
		writer.write("    <CompositeFrame version=\"1\" created=\""+dateFormat.format(line.getNetwork().getVersionDate())+"\" " +
				"id=\""+line.objectIdPrefix()+":Neptune:CompositeFrame:"+line.objectIdSuffix()+"\">\n");
		writer.write("      <Name>NEPTUNE Mapping Frame</Name>\n");
		writer.write("      <!-- NEPTUNE [mapping:fixed] : This is a NEPTUNE to NeTEx mapping frame -->\n");
		writer.write("      <TypeOfFrameRef version=\"01\" ref=\"Neptune:TypeOfFrame:CompositeFrame\"/>\n");
		writer.write("      <codespaces>\n");
		writer.write("        <Codespace id=\""+line.objectIdPrefix()+"\">\n");
		writer.write("          <Xmlns>"+line.objectIdPrefix()+"</Xmlns>\n");
		writer.write("        </Codespace>\n");
		writer.write("      </codespaces>\n");
		writer.write("      <FrameDefaults>\n");
		writer.write("        <DefaultCodespaceRef ref=\""+line.objectIdPrefix()+"\"/>\n");
		writer.write("        <!-- NEPTUNE [mapping:fixed] : NEPTUNE is in French ! -->\n");
		writer.write("        <DefaultLocale>\n");
		writer.write("          <TimeZoneOffset>-1</TimeZoneOffset>\n");
		writer.write("          <SummerTimeZoneOffset>-2</SummerTimeZoneOffset>\n");
		writer.write("          <DefaultLanguage>fr</DefaultLanguage>\n");
		writer.write("        </DefaultLocale>\n");
		writer.write("        <!-- NEPTUNE [mapping:fixed] : EPSG:4326 is WGS84 which is the only mandatory location system for NEPTUNE -->\n");
		writer.write("        <DefaultLocationSystem>EPSG:4326</DefaultLocationSystem>\n");
		writer.write("      </FrameDefaults>\n");
		writer.write("      <frames>\n");
//		        #parse( "templates/resource_frame.vm" )
		ResourceFrameWriter.write(writer, data);
//		        #parse( "templates/service_frame.vm" )
		ServiceFrameWriter.write(writer, data);
//		        #parse( "templates/site_frame.vm" )
		SiteFrameWriter.write(writer, data);
//		        #parse( "templates/service_calendar_frame.vm" )
		ServiceCalendarFrameWriter.write(writer, data);
//		        #parse( "templates/time_table_frame.vm" )
		TimeTableFrameWriter.write(writer, data);
		writer.write("      </frames>\n");
		writer.write("    </CompositeFrame>\n");
		writer.write("  </dataObjects>\n");
		writer.write("</PublicationDelivery>\n");

	}

}
