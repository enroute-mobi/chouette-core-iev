package mobi.chouette.exchange.netex_stif.exporter.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import mobi.chouette.exchange.netex_stif.exporter.ExportableData;
import mobi.chouette.model.Company;
import mobi.chouette.model.GroupOfLine;
import mobi.chouette.model.Line;
import mobi.chouette.model.Network;

public class NetexLignesWriter extends AbstractWriter {

	public static void write(Writer writer, ExportableData data) throws IOException {
		// TODO should be refactored with CODIFLIGNE V2
		String participantRef = OFFRE_PARTICIPANT_REF;
		String prefix = ROOT_PREFIX;

		openPublicationDelivery(writer, participantRef, null, "CODIFLIGNE",
				FILE_TYPE.CODIFLIGNE);
		openCompositeFrame(writer, prefix, "NETEX_IDF", "", false);
		writeNetworks(writer, data);
		openServiceFrame(writer, "STIF:CODIFLIGNE:ServiceFrame:lineid");
		writeLines(writer, data);
		writeGroupsOfLines(writer, data);
		closeServiceFrame(writer);
		openResourceFrame(writer);
		writeTypesOfValues(writer, data);
		writeOrganisations(writer, data);
		closeResourceFrame(writer);
		closeCompositeFrame(writer, false);
		closePublicationDelivery(writer);
	}

	private static void writeTypesOfValues(Writer writer, ExportableData data)  throws IOException {
		write(writer, 5, "<typesOfValue>");
		write(writer, 6, "<TypeOfLine version=\"any\" id=\"STIF:CODIFLIGNE:seasonal\"/>");
		write(writer, 5, "</typesOfValue>");

	}

	private static void openServiceFrame(Writer writer, String frameId) throws IOException {
		write(writer, 4, "<ServiceFrame version=\"any\" id=\"" + frameId + "\">");
	}

	private static void closeServiceFrame(Writer writer) throws IOException {
		write(writer, 4, "</ServiceFrame>");

	}

	private static void writeLines(Writer writer, ExportableData data) throws IOException {
		write(writer, 5, "<lines>");
		for (Line line : data.getLines()) {
			writeXml(writer, line.getImportXml(), 6);
		}
		write(writer, 5, "</lines>");
	}

	private static void writeGroupsOfLines(Writer writer, ExportableData data) throws IOException {
		if (data.getGroupOfLines().isEmpty())
			return;
		write(writer, 5, "<groupsOfLines>");
		for (GroupOfLine group : data.getGroupOfLines()) {
			writeXml(writer, group.getImportXml(), 6);
		}
		write(writer, 5, "</groupsOfLines>");
	}

	private static void openResourceFrame(Writer writer) throws IOException {
		write(writer, 4, "<ResourceFrame version=\"any\" id=\"STIF:CODIFLIGNE:ResourceFrame:1\">");

	}

	private static void writeOrganisations(Writer writer, ExportableData data) throws IOException {
		write(writer, 5, "<organisations>");
		for (Company company : data.getCompanies()) {
			writeXml(writer, company.getImportXml(), 6);
		}
		write(writer, 5, "</organisations>");
	}

	private static void closeResourceFrame(Writer writer) throws IOException {
		write(writer, 4, "</ResourceFrame>");
	}

	private static void writeNetworks(Writer writer, ExportableData data) throws IOException {
		Set<Network> networks = data.getNetworks();

		for (Network network : networks) {
			String frameId = network.getObjectId().replace(":PTNetwork:", ":ServiceFrame:");
			openServiceFrame(writer, frameId);
			writeXml(writer, network.getImportXml(), 5);
			closeServiceFrame(writer);
		}

	}

}
