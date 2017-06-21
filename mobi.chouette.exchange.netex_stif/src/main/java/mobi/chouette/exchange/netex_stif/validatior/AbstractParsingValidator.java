package mobi.chouette.exchange.netex_stif.validatior;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.parser.NetexCheckPoints;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;

public abstract class AbstractParsingValidator implements NetexCheckPoints {

	public AbstractParsingValidator() {

	}

	public AbstractParsingValidator(Context context) {
		init(context);
	}

	public void init(Context context) {
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// --criticité ERROR
		validationReporter.addItemToValidationReport(context, L2_NeTExSTIF_4, "E");
	}

	/*
	 * https://projects.af83.io/issues/2293 Description
	 * 
	 * 
	 * Code : 2-NeTExSTIF-4
	 * 
	 * Variables : néant
	 * 
	 * Prérequis : néant
	 * 
	 * Prédicat : L'identifiant d'un objet NeTEx doit respecter la syntaxe
	 * définie et le type d'objet doit correspondre à la balise NeTEx de l'objet
	 * 
	 * Message : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'identifiant
	 * {objectId} de l'objet {typeNeTEx} ne respecte pas la syntaxe
	 * [CODESPACE]:{typeNeTEx}:[identifiant Technique]:LOC
	 * 
	 * Criticité : error
	 */
	public boolean checkNetexId(Context context, String type, String id, int lineNumber, int columnNumber) {
		boolean result = true;

		String[] token = id.split(":");
		if (token.length != 4 || !token[1].equals(type) || !token[3].equals("LOC")) {
			result = false;
		}
		if (!result) {

			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_4, location, id);
		}
		return result;
	}
}
