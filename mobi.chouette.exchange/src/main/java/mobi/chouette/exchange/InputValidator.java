package mobi.chouette.exchange;

import java.nio.file.Path;

import mobi.chouette.exchange.parameters.AbstractParameter;

public interface InputValidator {
	
	// BOIV service interface
	AbstractParameter toActionParameter(Object task);

	// Chouette service interface
	AbstractParameter toActionParameter(String abstractParameter);
	
	boolean checkParameters(String abstractParameter);

	boolean checkFilename(String fileName);
	
	boolean checkFile(String fileName, Path filePath, AbstractParameter abstractParameter);

	boolean checkParameters(AbstractParameter abstractParameter);
	
}
