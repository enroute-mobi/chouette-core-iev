package mobi.chouette.exchange.netex_stif.exporter.writer;

import java.io.IOException;
import java.io.Writer;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.exporter.ExportableData;
import mobi.chouette.model.Footnote;

public class NetexCommunWriter extends AbstractWriter {

	public static void write(Context context, Writer writer, ExportableData data) throws IOException {
		String participantRef = OFFRE_PARTICIPANT_REF;
		String prefix = FRAME_REF_PREFIX;
		String lineName = ""; // TODO
		
		openPublicationDelivery(writer, participantRef, data.getGlobalValidityPeriod(), lineName);
		openGeneralFrame(writer, prefix, "NETEX_COMMUN", null, false);
		writeNotices(writer, data);
		writeOrganisationalUnits(writer, data);
		closeGeneralFrame(writer, false);
		closePublicationDelivery(writer);
		
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
		// TODO à ajouter selon NT60 V1.9
	}

}
