package mobi.chouette.exchange.validator.checkpoints;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.ChouetteIdentifiedObject;
import mobi.chouette.model.Route;

public abstract class GenericValidator<T extends ChouetteIdentifiedObject> {
	
	private static final String[] genericCodes = {"3-Generic-1","3-Generic-2","3-Generic-3"};

	public abstract void validate(Context context, T object, ValidateParameters parameters, String transportMode) ;

	public void validate(Context context, T object, ValidateParameters parameters, String transportMode,
			String[] codes) {
		
		// first check generic codes for class
		for (String code : genericCodes) {
			// find checkpoint for code
			// in transportModes
			// else in global
			
			
		}
		
		
		// then check specific codes 
		for (String code : codes) {
			// find checkpoint for code
			// in transportModes
			// else in global
		
			
		}
		
	}
}
