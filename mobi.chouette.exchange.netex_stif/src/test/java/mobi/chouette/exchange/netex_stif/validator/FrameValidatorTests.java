package mobi.chouette.exchange.netex_stif.validator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.BasicConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import lombok.extern.log4j.Log4j;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.exchange.netex_stif.JobDataTest;
import mobi.chouette.exchange.netex_stif.importer.NetexStifImportParameters;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.validator.RouteValidator;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.FileReport;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.exchange.report.ActionReporter.FILE_STATE;
import mobi.chouette.exchange.validation.report.CheckPointErrorReport;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.util.Referential;
import mobi.chouette.persistence.hibernate.ContextHolder;

@Log4j
public class FrameValidatorTests implements Constant {

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

		// test.setPathName("target/referential/test");
		// File f = new File("target/referential/test");
		// if (f.exists())
		// try {
		// FileUtils.deleteDirectory(f);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// f.mkdirs();
		// test.setReferential("chouette_gui");
		test.setAction(JobData.ACTION.importer);
		test.setType("netex_stif");
		context.put(TESTNG, "true");
		context.put(OPTIMIZED, Boolean.FALSE);
		return context;
	}

	private void checkReports(Context context, String fileName, String checkPointCode, String messageCode,
			String value) {
		ActionReport report = (ActionReport) context.get(REPORT);

		ValidationReport valReport = (ValidationReport) context.get(VALIDATION_REPORT);
		log.info(report);
		log.info(valReport);
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
	private void checkNoReports(Context context, String fileName) {
		ActionReport report = (ActionReport) context.get(REPORT);

		ValidationReport valReport = (ValidationReport) context.get(VALIDATION_REPORT);
		log.info(report);
		log.info(valReport);
		Assert.assertEquals(report.getResult(), "OK", "result");
		Assert.assertEquals(report.getFiles().size(), 1, "file reported size ");
		FileReport file = report.getFiles().get(0);
		Assert.assertEquals(file.getStatus(), FILE_STATE.OK, "file status reported");
		Assert.assertEquals(file.getCheckPointErrorCount(), 0, "no file error reported");

	}

	@Test(groups = { "Calendrier" }, description = "Good GeneralFrames list ", priority = 1)
	public void verifyCalendrierRightMandatoryGeneralFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "calendrier.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "calendrier.xml", IO_TYPE.INPUT);
		
		ArrayList<String> frames = new ArrayList<>();
		frames.add("NETEX_CALENDRIER");
		boolean result = validator.checkMandatoryGeneralFrames(context, frames, 1, 2);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, "calendrier.xml");

	}

	@Test(groups = { "Calendrier" }, description = "Good GeneralFrame ", priority = 2)
	public void verifyCalendrierValidGeneralFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "calendrier.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "calendrier.xml", IO_TYPE.INPUT);
		String frame = "NETEX_CALENDRIER";
		boolean result = validator.checkForbiddenGeneralFrames(context, frame, 1, 2);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, "calendrier.xml");

	}

	@Test(groups = { "Calendrier" }, description = "Bad GeneralFrames list ", priority = 3)
	public void verifyCalendrierWrongtMandatoryGeneralFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "calendrier.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "calendrier.xml", IO_TYPE.INPUT);
		ArrayList<String> frames = new ArrayList<>();
		frames.add("NETEX_CALENDRER");
		boolean result = validator.checkMandatoryGeneralFrames(context, frames, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, "calendrier.xml",NetexCheckPoints.L2_NeTExSTIF_2,"2_netexstif_2_1",null);
		frames.add("NETEX_CALENDRIER");
		result = validator.checkMandatoryGeneralFrames(context, frames, 1, 2);
		Assert.assertTrue(result, "validator result ");

	}

	@Test(groups = { "Calendrier" }, description = "Bad GeneralFrame ", priority = 4)
	public void verifyCalendrierForbiddenGeneralFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "calendrier.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "calendrier.xml", IO_TYPE.INPUT);
		String frame = "NETEX_CALENDRER";
		boolean result = validator.checkForbiddenGeneralFrames(context, frame, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, "calendrier.xml",NetexCheckPoints.L2_NeTExSTIF_2,"2_netexstif_2_2","NETEX_CALENDRER");

	}

	@Test(groups = { "Calendrier" }, description = "Good CompositeFrames list ", priority = 5)
	public void verifyCalendrierRightMandatoryCompositeFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "calendrier.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "calendrier.xml", IO_TYPE.INPUT);
		ArrayList<String> frames = new ArrayList<>();
		boolean result = validator.checkMandatoryCompositeFrames(context, frames, 1, 2);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, "calendrier.xml");

	}

	@Test(groups = { "Calendrier" }, description = "Bad CompositeFrame ", priority = 6)
	public void verifyCalendrierForbiddenCompositeFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "calendrier.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "calendrier.xml", IO_TYPE.INPUT);
		String frame = "NETEX_CALENDRER";
		boolean result = validator.checkForbiddenCompositeFrames(context, frame, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, "calendrier.xml",NetexCheckPoints.L2_NeTExSTIF_2,"2_netexstif_2_2","CompositeFrame");
		frame = "NETEX_OFFRE_LIGNE";
		result = validator.checkForbiddenCompositeFrames(context, frame, 1, 2);
		Assert.assertFalse(result, "validator result");
	}

	@Test(groups = { "Commun" }, description = "Good GeneralFrames list ", priority = 7)
	public void verifyCommunRightMandatoryGeneralFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "commun.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "commun.xml", IO_TYPE.INPUT);
		ArrayList<String> frames = new ArrayList<>();
		frames.add("NETEX_COMMUN");
		boolean result = validator.checkMandatoryGeneralFrames(context, frames, 1, 6);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, "commun.xml");

	}

	@Test(groups = { "Commun" }, description = "Good GeneralFrame ", priority = 8)
	public void verifyCommunValidGeneralFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "commun.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "commun.xml", IO_TYPE.INPUT);
		String frame = "NETEX_COMMUN";
		boolean result = validator.checkForbiddenGeneralFrames(context, frame, 1, 2);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, "commun.xml");

	}

	@Test(groups = { "Commun" }, description = "Bad GeneralFrames list ", priority = 9)
	public void verifyCommunWrongtMandatoryGeneralFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "commun.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "commun.xml", IO_TYPE.INPUT);
		ArrayList<String> frames = new ArrayList<>();
		frames.add("NETEX_COMMN");
		boolean result = validator.checkMandatoryGeneralFrames(context, frames, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, "commun.xml",NetexCheckPoints.L2_NeTExSTIF_1,"2_netexstif_1_1",null);
		frames.add("NETEX_COMMUN");
		result = validator.checkMandatoryGeneralFrames(context, frames, 1, 2);
		Assert.assertTrue(result, "validator result ");

	}

	@Test(groups = { "Commun" }, description = "Bad GeneralFrame ", priority = 10)
	public void verifyCommunForbiddenGeneralFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "commun.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "commun.xml", IO_TYPE.INPUT);
		String frame = "NETEX_COMMN";
		boolean result = validator.checkForbiddenGeneralFrames(context, frame, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, "commun.xml",NetexCheckPoints.L2_NeTExSTIF_1,"2_netexstif_1_2","NETEX_COMMN");

	}

	@Test(groups = { "Commun" }, description = "Good CompositeFrames list ", priority = 11)
	public void verifyCommunRightMandatoryCompositeFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "commun.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "commun.xml", IO_TYPE.INPUT);
		ArrayList<String> frames = new ArrayList<>();
		boolean result = validator.checkMandatoryCompositeFrames(context, frames, 1, 2);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, "commun.xml");

	}

	@Test(groups = { "Commun" }, description = "Bad CompositeFrame ", priority = 12)
	public void verifyCommunForbiddenCompositeFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "commun.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "commun.xml", IO_TYPE.INPUT);
		String frame = "NETEX_COMMUN";
		boolean result = validator.checkForbiddenCompositeFrames(context, frame, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, "commun.xml",NetexCheckPoints.L2_NeTExSTIF_1,"2_netexstif_1_2","CompositeFrame");
		frame = "NETEX_OFFRE_LIGNE";
		result = validator.checkForbiddenCompositeFrames(context, frame, 1, 2);
		Assert.assertFalse(result, "validator result");
	}

	
	
	
	
	
	/*
	
	
	
	
	@Test(groups = { "Offre" }, description = "Good GeneralFrames list ", priority = 7)
	public void verifyOffreRightMandatoryGeneralFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "commun.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "commun.xml", IO_TYPE.INPUT);
		ArrayList<String> frames = new ArrayList<>();
		frames.add("NETEX_COMMUN");
		boolean result = validator.checkMandatoryGeneralFrames(context, frames, 1, 6);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, "commun.xml");

	}

	@Test(groups = { "Commun" }, description = "Good GeneralFrame ", priority = 8)
	public void verifyCommunValidGeneralFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "commun.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "commun.xml", IO_TYPE.INPUT);
		String frame = "NETEX_COMMUN";
		boolean result = validator.checkForbiddenGeneralFrames(context, frame, 1, 2);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, "commun.xml");

	}

	@Test(groups = { "Commun" }, description = "Bad GeneralFrames list ", priority = 9)
	public void verifyCommunWrongtMandatoryGeneralFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "commun.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "commun.xml", IO_TYPE.INPUT);
		ArrayList<String> frames = new ArrayList<>();
		frames.add("NETEX_COMMN");
		boolean result = validator.checkMandatoryGeneralFrames(context, frames, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, "commun.xml",NetexCheckPoints.L2_NeTExSTIF_1,"2_netexstif_1_1",null);
		frames.add("NETEX_COMMUN");
		result = validator.checkMandatoryGeneralFrames(context, frames, 1, 2);
		Assert.assertTrue(result, "validator result ");

	}

	@Test(groups = { "Commun" }, description = "Bad GeneralFrame ", priority = 10)
	public void verifyCommunForbiddenGeneralFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "commun.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "commun.xml", IO_TYPE.INPUT);
		String frame = "NETEX_COMMN";
		boolean result = validator.checkForbiddenGeneralFrames(context, frame, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, "commun.xml",NetexCheckPoints.L2_NeTExSTIF_1,"2_netexstif_1_2","NETEX_COMMN");

	}

	@Test(groups = { "Commun" }, description = "Good CompositeFrames list ", priority = 11)
	public void verifyCommunRightMandatoryCompositeFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "commun.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "commun.xml", IO_TYPE.INPUT);
		ArrayList<String> frames = new ArrayList<>();
		boolean result = validator.checkMandatoryCompositeFrames(context, frames, 1, 2);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, "commun.xml");

	}

	@Test(groups = { "Commun" }, description = "Bad CompositeFrame ", priority = 12)
	public void verifyCommunForbiddenCompositeFrame() throws Exception {
		Context context = initImportContext();
		FrameValidator validator = new FrameValidator(context);
		context.put(FILE_NAME, "commun.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "commun.xml", IO_TYPE.INPUT);
		String frame = "NETEX_COMMUN";
		boolean result = validator.checkForbiddenCompositeFrames(context, frame, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, "commun.xml",NetexCheckPoints.L2_NeTExSTIF_1,"2_netexstif_1_2","CompositeFrame");
		frame = "NETEX_OFFRE_LIGNE";
		result = validator.checkForbiddenCompositeFrames(context, frame, 1, 2);
		Assert.assertFalse(result, "validator result");
	}
*/
	
	
}
