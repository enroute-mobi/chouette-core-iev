package mobi.chouette.exchange.netex_stif.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.BasicConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.FILE_STATE;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Line;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.type.AlightingPossibilityEnum;
import mobi.chouette.model.type.BoardingPossibilityEnum;

@Log4j
public class RouteValidatorTests extends AbstractTest {

	public static String TEST_FILENAME = "offre_xxx.xml";

	@Getter
	@Setter
	class TestContext {
		RouteValidator routeValidator;
		Context context = new Context();
		ValidationReport validationReport = new ValidationReport();
		ActionReport actionReport = new ActionReport();
		Route fakeRoute = new Route();
		ServiceJourneyPatternValidator serviceJourneyPatternValidator;

		public TestContext() {
			context.put(Constant.REPORT, actionReport);
			context.put(Constant.VALIDATION_REPORT, validationReport);
			context.put(Constant.FILE_NAME, TEST_FILENAME);

			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addFileReport(context, TEST_FILENAME, IO_TYPE.INPUT);

			routeValidator = (RouteValidator) ValidatorFactory.getValidator(context, RouteValidator.class);
			serviceJourneyPatternValidator = (ServiceJourneyPatternValidator) ValidatorFactory.getValidator(context,
					ServiceJourneyPatternValidator.class);

			Line fakeLine = new Line();
			fakeLine.setObjectId("STIF:line:1234:LOC");
			fakeRoute.setLine(fakeLine);
			fakeRoute.setObjectId("CITYWAY:Route:1234:LOC");
		}

		public void addStopPointAlighting(String objectId, Integer position, Boolean alighting) {
			serviceJourneyPatternValidator.addStopPointAlighting(context, objectId, position, alighting);
		}

		public void addStopPointBoarding(String objectId, Integer position, Boolean boarding) {
			serviceJourneyPatternValidator.addStopPointBoarding(context, objectId, position, boarding);
		}

		boolean result = false;

	}

	@BeforeSuite
	public void init() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		Locale.setDefault(Locale.ENGLISH);
	}

	private void validateRouteDirectionType(TestContext tc, String directionType) {

		int lineNumber = 1;
		int columnNumber = 2;

		tc.getFakeRoute().setWayback(directionType);

		RouteValidator validator = tc.getRouteValidator();

		boolean result = validator.check2NeTExSTIFRoute1(tc.getContext(), tc.getFakeRoute(), lineNumber, columnNumber);
		log.info("Validation Report ===>" + tc.getValidationReport().toString());
		log.info("Validation Report Result = " + tc.getValidationReport().getResult());
		log.info("Action Report ===>" + tc.getActionReport().toString());
		log.info("Action Report Result = " + tc.getActionReport().getResult());

		tc.setResult(result);
	}

	@Test(groups = { "Route",
			"DirectionType" }, description = "Cas erreur 1: Route DirectionType incorrect : neither 'inboud', nor 'outbound' ", priority = 1001)
	public void verifyRouteDirectionTypeIncorrect() throws Exception {
		String directionType = "xxxx";
		TestContext tc = new TestContext();
		validateRouteDirectionType(tc, directionType);
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Route_1, "2_netexstif_route_1",
				"xxxx", FILE_STATE.ERROR);
	}

	@Test(groups = { "Route",
			"DirectionType" }, description = "Cas erreur 2 : Route DirectionType incorrect (null) ", priority = 1002)
	public void verifyRouteDirectionTypeNull() throws Exception {
		String directionType = null;
		TestContext tc = new TestContext();
		validateRouteDirectionType(tc, directionType);

		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Route_1, "2_netexstif_route_1",
				"null", FILE_STATE.ERROR);

	}

	@Test(groups = { "Route",
			"DirectionType" }, description = "Cas nominal 1 : Route DirectionType is inbound", priority = 1003)
	public void verifyRouteDirectionTypeWithInbound() throws Exception {
		String directionType = NetexStifConstant.DIRECTION_INBOUND;
		TestContext tc = new TestContext();
		validateRouteDirectionType(tc, directionType);

		Assert.assertTrue(tc.isResult());
		checkNoReports(tc.getContext(), TEST_FILENAME);
	}

	@Test(groups = { "Route",
			"DirectionType" }, description = "Cas nominal 2 : Route DirectionType is outbound", priority = 1004)
	public void verifyRouteDirectionTypeWithOutbound() throws Exception {
		String directionType = NetexStifConstant.DIRECTION_OUTBOUND;
		TestContext tc = new TestContext();
		validateRouteDirectionType(tc, directionType);
		Assert.assertTrue(tc.isResult());
		checkNoReports(tc.getContext(), TEST_FILENAME);
	}

	private TestContext validateInverseRouteRef(TestContext tc, Route oppositeRoute) {

		int lineNumber = 1;
		int columnNumber = 2;

		tc.getFakeRoute().setOppositeRoute(oppositeRoute);

//		tc.getRouteValidator().addXmlId(tc.getContext(), tc.getFakeRoute().getObjectId(),
//				tc.getFakeRoute().getObjectId());
//		if (oppositeRoute != null)
//			tc.getRouteValidator().addXmlId(tc.getContext(), oppositeRoute.getObjectId(),
//					oppositeRoute.getObjectId());
		boolean result = tc.getRouteValidator().check2NeTExSTIFRoute2_1(tc.getContext(), tc.getFakeRoute(), lineNumber,
				columnNumber);
		log.info("Validation Report ===>" + tc.getValidationReport().toString());
		log.info("Validation Report Result = " + tc.getValidationReport().getResult());
		log.info("Action Report ===>" + tc.getActionReport().toString());
		log.info("Action Report Result = " + tc.getActionReport().getResult());

		tc.setResult(result);
		return tc;
	}

	@Test(groups = { "Route",
			"InverseRouteRef" }, description = "Error 1 : Opposite Route of a given Route is NOT correct ", priority = 1005)
	public void verifyRouteInverseRouteRefIncorrect() throws Exception {
		Route oppositeRoute = new Route();
		oppositeRoute.setId(System.currentTimeMillis());
		oppositeRoute.setOppositeRoute(oppositeRoute);
		oppositeRoute.setObjectId("Codespace:type:identifierAABB:LOC");
		oppositeRoute.setFilled(true);
		TestContext tc = new TestContext();
		tc.getRouteValidator().addInverseRouteRef(tc.getContext(), oppositeRoute.getObjectId(),
				"Codespace:type:identifierAABB_INCORRECT:LOC");
		validateInverseRouteRef(tc, oppositeRoute);
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Route_2, "2_netexstif_route_2_1",
				"Codespace:type:identifierAABB:LOC", FILE_STATE.WARNING);
	}

	@Test(groups = { "Route", "InverseRouteRef" }, description = "Nominal 1 : No Opposite Route ", priority = 1006)
	public void verifyRouteInverseRouteRefOnNoOppositeRoute() throws Exception {
		TestContext tc = new TestContext();
		validateInverseRouteRef(tc, null);
		Assert.assertTrue(tc.isResult());
	}

	@Test(groups = { "Route",
			"InverseRouteRef" }, description = "Nominal 2 : Opposite Route of a given Route is correct ", priority = 1007)
	public void verifyRouteInverseRouteRefCorrect() throws Exception {

		Route oppositeRoute = new Route();
		oppositeRoute.setId(System.currentTimeMillis());
		oppositeRoute.setOppositeRoute(oppositeRoute);

		TestContext tc = new TestContext();
		oppositeRoute.setObjectId("Codespace:type:identifierAABB:LOC");
		tc.getRouteValidator().addInverseRouteRef(tc.getContext(), oppositeRoute.getObjectId(),
				tc.getFakeRoute().getObjectId());

		validateInverseRouteRef(tc, oppositeRoute);

		Assert.assertTrue(tc.isResult());
	}

	private TestContext validateOppositeRouteWaybackValue(Route oppositeRoute, String wayback) {

		int lineNumber = 1;
		int columnNumber = 2;

		TestContext tc = new TestContext();
		tc.getFakeRoute().setId(System.currentTimeMillis());

		tc.getFakeRoute().setOppositeRoute(oppositeRoute);
		tc.getFakeRoute().setWayback(wayback);

		RouteValidator validator = tc.getRouteValidator();

		boolean result = validator.check2NeTExSTIFRoute2_2(tc.getContext(), tc.getFakeRoute(), lineNumber,
				columnNumber);
		log.info("Validation Report ===>" + tc.getValidationReport().toString());
		log.info("Validation Report Result = " + tc.getValidationReport().getResult());
		log.info("Action Report ===>" + tc.getActionReport().toString());
		log.info("Action Report Result = " + tc.getActionReport().getResult());

		tc.setResult(result);
		return tc;
	}

	@Test(groups = { "Route",
			"InverseRouteRef" }, description = "Error : Opposite Route of a given Route is not correct ", priority = 1008)
	public void verifyInverseWaybackValueIncorrect() throws Exception {
		// -- error : 'inbound' twice
		Route oppositeRoute = new Route();
		oppositeRoute.setObjectId("Codespace:type:identifierAABB:LOC");
		oppositeRoute.setWayback(NetexStifConstant.DIRECTION_INBOUND);
		oppositeRoute.setFilled(true);
		TestContext tc = validateOppositeRouteWaybackValue(oppositeRoute, NetexStifConstant.DIRECTION_INBOUND);
		Assert.assertFalse(tc.isResult(), "error : 'inbound' twice");
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Route_2, "2_netexstif_route_2_2",
				oppositeRoute.getObjectId(), FILE_STATE.WARNING);
		//
		// -- error : 'outbound' twice
		oppositeRoute.setWayback(NetexStifConstant.DIRECTION_OUTBOUND);
		tc = validateOppositeRouteWaybackValue(oppositeRoute, NetexStifConstant.DIRECTION_OUTBOUND);
		Assert.assertFalse(tc.isResult(), "error : 'outbound' twice");
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Route_2, "2_netexstif_route_2_2",
				oppositeRoute.getObjectId(), FILE_STATE.WARNING);
		//
		// -- error : 'outbound' twice, default value for route wayback (null)
		// is 'outbound'
		oppositeRoute.setWayback(null);
		tc = validateOppositeRouteWaybackValue(oppositeRoute, null);
		Assert.assertFalse(tc.isResult(),
				"error : 'outbound' twice, default value for route wayback (null) is 'outbound'");
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Route_2, "2_netexstif_route_2_2",
				oppositeRoute.getObjectId(), FILE_STATE.WARNING);
	}

	@Test(groups = { "Route", "InverseRouteRef" }, description = "Nominal : No Opposite Route ", priority = 1009)
	public void verifyInverseWaybackValueOK() throws Exception {
		Route oppositeRoute = new Route();
		oppositeRoute.setId(System.currentTimeMillis());
		oppositeRoute.setWayback(NetexStifConstant.DIRECTION_INBOUND);
		TestContext tc = validateOppositeRouteWaybackValue(oppositeRoute, null);
		Assert.assertTrue(tc.isResult());
		checkNoReports(tc.getContext(), TEST_FILENAME);
		//
		oppositeRoute = new Route();
		oppositeRoute.setId(System.currentTimeMillis());
		oppositeRoute.setWayback(null);
		tc = validateOppositeRouteWaybackValue(oppositeRoute, NetexStifConstant.DIRECTION_INBOUND);
		Assert.assertTrue(tc.isResult());
		checkNoReports(tc.getContext(), TEST_FILENAME);
		//
		oppositeRoute = new Route();
		oppositeRoute.setId(System.currentTimeMillis());
		oppositeRoute.setWayback(null);
		tc = validateOppositeRouteWaybackValue(oppositeRoute, NetexStifConstant.DIRECTION_INBOUND);
		Assert.assertTrue(tc.isResult());
		checkNoReports(tc.getContext(), TEST_FILENAME);
	}

	// TODO : reprendre le test en mettant 2 missions qui se contredisent !
	/*
	 * StopPoint Order
	 */

//	private TestContext validateRouteStopPointOrder(List<StopPoint> list) {
//		int lineNumber = 0;
//		int columnNumber = 0;
//		TestContext tc = new TestContext();
//		tc.getFakeRoute().setStopPoints(list);
//		RouteValidator validator = tc.getRouteValidator();
//		boolean result = validator.check2NeTExSTIFRoute3(tc.getContext(), tc.getFakeRoute(), lineNumber, columnNumber);
//		log.info("Validation Report ===>" + tc.getValidationReport().toString());
//		log.info("Validation Report Result = " + tc.getValidationReport().getResult());
//		log.info("Action Report ===>" + tc.getActionReport().toString());
//		log.info("Action Report Result = " + tc.getActionReport().getResult());
//		tc.setResult(result);
//		return tc;
//	}

//	@Test(groups = { "Route",
//			"StopPointOrder" }, description = "Error 1 : StopPoint Order in Route is incorrect  ", priority = 1)
//	public void verifyRouteStopPointOrderIncorrect() throws Exception {
//
//		List<StopPoint> list = new ArrayList<StopPoint>(
//				Arrays.asList(StopPointBuilder.newInstance().id(0L).position(10).build(),
//						StopPointBuilder.newInstance().id(1L).position(20).build(),
//						StopPointBuilder.newInstance().id(2L).position(30).build(),
//						StopPointBuilder.newInstance().id(3L).position(60).build(), // --incorrect
//																					// position
//						StopPointBuilder.newInstance().id(4L).position(50).build()));
//		TestContext tc = validateRouteStopPointOrder(list);
//		Assert.assertFalse(tc.isResult());
//	}
//
//	@Test(groups = { "Route",
//			"StopPointOrder" }, description = "Nominal 1 : StopPoint Order in Route is correct ", priority = 1)
//	public void verifyRouteStopPointOrderOK() throws Exception {
//		List<StopPoint> list = new ArrayList<StopPoint>(
//				Arrays.asList(StopPointBuilder.newInstance().id(0L).position(10).build(),
//						StopPointBuilder.newInstance().id(1L).position(20).build(),
//						StopPointBuilder.newInstance().id(2L).position(30).build(),
//						StopPointBuilder.newInstance().id(3L).position(50).build(),
//						StopPointBuilder.newInstance().id(4L).position(60).build()));
//
//		TestContext tc = validateRouteStopPointOrder(list);
//		Assert.assertTrue(tc.isResult());
//
//	}

	static class StopPointBuilder {
		protected Long id;
		private Integer position;
		private AlightingPossibilityEnum forAlighting = AlightingPossibilityEnum.normal;
		private BoardingPossibilityEnum forBoarding = BoardingPossibilityEnum.normal;
		private String oid;
		private String comment;

		public static StopPointBuilder newInstance() {
			return new StopPointBuilder();
		}

		public StopPointBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public StopPointBuilder objectId(String oid) {
			this.oid = oid;
			return this;
		}

		public StopPointBuilder comment(String comment) {
			this.comment = comment;
			return this;
		}

		public StopPointBuilder forBoarding(boolean b) {
			this.forBoarding = b ? BoardingPossibilityEnum.normal : BoardingPossibilityEnum.forbidden;
			return this;
		}

		public StopPointBuilder forAlighting(boolean b) {
			this.forAlighting = b ? AlightingPossibilityEnum.normal : AlightingPossibilityEnum.forbidden;
			return this;
		}

		public StopPointBuilder position(Integer pos) {
			this.position = pos;
			return this;
		}

		public StopPoint build() {
			StopPoint p = new StopPoint();
			p.setObjectId(this.oid);
			p.setId(this.id);
			p.setPosition(this.position);
			p.setComment(this.comment);
			p.setForAlighting(this.forAlighting);
			p.setForBoarding(this.forBoarding);
			return p;
		}
	}

	/*
	 * StopPoint Alighting/Boarding in route match those in JourneyPatterns
	 * NeTExSTIF_Route_4
	 */

	private void validateRouteStopPointAlightingBoarding(TestContext tc, List<StopPoint> list) {
		int lineNumber = 1;
		int columnNumber = 2;
		tc.getFakeRoute().setStopPoints(list);
		RouteValidator validator = tc.getRouteValidator();
        Assert.assertNotNull(validator,"validator");
		boolean result = validator.check2NeTExSTIFRoute4(tc.getContext(), tc.getFakeRoute(), lineNumber, columnNumber);
		log.info("Validation Report ===>" + tc.getValidationReport().toString());
		log.info("Validation Report Result = " + tc.getValidationReport().getResult());
		log.info("Action Report ===>" + tc.getActionReport().toString());
		log.info("Action Report Result = " + tc.getActionReport().getResult());
		tc.setResult(result);
	}

	@Test(groups = { "Route",
			"AlightingBoarding" }, description = "Error 1 : AlightingBoarding in Route vs JourneyPattern is incorrect  ", priority = 1010)
	public void verifyRouteStopPointAlightingBoardingIncorrect() throws Exception {

		List<StopPoint> list = new ArrayList<StopPoint>(Arrays.asList(
				StopPointBuilder.newInstance().id(0L).objectId("CSP:TYP:10:LOC").comment("p10").position(10)
						.forAlighting(false).forBoarding(true).build(),
				StopPointBuilder.newInstance().id(1L).objectId("CSP:TYP:20:LOC").comment("p20").position(20)
						.forAlighting(false).forBoarding(true).build(),
				StopPointBuilder.newInstance().id(2L).objectId("CSP:TYP:30:LOC").comment("p30").position(30)
						.forAlighting(false).forBoarding(true).build(),
				StopPointBuilder.newInstance().id(3L).objectId("CSP:TYP:40:LOC").comment("p40").position(40)
						.forAlighting(false).forBoarding(true).build(),
				StopPointBuilder.newInstance().id(4L).objectId("CSP:TYP:50:LOC").comment("p50").position(50)
						.forAlighting(false).forBoarding(true).build()));
		TestContext tc = new TestContext();

		JourneyPattern jp1 = new JourneyPattern();
		jp1.setObjectId("Cityway:journeypattern:JP1_AABBCC:LOC");
		String obj = jp1.getObjectId();
		tc.addStopPointAlighting(obj, 10, false);
		tc.addStopPointAlighting(obj, 20, false);
		tc.addStopPointAlighting(obj, 30, false);
		tc.addStopPointAlighting(obj, 40, false);
		tc.addStopPointAlighting(obj, 50, false);
		//
		tc.addStopPointBoarding(obj, 10, true);
		tc.addStopPointBoarding(obj, 20, true);
		tc.addStopPointBoarding(obj, 30, true);
		tc.addStopPointBoarding(obj, 40, true);
		tc.addStopPointBoarding(obj, 50, true);

		JourneyPattern jp2 = new JourneyPattern();
		jp2.setObjectId("Cityway:journeypattern:JP2_AABBCC:LOC");
		obj = jp2.getObjectId();
		tc.addStopPointAlighting(obj, 10, false);
		tc.addStopPointAlighting(obj, 20, false);
		tc.addStopPointAlighting(obj, 30, false);
		tc.addStopPointAlighting(obj, 40, false);
		tc.addStopPointAlighting(obj, 50, false);
		//
		tc.addStopPointBoarding(obj, 10, true);
		tc.addStopPointBoarding(obj, 20, true);
		tc.addStopPointBoarding(obj, 30, true);
		tc.addStopPointBoarding(obj, 40, false); // incorrect
		tc.addStopPointBoarding(obj, 50, true);

		tc.getFakeRoute().getJourneyPatterns().add(jp1);
		tc.getFakeRoute().getJourneyPatterns().add(jp2);

		validateRouteStopPointAlightingBoarding(tc, list);
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Route_4, "2_netexstif_route_4",
				"CSP:TYP:40:LOC", FILE_STATE.WARNING);
	}

//	@Test(groups = { "Route",
//			"AlightingBoarding" }, description = "Nominal 1 : AlightingBoarding in Route vs JourneyPattern correct  ", priority = 1)
	public void verifyRouteStopPointAlightingBoardingcorrect() throws Exception {

		List<StopPoint> list = new ArrayList<StopPoint>(Arrays.asList(
				StopPointBuilder.newInstance().id(0L).comment("p10").position(10).forAlighting(false).forBoarding(true)
						.build(),
				StopPointBuilder.newInstance().id(1L).comment("p20").position(20).forAlighting(false).forBoarding(true)
						.build(),
				StopPointBuilder.newInstance().id(2L).comment("p30").position(30).forAlighting(false).forBoarding(true)
						.build(),
				StopPointBuilder.newInstance().id(3L).comment("p40").position(40).forAlighting(false).forBoarding(true)
						.build(),
				StopPointBuilder.newInstance().id(4L).comment("p50").position(50).forAlighting(false).forBoarding(true)
						.build()));
		TestContext tc = new TestContext();

		JourneyPattern jp1 = new JourneyPattern();
		jp1.setObjectId("Cityway:journeypattern:JP1_AABBCC:LOC");
		String obj = jp1.getObjectId();
		tc.addStopPointAlighting(obj, 10, false);
		tc.addStopPointAlighting(obj, 20, false);
		tc.addStopPointAlighting(obj, 30, false);
		tc.addStopPointAlighting(obj, 40, false);
		tc.addStopPointAlighting(obj, 50, false);
		//
		tc.addStopPointBoarding(obj, 10, true);
		tc.addStopPointBoarding(obj, 20, true);
		tc.addStopPointBoarding(obj, 30, true);
		tc.addStopPointBoarding(obj, 40, true);
		tc.addStopPointBoarding(obj, 50, true);

		JourneyPattern jp2 = new JourneyPattern();
		jp2.setObjectId("Cityway:journeypattern:JP2_AABBCC:LOC");
		obj = jp2.getObjectId();
		tc.addStopPointAlighting(obj, 10, false);
		tc.addStopPointAlighting(obj, 20, false);
		tc.addStopPointAlighting(obj, 30, false);
		tc.addStopPointAlighting(obj, 40, false);
		tc.addStopPointAlighting(obj, 50, false);
		//
		tc.addStopPointBoarding(obj, 10, true);
		tc.addStopPointBoarding(obj, 20, true);
		tc.addStopPointBoarding(obj, 30, true);
		tc.addStopPointBoarding(obj, 40, true);
		tc.addStopPointBoarding(obj, 50, true);

		tc.getFakeRoute().getJourneyPatterns().add(jp1);
		tc.getFakeRoute().getJourneyPatterns().add(jp2);

		validateRouteStopPointAlightingBoarding(tc, list);
		Assert.assertTrue(tc.isResult());
		checkNoReports(tc.getContext(), TEST_FILENAME);
	}

}
