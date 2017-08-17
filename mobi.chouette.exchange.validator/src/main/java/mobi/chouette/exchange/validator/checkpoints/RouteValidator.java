package mobi.chouette.exchange.validator.checkpoints;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.Route;

public class RouteValidator extends GenericValidator<Route> {

	private static final String[] codes = {"3-Route-1","3-Route-2","3-Route-3"};
	@Override
	public void validate(Context context, Route object, ValidateParameters parameters, String transportMode) {
		super.validate( context,  object,  parameters,transportMode,codes);

	}

	private void check3Route1(Context context, Route object, CheckpointParameters parameters)
	{
	     // TODO 	
	}
}
