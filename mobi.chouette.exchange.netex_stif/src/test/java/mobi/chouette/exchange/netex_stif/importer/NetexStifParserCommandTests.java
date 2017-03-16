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

import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.importer.ParserUtils;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.JobDataTest;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ReportConstant;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Line;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.type.DayTypeEnum;
import mobi.chouette.model.type.PTDirectionEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;
import mobi.chouette.persistence.hibernate.ContextHolder;

public class NetexStifParserCommandTests implements Constant, ReportConstant {

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
		context.put("testng", "true");
		context.put(OPTIMIZED, Boolean.FALSE);
		initLines(context);
		return context;

	}

	private void initLines(Context context) {
		Referential referential = (Referential) context.get(REFERENTIAL);
		Line line = ObjectFactory.getLine(referential, "STIF:CODIFLIGNE:Line:12234");
		line.setObjectId("STIF:CODIFLIGNE:Line:12234");
	}

	//@Test(groups = { "Nominal" }, description = "offre")
	public void verifiyOfferParser() throws Exception {
		Context context = initImportContext();

		NetexStifParserCommand parser = (NetexStifParserCommand) CommandFactory.create(initialContext,
				NetexStifParserCommand.class.getName());
		File f = new File(path, "offre.xml");
		Referential referential = (Referential) context.get(REFERENTIAL);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);
		parser.setFileURL("file://" + f.getAbsolutePath());
		parser.execute(context);
		assertRoute(referential, "CITYWAY:Route:1:LOC", "route 1", "STIF:CODIFLIGNE:Line:12234", PTDirectionEnum.A,
				"Par ici", "CITYWAY:Route:2:LOC");
		assertRoute(referential, "CITYWAY:Route:2:LOC", "route 2", "STIF:CODIFLIGNE:Line:12234", PTDirectionEnum.R,
				"Par là", "CITYWAY:Route:1:LOC");

		assertJourneyPattern(referential, "CITYWAY:ServiceJourneyPattern:1:LOC", "Par ici", "Mission 1", "1234",
				"CITYWAY:Route:1:LOC");
		assertJourneyPattern(referential, "CITYWAY:ServiceJourneyPattern:2:LOC", "Par là", "Mission 2", "2345",
				"CITYWAY:Route:2:LOC");
		assertVehicleJourney(referential, "CITYWAY:ServiceJourney:1-1:LOC", "Course 1 par ici",
				"CITYWAY:ServiceJourneyPattern:1:LOC", "STIF:CODIFLIGNE:Operator:1:LOC", "1234");
		assertVehicleJourneyAtStop(referential, "CITYWAY:ServiceJourney:1-1:LOC", "01:01:00.000", 0, "01:01:00.000", 0);
		assertVehicleJourneyAtStop(referential, "CITYWAY:ServiceJourney:1-1:LOC", "01:05:00.000", 0, "01:05:00.000", 0);
		assertStopPoint(referential, "CITYWAY:ScheduledStopPoint:1-1:LOC:1", 1, "FR:78217:ZDE:18304:STIF", "CITYWAY:Route:1:LOC");
		assertStopPoint(referential, "CITYWAY:ScheduledStopPoint:1-2:LOC:2", 2, "FR:78217:ZDE:32521:STIF", "CITYWAY:Route:1:LOC");
		assertStopPoint(referential, "CITYWAY:ScheduledStopPoint:2-1:LOC:1", 1, "FR:78217:ZDE:32522:STIF", "CITYWAY:Route:2:LOC");
		assertStopPoint(referential, "CITYWAY:ScheduledStopPoint:2-2:LOC:2", 2, "FR:78217:ZDE:18305:STIF", "CITYWAY:Route:2:LOC");
	}

	private void assertStopPoint(Referential referential, String id, int position, String quayRef, String routeId) {
		StopPoint stopPoint = ObjectFactory.getStopPoint(referential, id);
		Assert.assertEquals(stopPoint.getPosition(), new Integer(position));
		Assert.assertEquals(stopPoint.getRoute().getObjectId(), routeId);
		Assert.assertEquals(stopPoint.getContainedInStopArea().getObjectId(), quayRef);
	}

	/// Warning FLA : on considère arriaval time unique pour un vehicleJourney
	private void assertVehicleJourneyAtStop(Referential referential, String vehicleJourneyId, String arrivalTimeStr,
			int arrivalDayOffset, String departureTimeStr, int departureDayOffset) throws ParseException {
		Time arrivalTime = ParserUtils.getSQLTime(arrivalTimeStr);
		Time departureTime = ParserUtils.getSQLTime(departureTimeStr);
		VehicleJourney vehicleJourney = ObjectFactory.getVehicleJourney(referential, vehicleJourneyId);
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

	private void assertVehicleJourney(Referential referential, String id, String publishedName, String journeyPatternId,
			String companyId, String publishedJourneyIdentifier) {
		VehicleJourney vehicleJourney = ObjectFactory.getVehicleJourney(referential, id);
		Assert.assertEquals(vehicleJourney.getPublishedJourneyName(), publishedName);
		Assert.assertEquals(vehicleJourney.getJourneyPattern().getObjectId(), journeyPatternId);
		//Assert.assertEquals(vehicleJourney.getCompanyId(), companyId);
		Assert.assertEquals(vehicleJourney.getPublishedJourneyIdentifier(), publishedJourneyIdentifier);

	}

	private void assertJourneyPattern(Referential referential, String id, String name, String publishedName,
			String registrationNumber, String routeId) {
		JourneyPattern journeyPattern = ObjectFactory.getJourneyPattern(referential, id);
		Assert.assertEquals(journeyPattern.getName(), name);
		Assert.assertEquals(journeyPattern.getPublishedName(), publishedName);
		Assert.assertEquals(journeyPattern.getRegistrationNumber(), registrationNumber);
		Assert.assertEquals(journeyPattern.getRoute().getObjectId(), routeId);
	}

	private void assertRoute(Referential referential, String id, String name, String lineRef, PTDirectionEnum dir,
			String publishedName, String inverse) {
		Route route = ObjectFactory.getRoute(referential, id);
		Assert.assertEquals(route.getName(), name);
		//Assert.assertEquals(route.getLineId(), lineRef);
		Assert.assertEquals(route.getDirection(), dir);
		Assert.assertEquals(route.getPublishedName(), publishedName);
		Assert.assertEquals(route.getOppositeRoute().getObjectId(), inverse);

	}

	//@Test(groups = { "Nominal" }, description = "commun")
	public void verifiyCommunParser() throws Exception {
		Context context = initImportContext();

		NetexStifParserCommand parser = (NetexStifParserCommand) CommandFactory.create(initialContext,
				NetexStifParserCommand.class.getName());
		File f = new File(path, "commun.xml");
		parser.setFileURL("file://" + f.getAbsolutePath());
		parser.execute(context);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);

		assertNotice(factory, "CITYWAY:Notice:1:LOC", "Notice 1", "1");
		assertNotice(factory, "CITYWAY:Notice:2:LOC", "Notice 2", "2");
		assertNotice(factory, "CITYWAY:Notice:3:LOC", "Notice 3", "3");

	}

	private void assertNotice(NetexStifObjectFactory nsof, String id, String text, String publicCode) {
		Footnote fn = nsof.getFootnote(id);
		Assert.assertEquals(fn.getLabel(), text);
		Assert.assertEquals(fn.getCode(), publicCode);

	}

	//@Test(groups = { "Nominal" }, description = "calendrier")
	public void verifiyCalendrierParser() throws Exception {
		Context context = initImportContext();

		NetexStifParserCommand parser = (NetexStifParserCommand) CommandFactory.create(initialContext,
				NetexStifParserCommand.class.getName());
		File f = new File(path, "calendrier.xml");
		parser.setFileURL("file://" + f.getAbsolutePath());
		parser.execute(context);
		Referential referential = (Referential) context.get(REFERENTIAL);
		assertTimetable(referential, "CITYWAY:DayType:1:LOC", "Semaine", "Monday,Tuesday,Wednesday,Thursday,Friday");
		assertTimetable(referential, "CITYWAY:DayType:2:LOC", "Fin de semaine", "Saturday,Sunday");
		assertTimetable(referential, "CITYWAY:DayType:3:LOC", "Service spécial", null);
		assertTimetable(referential, "CITYWAY:DayType:4:LOC", "Restriction", null);

	}

	private void assertTimetable(Referential referential, String id, String comment, String daytypes) {
		Timetable timetable = ObjectFactory.getTimetable(referential, id);
		if (daytypes != null) {
			String[] types = daytypes.split(",");
			List<DayTypeEnum> list = timetable.getDayTypes();
			Assert.assertEquals(list.size(), types.length);
			for (String tmp : types) {
				Assert.assertTrue(list.contains(DayTypeEnum.valueOf(tmp)), "Daytype " + tmp + " not found");
			}
		}

		Assert.assertEquals(timetable.getComment(), comment);
	}
}
