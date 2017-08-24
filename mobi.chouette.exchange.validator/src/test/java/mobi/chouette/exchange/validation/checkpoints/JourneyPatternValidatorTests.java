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
import mobi.chouette.dao.JourneyPatternDAO;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.exchange.validator.checkpoints.CheckpointParameters;
import mobi.chouette.exchange.validator.checkpoints.JourneyPatternValidator;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.util.Referential;

@Log4j
public class JourneyPatternValidatorTests extends AbstractTestValidation {

	@EJB
	JourneyPatternDAO dao;

	@Deployment
	public static EnterpriseArchive createDeployment() {

		return buildDeployment(JourneyPatternValidatorTests.class);
	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "journey-pattern" }, description = "3_JourneyPattern_2", priority = 41)
	public void verifyTest_3_JourneyPattern_1() throws Exception {
		log.info(Color.CYAN + " check " + L3_JourneyPattern_2 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();

			TestContext tc = new TestContext(context, L3_JourneyPattern_2);
			JourneyPattern jp = tc.getObjectForTest();

			// -- Nominal
			tc.runValidation();
			checkNoReports(context, jp.getObjectId());

			// -- Error
			jp.setVehicleJourneys(new ArrayList<VehicleJourney>()); //-- remove all vehiclejourneys
			tc.runValidation();
			checkReports(context, tc.getLine().getObjectId(), tc.getCheckPointName(), tc.getFormattedCheckPointName(), null,
					OBJECT_STATE.WARNING);

		} finally {
			utx.rollback();
		}
	}

	class TestContext {

		private Context context;
		private Referential referential;
		private JourneyPattern objectForTest;
		private LineLite line;
		private String checkPointName;
		private JourneyPatternValidator validator;

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
			long jpId = 2;
			objectForTest = dao.find(jpId);
			Assert.assertNotNull(objectForTest, "JourneyPattern id " + jpId + " not found");
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
			validator = new JourneyPatternValidator();
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

		public JourneyPattern getObjectForTest() {
			return objectForTest;
		}

		public void setObjectForTest(JourneyPattern objectForTest) {
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
