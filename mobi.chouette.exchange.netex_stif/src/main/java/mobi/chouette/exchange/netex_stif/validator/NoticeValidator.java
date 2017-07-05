package mobi.chouette.exchange.netex_stif.validator;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.model.Footnote;

public class NoticeValidator extends AbstractValidator {

	public static final String LOCAL_CONTEXT = NOTICE;

	protected String getLocalContext() {
		return LOCAL_CONTEXT;
	}

	@Override
	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_Notice_1);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_Notice_2);
	}

	public void addTypeOfNoticeRef(Context context, String objectId, String typeOfNoticeRef) {
		Context objectContext = getObjectContext(context, LOCAL_CONTEXT, objectId);
		objectContext.put(TYPE_OF_NOTICE_REF, typeOfNoticeRef);

	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet Notice : présence de l'attribut Text
	 * <p>
	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2301">Cartes #2301</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-Notice-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'attribut Text de l'objet Notice doit être renseigné
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet Notice d'identifiant {objectId} doit définir
	 * un texte
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check2NeTExSTIFNotice1(Context context, Footnote footnote, int lineNumber, int columnNumber) {
		boolean result = footnote.getLabel() != null && !footnote.getLabel().isEmpty();

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			location.setObjectId(footnote.getObjectId());
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_Notice_1, location);
		}
		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet Notice : TypeOfNoticeRef
	 * <p>
	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2302">Cartes #2302</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-Notice-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Seules les Notices de type ServiceJourneyNotice sont importées
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet Notice d'identifiant {objectId} de type
	 * {TypeOfNoticeRef} est ignoré
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check2NeTExSTIFNotice2(Context context, Footnote footnote, int lineNumber, int columnNumber) {
		// TODO : [STIF] @Didier Implementation Controle 2-NeTExSTIF-Notice-2 : [Netex] Contrôle de l'objet Notice :
		// TypeOfNoticeRef
		boolean result = true;

		Context footnoteContext = getObjectContext(context, LOCAL_CONTEXT, footnote.getObjectId());
		// récupération de la valeur d'attribut sauvegardée
		String type = (String) footnoteContext.get(TYPE_OF_NOTICE_REF);

		if (type == null || !type.equals(SERVICE_JOURNEY_NOTICE)) {
			result = false;
		}

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber);
			location.setObjectId(footnote.getObjectId());
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_Notice_2, location, ""+type);
		}

		return result;
	}

}
