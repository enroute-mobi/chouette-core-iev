package mobi.chouette.exchange.netex_stif.validator;

import java.sql.Time;
import java.util.Locale;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.BasicConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.exchange.netex_stif.JobDataImpl;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.importer.NetexStifImportParameters;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.validator.ServiceJourneyValidator.PassingTimeComparator;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.FILE_STATE;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.util.Referential;
import mobi.chouette.persistence.hibernate.ContextHolder;

public class ServiceJourneyValidatorTests extends AbstractTest {

	protected static InitialContext initialContext;

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
		context.put(Constant.FILE_NAME, "offre_xxx.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "offre_xxx.xml", IO_TYPE.INPUT);

		test.setAction(JobData.ACTION.importer);
		test.setType("netex_stif");
		context.put(Constant.TESTNG, "true");
		context.put(Constant.OPTIMIZED, Boolean.FALSE);
		return context;
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

	private VehicleJourney buildVehicleJourney() {
		Route route = new Route();
		route.setObjectId("CITYWAY:Route:1234:LOC");
		route.setName("My Route");
		StopPoint sp1 = new StopPoint();
		sp1.setPosition(0);
		sp1.setObjectId("CITYWAY:StopPoint:1:LOC");
		sp1.setRoute(route);
		StopPoint sp2 = new StopPoint();
		sp2.setPosition(1);
		sp2.setObjectId("CITYWAY:StopPoint:2:LOC");
		sp2.setRoute(route);
		JourneyPattern jp = new JourneyPattern();
		jp.setObjectId("CITYWAY:ServiceJourneyPattern:1234:LOC");
		jp.setName("My Journey Pattern");
		jp.setRoute(route);
		jp.addStopPoint(sp1);
		jp.addStopPoint(sp2);
		VehicleJourney journey = new VehicleJourney();
		journey.setObjectId("CITYWAY:ServiceJourney:1234:LOC");
		journey.setJourneyPattern(jp);
		VehicleJourneyAtStop vjas1 = new VehicleJourneyAtStop();
		vjas1.setVehicleJourney(journey);
		vjas1.setStopPoint(sp1);
		VehicleJourneyAtStop vjas2 = new VehicleJourneyAtStop();
		vjas2.setVehicleJourney(journey);
		vjas2.setStopPoint(sp2);
		return journey;
	}

	@SuppressWarnings("deprecation")
	@Test(groups = { "PassingTimeComparator" }, description = "valid comparator", priority = 1301)
	public void validatePassingTimeComparator() throws Exception {
		Context context = initImportContext();
		ServiceJourneyValidator validator = (ServiceJourneyValidator) ValidatorFactory.getValidator(context,
				ServiceJourneyValidator.class);
		PassingTimeComparator comparator = validator.new PassingTimeComparator();

		VehicleJourneyAtStop vjas1 = new VehicleJourneyAtStop();
		VehicleJourneyAtStop vjas2 = new VehicleJourneyAtStop();

		vjas1.setDepartureDayOffset(0);
		vjas1.setDepartureTime(new Time(15, 10, 00));

		vjas2.setArrivalDayOffset(0);
		vjas2.setArrivalTime(new Time(15, 10, 00));

		int res = comparator.compare(vjas1, vjas2);
		Assert.assertEquals(res, 0, "passing times should be equals");

		vjas2.setArrivalTime(new Time(15, 10, 01));
		res = comparator.compare(vjas1, vjas2);
		Assert.assertTrue(res < 0, "passing times should be good");

		vjas2.setArrivalTime(new Time(15, 9, 59));
		res = comparator.compare(vjas1, vjas2);
		Assert.assertTrue(res > 0, "passing times should be bad");

		vjas2.setArrivalDayOffset(1);
		res = comparator.compare(vjas1, vjas2);
		Assert.assertTrue(res < 0, "passing times should be good");

		vjas1.setDepartureDayOffset(1);
		vjas2.setArrivalDayOffset(0);
		vjas2.setArrivalTime(new Time(15, 10, 01));
		res = comparator.compare(vjas1, vjas2);
		Assert.assertTrue(res > 0, "passing times should be bad");
	}

	@Test(groups = { "ServiceJourney" }, description = "missing journeyPattern", priority = 1302)
	public void validateMissingJourneyPattern() throws Exception {
		Context context = initImportContext();
		VehicleJourney journey = new VehicleJourney();
		journey.setObjectId("CITYWAY:ServiceJourney:1234:LOC");
		ServiceJourneyValidator validator = (ServiceJourneyValidator) ValidatorFactory.getValidator(context,
				ServiceJourneyValidator.class);

		boolean res = validator.check2NeTExSTIFServiceJourney1(context, journey, 1, 2);
		Assert.assertFalse(res, "validation should be not ok");
		checkReports(context, "offre_xxx.xml", NetexCheckPoints.L2_NeTExSTIF_ServiceJourney_1,
				"2_netexstif_servicejourney_1", null, FILE_STATE.ERROR);

	}

	@Test(groups = { "ServiceJourney" }, description = "too many train numbers", priority = 1303)
	public void validateTooManyTrainNumbers() throws Exception {

		Context context = initImportContext();
		VehicleJourney journey = buildVehicleJourney();
		ServiceJourneyValidator validator = (ServiceJourneyValidator) ValidatorFactory.getValidator(context,
				ServiceJourneyValidator.class);
		validator.addTrainNumberRef(context, journey.getObjectId(), "ref1");
		validator.addTrainNumberRef(context, journey.getObjectId(), "ref2");
		boolean res = validator.check2NeTExSTIFServiceJourney2(context, journey, 1, 2);
		Assert.assertFalse(res, "validation should be not ok");
		checkReports(context, "offre_xxx.xml", NetexCheckPoints.L2_NeTExSTIF_ServiceJourney_2,
				"2_netexstif_servicejourney_2", null, FILE_STATE.ERROR);

	}

	@Test(groups = { "ServiceJourney" }, description = "wrong passingTime count", priority = 1304)
	public void validateWrongPassingTimeCount() throws Exception {
		Context context = initImportContext();
		ServiceJourneyValidator validator = (ServiceJourneyValidator) ValidatorFactory.getValidator(context,
				ServiceJourneyValidator.class);
		VehicleJourney journey = buildVehicleJourney();
		journey.getVehicleJourneyAtStops().remove(1);
		boolean res = validator.check2NeTExSTIFServiceJourney3(context, journey, 1, 2);
		Assert.assertFalse(res, "validation should be not ok");
		checkReports(context, "offre_xxx.xml", NetexCheckPoints.L2_NeTExSTIF_ServiceJourney_3,
				"2_netexstif_servicejourney_3", "1", FILE_STATE.ERROR);

	}

	@SuppressWarnings("deprecation")
	@Test(groups = { "ServiceJourney" }, description = "wrong passingTime times", priority = 1305)
	public void validateWrongPassingTimes() throws Exception {
		Context context = initImportContext();
		ServiceJourneyValidator validator = (ServiceJourneyValidator) ValidatorFactory.getValidator(context,
				ServiceJourneyValidator.class);
		VehicleJourney journey = buildVehicleJourney();
		VehicleJourneyAtStop vjas1 = journey.getVehicleJourneyAtStops().get(0);
		VehicleJourneyAtStop vjas2 = journey.getVehicleJourneyAtStops().get(1);
		vjas1.setDepartureDayOffset(0);
		vjas1.setDepartureTime(new Time(15, 10, 00));
		vjas2.setArrivalDayOffset(0);
		vjas2.setArrivalTime(new Time(15, 9, 00));

		boolean res = validator.check2NeTExSTIFServiceJourney4(context, journey, 1, 2);
		Assert.assertFalse(res, "validation should be not ok");
		checkReports(context, "offre_xxx.xml", NetexCheckPoints.L2_NeTExSTIF_ServiceJourney_4,
				"2_netexstif_servicejourney_4", "1", FILE_STATE.ERROR);

	}

}
