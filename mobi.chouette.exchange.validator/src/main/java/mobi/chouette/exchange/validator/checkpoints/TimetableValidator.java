package mobi.chouette.exchange.validator.checkpoints;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.Timetable;

public class TimetableValidator extends GenericValidator<Timetable> {

	private static final String[] codes = { };

	@Override
	public void validate(Context context, Timetable object, ValidateParameters parameters,
			String transportMode) {
		super.validate(context, object, parameters, transportMode, codes);
	}

}
