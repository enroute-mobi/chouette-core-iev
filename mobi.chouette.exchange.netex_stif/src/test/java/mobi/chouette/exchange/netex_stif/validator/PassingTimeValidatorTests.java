package mobi.chouette.exchange.netex_stif.validator;

import java.sql.Time;
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
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Route;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.util.Referential;
import mobi.chouette.persistence.hibernate.ContextHolder;

@Log4j
public class PassingTimeValidatorTests extends AbstractTest {

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

	private VehicleJourneyAtStop buildVehicleJourneyAtStop() {
		Route route = new Route();
		route.setObjectId("CITYWAY:Route:1234:LOC");
		route.setName("My Route");
		JourneyPattern jp = new JourneyPattern();
		jp.setObjectId("CITYWAY:ServiceJourneyPattern:1234:LOC");
		jp.setName("My Journey Pattern");
		jp.setRoute(route);
		VehicleJourney journey = new VehicleJourney();
		journey.setObjectId("CITYWAY:ServiceJourney:1234:LOC");
		journey.setJourneyPattern(jp);
		VehicleJourneyAtStop vjas = new VehicleJourneyAtStop();
		vjas.setVehicleJourney(journey);
		return vjas;
	}

	@SuppressWarnings("deprecation")
	@Test(groups = { "PassingTime" }, description = "GoodPassingTime", priority = 1)
	public void verifyGoodPassingTime() throws Exception {
		Context context = initImportContext();
		PassingTimeValidator validator = (PassingTimeValidator) ValidatorFactory.getValidator(context,
				PassingTimeValidator.class);
		context.put(FILE_NAME, "offre_xxx.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "offre_xxx.xml", IO_TYPE.INPUT);
		VehicleJourneyAtStop vjas = buildVehicleJourneyAtStop();
		vjas.setDepartureTime(new Time(15, 18, 00));
		boolean result = validator.validate(context, vjas, 1, 2, 0);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, "offre_xxx.xml");
		vjas.setArrivalTime(new Time(15, 17, 00));
		result = validator.validate(context, vjas, 1, 2, 0);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, "offre_xxx.xml");
		vjas.setDepartureDayOffset(1);
		vjas.setDepartureTime(new Time(13, 18, 00));
		result = validator.validate(context, vjas, 1, 2, 0);
		Assert.assertTrue(result, "validator result");
		checkNoReports(context, "offre_xxx.xml");
	}

	@Test(groups = { "PassingTime" }, description = "missing departure time", priority = 2)
	public void verifyMissingDepartureTime() throws Exception {
		Context context = initImportContext();
		PassingTimeValidator validator = (PassingTimeValidator) ValidatorFactory.getValidator(context,
				PassingTimeValidator.class);
		context.put(FILE_NAME, "offre_xxx.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "offre_xxx.xml", IO_TYPE.INPUT);
		VehicleJourneyAtStop vjas = buildVehicleJourneyAtStop();
		boolean result = validator.validate(context, vjas, 1, 2, 0);
		Assert.assertFalse(result, "validator result");

		ValidationReport vr = (ValidationReport) context.get(VALIDATION_REPORT);
		ActionReport ar = (ActionReport) context.get(REPORT);

		log.info("Validation Report ===>" + vr.toString());
		log.info("Validation Report Result = " + vr.getResult());
		log.info("Action Report ===>" + ar.toString());
		log.info("Action Report Result = " + ar.getResult());
		checkReports(context, "offre_xxx.xml", NetexCheckPoints.L2_NeTExSTIF_PassingTime_1, "2_netexstif_passingtime_1",
				"0");
	}

	@SuppressWarnings("deprecation")
	@Test(groups = { "PassingTime" }, description = "arrival time after departure time", priority = 3)
	public void verifyArrivalTimeAfterDepartureTime() throws Exception {
		Context context = initImportContext();
		PassingTimeValidator validator = (PassingTimeValidator) ValidatorFactory.getValidator(context,
				PassingTimeValidator.class);
		context.put(FILE_NAME, "offre_xxx.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "offre_xxx.xml", IO_TYPE.INPUT);
		VehicleJourneyAtStop vjas = buildVehicleJourneyAtStop();
		vjas.setDepartureTime(new Time(15, 18, 00));
		vjas.setArrivalTime(new Time(15, 18, 01));
		boolean result = validator.validate(context, vjas, 1, 2, 0);
		Assert.assertFalse(result, "validator result");
		checkReports(context, "offre_xxx.xml", NetexCheckPoints.L2_NeTExSTIF_PassingTime_2, "2_netexstif_passingtime_2",
				"0");
	}

	@SuppressWarnings("deprecation")
	@Test(groups = { "PassingTime" }, description = "arrival offest after departure offset", priority = 3)
	public void verifyArrivalDayOffestAfterDepartureDayOffset() throws Exception {
		Context context = initImportContext();
		PassingTimeValidator validator = (PassingTimeValidator) ValidatorFactory.getValidator(context,
				PassingTimeValidator.class);
		context.put(FILE_NAME, "offre_xxx.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "offre_xxx.xml", IO_TYPE.INPUT);
		VehicleJourneyAtStop vjas = buildVehicleJourneyAtStop();
		vjas.setDepartureTime(new Time(15, 18, 00));
		vjas.setArrivalTime(new Time(15, 17, 00));
		vjas.setArrivalDayOffset(1);
		boolean result = validator.validate(context, vjas, 1, 2, 0);
		Assert.assertFalse(result, "validator result");
		checkReports(context, "offre_xxx.xml", NetexCheckPoints.L2_NeTExSTIF_PassingTime_2, "2_netexstif_passingtime_2",
				"0");
	}

}
