package mobi.chouette.exchange.netex_stif.validator;

import java.util.Collection;

import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;

public class FrameValidator extends AbstractValidator implements Constant{

	private static final String CALENDRIER_FILE_NAME = "calendrier.xml";
	private static final String COMMUN_FILE_NAME = "commun.xml";

	public FrameValidator(Context context) {
		init(context);

	}

	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck ; may be set only on test call
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_1); // Frames
																				// de
																				// commun.xml
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_2); // Frames
																				// de
																				// calendrier.xml
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_3); // Frames
																				// de
																				// Offre_xxx.xml
	}

	public boolean checkMandatoryGeneralFrames(Context context, Collection<String> frameNames, int lineNumber,
			int columnNumber) {
		String fileName = (String) context.get(Constant.FILE_NAME);
		if (fileName.endsWith(COMMUN_FILE_NAME)) {
			return check2NeTExSTIF1_1(context, frameNames, lineNumber, columnNumber);
		} else if (fileName.endsWith(CALENDRIER_FILE_NAME)) {
			return check2NeTExSTIF2_1(context, frameNames, lineNumber, columnNumber);

		} else {
			return check2NeTExSTIF3_3(context, frameNames, lineNumber, columnNumber);
		}
	}

	public boolean checkMandatoryCompositeFrames(Context context, Collection<String> frameNames, int lineNumber,
			int columnNumber) {
		String fileName = (String) context.get(Constant.FILE_NAME);
		// no mandatory composite frames for commun and calendrier ; skip checkpoint
		if (fileName.endsWith(COMMUN_FILE_NAME)) {
			return true;
		} else if (fileName.endsWith(CALENDRIER_FILE_NAME)) {
			return true;

		} else {
			return check2NeTExSTIF3_1(context, frameNames, lineNumber, columnNumber);
		}
	}

	public boolean checkForbiddenGeneralFrames(Context context, String frameName, int lineNumber, int columnNumber) {
		String fileName = (String) context.get(Constant.FILE_NAME);
		if (fileName.endsWith(COMMUN_FILE_NAME)) {
			return check2NeTExSTIF1_2(context, frameName, lineNumber, columnNumber);
		} else if (fileName.endsWith(CALENDRIER_FILE_NAME)) {
			return check2NeTExSTIF2_2(context, frameName, lineNumber, columnNumber);

		} else {
			return check2NeTExSTIF3_4(context, frameName, lineNumber, columnNumber);
		}
	}

	public boolean checkForbiddenCompositeFrames(Context context, String frameName, int lineNumber, int columnNumber) {
		String fileName = (String) context.get(Constant.FILE_NAME);
		// no composite frame for calendrier or commun : creare error using CompositeFrame as name
		if (fileName.endsWith(COMMUN_FILE_NAME)) {
			return check2NeTExSTIF1_2(context, "CompositeFrame", lineNumber, columnNumber);
		} else if (fileName.endsWith(CALENDRIER_FILE_NAME)) {
			return check2NeTExSTIF2_2(context, "CompositeFrame", lineNumber, columnNumber);

		} else {
		return check2NeTExSTIF3_2(context, frameName, lineNumber, columnNumber);
		}
	}

	/*
	 * https://projects.af83.io/issues/2113
	 * 
	 * Code : 2-NeTExSTIF-1
	 * 
	 * Variables : néant
	 * 
	 * Prérequis : présence du fichier commun.xml
	 * 
	 * Prédicat : le fichier commun doit respecter l'organisation en frame de la
	 * NT44
	 * 
	 * Message : 1- Le fichier commun.xml ne contient pas de frame nommée
	 * NETEX_COMMUN 2- Le fichier commun.xml contient une frame nommée
	 * {nomFrame} non acceptée
	 * 
	 * Criticité : error
	 * 
	 */

	private boolean check2NeTExSTIF1_1(Context context, Collection<String> frameNames, int lineNumber,
			int columnNumber) {
		boolean result = false;
		for (String frameName : frameNames) {
			if (frameName.equals(NETEX_COMMUN)) {
				result = true;
			}
		}
		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_1, "1", location);
		}
		return result;
	}

	private boolean check2NeTExSTIF1_2(Context context, String frameName, int lineNumber, int columnNumber) {
		boolean result = true;
		if (!frameName.equals(NETEX_COMMUN)) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_1, "2", location, frameName);
			result = false;
		}
		return result;
	}

	/*
	 * https://projects.af83.io/issues/2114
	 * 
	 * Code : 2-NeTExSTIF-2
	 * 
	 * Variables : néant
	 * 
	 * Prérequis : néant
	 * 
	 * Prédicat : le fichier de calendrier doit respecter l'organisation en
	 * frame de la NT44
	 * 
	 * Message : 1- Le fichier calendriers.xml ne contient pas de frame nommée
	 * NETEX_CALENDRIER 2- Le fichier calendriers.xml contient une frame nommée
	 * {nomFrame} non acceptée
	 * 
	 * Criticité : error
	 * 
	 */
	private boolean check2NeTExSTIF2_1(Context context, Collection<String> frameNames, int lineNumber,
			int columnNumber) {
		boolean result = false;
		for (String frameName : frameNames) {
			if (frameName.equals(NETEX_CALENDRIER)) {
				result = true;
			}
		}
		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_2, "1", location);
		}
		return result;
	}

	private boolean check2NeTExSTIF2_2(Context context, String frameName, int lineNumber, int columnNumber) {
		boolean result = true;
		if (!frameName.equals(NETEX_CALENDRIER)) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_2, "2", location, frameName);
			result = false;
		}
		return result;
	}

	/*
	 * https://projects.af83.io/issues/2115
	 *
	 * Code : 2-NeTExSTIF-3
	 * 
	 * Variables : néant
	 * 
	 * Prérequis : néant
	 * 
	 * Prédicat : les fichiers d'offre doivent respecter l'organisation en frame
	 * de la NT44
	 * 
	 * Message :
	 * 
	 * 1- Le fichier {nomFichier} ne contient pas de frame nommée
	 * NETEX_OFFRE_LIGNE 2- Le fichier {nomFichier} contient une frame nommée
	 * {nomFrame} non acceptée 3- la frame NETEX_OFFRE_LIGNE du fichier
	 * {nomFichier} ne contient pas la frame {NETEX_STRUCTURE|NETEX_HORAIRE}
	 * obligatoire 4- la frame NETEX_OFFRE_LIGNE du fichier {nomFichier}
	 * contient une frame {nomFrame} non acceptée
	 * 
	 * Criticité : error
	 * 
	 */
	private boolean check2NeTExSTIF3_1(Context context, Collection<String> frameNames, int lineNumber,
			int columnNumber) {
		boolean result = false;
		for (String frameName : frameNames) {
			if (frameName.equals(NETEX_OFFRE_LIGNE)) {
				result = true;
			}
		}
		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_3, "1", location);
		}
		return result;
	}

	private boolean check2NeTExSTIF3_2(Context context, String frameName, int lineNumber, int columnNumber) {
		boolean result = true;
		if (!frameName.equals(NETEX_OFFRE_LIGNE)) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_3, "2", location, frameName);
			result = false;
		}

		return result;
	}

	private boolean check2NeTExSTIF3_3(Context context, Collection<String> frameNames, int lineNumber,
			int columnNumber) {
		boolean result = true;
		boolean structure = false;
		boolean horaire = false;
		for (String frameName : frameNames) {
			if (frameName.equals(NETEX_STRUCTURE)) {
				structure = true;
			}
			if (frameName.equals(NETEX_HORAIRE)) {
				horaire = true;
			}
		}
		if (!structure) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_3, "3", location, NETEX_STRUCTURE);
			result = false;
		}
		if (!horaire) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_3, "3", location, NETEX_HORAIRE);
			result = false;
		}
		return result;

	}

	private boolean check2NeTExSTIF3_4(Context context, String frameName, int lineNumber, int columnNumber) {
		boolean result = true;
		if (!frameName.equals(NETEX_STRUCTURE) && !frameName.equals(NETEX_HORAIRE)) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_3, "4", location, frameName);
			result = false;
		}

		return result;
	}
}
