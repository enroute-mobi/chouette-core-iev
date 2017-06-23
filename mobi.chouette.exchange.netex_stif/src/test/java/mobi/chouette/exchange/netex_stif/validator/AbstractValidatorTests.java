package mobi.chouette.exchange.netex_stif.validator;

import org.testng.Assert;
import org.testng.annotations.Test;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.ValidationReport;

public class AbstractValidatorTests {

	
	
	private boolean validateId(String id, String type) {
		Context context = new Context();
		AbstractValidator validator = new RouteValidator(context); // --
																	// RouteValidator
																	// étend
																	// AbstractParsingValidator
		int lineNumber = 0;
		int columnNumber = 0;
		ValidationReport report = new ValidationReport();
		context.put(Constant.FILE_NAME, "fakeFilename");
		context.put(Constant.VALIDATION_REPORT, report);
		boolean result = validator.checkNetexId(context, type, id, lineNumber, columnNumber);
		System.out.println(report.toString());

		System.out.println("REPORT Result = " + report.getResult());

		return result;
	}

	@Test(groups = { "Cas erreur 1" }, description = "nombre de champs composant ObjectId incorrect", priority = 3)
	public void verifiyIdNumberOfFieldsIncorrect() throws Exception {
		String id = "abc";
		String expectedType = "";

		boolean result = validateId(id, expectedType);

		Assert.assertTrue(!result);
	}

	@Test(groups = {
			"Cas erreur 2" }, description = "Type indiqué dans ObjectId ne correspond pas à celui attendu", priority = 3)
	public void verifiyIdTypeIncorrect() throws Exception {
		String id = "[CODESPACE]:autretype:[identifiant Technique]:LOC";
		String expectedType = "montype";

		boolean result = validateId(id, expectedType);

		Assert.assertTrue(!result);
	}

	@Test(groups = { "Cas erreur 3" }, description = "Le champ #4 n'est pas 'LOC'", priority = 3)
	public void verifiyIdField4NotLOC() throws Exception {
		String id = "[CODESPACE]:montype:[identifiant Technique]:COL";
		String expectedType = "montype";

		boolean result = validateId(id, expectedType);
		Assert.assertTrue(!result);
	}

	@Test(groups = { "Nominal" }, description = "ObjectId correct ", priority = 3)
	public void verifiyId() throws Exception {
		String id = "[CODESPACE]:montype:[identifiant Technique]:LOC";
		String expectedType = "montype";

		boolean result = validateId(id, expectedType);
		Assert.assertTrue(result);
	}

}
