package mobi.chouette.exchange.validator.checkpoints;

import java.util.ArrayList;
import java.util.Collection;
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
import mobi.chouette.dao.RouteDAO;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.exchange.validator.Constant;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.exchange.validator.checkpoints.CheckPointConstant;
import mobi.chouette.exchange.validator.checkpoints.CheckpointParameters;
import mobi.chouette.exchange.validator.checkpoints.GenericCheckpointParameters;
import mobi.chouette.exchange.validator.checkpoints.RouteValidator;
import mobi.chouette.exchange.validator.checkpoints.VehicleJourneyValidator;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.util.Referential;

@Log4j
public class GenericValidatorTests extends AbstractTestValidation {

	@EJB
	RouteDAO dao;

	@Deployment
	public static EnterpriseArchive createDeployment() {

		return buildDeployment(GenericValidatorTests.class);
	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "route" }, description = "3_Generic_1", priority = 101)
	public void verifyTest_3_Generic_1() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Generic_1 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();
			Referential ref = (Referential) context.get(Constant.REFERENTIAL);
			Route route = dao.find(Long.valueOf(2));
			Assert.assertNotNull(route, "route id 2 not found");
			LineLite line = ref.findLine(route.getLineId());
			route.setLineLite(line);
			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			RouteValidator validator = new RouteValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(Constant.CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new GenericCheckpointParameters(CheckPointConstant.L3_Generic_1,CheckPointConstant.L3_Generic_1,CheckPointConstant.L3_Generic_1, 0L ,false, null,null, "^[\\w ]+$", "Route", "name");
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(CheckPointConstant.L3_Generic_1, checkPoints);
			String transportMode = line.getTransportModeName();
			validator.validate(context, route, parameters, transportMode);

			checkNoReports(context, line.getObjectId());
			route.setName("ça va pas!");
			validator.validate(context, route, parameters, transportMode);
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Generic_1, "3_generic_1", route.getName(), OBJECT_STATE.WARNING);
		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "route" }, description = "3_Generic_2", priority = 102)
	public void verifyTest_3_Generic_2() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Generic_2 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();
			Referential ref = (Referential) context.get(Constant.REFERENTIAL);
			Route route = dao.find(Long.valueOf(2));
			Assert.assertNotNull(route, "route id 2 not found");
			LineLite line = ref.findLine(route.getLineId());
			route.setLineLite(line);
			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			VehicleJourneyValidator validator = new VehicleJourneyValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(Constant.CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new GenericCheckpointParameters(CheckPointConstant.L3_Generic_2,CheckPointConstant.L3_Generic_2,CheckPointConstant.L3_Generic_2, 0L, false, "3", "6",null, "VehicleJourney", "number");
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(CheckPointConstant.L3_Generic_2, checkPoints);
			String transportMode = line.getTransportModeName();
			VehicleJourney vj = route.getJourneyPatterns().get(0).getVehicleJourneys().get(0);
			vj.setNumber(4L);
			validator.validate(context, vj, parameters, transportMode);
			checkNoReports(context, line.getObjectId());

			// max value
			vj.setNumber(12L);
			validator.validate(context, vj, parameters, transportMode);
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Generic_2, "3_generic_2_1", "12", OBJECT_STATE.WARNING);
			
			// min value
			context.put(Constant.REPORT, new ActionReport());
			context.put(Constant.VALIDATION_REPORT, new ValidationReport());
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			vj.setNumber(2L);
			validator.validate(context, vj, parameters, transportMode);
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Generic_2, "3_generic_2_2", "2", OBJECT_STATE.WARNING);

		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test(groups = { "route" }, description = "3_Generic_3", priority = 103)
	public void verifyTest_3_Generic_3() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Generic_3 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();
			Referential ref = (Referential) context.get(Constant.REFERENTIAL);
			List<Route> routes = dao.findByLineId(7L);
			Assert.assertNotNull(routes, "routes for line 7 not found");
			Assert.assertEquals(routes.size(), 4, " 4 routes for line 7 ");
			LineLite line = ref.findLine(7L);
			routes.stream().forEach(route -> {
				route.setLineLite(line);
			});
			
			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			RouteValidator validator = new RouteValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(Constant.CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new GenericCheckpointParameters(CheckPointConstant.L3_Generic_3,CheckPointConstant.L3_Generic_3,CheckPointConstant.L3_Generic_3, 0L, false, null, null, null,"Route", "name");
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(CheckPointConstant.L3_Generic_3, checkPoints);
			String transportMode = line.getTransportModeName();
			routes.stream().forEach(route -> {
			validator.validate(context, route, parameters, transportMode);
			} );

			checkNoReports(context, line.getObjectId());
			Map<String, Map<String, DataLocation>> generic3Context = (Map<String, Map<String, DataLocation>>) context
					.get(Constant.ATTRIBUTE_CONTEXT);
			
			generic3Context.values().stream().forEach(map -> map.clear());
			generic3Context.clear();
			routes.get(0).setName(routes.get(1).getName());
			routes.stream().forEach(route -> {
			validator.validate(context, route, parameters, transportMode);
			} );
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Generic_3, "3_generic_3", routes.get(0).getName(), OBJECT_STATE.WARNING);
		} finally {
			utx.rollback();
		}

	}

}
