package mobi.chouette.exchange.netex_stif.importer;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.BasicConfigurator;
import org.testng.Assert;
import org.testng.annotations.Test;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.importer.ParserUtils;
import mobi.chouette.exchange.netex_stif.JobDataImpl;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.CompanyLite;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.RoutingConstraint;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.type.DayTypeEnum;
import mobi.chouette.model.type.PTDirectionEnum;
import mobi.chouette.model.util.Referential;
import mobi.chouette.persistence.hibernate.ContextHolder;

@Log4j
public class NetexStifParserCommandTests  {

	private static final String path = "src/test/data/";

	protected static InitialContext initialContext;

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
		init();
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
		configuration.setReferentialId(1L);
		JobDataImpl test = new JobDataImpl();
		context.put(Constant.JOB_DATA, test);

		test.setPathName("target/referential/test");
		File f = new File("target/referential/test");
		if (f.exists())
			try {
				FileUtils.deleteDirectory(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		f.mkdirs();
		test.setReferential("chouette_gui");
		test.setAction(JobData.ACTION.importer);
		test.setType("netex_stif");
		context.put(Constant.TESTNG, "true");
		context.put(Constant.OPTIMIZED, Boolean.FALSE);
		initLines(context);
		return context;

	}

	private void initLines(Context context) {
		Referential referential = (Referential) context.get(Constant.REFERENTIAL);
		// Line line = ObjectFactory.getLine(referential,
		// "STIF:CODIFLIGNE:Line:12234");
		// line.setObjectId("STIF:CODIFLIGNE:Line:12234");
		LineLite line = new LineLite();
		line.setId(1L);
		line.setObjectId("STIF:CODIFLIGNE:Line:C00108");
		referential.getSharedReadOnlyLines().put(line.getObjectId(), line);
		context.put(Constant.LINE, line);
		CompanyLite company = new CompanyLite();
		company.setId(1L);
		company.setObjectId("STIF:CODIFLIGNE:Operator:011");
		referential.getSharedReadOnlyCompanies().put(company.getObjectId(), company);
		StopAreaLite quay1 = new StopAreaLite();
		quay1.setId(18304L);
		quay1.setObjectId("FR:78217:ZDE:18304:STIF");
		referential.getSharedReadOnlyStopAreas().put(quay1.getObjectId(), quay1);
		StopAreaLite quay2 = new StopAreaLite();
		quay2.setId(32522L);
		quay2.setObjectId("FR:78217:ZDE:32522:STIF");
		referential.getSharedReadOnlyStopAreas().put(quay2.getObjectId(), quay2);
		StopAreaLite quay3 = new StopAreaLite();
		quay3.setId(32521L);
		quay3.setObjectId("FR:78217:ZDE:32521:STIF");
		referential.getSharedReadOnlyStopAreas().put(quay3.getObjectId(), quay3);
		StopAreaLite quay4 = new StopAreaLite();
		quay4.setId(18305L);
		quay4.setObjectId("FR:78217:ZDE:18305:STIF");
		referential.getSharedReadOnlyStopAreas().put(quay4.getObjectId(), quay4);
	}

	@Test(groups = { "Nominal" }, description = "offre", priority = 303)
	public void verifyOfferParser() throws Exception{
		verifyOfferParser("calendriers.xml", "commun.xml", "offre.xml");
		
	}

	
	//@Test(groups = { "Nominal" }, description = "offre", priority = 3)
	public void verifyOfferParser(String calendrier, String commun, String offre) throws Exception {
		Context context = initImportContext();
        ActionReport report = (ActionReport) context.get(Constant.REPORT);
        ValidationReport valReport = (ValidationReport) context.get(Constant.VALIDATION_REPORT);
		NetexStifParserCommand parser = (NetexStifParserCommand) CommandFactory.create(initialContext,
				NetexStifParserCommand.class.getName());
		Referential referential = (Referential) context.get(Constant.REFERENTIAL);
		{
			File f = new File(path, calendrier);
			parser.setFileURL(f.toURI().toString());
			parser.execute(context);
			log.info(report);
			log.info(valReport.getCheckPointErrors());
			Assert.assertFalse(referential.getSharedTimetableTemplates().isEmpty(), " no timetables");
		}
		{
			File f = new File(path, "commun.xml");
			parser.setFileURL(f.toURI().toString());
			parser.execute(context);
			log.info(report);
			log.info(valReport.getCheckPointErrors());
			Assert.assertFalse(referential.getSharedFootnotes().isEmpty(), " no footnotes");
			Assert.assertFalse(referential.getSharedTimetableTemplates().isEmpty(), " no timetables");
		}
		File f = new File(path, "offre.xml");
		parser.setFileURL(f.toURI().toString());
		parser.execute(context);
		log.info(report);
		log.info(valReport.getCheckPointErrors());
		assertRoute(referential, "CITYWAY:Route:1:LOC", "CITYWAY:Route:1:LOC", "route 1", "STIF:CODIFLIGNE:Line:12234", PTDirectionEnum.A,
				"Par ici", "CITYWAY:Route:2:LOC");
		assertRoute(referential, "CITYWAY:Route:2:LOC", "CITYWAY:Route:2:LOC", "route 2", "STIF:CODIFLIGNE:Line:12234", PTDirectionEnum.R,
				"Par là", "CITYWAY:Route:1:LOC");

		assertJourneyPattern(referential, "CITYWAY:ServiceJourneyPattern:1:LOC", "CITYWAY:ServiceJourneyPattern:1:LOC", "Par ici", "Mission 1", "1234",
				"CITYWAY:Route:1:LOC");
		assertJourneyPattern(referential,  "CITYWAY:ServiceJourneyPattern:2:LOC","CITYWAY:ServiceJourneyPattern:2:LOC", "Par là", "Mission 2", "2345",
				"CITYWAY:Route:2:LOC");
		assertVehicleJourney(referential, "CITYWAY:ServiceJourney:1-1:LOC","CITYWAY:ServiceJourney:1-1:LOC", "Course 1 par ici",
				"CITYWAY:ServiceJourneyPattern:1:LOC", "STIF:CODIFLIGNE:Operator:1:LOC", "1234", 1, 1);
		assertVehicleJourneyAtStop(referential, "CITYWAY:ServiceJourney:1-1:LOC", "01:01:00.000", 0, "01:01:00.000", 0);
		assertVehicleJourneyAtStop(referential, "CITYWAY:ServiceJourney:1-1:LOC", "01:05:00.000", 0, "01:05:00.000", 0);
		assertStopPoint(referential, "CITYWAY:ScheduledStopPoint:1-1-1-1:LOC","", 1, 18304L, "CITYWAY:Route:1:LOC");
		assertStopPoint(referential, "CITYWAY:ScheduledStopPoint:1-1-2-2:LOC","", 2, 32521L, "CITYWAY:Route:1:LOC");
		assertStopPoint(referential, "CITYWAY:ScheduledStopPoint:2-2-1-1:LOC","", 1, 32522L, "CITYWAY:Route:2:LOC");
		assertStopPoint(referential, "CITYWAY:ScheduledStopPoint:2-2-2-2:LOC","", 2, 18305L, "CITYWAY:Route:2:LOC");
		
		assertRoutingConstraint(referential, "CITYWAY:RoutingConstraintZone:1-1:LOC", "ITL 1", "route 1","");
	}
	
	private void assertRoutingConstraint (Referential referential, String id, String name, String routeName, String stopPointsId){
		RoutingConstraint routingConstraint = referential.getRoutingConstraints().get(id);
		Assert.assertEquals(routingConstraint.getName(), name);
		Assert.assertEquals(routingConstraint.getRoute().getName(), routeName);
		
	}

	private void assertStopPoint(Referential referential, String id, String objectId, int position, Long quayRef, String routeId) {
		StopPoint stopPoint = referential.getStopPoints().get(id);
		if (stopPoint == null)
		{
			log.info("stopPointIds = "+referential.getStopPoints().keySet());
		}
		Assert.assertNotNull(stopPoint, " stopPoint id = " + id);
		Assert.assertEquals(stopPoint.getPosition(), new Integer(position));
		Assert.assertEquals(stopPoint.getRoute().getObjectId(), routeId);
		Assert.assertEquals(stopPoint.getStopAreaId(), quayRef);
	}

	/// Warning FLA : on considère arriaval time unique pour un vehicleJourney
	private void assertVehicleJourneyAtStop(Referential referential, String vehicleJourneyId, String arrivalTimeStr,
			Integer arrivalDayOffset, String departureTimeStr, Integer departureDayOffset) throws ParseException {
		Time arrivalTime = ParserUtils.getSQLTime(arrivalTimeStr);
		Time departureTime = ParserUtils.getSQLTime(departureTimeStr);
		VehicleJourney vehicleJourney = referential.getVehicleJourneys().get(vehicleJourneyId);
		Assert.assertNotNull(vehicleJourney, " vehicleJourney id = " + vehicleJourneyId);
		List<VehicleJourneyAtStop> list = vehicleJourney.getVehicleJourneyAtStops();
		boolean treat = false;
		for (VehicleJourneyAtStop vjas : list) {
			if (vjas.getArrivalTime().equals(arrivalTime)) {
				Assert.assertEquals(vjas.getArrivalDayOffset(), arrivalDayOffset);
				Assert.assertEquals(vjas.getDepartureDayOffset(), departureDayOffset);
				Assert.assertTrue(vjas.getDepartureTime().equals(departureTime),
						"Invalid departure time, has " + vjas.getDepartureTime() + " expected" + departureTime);
				treat = true;
			}
		}
		Assert.assertTrue(treat, "VehicleJourneyAtStop not found (arrivalTime :) :" + arrivalTimeStr);
	}

	private void assertVehicleJourney(Referential referential, String id, String objectId, String publishedName, String journeyPatternId,
			String companyId, String publishedJourneyIdentifier, int noteCount, int timetableCount) {
		VehicleJourney vehicleJourney = referential.getVehicleJourneys().get(id);
		Assert.assertNotNull(vehicleJourney, " vehicleJourney id = " + id);
		Assert.assertEquals(vehicleJourney.getPublishedJourneyName(), publishedName);
		Assert.assertEquals(vehicleJourney.getJourneyPattern().getObjectId(), journeyPatternId);
		// Assert.assertEquals(vehicleJourney.getCompanyId(), companyId);
		Assert.assertEquals(vehicleJourney.getPublishedJourneyIdentifier(), publishedJourneyIdentifier);
		Assert.assertEquals(vehicleJourney.getFootnotes().size(), noteCount,
				" vehicleJourney id = " + id + " notes count");
		Assert.assertEquals(vehicleJourney.getTimetables().size(), timetableCount,
				" vehicleJourney id = " + id + " timetables count");
		String tmId = vehicleJourney.getTimetables().get(0).getObjectId();
		Assert.assertEquals(tmId, "CITYWAY:DayType:1-without-4:LOC", "tm objectId");
	}

	private void assertJourneyPattern(Referential referential, String id, String objectId, String name, String publishedName,
			String registrationNumber, String routeId) {
		JourneyPattern journeyPattern = referential.getJourneyPatterns().get(id);
		Assert.assertNotNull(journeyPattern, " journeyPattern id = " + id);
		Assert.assertEquals(journeyPattern.getName(), name);
		Assert.assertEquals(journeyPattern.getPublishedName(), publishedName);
		Assert.assertEquals(journeyPattern.getRegistrationNumber(), registrationNumber);
		Assert.assertEquals(journeyPattern.getRoute().getObjectId(), routeId);
	}

	private void assertRoute(Referential referential, String id, String objectId, String name, String lineRef, PTDirectionEnum dir,
			String publishedName, String inverse) {
		Route route = referential.getRoutes().get(id);
		Assert.assertNotNull(route, " route id = " + id);
		Assert.assertEquals(route.getName(), name);
		// Assert.assertEquals(route.getLineId(), lineRef);
		Assert.assertEquals(route.getDirection(), dir);
		Assert.assertEquals(route.getPublishedName(), publishedName);
		Assert.assertEquals(route.getOppositeRoute().getObjectId(), inverse);

	}

	@Test(groups = { "Nominal" }, description = "commun", priority = 301)
	public void verifyCommunParser() throws Exception {
		try
		{
		Context context = initImportContext();

		NetexStifParserCommand parser = (NetexStifParserCommand) CommandFactory.create(initialContext,
				NetexStifParserCommand.class.getName());
		File f = new File(path, "commun.xml");
		parser.setFileURL(f.toURI().toString());
		parser.execute(context);
		Referential referential = (Referential) context.get(Constant.REFERENTIAL);
		assertNotice(referential, "CITYWAY:Notice:1:LOC", "Notice 1", "1");
		assertNotice(referential, "CITYWAY:Notice:2:LOC", "Notice 2", "2");
		assertNotice(referential, "CITYWAY:Notice:3:LOC", "Notice 3", "3");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
	}

	private void assertNotice(Referential referential, String id, String text, String publicCode) {
		Footnote fn = referential.getSharedFootnotes().get(id);
		Assert.assertNotNull(fn, " footnote id = " + id);
		Assert.assertEquals(fn.getLabel(), text);
		Assert.assertEquals(fn.getCode(), publicCode);

	}

	@Test(groups = { "Nominal" }, description = "calendrier", priority = 304)
	public void verifyCalendrierParser() throws Exception {
		Context context = initImportContext();

		NetexStifParserCommand parser = (NetexStifParserCommand) CommandFactory.create(initialContext,
				NetexStifParserCommand.class.getName());
		File f = new File(path, "calendriers.xml");
		parser.setFileURL(f.toURI().toString());
		parser.execute(context);
		Referential referential = (Referential) context.get(Constant.REFERENTIAL);
		assertTimetable(referential, "CITYWAY:DayType:1:LOC", "Semaine", "Monday,Tuesday,Wednesday,Thursday,Friday", 0,
				1);
		assertTimetable(referential, "CITYWAY:DayType:2:LOC", "Fin de semaine", "Saturday,Sunday", 0, 1);
		assertTimetable(referential, "CITYWAY:DayType:3:LOC", "Service spécial", null, 1, 0);
		assertTimetable(referential, "CITYWAY:DayType:4:LOC", "Restriction", null, 1, 0);

	}

	private void assertTimetable(Referential referential, String id, String comment, String daytypes, int dateSize,
			int periodSize) {
		Timetable timetable = referential.getSharedTimetableTemplates().get(id);
		Assert.assertNotNull(timetable, " timetable id = " + id);
		if (daytypes != null) {
			String[] types = daytypes.split(",");
			List<DayTypeEnum> list = timetable.getDayTypes();
			Assert.assertEquals(list.size(), types.length);
			for (String tmp : types) {
				Assert.assertTrue(list.contains(DayTypeEnum.valueOf(tmp)),
						"Timetable " + id + " Daytype " + tmp + " not found");
			}
		} else {
			Assert.assertTrue(timetable.getDayTypes().isEmpty(), "Timetable " + id + " no daytypes ");
		}

		Assert.assertEquals(timetable.getCalendarDays().size(), dateSize, "Timetable " + id + " CalendarDay size");
		Assert.assertEquals(timetable.getPeriods().size(), periodSize, "Timetable " + id + " period size");
	}
	
}
