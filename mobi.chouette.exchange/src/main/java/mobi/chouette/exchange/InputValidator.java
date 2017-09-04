package mobi.chouette.exchange;

import java.nio.file.Path;

import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.exchange.validation.parameters.ValidationParameters;

public interface InputValidator {
	AbstractParameter toActionParameter(String abstractParameter);
	
	AbstractParameter toActionParameter(Object task);

	// ValidationParameters toValidation(String validationParameters);

	boolean checkParameters(String abstractParameter, String validationParameters);

	boolean checkParameters(AbstractParameter abstractParameter, ValidationParameters validationParameters);

	boolean checkFilename(String fileName);
	
	boolean checkFile(String fileName, Path filePath, AbstractParameter abstractParameter);
	
	// boolean initReport2(JobData data);
	
	// List<TestDescription> getTestList();
}
