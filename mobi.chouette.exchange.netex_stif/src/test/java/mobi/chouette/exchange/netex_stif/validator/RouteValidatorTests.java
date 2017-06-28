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
import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.FILE_STATE;
import mobi.chouette.exchange.report.FileReport;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.exchange.validation.report.CheckPointErrorReport;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.Line;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopPoint;

@Log4j
public class RouteValidatorTests implements Constant {

	public static String TEST_FILENAME = "offre_xxx.xml";

	@Getter
	@Setter
	class TestContext {
		RouteValidator validator;
		Context context = new Context();
		ValidationReport validationReport = new ValidationReport();
		ActionReport actionReport = new ActionReport();
		Route fakeRoute = new Route();

		public TestContext() {
			context.put(Constant.REPORT, actionReport);
			context.put(Constant.VALIDATION_REPORT, validationReport);
			context.put(Constant.FILE_NAME, TEST_FILENAME);

			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addFileReport(context, TEST_FILENAME, IO_TYPE.INPUT);

			validator = (RouteValidator) ValidatorFactory.getValidator(context, RouteValidator.class);

			Line fakeLine = new Line();
			fakeLine.setObjectId("STIF:line:1234:LOC");
			fakeRoute.setLine(fakeLine);
			fakeRoute.setObjectId("CITYWAY:Route:1234:LOC");
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

		tc.getFakeRoute().setWayBack(directionType);

		RouteValidator validator = tc.getValidator();

		boolean result = validator.check2NeTExSTIFRoute1(tc.getContext(), tc.getFakeRoute(), lineNumber, columnNumber);
		log.info("Validation Report ===>" + tc.getValidationReport().toString());
		log.info("Validation Report Result = " + tc.getValidationReport().getResult());
		log.info("Action Report ===>" + tc.getActionReport().toString());
		log.info("Action Report Result = " + tc.getActionReport().getResult());

		tc.setResult(result);
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

	private void checkReports(Context context, String fileName, String checkPointCode, String messageCode, String value,
			FILE_STATE fileState) {
		ActionReport report = (ActionReport) context.get(REPORT);

		ValidationReport valReport = (ValidationReport) context.get(VALIDATION_REPORT);
		log.info(report);
		log.info(valReport.getCheckPointErrors());
		Assert.assertEquals(report.getResult(), "OK", "result");
		Assert.assertEquals(report.getFiles().size(), 1, "file reported size ");
		FileReport file = report.getFiles().get(0);
		CheckPointErrorReport error = null;
		if (fileState.equals(FILE_STATE.ERROR)) {
			Assert.assertEquals(file.getStatus(), FILE_STATE.ERROR, "file status reported");
			Assert.assertEquals(file.getCheckPointErrorCount(), 1, "file error reported");
			error = valReport.getCheckPointErrors().get(file.getCheckPointErrorKeys().get(0).intValue());
		} else {
			Assert.assertEquals(file.getStatus(), FILE_STATE.OK, "file status reported");
			Assert.assertEquals(file.getCheckPointWarningCount(), 1, "file warning reported");
			error = valReport.getCheckPointErrors().get(file.getCheckPointWarningKeys().get(0).intValue());
		}
		Assert.assertEquals(error.getTestId(), checkPointCode, "checkpoint code");
		Assert.assertEquals(error.getKey(), messageCode, "message code");
		if (value == null)
			Assert.assertNull(error.getValue(), "value");
		else
			Assert.assertEquals(error.getValue(), value, "value");
		Assert.assertNotNull(error.getSource().getObjectId(), "source objectId");
		Assert.assertEquals(error.getSource().getFile().getFilename(), fileName, "source filename");
		Assert.assertEquals(error.getSource().getFile().getLineNumber(), Integer.valueOf(1), "source line number");
		Assert.assertEquals(error.getSource().getFile().getColumnNumber(), Integer.valueOf(2), "source column number");

	}


	@Test(groups = { "Route",
			"DirectionType" }, description = "Cas erreur 1: Route DirectionType incorrect : neither 'inboud', nor 'outbound' ", priority = 1)
	public void verifyRouteDirectionTypeIncorrect() throws Exception {
		String directionType = "xxxx";
		TestContext tc = new TestContext();
		validateRouteDirectionType(tc, directionType);
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Route_1, "2_netexstif_route_1",
				"xxxx", FILE_STATE.ERROR);
	}

	@Test(groups = { "Route",
			"DirectionType" }, description = "Cas erreur 2 : Route DirectionType incorrect (null) ", priority = 1)
	public void verifyRouteDirectionTypeNull() throws Exception {
		String directionType = null;
		TestContext tc = new TestContext();
		validateRouteDirectionType(tc, directionType);

		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Route_1, "2_netexstif_route_1",
				null, FILE_STATE.ERROR);

	}

	@Test(groups = { "Route",
			"DirectionType" }, description = "Cas nominal 1 : Route DirectionType is inbound", priority = 1)
	public void verifyRouteDirectionTypeWithInbound() throws Exception {

		String directionType = DIRECTION_INBOUND;
		TestContext tc = new TestContext();
		validateRouteDirectionType(tc, directionType);

		Assert.assertTrue(tc.isResult());
		checkNoReports(tc.getContext(), TEST_FILENAME);
	}

	@Test(groups = { "Route",
			"DirectionType" }, description = "Cas nominal 2 : Route DirectionType is outbound", priority = 1)
	public void verifyRouteDirectionTypeWithOutbound() throws Exception {
		String directionType = DIRECTION_OUTBOUND;
		TestContext tc = new TestContext();
		validateRouteDirectionType(tc, directionType);
		Assert.assertTrue(tc.isResult());
		checkNoReports(tc.getContext(), TEST_FILENAME);
	}

	private TestContext validateInverseRouteRef(TestContext tc, Route oppositeRoute) {

		int lineNumber = 1;
		int columnNumber = 2;

		tc.getFakeRoute().setOppositeRoute(oppositeRoute);

		boolean result = tc.getValidator().check2NeTExSTIFRoute2_1(tc.getContext(), tc.getFakeRoute(), lineNumber,
				columnNumber);
		log.info("Validation Report ===>" + tc.getValidationReport().toString());
		log.info("Validation Report Result = " + tc.getValidationReport().getResult());
		log.info("Action Report ===>" + tc.getActionReport().toString());
		log.info("Action Report Result = " + tc.getActionReport().getResult());

		tc.setResult(result);
		return tc;
	}

	@Test(groups = { "Route",
			"InverseRouteRef" }, description = "Error 1 : Opposite Route of a given Route is NOT correct ", priority = 1)
	public void verifyRouteInverseRouteRefIncorrect() throws Exception {
		Route oppositeRoute = new Route();
		oppositeRoute.setId(System.currentTimeMillis());
		oppositeRoute.setOppositeRoute(oppositeRoute);
		oppositeRoute.setObjectId("Codespace:type:identifierAABB:LOC");
		TestContext tc = new TestContext();
		tc.getValidator().addInverseRouteRef(tc.getContext(), oppositeRoute.getObjectId(),
				"Codespace:type:identifierAABB_INCORRECT:LOC");
		validateInverseRouteRef(tc, oppositeRoute);
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Route_2, "2_netexstif_route_2_1",
				"Codespace:type:identifierAABB:LOC", FILE_STATE.OK);
	}

	@Test(groups = { "Route", "InverseRouteRef" }, description = "Nominal 1 : No Opposite Route ", priority = 1)
	public void verifyRouteInverseRouteRefOnNoOppositeRoute() throws Exception {
		TestContext tc = new TestContext();
		validateInverseRouteRef(tc, null);
		Assert.assertTrue(tc.isResult());
	}

	@Test(groups = { "Route",
			"InverseRouteRef" }, description = "Nominal 2 : Opposite Route of a given Route is correct ", priority = 1)
	public void verifyRouteInverseRouteRefCorrect() throws Exception {

		Route oppositeRoute = new Route();
		oppositeRoute.setId(System.currentTimeMillis());
		oppositeRoute.setOppositeRoute(oppositeRoute);

		TestContext tc = new TestContext();
		oppositeRoute.setObjectId("Codespace:type:identifierAABB:LOC");
		tc.getValidator().addInverseRouteRef(tc.getContext(), oppositeRoute.getObjectId(),
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
		tc.getFakeRoute().setWayBack(wayback);

		RouteValidator validator = tc.getValidator();

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
			"InverseRouteRef" }, description = "Error : Opposite Route of a given Route is not correct ", priority = 1)
	public void verifyInverseWaybackValueIncorrect() throws Exception {
		// -- error : 'inbound' twice
		Route oppositeRoute = new Route();
		oppositeRoute.setObjectId("Codespace:type:identifierAABB:LOC");
		oppositeRoute.setWayBack(DIRECTION_INBOUND);
		TestContext tc = validateOppositeRouteWaybackValue(oppositeRoute, DIRECTION_INBOUND);
		Assert.assertFalse(tc.isResult(), "error : 'inbound' twice");
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Route_2, "2_netexstif_route_2_2",
				oppositeRoute.getObjectId(), FILE_STATE.OK);
		//
		// -- error : 'outbound' twice
		oppositeRoute.setWayBack(DIRECTION_OUTBOUND);
		tc = validateOppositeRouteWaybackValue(oppositeRoute, DIRECTION_OUTBOUND);
		Assert.assertFalse(tc.isResult(), "error : 'outbound' twice");
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Route_2, "2_netexstif_route_2_2",
				oppositeRoute.getObjectId(), FILE_STATE.OK);
		//
		// -- error : 'outbound' twice, default value for route wayback (null) is 'outbound'
		oppositeRoute.setWayBack(null);
		tc = validateOppositeRouteWaybackValue(oppositeRoute, null);
		Assert.assertFalse(tc.isResult(),
				"error : 'outbound' twice, default value for route wayback (null) is 'outbound'");
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Route_2, "2_netexstif_route_2_2",
				oppositeRoute.getObjectId(), FILE_STATE.OK);
	}

	@Test(groups = { "Route", "InverseRouteRef" }, description = "Nominal : No Opposite Route ", priority = 1)
	public void verifyInverseWaybackValueOK() throws Exception {
		Route oppositeRoute = new Route();
		oppositeRoute.setId(System.currentTimeMillis());
		oppositeRoute.setWayBack(DIRECTION_INBOUND);
		TestContext tc = validateOppositeRouteWaybackValue(oppositeRoute, null);
		Assert.assertTrue(tc.isResult());
		checkNoReports(tc.getContext(), TEST_FILENAME);
		//
		oppositeRoute = new Route();
		oppositeRoute.setId(System.currentTimeMillis());
		oppositeRoute.setWayBack(null);
		tc = validateOppositeRouteWaybackValue(oppositeRoute, DIRECTION_INBOUND);
		Assert.assertTrue(tc.isResult());
		checkNoReports(tc.getContext(),TEST_FILENAME);
		//
		oppositeRoute = new Route();
		oppositeRoute.setId(System.currentTimeMillis());
		oppositeRoute.setWayBack(null);
		tc = validateOppositeRouteWaybackValue(oppositeRoute, DIRECTION_INBOUND);
		Assert.assertTrue(tc.isResult());
		checkNoReports(tc.getContext(), TEST_FILENAME);
	}

	private TestContext validateRouteStopPointOrder(List<StopPoint> list) {
		int lineNumber = 0;
		int columnNumber = 0;
		TestContext tc = new TestContext();
		tc.getFakeRoute().setId(System.currentTimeMillis());
		tc.getFakeRoute().setStopPoints(list);
		RouteValidator validator = tc.getValidator();
		boolean result = validator.check2NeTExSTIFRoute3(tc.getContext(), tc.getFakeRoute(), lineNumber, columnNumber);
		log.info("Validation Report ===>" + tc.getValidationReport().toString());
		log.info("Validation Report Result = " + tc.getValidationReport().getResult());
		log.info("Action Report ===>" + tc.getActionReport().toString());
		log.info("Action Report Result = " + tc.getActionReport().getResult());
		tc.setResult(result);
		return tc;
	}

	@Test(groups = { "Route",
			"StopPointOrder" }, description = "Error 1 : StopPoint Order in Route is incorrect  ", priority = 1)
	public void verifyRouteStopPointOrderIncorrect() throws Exception {
		
		
		
		List<StopPoint> list = new ArrayList<StopPoint>(
				Arrays.asList(StopPointBuilder.newInstance().id(0L).position(10).build(),
						StopPointBuilder.newInstance().id(1L).position(20).build(),
						StopPointBuilder.newInstance().id(2L).position(30).build(),
						StopPointBuilder.newInstance().id(3L).position(60).build(), // --incorrect position
						StopPointBuilder.newInstance().id(4L).position(50).build()));
		TestContext tc = validateRouteStopPointOrder(list);
		Assert.assertFalse(tc.isResult());
	}

	@Test(groups = { "Route",
			"StopPointOrder" }, description = "Nominal 1 : StopPoint Order in Route is correct ", priority = 1)
	public void verifyRouteStopPointOrderOK() throws Exception {
		List<StopPoint> list = new ArrayList<StopPoint>(
				Arrays.asList(StopPointBuilder.newInstance().id(0L).position(10).build(),
						StopPointBuilder.newInstance().id(1L).position(20).build(),
						StopPointBuilder.newInstance().id(2L).position(30).build(),
						StopPointBuilder.newInstance().id(3L).position(50).build(),
						StopPointBuilder.newInstance().id(4L).position(60).build()));

		TestContext tc = validateRouteStopPointOrder(list);
		Assert.assertTrue(tc.isResult());

	}

	static class StopPointBuilder {
		protected Long id;
		private Integer position;

		public static StopPointBuilder newInstance() {
			return new StopPointBuilder();
		}

		public StopPointBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public StopPointBuilder position(Integer pos) {
			this.position = pos;
			return this;
		}

		public StopPoint build() {
			StopPoint p = new StopPoint();
			p.setId(this.id);
			p.setPosition(this.position);
			return p;
		}
	}

}
