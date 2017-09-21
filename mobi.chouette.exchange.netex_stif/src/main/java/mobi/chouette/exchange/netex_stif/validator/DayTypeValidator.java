package mobi.chouette.exchange.netex_stif.validator;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.util.Referential;

public class DayTypeValidator extends AbstractValidator {

	public static final String LOCAL_CONTEXT = DAY_TYPE;

	protected String getLocalContext() {
		return LOCAL_CONTEXT;
	}

	@Override
	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_DayType_1);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_DayType_2);
	}

	public boolean validateAll(Context context) {
		boolean result = true;
		Referential ref = (Referential) context.get(REFERENTIAL);
		for (Timetable timetable : ref.getSharedTimetableTemplates().values()) {
			DataLocation location = getLocation(context, timetable.getObjectId());
			checkChanged(context,  DAY_TYPE, timetable, location.getLineNumber(), location.getColumnNumber());
			result &= checkModification(context, DAY_TYPE, timetable, location.getLineNumber(), location.getColumnNumber());
			check2NeTExSTIFDayType1(context, timetable);
            result &= check2NeTExSTIFDayType2(context, timetable);
		}
		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet DayType : complétude
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2305">Cartes
	 * #2305</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-DayType-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'objet DayType doit être référencé dans au moins un
	 * objet DayTypeAssignment
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * DayType d'identifiant {objectId} ne définit aucun calendrier, il est
	 * ignoré
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check2NeTExSTIFDayType1(Context context, Timetable timetable) {
		boolean result = !timetable.getCalendarDays().isEmpty() || !timetable.getPeriods().isEmpty();
		if (!result) {
			DataLocation location = getLocation(context, timetable.getObjectId());
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_DayType_1, location);
		}
		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet DayType : types de jour sur
	 * période
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2307">Cartes
	 * #2307</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-DayType-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Si l'objet DayType est en relation avec au moins un
	 * objet OperatingPeriod, alors il doit définir au moins un PropertyOfDay de
	 * valeur Monday, Tuesday, Wednesday, Thursday, Friday, Saturday ou Sunday
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * DayType d'identifiant {objectId} est relié à des périodes mais ne
	 * définit pas de types de jours
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check2NeTExSTIFDayType2(Context context, Timetable timetable) {
		boolean result = timetable.getDayTypes().isEmpty() == timetable.getPeriods().isEmpty();
		if (!result) {
			DataLocation location = getLocation(context, timetable.getObjectId());
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_DayType_2, location);
		}
		return result;
	}

}
