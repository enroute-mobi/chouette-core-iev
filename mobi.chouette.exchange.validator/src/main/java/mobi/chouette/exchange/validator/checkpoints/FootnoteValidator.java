package mobi.chouette.exchange.validator.checkpoints;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.Footnote;

public class FootnoteValidator extends GenericValidator<Footnote> {

	private static final String[] codes = { };

	@Override
	public void validate(Context context, Footnote object, ValidateParameters parameters,
			String transportMode) {
		super.validate(context, object, parameters, transportMode, codes);
	}

}
