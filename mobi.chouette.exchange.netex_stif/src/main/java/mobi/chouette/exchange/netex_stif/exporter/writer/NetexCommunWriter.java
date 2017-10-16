package mobi.chouette.exchange.netex_stif.exporter.writer;

import java.io.IOException;
import java.io.Writer;

import mobi.chouette.exchange.netex_stif.exporter.ExportableData;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.Route;

public class NetexCommunWriter extends AbstractWriter {

	public static void write(Writer writer, ExportableData data) throws IOException {
		// TODO check frame id and version
		writer.write(
				"          <netex:GeneralFrame id=\"CITYWAY:GeneralFrame:NETEX_COMMUN-20170214090012:LOC\" version=\"any\">\n");
		writer.write("             <netex:TypeOfFrameRef ref=\"NETEX_COMMUN\"/>\n");
		writer.write("             <netex:members>\n");
		writeNotices(writer, data);
		writeOrganisationalUnits(writer, data);
		writer.write("             </netex:members>\n");
		writer.write("          </netex:GeneralFrame>\n");
		
	}

	private static void writeNotices(Writer writer, ExportableData data) throws IOException {
		// TODO check version
		for (Footnote object : data.getNotices()) {
			writer.write("                <netex:Notice id=\""
					+ object.getObjectId() + "\" version=\"any\">\n");
			writer.write("                   <netex:Text>" + toXml(object.getLabel()) + "</netex:Text>\n");
			writer.write("                   <netex:PublicCode>" + toXml(object.getKey()) + "</netex:PublicCode>\n");
			writer.write("                   <netex:TypeOfNoticeRef ref=\"ServiceJourneyNotice\" />\n");
			writer.write("                </netex:Notice>\n");
		
		}
		
	}

	private static void writeOrganisationalUnits(Writer writer, ExportableData data)  throws IOException {
		// TODO Auto-generated method stub
		
	}

}
