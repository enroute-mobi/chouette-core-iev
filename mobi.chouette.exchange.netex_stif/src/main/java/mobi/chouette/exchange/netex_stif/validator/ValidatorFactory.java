package mobi.chouette.exchange.netex_stif.validator;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.Constant;

public class ValidatorFactory implements Constant{
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static AbstractValidator getValidator(Context context, Class<?  extends AbstractValidator> clazz)
	{
		Context validationContext = (Context) context.get(VALIDATION_CONTEXT);
		if (validationContext == null) {
			validationContext = new Context();
			context.put(VALIDATION_CONTEXT, validationContext);
		}
		Map<Class,AbstractValidator> validatorMap = (Map<Class, AbstractValidator>) validationContext.get(VALIDATORS);
        if (validatorMap == null)
        {
        	validatorMap = new HashMap<>();
        	validationContext.put(VALIDATORS, validatorMap);
        }
        AbstractValidator validator = validatorMap.get(clazz);
        if (validator == null)
        {
        	try {
				validator = (AbstractValidator) clazz.getConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new RuntimeException("unable to create validator",e);
			}
        	validatorMap.put(clazz, validator);
        }
        validator.init(context);
		return validator;

	}

}
