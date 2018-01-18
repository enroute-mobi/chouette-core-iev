package mobi.chouette.exchange.netex_stif.validator;

import java.util.Locale;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.BasicConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.exchange.netex_stif.JobDataImpl;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.importer.NetexStifImportParameters;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.FILE_STATE;
import mobi.chouette.exchange.report.FileReport;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.exchange.validation.report.CheckPointErrorReport;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.Route;
import mobi.chouette.model.util.Referential;
import mobi.chouette.persistence.hibernate.ContextHolder;

@Log4j

public class AbstractValidatorTests extends AbstractTest {

	private static final String FAKE_FILENAME = "fakeFilename";
	protected static InitialContext initialContext;

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

	protected Context initImportContext() {

		ContextHolder.setContext("chouette_gui"); // set tenant schema

		Context context = new Context();
		context.put(Constant.INITIAL_CONTEXT, initialContext);
		context.put(Constant.REPORT, new ActionReport());
		context.put(Constant.VALIDATION_REPORT, new ValidationReport());
		NetexStifImportParameters configuration = new NetexStifImportParameters();
		context.put(Constant.CONFIGURATION, configuration);
		context.put(Constant.REFERENTIAL, new Referential());
		context.put(NetexStifConstant.NETEX_STIF_OBJECT_FACTORY, new NetexStifObjectFactory());
		configuration.setName("name");
		configuration.setUserName("userName");
		configuration.setNoSave(true);
		configuration.setOrganisationName("organisation");
		configuration.setReferentialName("test");
		JobDataImpl test = new JobDataImpl();
		context.put(Constant.JOB_DATA, test);

		test.setAction(JobData.ACTION.importer);
		test.setType("netex_stif");
		context.put(Constant.TESTNG, "true");
		context.put(Constant.OPTIMIZED, Boolean.FALSE);
		return context;
	}

	private boolean validateId(String id, String type) {
		Context context = initImportContext();
		context.put(Constant.FILE_NAME, FAKE_FILENAME);
		AbstractValidator validator = ValidatorFactory.getValidator(context, RouteValidator.class); // --
		// RouteValidator
		// étend
		// AbstractParsingValidator
		int lineNumber = 0;
		int columnNumber = 0;
		ValidationReport report = (ValidationReport) context.get(Constant.VALIDATION_REPORT);
		boolean result = validator.checkNetexId(context, type, id, lineNumber, columnNumber);
		log.info(report.getCheckPointErrors());

		log.info("REPORT Result = " + report.getResult());

		return result;
	}

	protected void checkReports(Context context, String fileName, String checkPointCode, String messageCode,
			String value) {
		ActionReport report = (ActionReport) context.get(Constant.REPORT);

		ValidationReport valReport = (ValidationReport) context.get(Constant.VALIDATION_REPORT);
		log.info(report);
		log.info(valReport.getCheckPointErrors());
		Assert.assertEquals(report.getResult(), "OK", "result");
		Assert.assertEquals(report.getFiles().size(), 1, "file reported size ");
		FileReport file = report.getFiles().get(0);
		Assert.assertEquals(file.getStatus(), FILE_STATE.ERROR, "file status reported");
		Assert.assertEquals(file.getCheckPointErrorCount(), 1, "file error reported");
		CheckPointErrorReport error = valReport.getCheckPointErrors()
				.get(file.getCheckPointErrorKeys().get(0).intValue());
		Assert.assertEquals(error.getTestId(), checkPointCode, "checkpoint code");
		Assert.assertEquals(error.getKey(), messageCode, "message code");
		if (value == null)
			Assert.assertNull(error.getValue(), "value");
		else
			Assert.assertEquals(error.getValue(), value, "value");
		Assert.assertEquals(error.getSource().getFile().getFilename(), fileName, "source filename");
		Assert.assertEquals(error.getSource().getFile().getLineNumber(), Integer.valueOf(1), "source line number");
		Assert.assertEquals(error.getSource().getFile().getColumnNumber(), Integer.valueOf(2), "source column number");

	}


	@Test(groups = { "Cas erreur 1" }, description = "nombre de champs composant ObjectId incorrect", priority = 501)
	public void verifyIdNumberOfFieldsIncorrect() throws Exception {
		String id = "abc";
		String expectedType = "";

		boolean result = validateId(id, expectedType);

		Assert.assertTrue(!result);
	}

	@Test(groups = {
			"Cas erreur 2" }, description = "Type indiqué dans ObjectId ne correspond pas à celui attendu", priority = 502)
	public void verifyIdTypeIncorrect() throws Exception {
		String id = "Code_Space-1:autretype:identifiant-Technique_1:LOC";
		String expectedType = "montype";

		boolean result = validateId(id, expectedType);

		Assert.assertFalse(result);
	}

	@Test(groups = { "Cas erreur 3" }, description = "Caractères non autorisés", priority = 503)
	public void verifiyIdBadChars() throws Exception {
		String expectedType = "montype";

		String id = "es pace:montype:identifiant-Technique_1:LOC";
		boolean result = validateId(id, expectedType);
		Assert.assertFalse(result, "espace dans codespace");

		id = "Code_Space-1:montype:identifiant espace:LOC";
		result = validateId(id, expectedType);
		Assert.assertFalse(result, "espace dans identifiant");

		id = "accentué:montype:identifiant-Technique_1:LOC";
		result = validateId(id, expectedType);
		Assert.assertFalse(result, "accent dans codespace");

		id = "Code_Space-1:montype:accentué:LOC";
		result = validateId(id, expectedType);
		Assert.assertFalse(result, "accent dans identifiant");
	}

	@Test(groups = { "Nominal" }, description = "ObjectId correct ", priority = 504)
	public void verifyId() throws Exception {
		String id = "Code_Space-1:montype:identifiant-Technique_1:LOC";
		String expectedType = "montype";

		boolean result = validateId(id, expectedType);
		Assert.assertTrue(result);
	}

	@Test(groups = { "Nominal" }, description = "ObjectId correct ", priority = 505)
	public void verifyRef() throws Exception {
		String type = NetexStifConstant.LINE_REF;
		String ref = "STIF:CODIFLIGNE:Line:1234"; // old fashion line ref

		boolean result = validateRef(ref, type, true);
		Assert.assertTrue(result, "old fashion line ref ok");

		ref = "STIF-LIGNE:Line:1234:STIF"; // new fashion line ref
		result = validateRef(ref, type, true);
		Assert.assertTrue(result, "new fashion line ref ok");

		type = NetexStifConstant.OPERATOR_REF;
		ref = "STIF:CODIFLIGNE:Operator:1234"; // old fashion operator ref
		result = validateRef(ref, type, true);
		Assert.assertTrue(result, "old fashion operator ref ok");

		ref = "STIF-LIGNE:Operator:1234:STIF"; // new fashion operator ref
		result = validateRef(ref, type, true);
		Assert.assertTrue(result, "new fashion operator ref ok");

		type = NetexStifConstant.QUAY_REF;
		ref = "FR:14526:ZDE:1234:STIF"; // quay ref
		result = validateRef(ref, type, true);
		Assert.assertTrue(result, "old fashion operator ref ok");

		type = NetexStifConstant.ROUTE_REF;
		ref = "CITYWAY:Route:1245:LOC"; // quay ref
		result = validateRef(ref, type, true);
		Assert.assertTrue(result, "internal ref");
		

	}
	
	@Test(groups = { "Cas d'erreur de ref" }, description = "ObjectId incorrect ", priority = 506)
	public void verifyBadRef() throws Exception {
		String type = NetexStifConstant.LINE_REF;
		String ref = "STIF:CODIFIGNE:Line:1234"; // old fashion line ref

		boolean result = validateRef(ref, type, false);
		Assert.assertFalse(result, "old fashion line ref");

		ref = "STIF-LIGNE:Line:1234:LOC"; // new fashion line ref
		result = validateRef(ref, type, false);
		Assert.assertFalse(result, "new fashion line ref");

		type = NetexStifConstant.OPERATOR_REF;
		ref = "STIF-CODIFLIGNE:Operator:1234"; // old fashion operator ref
		result = validateRef(ref, type, false);
		Assert.assertFalse(result, "old fashion operator ref");

		ref = "STIF-LIGNE:Company:1234:STIF"; // new fashion operator ref
		result = validateRef(ref, type, false);
		Assert.assertFalse(result, "new fashion operator ref");

		type = NetexStifConstant.QUAY_REF;
		ref = "FR:14526:ZDEs:1234:STIF"; // quay ref
		result = validateRef(ref, type, false);
		Assert.assertFalse(result, "stop ref");

		type = NetexStifConstant.ROUTE_REF;
		ref = "CITYWAY:Route:1245:STIF"; // quay ref
		result = validateRef(ref, type, false);
		Assert.assertFalse(result, "internal ref");

	}
	
	

	private boolean validateRef(String ref, String refType, boolean b) {
		Context context = initImportContext();

		context.put(Constant.FILE_NAME, FAKE_FILENAME);
		AbstractValidator validator = ValidatorFactory.getValidator(context, RouteValidator.class); // --
		// RouteValidator
		// étend
		// AbstractValidator
		int lineNumber = 1;
		int columnNumber = 2;
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, FAKE_FILENAME, IO_TYPE.INPUT);
		Route object = new Route();
		object.setObjectId("CITYWAY:Route:1234:LOC");
		object.setName("route de test");
		boolean result = validator.checkNetexRef(context, object, refType, ref, lineNumber, columnNumber);
        if (b) checkNoReports(context, FAKE_FILENAME);
        else checkReports(context, FAKE_FILENAME, NetexCheckPoints.L2_NeTExSTIF_7, "2_netexstif_7", ref);
        
		return result;

	}
}
