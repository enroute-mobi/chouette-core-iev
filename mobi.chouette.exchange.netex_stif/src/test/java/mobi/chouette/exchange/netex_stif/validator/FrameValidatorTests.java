package mobi.chouette.exchange.netex_stif.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.BasicConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.exchange.netex_stif.JobDataTest;
import mobi.chouette.exchange.netex_stif.importer.NetexStifImportParameters;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.FILE_STATE;
import mobi.chouette.exchange.report.FileReport;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.exchange.validation.report.CheckPointErrorReport;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.util.Referential;
import mobi.chouette.persistence.hibernate.ContextHolder;

@Log4j
public class FrameValidatorTests extends AbstractTest {

	private static final String FRAME_HORAIRE = "NETEX_HORAIRE";
	private static final String FRAME_STRUCTURE = "NETEX_STRUCTURE";
	private static final String FRAME_OFFRE_LIGNE = "NETEX_OFFRE_LIGNE";
	private static final String FRAME_COMMUN = "NETEX_COMMUN";
	private static final String FRAME_CALENDRIER = "NETEX_CALENDRIER";
	private static final String OFFRE_FILE_NAME = "offre_test.xml";

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

	@Test(groups = { "Calendrier" }, description = "Good GeneralFrames list ", priority = 701)
	public void verifyCalendrierRightMandatoryGeneralFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, CALENDRIER_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, CALENDRIER_FILE_NAME, IO_TYPE.INPUT);

		ArrayList<String> frames = new ArrayList<>();
		frames.add(FRAME_CALENDRIER);
		boolean result = validator.checkMandatoryGeneralFrames(context, frames, 1, 2);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, CALENDRIER_FILE_NAME);

	}

	@Test(groups = { "Calendrier" }, description = "Good GeneralFrame ", priority = 702)
	public void verifyCalendrierValidGeneralFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, CALENDRIER_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, CALENDRIER_FILE_NAME, IO_TYPE.INPUT);
		String frame = FRAME_CALENDRIER;
		boolean result = validator.checkForbiddenGeneralFrames(context, frame, 1, 2);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, CALENDRIER_FILE_NAME);

	}

	@Test(groups = { "Calendrier" }, description = "Bad GeneralFrames list ", priority = 703)
	public void verifyCalendrierWrongtMandatoryGeneralFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, CALENDRIER_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, CALENDRIER_FILE_NAME, IO_TYPE.INPUT);
		ArrayList<String> frames = new ArrayList<>();
		frames.add("NETEX_CALENDRER");
		boolean result = validator.checkMandatoryGeneralFrames(context, frames, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, CALENDRIER_FILE_NAME, NetexCheckPoints.L2_NeTExSTIF_2, "2_netexstif_2_1", null);
		frames.add(FRAME_CALENDRIER);
		result = validator.checkMandatoryGeneralFrames(context, frames, 1, 2);
		Assert.assertTrue(result, "validator result ");

	}

	@Test(groups = { "Calendrier" }, description = "Bad GeneralFrame ", priority = 704)
	public void verifyCalendrierForbiddenGeneralFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, CALENDRIER_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, CALENDRIER_FILE_NAME, IO_TYPE.INPUT);
		String frameWithIncorrectName = "NETEX_CALENDRER";
		boolean result = validator.checkForbiddenGeneralFrames(context, frameWithIncorrectName, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, CALENDRIER_FILE_NAME, NetexCheckPoints.L2_NeTExSTIF_2, "2_netexstif_2_2",
				"NETEX_CALENDRER");

	}

	@Test(groups = { "Calendrier" }, description = "Good CompositeFrames list ", priority = 705)
	public void verifyCalendrierRightMandatoryCompositeFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, CALENDRIER_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, CALENDRIER_FILE_NAME, IO_TYPE.INPUT);
		ArrayList<String> frames = new ArrayList<>();
		boolean result = validator.checkMandatoryCompositeFrames(context, frames, 1, 2);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, CALENDRIER_FILE_NAME);

	}

	@Test(groups = { "Calendrier" }, description = "Bad CompositeFrame ", priority = 706)
	public void verifyCalendrierForbiddenCompositeFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, CALENDRIER_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, CALENDRIER_FILE_NAME, IO_TYPE.INPUT);
		String frame = "NETEX_CALENDRER";
		boolean result = validator.checkForbiddenCompositeFrames(context, frame, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, CALENDRIER_FILE_NAME, NetexCheckPoints.L2_NeTExSTIF_2, "2_netexstif_2_2",
				"CompositeFrame");
		frame = FRAME_OFFRE_LIGNE;
		result = validator.checkForbiddenCompositeFrames(context, frame, 1, 2);
		Assert.assertFalse(result, "validator result");
	}

	@Test(groups = { "Commun" }, description = "Good GeneralFrames list ", priority = 707)
	public void verifyCommunRightMandatoryGeneralFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, COMMUN_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, COMMUN_FILE_NAME, IO_TYPE.INPUT);
		ArrayList<String> frames = new ArrayList<>();
		frames.add(FRAME_COMMUN);
		boolean result = validator.checkMandatoryGeneralFrames(context, frames, 1, 6);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, COMMUN_FILE_NAME);

	}

	@Test(groups = { "Commun" }, description = "Good GeneralFrame ", priority = 708)
	public void verifyCommunValidGeneralFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, COMMUN_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, COMMUN_FILE_NAME, IO_TYPE.INPUT);
		String frame = FRAME_COMMUN;
		boolean result = validator.checkForbiddenGeneralFrames(context, frame, 1, 2);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, COMMUN_FILE_NAME);

	}

	@Test(groups = { "Commun" }, description = "Bad GeneralFrames list ", priority = 709)
	public void verifyCommunWrongtMandatoryGeneralFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, COMMUN_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, COMMUN_FILE_NAME, IO_TYPE.INPUT);
		ArrayList<String> frames = new ArrayList<>();
		frames.add("NETEX_COMMN");
		boolean result = validator.checkMandatoryGeneralFrames(context, frames, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, COMMUN_FILE_NAME, NetexCheckPoints.L2_NeTExSTIF_1, "2_netexstif_1_1", null);
		frames.add(FRAME_COMMUN);
		result = validator.checkMandatoryGeneralFrames(context, frames, 1, 2);
		Assert.assertTrue(result, "validator result ");

	}

	@Test(groups = { "Commun" }, description = "Bad GeneralFrame ", priority = 710)
	public void verifyCommunForbiddenGeneralFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, COMMUN_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, COMMUN_FILE_NAME, IO_TYPE.INPUT);
		String frameWithIncorrecName = "NETEX_COMMN";
		boolean result = validator.checkForbiddenGeneralFrames(context, frameWithIncorrecName, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, COMMUN_FILE_NAME, NetexCheckPoints.L2_NeTExSTIF_1, "2_netexstif_1_2", "NETEX_COMMN");

	}

	@Test(groups = { "Commun" }, description = "Good CompositeFrames list ", priority = 711)
	public void verifyCommunRightMandatoryCompositeFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, COMMUN_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, COMMUN_FILE_NAME, IO_TYPE.INPUT);
		ArrayList<String> frames = new ArrayList<>();
		boolean result = validator.checkMandatoryCompositeFrames(context, frames, 1, 2);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, COMMUN_FILE_NAME);

	}

	@Test(groups = { "Commun" }, description = "Bad CompositeFrame ", priority = 712)
	public void verifyCommunForbiddenCompositeFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, COMMUN_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, COMMUN_FILE_NAME, IO_TYPE.INPUT);
		String frame = FRAME_COMMUN;
		boolean result = validator.checkForbiddenCompositeFrames(context, frame, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, COMMUN_FILE_NAME, NetexCheckPoints.L2_NeTExSTIF_1, "2_netexstif_1_2", "CompositeFrame");
		frame = FRAME_OFFRE_LIGNE;
		result = validator.checkForbiddenCompositeFrames(context, frame, 1, 2);
		Assert.assertFalse(result, "validator result");
	}

	@Test(groups = { "Offre" }, description = "Good GeneralFrames list ", priority = 713)
	public void verifyOffreRightMandatoryGeneralFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, OFFRE_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, OFFRE_FILE_NAME, IO_TYPE.INPUT);
		Collection<String> compositeFrameNames =  new ArrayList<String>();
		context.put(COMPOSITE_FRAMES, compositeFrameNames);
		compositeFrameNames.add(NETEX_OFFRE_LIGNE);
		ArrayList<String> frames = new ArrayList<>();
		frames.add(FRAME_STRUCTURE);
		frames.add(FRAME_HORAIRE);
		boolean result = validator.checkMandatoryGeneralFrames(context, frames, 1, 6);
		ActionReport report = (ActionReport) context.get(REPORT);

		ValidationReport valReport = (ValidationReport) context.get(VALIDATION_REPORT);
		log.info(report);
		log.info(valReport);

		Assert.assertTrue(result, "validator result");
		checkNoReports(context, OFFRE_FILE_NAME);

	}

	@Test(groups = { "Offre" }, description = "Good GeneralFrame ", priority = 714)
	public void verifyOffreValidGeneralFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, OFFRE_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, OFFRE_FILE_NAME, IO_TYPE.INPUT);
		String frame = FRAME_STRUCTURE;
		boolean result = validator.checkForbiddenGeneralFrames(context, frame, 1, 2);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, OFFRE_FILE_NAME);
		frame = FRAME_HORAIRE;
		result = validator.checkForbiddenGeneralFrames(context, frame, 1, 2);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, OFFRE_FILE_NAME);

	}

	@Test(groups = { "Offre" }, description = "Bad GeneralFrames list ", priority = 715)
	public void verifyOffreWrongtMandatoryGeneralFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, OFFRE_FILE_NAME);
		Collection<String> compositeFrameNames =  new ArrayList<String>();
		context.put(COMPOSITE_FRAMES, compositeFrameNames);
		compositeFrameNames.add(NETEX_OFFRE_LIGNE);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, OFFRE_FILE_NAME, IO_TYPE.INPUT);
		ArrayList<String> frames = new ArrayList<>();
		frames.add(FRAME_STRUCTURE);
		boolean result = validator.checkMandatoryGeneralFrames(context, frames, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, OFFRE_FILE_NAME, NetexCheckPoints.L2_NeTExSTIF_3, "2_netexstif_3_3", FRAME_HORAIRE);
		frames.add(FRAME_HORAIRE);
		result = validator.checkMandatoryGeneralFrames(context, frames, 1, 2);
		Assert.assertTrue(result, "validator result ");

	}

	@Test(groups = { "Offre" }, description = "Bad GeneralFrame ", priority = 716)
	public void verifyOffreForbiddenGeneralFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, OFFRE_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, OFFRE_FILE_NAME, IO_TYPE.INPUT);
		String frame = "NETEX_SCTE";
		boolean result = validator.checkForbiddenGeneralFrames(context, frame, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, OFFRE_FILE_NAME, NetexCheckPoints.L2_NeTExSTIF_3, "2_netexstif_3_4", frame);

	}

	@Test(groups = { "Offre" }, description = "Good CompositeFrames list ", priority = 717)
	public void verifyOffreRightMandatoryCompositeFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, OFFRE_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, OFFRE_FILE_NAME, IO_TYPE.INPUT);
		ArrayList<String> frames = new ArrayList<>();
		frames.add(FRAME_OFFRE_LIGNE);
		boolean result = validator.checkMandatoryCompositeFrames(context, frames, 1, 2);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, OFFRE_FILE_NAME);

	}



	@Test(groups = { "Offre" }, description = "Bad CompositeFrame ", priority = 718)
	public void verifyOffreForbiddenCompositeFrame() throws Exception {
		Context context = initImportContext();
		context.put(FILE_NAME, OFFRE_FILE_NAME);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, OFFRE_FILE_NAME, IO_TYPE.INPUT);
		String frame = FRAME_COMMUN;
		boolean result = validator.checkForbiddenCompositeFrames(context, frame, 1, 2);
		Assert.assertFalse(result, "validator result");
		checkReports(context, OFFRE_FILE_NAME, NetexCheckPoints.L2_NeTExSTIF_3, "2_netexstif_3_2", frame);
		frame = FRAME_OFFRE_LIGNE;
		result = validator.checkForbiddenCompositeFrames(context, frame, 1, 2);
		Assert.assertTrue(result, "validator result");
	}

}
