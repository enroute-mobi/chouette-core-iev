package mobi.chouette.exchange.netex_stif.exporter.writer;

import java.io.IOException;
import java.io.Writer;

import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.exporter.ExportableData;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.Organisation;

public class NetexCommunWriter extends AbstractWriter {

	public static void write(Writer writer, ExportableData data) throws IOException {
		String participantRef = OFFRE_PARTICIPANT_REF;
		String prefix = ROOT_PREFIX;
		String lineName = ""; // TODO

		openPublicationDelivery(writer, participantRef, data.getGlobalValidityPeriod(), lineName, FILE_TYPE.COMMUN);
		openGeneralFrame(writer, prefix, NetexStifConstant.NETEX_COMMUN, null, false, false);
		writeNotices(writer, data);
		writeOrganisationalUnits(writer, data);
		closeGeneralFrame(writer, false, false);
		closePublicationDelivery(writer);

	}

	private static void writeNotices(Writer writer, ExportableData data) throws IOException {
		// TODO check version
		for (Footnote object : data.getNotices()) {
			write(writer, 4, "<Notice " + buildDataSourceRef(data, object) + " id=\"" + object.getObjectId()
					+ "\" version=\"any\">");
			write(writer, 5, "<Text>" + toXml(object.getLabel()) + "</Text>");
			write(writer, 5, "<PublicCode>" + toXml(object.getCode()) + "</PublicCode>");
			write(writer, 5, "<TypeOfNoticeRef ref=\"ServiceJourneyNotice\" />");
			write(writer, 4, "</Notice>");

		}

	}

	private static void writeOrganisationalUnits(Writer writer, ExportableData data) throws IOException {
		for (String code : data.getDataSourceRefs()) {
			if (data.getOrganisations().containsKey(code)) {
				Organisation object = data.getOrganisations().get(code);
				write(writer, 4, "<OrganisationalUnit id=\"" + ROOT_PREFIX+":OrganisationalUnit:"+object.getCode()
						+ "\" version=\"any\">");
				write(writer, 5, "<Name>" + toXml(object.getName()) + "</Name>");
				write(writer, 5, "<TypeOfOrganisationPartRef ref=\""+ROOT_PREFIX+"_Organisation\" />");
				write(writer, 4, "</OrganisationalUnit>");

			}
		}

	}

}
