package mobi.chouette.exchange.netex_stif.validator;

import java.util.Collection;

import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;

public class FrameValidator extends AbstractValidator implements Constant {

	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck ; may be set only on test call
		String fileName = (String) context.get(Constant.FILE_NAME);
		if (fileName.endsWith(COMMUN_FILE_NAME)) {
			// Frames de commun.xml
			validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_1);
		} else if (fileName.endsWith(CALENDRIER_FILE_NAME)) {
			// Frames de calendriers.xml
			validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_2);
		} else {
			// Frames de Offre_xxx.xml
			validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_3);
		}
	}

	public boolean checkMandatoryGeneralFrames(Context context, Collection<String> frameNames, int lineNumber,
			int columnNumber) {
		String fileName = (String) context.get(Constant.FILE_NAME);
		if (fileName.endsWith(COMMUN_FILE_NAME)) {
			return check2NeTExStif1_1(context, frameNames, lineNumber, columnNumber);
		} else if (fileName.endsWith(CALENDRIER_FILE_NAME)) {
			return check2NeTExStif2_1(context, frameNames, lineNumber, columnNumber);

		} else {
			return check2NeTExStif3_3(context, frameNames, lineNumber, columnNumber);
		}
	}

	public boolean checkMandatoryCompositeFrames(Context context, Collection<String> frameNames, int lineNumber,
			int columnNumber) {
		String fileName = (String) context.get(Constant.FILE_NAME);
		// no mandatory composite frames for commun and calendrier ; skip
		// checkpoint
		if (fileName.endsWith(COMMUN_FILE_NAME)) {
			return true;
		} else if (fileName.endsWith(CALENDRIER_FILE_NAME)) {
			return true;

		} else {
			return check2NeTExStif3_1(context, frameNames, lineNumber, columnNumber);
		}
	}

	public boolean checkForbiddenGeneralFrames(Context context, String frameName, int lineNumber, int columnNumber) {
		String fileName = (String) context.get(Constant.FILE_NAME);
		if (fileName.endsWith(COMMUN_FILE_NAME)) {
			return check2NeTExStif1_2(context, frameName, lineNumber, columnNumber);
		} else if (fileName.endsWith(CALENDRIER_FILE_NAME)) {
			return check2NeTExStif2_2(context, frameName, lineNumber, columnNumber);

		} else {
			return check2NeTExStif3_4(context, frameName, lineNumber, columnNumber);
		}
	}

	public boolean checkForbiddenCompositeFrames(Context context, String frameName, int lineNumber, int columnNumber) {
		String fileName = (String) context.get(Constant.FILE_NAME);
		// no composite frame for calendrier or commun : creare error using
		// CompositeFrame as name
		if (fileName.endsWith(COMMUN_FILE_NAME)) {
			return check2NeTExStif1_2(context, "CompositeFrame", lineNumber, columnNumber);
		} else if (fileName.endsWith(CALENDRIER_FILE_NAME)) {
			return check2NeTExStif2_2(context, "CompositeFrame", lineNumber, columnNumber);

		} else {
			return check2NeTExStif3_2(context, frameName, lineNumber, columnNumber);
		}
	}

	/**
	 * <b>Titre</b> :[Netex] Organisation des frames du fichier commun.xml
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2113">Cartes
	 * #2113</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExStif-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : présence du fichier commun.xml
	 * <p>
	 * <b>Prédicat</b> : le fichier commun doit respecter l'organisation en
	 * frame de la NT44
	 * <p>
	 * <b>Message</b> :
	 * <ol>
	 * <li>Le fichier commun.xml ne contient pas de frame nommée NETEX_COMMUN.
	 * </li>
	 * <li>Le fichier commun.xml contient une frame nommée {nomFrame} non
	 * acceptée</li>
	 * </ol>
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 * @param context
	 * @param frameNames
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	private boolean check2NeTExStif1_1(Context context, Collection<String> frameNames, int lineNumber,
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
			validationReporter.addCheckPointReportError(context, null, L2_NeTExSTIF_1, "1", location);
		}
		return result;
	}

	/**
	 * @param context
	 * @param frameName
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	private boolean check2NeTExStif1_2(Context context, String frameName, int lineNumber, int columnNumber) {
		boolean result = true;
		if (!frameName.equals(NETEX_COMMUN)) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, null, L2_NeTExSTIF_1, "2", location, frameName);
			result = false;
		}
		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Organisation des frames du fichier calendriers.xml
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2114">Cartes
	 * #2114</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExStif-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : le fichier de calendrier doit respecter l'organisation
	 * en frame de la NT44
	 * <p>
	 * <b>Message</b> :
	 * <ol>
	 * <li>Le fichier calendriers.xml ne contient pas de frame nommée
	 * NETEX_CALENDRIER.</li>
	 * <li>Le fichier calendriers.xml contient une frame nommée {nomFrame} non
	 * acceptée</li>
	 * </ol>
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 *
	 * @param context
	 * @param frameNames
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	private boolean check2NeTExStif2_1(Context context, Collection<String> frameNames, int lineNumber,
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
			validationReporter.addCheckPointReportError(context, null, L2_NeTExSTIF_2, "1", location);
		}
		return result;
	}

	/**
	 * @param context
	 * @param frameName
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	private boolean check2NeTExStif2_2(Context context, String frameName, int lineNumber, int columnNumber) {
		boolean result = true;
		if (!frameName.equals(NETEX_CALENDRIER)) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, null, L2_NeTExSTIF_2, "2", location, frameName);
			result = false;
		}
		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Organisation des frames des fichiers offre_xxx.xml
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2115">Cartes
	 * #2115</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExStif-3
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : les fichiers d'offre doivent respecter l'organisation
	 * en frame de la NT44
	 * <p>
	 * <b>Message</b> :
	 * <ol>
	 * <li>Le fichier {nomFichier} ne contient pas de frame nommée
	 * NETEX_OFFRE_LIGNE.</li>
	 * <li>Le fichier {nomFichier} contient une frame nommée {nomFrame} non
	 * acceptée.</li>
	 * <li>la frame NETEX_OFFRE_LIGNE du fichier {nomFichier} ne contient pas la
	 * frame {NETEX_STRUCTURE/NETEX_HORAIRE} obligatoire.</li>
	 * <li>la frame NETEX_OFFRE_LIGNE du fichier {nomFichier} contient une frame
	 * {nomFrame} non acceptée</li>
	 * </ol>
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 * @param context
	 * @param frameNames
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	private boolean check2NeTExStif3_1(Context context, Collection<String> frameNames, int lineNumber,
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
			validationReporter.addCheckPointReportError(context, null, L2_NeTExSTIF_3, "1", location);
		}
		return result;
	}

	/**
	 * @param context
	 * @param frameName
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	private boolean check2NeTExStif3_2(Context context, String frameName, int lineNumber, int columnNumber) {
		boolean result = true;
		if (!frameName.equals(NETEX_OFFRE_LIGNE)) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, null, L2_NeTExSTIF_3, "2", location, frameName);
			result = false;
		}

		return result;
	}

	/**
	 * @param context
	 * @param frameNames
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean check2NeTExStif3_3(Context context, Collection<String> frameNames, int lineNumber,
			int columnNumber) {
		boolean result = true;
		boolean structure = false;
		boolean horaire = false;
		Collection<String> compositeFrameNames = (Collection<String>) context.get(COMPOSITE_FRAMES);
		if (!compositeFrameNames.contains(NETEX_OFFRE_LIGNE)) return false;
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
			validationReporter.addCheckPointReportError(context, null, L2_NeTExSTIF_3, "3", location, NETEX_STRUCTURE);
			result = false;
		}
		if (!horaire) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, null, L2_NeTExSTIF_3, "3", location, NETEX_HORAIRE);
			result = false;
		}
		return result;

	}

	/**
	 * @param context
	 * @param frameName
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	private boolean check2NeTExStif3_4(Context context, String frameName, int lineNumber, int columnNumber) {
		boolean result = true;
		if (!frameName.equals(NETEX_STRUCTURE) && !frameName.equals(NETEX_HORAIRE)) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			validationReporter.addCheckPointReportError(context, null, L2_NeTExSTIF_3, "4", location, frameName);
			result = false;
		}

		return result;
	}

	@Override
	protected String getLocalContext() {
		return "FRAMES";
	}
}
