package mobi.chouette.exchange.netex_stif.exporter.writer;

import java.io.IOException;
import java.io.Writer;

import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.exporter.ExportableData;
import mobi.chouette.model.StopArea;

public class NetexArretsWriter extends AbstractWriter {

	public static void write(Writer writer, ExportableData data) throws IOException {
		// TODO should be refactored with REFLEX V2
		String participantRef = OFFRE_PARTICIPANT_REF;
		String prefix = ROOT_PREFIX;

		openPublicationDelivery(writer, participantRef, null, "REFLEX", FILE_TYPE.REFLEX);
		openCompositeFrame(writer, prefix, "NETEX_IDF", "", false);
		openGeneralFrame(writer, prefix, NetexStifConstant.NETEX_COMMUN, null, true, false);
		// Groupes de lieux
		if (!data.getGdls().isEmpty()) {
			writeGeneralGroupOfEntities(writer, data);
		}
		writeValueSets(writer);
		closeGeneralFrame(writer, true, false);
		openGeneralFrame(writer, prefix, NetexStifConstant.NETEX_ARRET_STIF, null, true, false);
		writeStops(writer, data);
		closeGeneralFrame(writer, true, false);
		closeCompositeFrame(writer, false);
		closePublicationDelivery(writer);
	}

	private static void writeValueSets(Writer writer) throws IOException {
		write(writer, 5, "<ValueSet version=\"any\" id=\"NETEX_ARRET_STIF:ValueSet:VS01\">");
		write(writer, 6, "<values>");
		write(writer, 7,
				"<PurposeOfGrouping version=\"any\" id=\"NETEX_ARRET_STIF:PurposeOfGrouping:groupOfStopPlace\">");
		write(writer, 8, "<Name>Group of Stop Place</Name>");
		write(writer, 8, "<classes>");
		write(writer, 9, "<ClassRef nameOfClass=\"StopPlace\"/>");
		write(writer, 8, "</classes>");
		write(writer, 7, "</PurposeOfGrouping>");
		write(writer, 6, "</values>");
		write(writer, 5, "</ValueSet>");
	}

	private static void writeGeneralGroupOfEntities(Writer writer, ExportableData data) throws IOException {
		for (StopArea stop : data.getGdls()) {
			writeXml(writer, stop.getImportXml(), 5);
		}
	}

	private static void writeStops(Writer writer, ExportableData data) throws IOException {
		for (StopArea stop : data.getLdas()) {
			writeXml(writer, stop.getImportXml(), 5);
		}
		for (StopArea stop : data.getZdlrs()) {
			writeXml(writer, stop.getImportXml(), 5);
		}
		for (StopArea stop : data.getZdlps()) {
			writeXml(writer, stop.getImportXml(), 5);
		}
		for (StopArea stop : data.getZders()) {
			writeXml(writer, stop.getImportXml(), 5);
		}
		for (StopArea stop : data.getZdeps()) {
			writeXml(writer, stop.getImportXml(), 5);
		}
	}

}
