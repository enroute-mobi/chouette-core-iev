package mobi.chouette.exchange.netex_stif.validator;

import java.text.SimpleDateFormat;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.model.OperatingPeriod;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;

public class OperatingPeriodValidator extends AbstractValidator {

	public static final String LOCAL_CONTEXT = NetexStifConstant.OPERATING_PERIOD;

	protected String getLocalContext() {
		return LOCAL_CONTEXT;
	}

	@Override
	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck
		validationReporter.prepareCheckPointReport(context, NetexCheckPoints.L2_NeTExSTIF_OperatingPeriod_1);
	}

	public boolean validate(Context context, OperatingPeriod period, int lineNumber, int columnNumber) {
		boolean result1 = checkNetexId(context, NetexStifConstant.OPERATING_PERIOD, period.getObjectId(), lineNumber, columnNumber);
		boolean result2 = check2NeTExSTIFOperatingPeriod1(context, period, lineNumber, columnNumber);
		return result1 && result2;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet OperatingPeriod : chronologie
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2306">Cartes
	 * #2306</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-OperatingPeriod-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : l'objet OperatingPeriod doit définir une période dont
	 * la date 'FromDate' doit être strictement inférieure à la date 'ToDate'
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * OperatingPeriod d'identifiant {objectId} a une date de fin {ToDate}
	 * inférieure ou égale à la date de début {FromDate}
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check2NeTExSTIFOperatingPeriod1(Context context, OperatingPeriod period, int lineNumber,
			int columnNumber) {
		boolean result = period.getPeriod().getEndDate().after(period.getPeriod().getStartDate());
		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, period);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			validationReporter.addCheckPointReportError(context, null, NetexCheckPoints.L2_NeTExSTIF_OperatingPeriod_1, location,sdf.format(period.getPeriod().getEndDate()),sdf.format(period.getPeriod().getStartDate()));
		}
		return result;
	}

}
