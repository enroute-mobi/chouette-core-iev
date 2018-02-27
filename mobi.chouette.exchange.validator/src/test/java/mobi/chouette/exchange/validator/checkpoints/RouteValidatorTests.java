package mobi.chouette.exchange.validator.checkpoints;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.exchange.validator.Constant;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.exchange.validator.checkpoints.CheckPointConstant;
import mobi.chouette.exchange.validator.checkpoints.CheckpointParameters;
import mobi.chouette.exchange.validator.checkpoints.RouteValidator;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.Referential;

@Log4j
public class RouteValidatorTests extends AbstractTestValidation {

	@EJB
	RouteDAO dao;

	@Deployment
	public static EnterpriseArchive createDeployment() {

		return buildDeployment(RouteValidatorTests.class);
	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "route" }, description = "3_Route_1", priority = 131)
	public void verifyTest_3_Route_1() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Route_1 + Color.NORMAL);
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
			CheckpointParameters checkPoint = new CheckpointParameters(CheckPointConstant.L3_Route_1,CheckPointConstant.L3_Route_1,CheckPointConstant.L3_Route_1, 0L, false, null, null, null);
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(CheckPointConstant.L3_Route_1, checkPoints);
			String transportMode = line.getTransportModeName();
			validator.validate(context, route, parameters, transportMode);

			checkNoReports(context, line.getObjectId());
			route.getStopPoints().get(1).setStopAreaId(route.getStopPoints().get(0).getStopAreaId());
			validator.validate(context, route, parameters, transportMode);
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Route_1, "3_route_1", null, OBJECT_STATE.WARNING);
		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "route" }, description = "3_Route_2", priority = 132)
	public void verifyTest_3_Route_2() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Route_2 + Color.NORMAL);
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
			// route 6 <-> 7 and 8 <-> 9
			Route route = getRoute(routes, 6L);
			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			RouteValidator validator = new RouteValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(Constant.CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new CheckpointParameters(CheckPointConstant.L3_Route_2,CheckPointConstant.L3_Route_2,CheckPointConstant.L3_Route_2, 0L, false, null, null, null);
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(CheckPointConstant.L3_Route_2, checkPoints);
			String transportMode = line.getTransportModeName();
			validator.validate(context, route, parameters, transportMode);
			checkNoReports(context, line.getObjectId());

			// cross reference of routes
			route.forceOppositeRoute(getRoute(routes, 8L));
			validator.validate(context, route, parameters, transportMode);
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Route_2, "3_route_2", null, OBJECT_STATE.WARNING);

			// wrong direction
			context.put(Constant.REPORT, new ActionReport());
			context.put(Constant.VALIDATION_REPORT, new ValidationReport());
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			route.forceOppositeRoute(getRoute(routes, 7L));
			route.setWayback(route.getOppositeRoute().getWayback());
			validator.validate(context, route, parameters, transportMode);
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Route_2, "3_route_2", null, OBJECT_STATE.WARNING);

		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "route" }, description = "3_Route_3", priority = 133)
	public void verifyTest_3_Route_3() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Route_3 + Color.NORMAL);
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
			CheckpointParameters checkPoint = new CheckpointParameters(CheckPointConstant.L3_Route_3,CheckPointConstant.L3_Route_3,CheckPointConstant.L3_Route_3, 0L, false, null, null, null);
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(CheckPointConstant.L3_Route_3, checkPoints);
			String transportMode = line.getTransportModeName();
			validator.validate(context, route, parameters, transportMode);

			checkNoReports(context, line.getObjectId());
			route.getJourneyPatterns().clear();
			validator.validate(context, route, parameters, transportMode);
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Route_3, "3_route_3", null, OBJECT_STATE.WARNING);
		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "route" }, description = "3_Route_5", priority = 134)
	public void verifyTest_3_Route_5() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Route_5 + Color.NORMAL);
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
			CheckpointParameters checkPoint = new CheckpointParameters(CheckPointConstant.L3_Route_5,CheckPointConstant.L3_Route_5,CheckPointConstant.L3_Route_5, 0L, false, null, null, null);
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(CheckPointConstant.L3_Route_5, checkPoints);
			String transportMode = line.getTransportModeName();
			validator.validate(context, route, parameters, transportMode);

			checkNoReports(context, line.getObjectId());
			route.getStopPoints().get(0).setStopAreaId(route.getStopPoints().get(1).getStopAreaId());
			validator.validate(context, route, parameters, transportMode);
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Route_5, "3_route_5", null, OBJECT_STATE.WARNING);
		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "route" }, description = "3_Route_6", priority = 135)
	public void verifyTest_3_Route_6() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Route_6 + Color.NORMAL);
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
			CheckpointParameters checkPoint = new CheckpointParameters(CheckPointConstant.L3_Route_6,CheckPointConstant.L3_Route_6,CheckPointConstant.L3_Route_6, 0L, false, null, null, null);
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(CheckPointConstant.L3_Route_6, checkPoints);
			String transportMode = line.getTransportModeName();
			validator.validate(context, route, parameters, transportMode);

			checkNoReports(context, line.getObjectId());
			StopPoint p = route.getStopPoints().get(0);
			route.getStopPoints().clear();
			route.getStopPoints().add(p);
			validator.validate(context, route, parameters, transportMode);
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Route_6, "3_route_6", null, OBJECT_STATE.WARNING);
		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "route" }, description = "3_Route_8", priority = 136)
	public void verifyTest_3_Route_8() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Route_8 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();
			Referential ref = (Referential) context.get(Constant.REFERENTIAL);
			Route route = dao.find(Long.valueOf(5));
			Assert.assertNotNull(route, "route id 5 not found");
			LineLite line = ref.findLine(route.getLineId());
			route.setLineLite(line);
			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			RouteValidator validator = new RouteValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(Constant.CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new CheckpointParameters(CheckPointConstant.L3_Route_8,CheckPointConstant.L3_Route_8,CheckPointConstant.L3_Route_8, 0L, false, null, null, null);
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(CheckPointConstant.L3_Route_8, checkPoints);
			String transportMode = line.getTransportModeName();
			validator.validate(context, route, parameters, transportMode);

			checkNoReports(context, line.getObjectId());

			// remove stop 1 on omnibus
			route.getJourneyPatterns().stream().forEach(journeyPattern -> {
				if (journeyPattern.getStopPoints().size() == route.getStopPoints().size()) {
					journeyPattern.removeStopPoint(journeyPattern.getStopPoints().get(1));
				}
			} );

			validator.validate(context, route, parameters, transportMode);
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Route_8, "3_route_8", null, OBJECT_STATE.WARNING);
		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "route" }, description = "3_Route_9", priority = 137)
	public void verifyTest_3_Route_9() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Route_9 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();
			Referential ref = (Referential) context.get(Constant.REFERENTIAL);
			Route route = dao.find(Long.valueOf(5));
			Assert.assertNotNull(route, "route id 5 not found");
			LineLite line = ref.findLine(route.getLineId());
			route.setLineLite(line);
			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			RouteValidator validator = new RouteValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(Constant.CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new CheckpointParameters(CheckPointConstant.L3_Route_9, CheckPointConstant.L3_Route_9, CheckPointConstant.L3_Route_9, 0L, false, null, null, null);
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(CheckPointConstant.L3_Route_9, checkPoints);
			String transportMode = line.getTransportModeName();
			validator.validate(context, route, parameters, transportMode);

			checkNoReports(context, line.getObjectId());

			// remove stop 1 on omnibus
			route.getJourneyPatterns().stream().forEach(journeyPattern -> {
				if (journeyPattern.getStopPoints().size() == route.getStopPoints().size()) {
					journeyPattern.removeStopPoint(journeyPattern.getStopPoints().get(1));
				}
			} );

			validator.validate(context, route, parameters, transportMode);
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Route_9, "3_route_9", null, OBJECT_STATE.WARNING);
		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "route" }, description = "3_Route_10", priority = 138)
	public void verifyTest_3_Route_10() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Route_10 + Color.NORMAL);
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
			CheckpointParameters checkPoint = new CheckpointParameters(CheckPointConstant.L3_Route_10,CheckPointConstant.L3_Route_10,CheckPointConstant.L3_Route_10, 0L, false, null, null, null);
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(CheckPointConstant.L3_Route_10, checkPoints);
			String transportMode = line.getTransportModeName();
			validator.validate(context, route, parameters, transportMode);

			checkNoReports(context, line.getObjectId());
			StopAreaLite stop = ref.findStopArea(route.getStopPoints().get(0).getStopAreaId());
			stop.setDeletedTime(new Date(Calendar.getInstance().getTimeInMillis()));
			validator.validate(context, route, parameters, transportMode);
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Route_10, "3_route_10", null, OBJECT_STATE.WARNING);
		} finally {
			utx.rollback();
		}

	}

	private Route getRoute(List<Route> routes, Long l) {
		Optional<Route> result = 
		routes.stream().filter(route -> (route.getId().equals(l))).findFirst();
		return result.orElse(null);
	}

}
