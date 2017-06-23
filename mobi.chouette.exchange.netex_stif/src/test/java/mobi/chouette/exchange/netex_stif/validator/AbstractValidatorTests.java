package mobi.chouette.exchange.netex_stif.validator;

import java.util.Locale;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.BasicConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.exchange.netex_stif.JobDataTest;
import mobi.chouette.exchange.netex_stif.importer.NetexStifImportParameters;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.util.Referential;
import mobi.chouette.persistence.hibernate.ContextHolder;

public class AbstractValidatorTests implements Constant{

	protected static InitialContext initialContext;
	
	protected Context initImportContext() {

		ContextHolder.setContext("chouette_gui"); // set tenant schema

		Context context = new Context();
		context.put(INITIAL_CONTEXT, initialContext);
		context.put(REPORT, new ActionReport());
		context.put(VALIDATION_REPORT, new ValidationReport());
		NetexStifImportParameters configuration = new NetexStifImportParameters();
		context.put(CONFIGURATION, configuration);
		context.put(REFERENTIAL, new Referential());
		context.put(NETEX_STIF_OBJECT_FACTORY, new NetexStifObjectFactory());
		configuration.setName("name");
		configuration.setUserName("userName");
		configuration.setNoSave(true);
		configuration.setOrganisationName("organisation");
		configuration.setReferentialName("test");
		JobDataTest test = new JobDataTest();
		context.put(JOB_DATA, test);

		test.setAction(JobData.ACTION.importer);
		test.setType("netex_stif");
		context.put(TESTNG, "true");
		context.put(OPTIMIZED, Boolean.FALSE);
		return context;
	}

	
	private boolean validateId(String id, String type) {
		Context context = initImportContext();
		AbstractValidator validator = new RouteValidator(context); // --
																	// RouteValidator
																	// étend
																	// AbstractParsingValidator
		int lineNumber = 0;
		int columnNumber = 0;
		ValidationReport report = (ValidationReport) context.get(VALIDATION_REPORT);
		context.put(Constant.FILE_NAME, "fakeFilename");
		context.put(Constant.VALIDATION_REPORT, report);
		boolean result = validator.checkNetexId(context, type, id, lineNumber, columnNumber);
		System.out.println(report.toString());

		System.out.println("REPORT Result = " + report.getResult());

		return result;
	}

	@BeforeSuite
	public void init() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		Locale.setDefault(Locale.ENGLISH);
		if (initialContext == null) {
			try {
				initialContext = new InitialContext();
			} catch (NamingException e) {
				e.printStackTrace();
			}

		}

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
		String id = "Code_Space-1:autretype:identifiant-Technique_1:LOC";
		String expectedType = "montype";

		boolean result = validateId(id, expectedType);

		Assert.assertFalse(result);
	}

	@Test(groups = { "Cas erreur 3" }, description = "Le champ #4 n'est pas 'LOC'", priority = 3)
	public void verifiyIdField4NotLOC() throws Exception {
		String id = "Code_Space-1:montype:identifiant-Technique_1:COL";
		String expectedType = "montype";

		boolean result = validateId(id, expectedType);
		Assert.assertFalse(result);
	}

	@Test(groups = { "Cas erreur 3" }, description = "Caractères non autorisés", priority = 3)
	public void verifiyIdBadChars() throws Exception {
		String expectedType = "montype";

		String id = "es pace:montype:identifiant-Technique_1:LOC";
		boolean result = validateId(id, expectedType);
		Assert.assertFalse(result,"espace dans codespace");
		
		id = "Code_Space-1:montype:identifiant espace:LOC";
        result = validateId(id, expectedType);
		Assert.assertFalse(result,"espace dans identifiant");

		id = "accentué:montype:identifiant-Technique_1:LOC";
        result = validateId(id, expectedType);
		Assert.assertFalse(result,"accent dans codespace");

		id = "Code_Space-1:montype:accentué:LOC";
        result = validateId(id, expectedType);
		Assert.assertFalse(result,"accent dans identifiant");
}

	@Test(groups = { "Nominal" }, description = "ObjectId correct ", priority = 3)
	public void verifiyId() throws Exception {
		String id = "Code_Space-1:montype:identifiant-Technique_1:LOC";
		String expectedType = "montype";

		boolean result = validateId(id, expectedType);
		Assert.assertTrue(result);
	}

}
