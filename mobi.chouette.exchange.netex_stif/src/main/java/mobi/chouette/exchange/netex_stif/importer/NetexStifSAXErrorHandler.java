package mobi.chouette.exchange.netex_stif.importer;

import java.io.File;
import java.net.URL;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import lombok.Getter;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.validator.NetexCheckPoints;
import mobi.chouette.exchange.validation.report.CheckPointReport;
import mobi.chouette.exchange.validation.report.CheckPointReport.SEVERITY;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;

/*
 * Manage XML and XSD errors as a Netex Checkpoint : 
 * 
 * https://projects.af83.io/issues/2111
 *
 * Code : 1-NeTExStif-2
 *
 * Variables : néant
 *
 * Prérequis : néant
 *
 * Prédicat : les fichiers xml doivent respecter la syntaxe XML du W3C et la XSD NeTEx
 *
 * Message :
 *    Le fichier {nomFichier} ne respecte pas la syntaxe XML ou la XSD NeTEx : message {message XERCES} rencontré
 *
 * Criticité : error
 * 
 * 
 */

public class NetexStifSAXErrorHandler implements ErrorHandler {

	private ValidationReporter validationReporter;
	private Context context;
	private String fileName;

	@Getter
	private boolean hasErrors = false;

	public NetexStifSAXErrorHandler(Context context, String fileURL) throws Exception {
		this.context = context;
		validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.addItemToValidationReport(context, NetexCheckPoints.L1_NetexStif_2, "E");
		validationReporter.updateCheckPointReportState(context, NetexCheckPoints.L1_NetexStif_2, ValidationReporter.RESULT.OK);

		fileName = new File(new URL(fileURL).toURI()).getName();
	}

	private void handleError(SAXParseException error, SEVERITY severity) {
		if (severity.equals(CheckPointReport.SEVERITY.ERROR)) {
			hasErrors = true;

			DataLocation location = new DataLocation(fileName, error.getLineNumber(), error.getColumnNumber());

			validationReporter.updateCheckPointReportSeverity(context, NetexCheckPoints.L1_NetexStif_2, severity);
			validationReporter.addCheckPointReportError(context, null, NetexCheckPoints.L1_NetexStif_2, location, error.getMessage());
		}

		return;
	}

	@Override
	public void warning(SAXParseException e) throws SAXException {

		handleError(e, SEVERITY.WARNING);

	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		handleError(e, SEVERITY.ERROR);
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		handleError(e, SEVERITY.ERROR);
		throw e;
	}

}
