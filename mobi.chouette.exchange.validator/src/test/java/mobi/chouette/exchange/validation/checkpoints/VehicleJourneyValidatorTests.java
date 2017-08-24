package mobi.chouette.exchange.validation.checkpoints;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.dao.VehicleJourneyDAO;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.exchange.validator.checkpoints.CheckpointParameters;
import mobi.chouette.exchange.validator.checkpoints.VehicleJourneyValidator;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.util.Referential;

@Log4j
public class VehicleJourneyValidatorTests extends AbstractTestValidation {

	@EJB
	VehicleJourneyDAO dao;

	@Deployment
	public static EnterpriseArchive createDeployment() {

		return buildDeployment(VehicleJourneyValidatorTests.class);
	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "vehicleJourney" }, description = "3_VehicleJourney_1", priority = 1)
	public void verifyTest_3_VehicleJourney_1() throws Exception {
		log.info(Color.CYAN + " check " + L3_VehicleJourney_1 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();
			Referential ref = (Referential) context.get(REFERENTIAL);
			VehicleJourney vehicleJourney = dao.find(Long.valueOf(2));
			Assert.assertNotNull(vehicleJourney, "vehiclejourney id 2 not found");
			Route route = vehicleJourney.getJourneyPattern().getRoute();
			LineLite line = ref.findLine(route.getLineId());
			route.setLineLite(line);
			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			VehicleJourneyValidator validator = new VehicleJourneyValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new CheckpointParameters(L3_VehicleJourney_1, false, "2", null);
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(L3_VehicleJourney_1, checkPoints);
			String transportMode = line.getTransportModeName();
			validator.validate(context, vehicleJourney, parameters, transportMode);

			checkNoReports(context, line.getObjectId());
			Time arrivalTime = vehicleJourney.getVehicleJourneyAtStops().get(1).getArrivalTime();
			arrivalTime.setTime(arrivalTime.getTime() - 180000L);
			validator.validate(context, vehicleJourney, parameters, transportMode);
			checkReports(context, line.getObjectId(), L3_VehicleJourney_1, "3_vehiclejourney_1", "3", OBJECT_STATE.WARNING);
		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "vehicleJourney" }, description = "3_VehicleJourney_2", priority = 2)
	public void verifyTest_3_VehicleJourney_2() throws Exception {
		log.info(Color.CYAN + " check " + L3_VehicleJourney_2 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();
			Referential ref = (Referential) context.get(REFERENTIAL);
			VehicleJourney vehicleJourney = dao.find(Long.valueOf(2));
			Assert.assertNotNull(vehicleJourney, "vehiclejourney id 2 not found");
			Route route = vehicleJourney.getJourneyPattern().getRoute();
			LineLite line = ref.findLine(route.getLineId());
			route.setLineLite(line);
			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			VehicleJourneyValidator validator = new VehicleJourneyValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new CheckpointParameters(L3_VehicleJourney_2, false, "11", "74");
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(L3_VehicleJourney_2, checkPoints);
			// nominal
			String transportMode = line.getTransportModeName();
			validator.validate(context, vehicleJourney, parameters, transportMode);
			checkNoReports(context, line.getObjectId());
			
			// speed error (in mode)
			Collection<CheckpointParameters> modeCheckPoints = new ArrayList<>();
			CheckpointParameters modeCheckPoint = new CheckpointParameters(L3_VehicleJourney_2, false, "11", "72");
			modeCheckPoints.add(modeCheckPoint);
			parameters.getControlParameters().getTransportModeCheckpoints().put(transportMode, new HashMap<>());
			parameters.getControlParameters().getTransportModeCheckpoints().get(transportMode).put(L3_VehicleJourney_2, modeCheckPoints);
			validator.validate(context, vehicleJourney, parameters, transportMode);
			checkReports(context, line.getObjectId(), L3_VehicleJourney_2, "3_vehiclejourney_2_1", "74", OBJECT_STATE.WARNING);

			// Speed error min
			context.put(REPORT, new ActionReport());
			context.put(VALIDATION_REPORT, new ValidationReport());
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			modeCheckPoint.setFirstValue("12");
			modeCheckPoint.setSecondValue("74");
			validator.validate(context, vehicleJourney, parameters, transportMode);
			checkReports(context, line.getObjectId(), L3_VehicleJourney_2, "3_vehiclejourney_2_2", "11", OBJECT_STATE.WARNING);

			
		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "vehicleJourney" }, description = "3_VehicleJourney_4", priority = 3)
	public void verifyTest_3_VehicleJourney_4() throws Exception {
		log.info(Color.CYAN + " check " + L3_VehicleJourney_4 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();
			Referential ref = (Referential) context.get(REFERENTIAL);
			VehicleJourney vehicleJourney = dao.find(Long.valueOf(2));
			Assert.assertNotNull(vehicleJourney, "vehiclejourney id 2 not found");
			Route route = vehicleJourney.getJourneyPattern().getRoute();
			LineLite line = ref.findLine(route.getLineId());
			route.setLineLite(line);
			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			VehicleJourneyValidator validator = new VehicleJourneyValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new CheckpointParameters(L3_VehicleJourney_4, false, null,null);
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(L3_VehicleJourney_4, checkPoints);
			// nominal
			String transportMode = line.getTransportModeName();
			validator.validate(context, vehicleJourney, parameters, transportMode);
			checkNoReports(context, line.getObjectId());
			
			// error
			vehicleJourney.getTimetables().clear();
			validator.validate(context, vehicleJourney, parameters, transportMode);
			checkReports(context, line.getObjectId(), L3_VehicleJourney_4, "3_vehiclejourney_4", null, OBJECT_STATE.WARNING);


		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "vehicleJourney" }, description = "3_VehicleJourney_5", priority = 5)
	public void verifyTest_3_VehicleJourney_5() throws Exception {
		log.info(Color.CYAN + " check " + L3_VehicleJourney_5 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();
			Referential ref = (Referential) context.get(REFERENTIAL);
			VehicleJourney vehicleJourney = dao.find(Long.valueOf(38));
			Assert.assertNotNull(vehicleJourney, "vehiclejourney id 38 not found");
			Route route = vehicleJourney.getJourneyPattern().getRoute();
			LineLite line = ref.findLine(route.getLineId());
			route.setLineLite(line);
			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			VehicleJourneyValidator validator = new VehicleJourneyValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new CheckpointParameters(L3_VehicleJourney_5, false, null,null);
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(L3_VehicleJourney_5, checkPoints);
			// nominal
			String transportMode = line.getTransportModeName();
			validator.validate(context, vehicleJourney, parameters, transportMode);
			checkNoReports(context, line.getObjectId());
			// error on first passingtime
			Time arrivalTime = vehicleJourney.getVehicleJourneyAtStops().get(0).getArrivalTime();
			arrivalTime.setTime(arrivalTime.getTime() +90000L);
			validator.validate(context, vehicleJourney, parameters, transportMode);
			checkReports(context, line.getObjectId(), L3_VehicleJourney_5, "3_vehiclejourney_5_1", "23:59", OBJECT_STATE.WARNING);
			arrivalTime.setTime(arrivalTime.getTime() -90000L);
            // error on second stop

			Time departureTime = vehicleJourney.getVehicleJourneyAtStops().get(1).getDepartureTime();
			departureTime.setTime(departureTime.getTime() +420000L);
			context.put(REPORT, new ActionReport());
			context.put(VALIDATION_REPORT, new ValidationReport());
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			validator.validate(context, vehicleJourney, parameters, transportMode);
			checkReports(context, line.getObjectId(), L3_VehicleJourney_5, "3_vehiclejourney_5_2", "00:11", OBJECT_STATE.WARNING);
			
		} finally {
			utx.rollback();
		}

	}


}
