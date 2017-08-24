package mobi.chouette.exchange.validation.checkpoints;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.dao.RoutingConstraintDAO;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.exchange.validator.checkpoints.CheckpointParameters;
import mobi.chouette.exchange.validator.checkpoints.RoutingConstraintValidator;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.RoutingConstraint;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.Referential;

@Log4j
public class RoutingConstraintValidatorTests extends AbstractTestValidation {

	@EJB
	RoutingConstraintDAO dao;

	@Deployment
	public static EnterpriseArchive createDeployment() {

		return buildDeployment(RoutingConstraintValidatorTests.class);
	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "routing-contraints" }, description = "3_itl_1", priority = 21)
	public void verifyTest_3_RoutingConstraint_1() throws Exception {
		log.info(Color.CYAN + " check " + L3_ITL_1 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();

			TestContext tc = new TestContext(context, L3_ITL_1);
			RoutingConstraint rc = tc.getObjectForTest();

			// -- Nominal
			tc.runValidation();
			checkNoReports(context, rc.getObjectId());

			// -- Error
			Long[] spIds = rc.getStopPointIds();
			List<StopPoint> spList = rc.getRoute().getStopPoints();
			Map<Long, StopPoint> spMap = spList.stream()
					.collect(Collectors.toMap(StopPoint::getId, Function.identity()));
			int warningCount = 0;
			for (int i = 0; i < spIds.length; i++) {

				if (i % 2 == 0) {

					long id = spIds[i];
					StopPoint p = spMap.get(id);
					StopAreaLite sa = tc.getReferential().findStopArea(p.getStopAreaId());
					sa.setDeletedTime(new Date());// -- desactivate sa
					warningCount++;
				}
			}

			tc.runValidation();
			checkReports(context, tc.getLine().getObjectId(), tc.getCheckPointName(), tc.getFormattedCheckPointName(), null,
					OBJECT_STATE.WARNING, warningCount);

		} finally {
			utx.rollback();
		}
	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "routing-contraints" }, description = "3_itl_2", priority = 22)
	public void verifyTest_3_RoutingConstraint_2() throws Exception {
		log.info(Color.CYAN + " check " + L3_ITL_2 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();

			TestContext tc = new TestContext(context, L3_ITL_2);

			// -- Nominal
			tc.runValidation();
			checkNoReports(context, tc.getObjectForTest().getObjectId());

			// -- Error
			Route route = tc.getObjectForTest().getRoute();
			List<Long> routeStopPointIdList = route.getStopPoints().stream().map(p -> p.getId())
					.collect(Collectors.toList());
			Long[] spIds = new Long[routeStopPointIdList.size()];
			routeStopPointIdList.toArray(spIds);
			tc.getObjectForTest().setStopPointIds(spIds);

			int warningCount = 1;
			tc.runValidation();
			checkReports(context, tc.getLine().getObjectId(), tc.getCheckPointName(), tc.getFormattedCheckPointName(),
					null, OBJECT_STATE.WARNING, warningCount);

		} finally {
			utx.rollback();
		}
	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "routing-contraints" }, description = "3_itl_3", priority = 23)
	public void verifyTest_3_RoutingConstraint_3() throws Exception {
		log.info(Color.CYAN + " check " + L3_ITL_3 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try { // -- Nominal
			em.joinTransaction();

			TestContext tc = new TestContext(context, L3_ITL_3);

			// -- Nominal
			tc.runValidation();
			checkNoReports(context, tc.getObjectForTest().getObjectId());

			// -- Error
			Long[] spIds = new Long[1];
			spIds[0] = tc.getObjectForTest().getStopPointIds()[0];
			tc.getObjectForTest().setStopPointIds(spIds);

			int warningCount = 1;
			tc.runValidation();
			checkReports(context, tc.getLine().getObjectId(), tc.getCheckPointName(), tc.getFormattedCheckPointName(),
					null, OBJECT_STATE.WARNING, warningCount);

		} finally {
			utx.rollback();
		}
	}

	class TestContext {

		private Context context;
		private Referential referential;
		private RoutingConstraint objectForTest;
		private LineLite line;
		private String checkPointName;
		private RoutingConstraintValidator validator;

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
			long rcId = 2;
			objectForTest = dao.find(rcId);
			Assert.assertNotNull(objectForTest, "routing-constraint id " + rcId + " not found");
			Route route = objectForTest.getRoute();
			// System.err.println("Route= " + route);
			line = referential.findLine(route.getLineId());
			// System.err.println("Line= " + line);
			route.setLineLite(line);
		}

		public void runValidation() {
			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			//
			validator = new RoutingConstraintValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new CheckpointParameters(checkPointName, false, null, null);
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(checkPointName, checkPoints);
			String transportMode = line.getTransportModeName();
			validator.validate(context, objectForTest, parameters, transportMode);
		}

		public Referential getReferential() {
			return referential;
		}

		public void setReferential(Referential referential) {
			this.referential = referential;
		}

		public RoutingConstraint getObjectForTest() {
			return objectForTest;
		}

		public void setObjectForTest(RoutingConstraint objectForTest) {
			this.objectForTest = objectForTest;
		}

		public LineLite getLine() {
			return line;
		}

		public void setLine(LineLite line) {
			this.line = line;
		}

		public String getFormattedCheckPointName() {
			return checkPointName.replace('-', '_').toLowerCase();
		}

	}

}
