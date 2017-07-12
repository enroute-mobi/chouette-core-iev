package mobi.chouette.exchange.netex_stif.validator;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.model.Direction;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;

public class DirectionValidator extends AbstractValidator {

	public static final String LOCAL_CONTEXT = DIRECTION;

	protected String getLocalContext() {
		return LOCAL_CONTEXT;
	}

	@Override
	public void init(Context context) {
		super.init(context);
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();

		// -- preset checkpoints to OK if uncheck
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_Direction_1);
		validationReporter.prepareCheckPointReport(context, L2_NeTExSTIF_Direction_2);
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet Direction : Name
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2311">Cartes
	 * #2311</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-Direction-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'objet Direction doit avoir son attribut Name
	 * renseigné.
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * Direction d'identifiant {objectId} n'a pas de valeur pour l'attribut Name
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check2NeTExSTIFDirection1(Context context, Direction direction, int lineNumber, int columnNumber) {
		boolean result = true;

		if (direction.getName() == null || direction.getName().trim().isEmpty()) {
			result = false;
		}

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, direction);
			validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_Direction_1, location);
		}

		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Contrôle de l'objet Direction : Attributs interdits
	 * <p>
	 * <b>R&eacute;ference Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2312">Cartes
	 * #2312</a>
	 * <p>
	 * <b>Code</b> : 2-NeTExSTIF-Direction-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'objet Direction doit avoir ses attributs
	 * DirectionType et OppositeDirectionRef non renseignés.
	 * <p>
	 * <b>Message</b> : {fichier}-Ligne {ligne}-Colonne {Colonne} : l'objet
	 * Direction d'identifiant {objectId} définit un attribut {attribut
	 * interdit} non autorisé
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check2NeTExSTIFDirection2(Context context, Direction direction, int lineNumber, int columnNumber) {
		boolean result = true;
		boolean forbiddenDirectionType = false;
		boolean forbiddenOppositeDirectionRef = false;

		if (direction.getOppositeDirectionRef() != null) {
			forbiddenOppositeDirectionRef = true;
			result = false;
		} else if (direction.getDirectionType() != null) {
			forbiddenDirectionType = true;
			result = false;
		}

		if (!result) {
			ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
			String fileName = (String) context.get(Constant.FILE_NAME);
			DataLocation location = new DataLocation(fileName, lineNumber, columnNumber, direction);
			if (forbiddenDirectionType)
				validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_Direction_2, location,
						DIRECTION_TYPE);
			if (forbiddenOppositeDirectionRef)
				validationReporter.addCheckPointReportError(context, L2_NeTExSTIF_Direction_2, location,
						OPPOSITE_DIRECTION_REF);
		}

		return result;
	}

	/**
	 * @param context
	 * @param direction
	 * @param lineNumber
	 * @param columnNumber
	 * @return
	 */
	public boolean validate(Context context, Direction direction, int lineNumber, int columnNumber) {
		boolean result1 = check2NeTExSTIFDirection1(context, direction, lineNumber, columnNumber);
		boolean result2 = check2NeTExSTIFDirection2(context, direction, lineNumber, columnNumber);

		return result1 && result2;

	}

}
