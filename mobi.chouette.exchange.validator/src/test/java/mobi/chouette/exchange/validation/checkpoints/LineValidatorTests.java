package mobi.chouette.exchange.validation.checkpoints;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.dao.JourneyPatternDAO;
import mobi.chouette.dao.RouteDAO;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.exchange.validator.ValidationException;
import mobi.chouette.exchange.validator.checkpoints.CheckpointParameters;
import mobi.chouette.exchange.validator.checkpoints.LineValidator;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.util.Referential;

@Log4j
public class LineValidatorTests extends AbstractTestValidation {

	@EJB
	RouteDAO dao;

	@EJB
	JourneyPatternDAO jpDao;

	@Deployment
	public static EnterpriseArchive createDeployment() {

		return buildDeployment(LineValidatorTests.class);
	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "line" }, description = "3_Line_1", priority = 31)
	public void verifyTest_3_Line_1() throws Exception {
		log.info(Color.CYAN + " check " + L3_Line_1 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();

			TestContext tc = new TestContext(context, L3_Line_1);
			LineLite line = tc.getObjectForTest();
			Collection<Route> routes = tc.getReferential().getRoutes().values();

			// -- Nominal test
			tc.runValidation();
			checkNoReports(context, line.getObjectId());

			// -- Error test : no opposite route
			routes.stream().forEach(r -> {
				r.setLineLite(line);
				r.setOppositeRoute(null); // -- force no opposite route
				tc.getReferential().getRoutes().put(r.getObjectId(), r);
			});
			tc.runValidation();
			int warningCount = routes.size();
			checkReports(context, line.getObjectId(), tc.getCheckPointName(), tc.getFormattedCheckPointName(), null,
					OBJECT_STATE.WARNING, warningCount);

			// -- Error test : no route for line
			tc.getReferential().getRoutes().clear();
			ValidationException e = null;
			try {
				tc.runValidation();
			} catch (ValidationException exp) {
				e = exp;
			}
			Assert.assertNotNull(e, "ValidationException should have been thrown, but nothing...");
		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "line" }, description = L3_Route_4 + " : error routes with same ordered stops", priority = 32)
	public void verifyTest_3_Line_1_ErrorRoutesWithSameOrderedStops() throws Exception {
		log.info(Color.CYAN + " check " + L3_Route_4 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();

			TestContext tc = new TestContext(context, L3_Route_4);
			LineLite line = tc.getObjectForTest();
			Collection<Route> routes = tc.getReferential().getRoutes().values();

			// -- Nominal test
			tc.runValidation();
			checkNoReports(context, line.getObjectId());

			// -- Error test
			Route orig = null;
			int warningCount = 0;
			for (Route r : routes) {
				if (orig == null) {
					orig = r;
				} else {
					r.setStopPoints(orig.getStopPoints());
					warningCount++;
					orig = null; // -- to have 2 successive routes with same stoppoints
				}
			}
			tc.runValidation();
			checkReports(context, line.getObjectId(), tc.getCheckPointName(), tc.getFormattedCheckPointName(), null,
					OBJECT_STATE.WARNING, warningCount);

		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "line" }, description = L3_JourneyPattern_1
			+ " : error 2 journeypatterns must have different different stops order ", priority = 32)
	public void verifyTest_3_Line_1_ErrorJourneyPatternWithSameOrderedStops() throws Exception {
		log.info(Color.CYAN + " check " + L3_JourneyPattern_1 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();

			TestContext tc = new TestContext(context, L3_JourneyPattern_1);
			LineLite line = tc.getObjectForTest();
			List<JourneyPattern> journeypatterns = new ArrayList<JourneyPattern>();

			Collection<Route> rt = tc.getReferential().getRoutes().values();
			rt.stream().forEach(r -> {
				List<JourneyPattern> jps = r.getJourneyPatterns();
				journeypatterns.addAll(jps);
			});
			Map<String, JourneyPattern> mapJourneyPatterns = new HashMap<String, JourneyPattern>();
			journeypatterns.stream().forEach(jp -> mapJourneyPatterns.put(jp.getObjectId(), jp));
			tc.getReferential().setJourneyPatterns(mapJourneyPatterns);

			// -- Nominal test
			tc.runValidation();
			checkNoReports(context, line.getObjectId());

			// -- Error test
			int warningCount = 1;
			JourneyPattern first = journeypatterns.get(0);
			JourneyPattern second = journeypatterns.get(1);
			first.setStopPoints(second.getStopPoints()); //-- copy stoppoints from jp0 to jp1

			tc.runValidation();

			checkReports(context, line.getObjectId(), tc.getCheckPointName(), tc.getFormattedCheckPointName(), null,
					OBJECT_STATE.WARNING, warningCount);

		} finally {
			utx.rollback();
		}

	}

	class TestContext {

		private Context context;
		private Referential referential;
		private LineLite objectForTest;
		private LineLite line;
		private String checkPointName;
		private LineValidator validator;
		private String firstParam;
		private String secondParam;

		public String getCheckPointName() {
			return checkPointName;
		}

		public void setCheckPointName(String checkPointName) {
			this.checkPointName = checkPointName;
		}

		public TestContext(Context context, String checkPointName) {
			this.context = context;
			referential = (Referential) context.get(REFERENTIAL);
			this.checkPointName = checkPointName;
			// -- Object to TestprepareNewTest()
			long id = 5L;
			line = objectForTest = referential.findLine(id);
			List<Route> routes = dao.findByLineId(line.getId());
			routes.stream().forEach(r -> {
				r.setLineLite(line);
				referential.getRoutes().put(r.getObjectId(), r);
			});
			Assert.assertNotNull(objectForTest, "Line id " + id + " not found");
		}

		public void runValidation() {
			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			//
			validator = new LineValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new CheckpointParameters(checkPointName, false, firstParam, secondParam, null);
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(checkPointName, checkPoints);
			String transportMode = line.getTransportModeName();
			validator.validate(context, objectForTest, parameters, transportMode);
		}

		public String getFirstParam() {
			return firstParam;
		}

		public void setFirstParam(String firstParam) {
			this.firstParam = firstParam;
		}

		public String getSecondParam() {
			return secondParam;
		}

		public void setSecondParam(String secondParam) {
			this.secondParam = secondParam;
		}

		public Referential getReferential() {
			return referential;
		}

		public void setReferential(Referential referential) {
			this.referential = referential;
		}

		public LineLite getObjectForTest() {
			return objectForTest;
		}

		public void setObjectForTest(LineLite objectForTest) {
			this.objectForTest = objectForTest;
		}

		public LineLite getLine() {
			return objectForTest;
		}

		public String getFormattedCheckPointName() {
			return checkPointName.replace('-', '_').toLowerCase();
		}

	}

}
