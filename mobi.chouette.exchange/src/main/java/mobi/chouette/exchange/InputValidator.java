package mobi.chouette.exchange;

import java.nio.file.Path;

import mobi.chouette.exchange.parameters.AbstractParameter;

public interface InputValidator {
	AbstractParameter toActionParameter(String abstractParameter);
	
	AbstractParameter toActionParameter(Object task);

	boolean checkParameters(String abstractParameter);

	boolean checkFilename(String fileName);
	
	boolean checkFile(String fileName, Path filePath, AbstractParameter abstractParameter);

	boolean checkParameters(AbstractParameter abstractParameter);
	
}
