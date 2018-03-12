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
import mobi.chouette.dao.FootnoteDAO;
import mobi.chouette.dao.RouteDAO;
import mobi.chouette.dao.TimetableDAO;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.exchange.validator.Constant;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.util.Referential;

@Log4j
public class GenericValidatorTests extends AbstractTestValidation {

	@EJB
	RouteDAO routeDao;

	@EJB
	FootnoteDAO noteDao;

	@EJB
	TimetableDAO timetableDao;

	@Deployment
	public static EnterpriseArchive createDeployment() {

		return buildDeployment(GenericValidatorTests.class);
	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "generic" }, description = "3_Generic_1_Route", priority = 101)
	public void verifyTest_3_Generic_1_Route() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Generic_1 + " on Route " + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();
			Referential ref = (Referential) context.get(Constant.REFERENTIAL);
			Route route = routeDao.find(Long.valueOf(2));
			Assert.assertNotNull(route, "route id 2 not found");
			LineLite line = ref.findLine(route.getLineId());
			route.setLineLite(line);
			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			RouteValidator validator = new RouteValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(Constant.CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new GenericCheckpointParameters(CheckPointConstant.L3_Generic_1,
					CheckPointConstant.L3_Generic_1, CheckPointConstant.L3_Generic_1, 0L, false, null, null,
					"^[\\w ]+$", "Route", "name");
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(CheckPointConstant.L3_Generic_1, checkPoints);
			String transportMode = line.getTransportModeName();
			validator.validate(context, route, parameters, transportMode);

			checkNoReports(context, line.getObjectId());
			route.setName("ça va pas!");
			validator.validate(context, route, parameters, transportMode);
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Generic_1, "3_generic_1", route.getName(),
					OBJECT_STATE.WARNING);
		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "generic" }, description = "3_Generic_2_VehicleJourney", priority = 102)
	public void verifyTest_3_Generic_2_VehicleJourney() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Generic_2 + " on VehicleJourney " + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();
			Referential ref = (Referential) context.get(Constant.REFERENTIAL);
			Route route = routeDao.find(Long.valueOf(2));
			Assert.assertNotNull(route, "route id 2 not found");
			LineLite line = ref.findLine(route.getLineId());
			route.setLineLite(line);
			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			VehicleJourneyValidator validator = new VehicleJourneyValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(Constant.CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new GenericCheckpointParameters(CheckPointConstant.L3_Generic_2,
					CheckPointConstant.L3_Generic_2, CheckPointConstant.L3_Generic_2, 0L, false, "3", "6", null,
					"VehicleJourney", "number");
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
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Generic_2, "3_generic_2_1", "12",
					OBJECT_STATE.WARNING);

			// min value
			context.put(Constant.REPORT, new ActionReport());
			context.put(Constant.VALIDATION_REPORT, new ValidationReport());
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			vj.setNumber(2L);
			validator.validate(context, vj, parameters, transportMode);
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Generic_2, "3_generic_2_2", "2",
					OBJECT_STATE.WARNING);

		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test(groups = { "generic" }, description = "3_Generic_3_Route", priority = 103)
	public void verifyTest_3_Generic_3_Route() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Generic_3 + " on Route " + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();
			Referential ref = (Referential) context.get(Constant.REFERENTIAL);
			List<Route> routes = routeDao.findByLineId(7L);
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
			CheckpointParameters checkPoint = new GenericCheckpointParameters(CheckPointConstant.L3_Generic_3,
					CheckPointConstant.L3_Generic_3, CheckPointConstant.L3_Generic_3, 0L, false, null, null, null,
					"Route", "name");
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(CheckPointConstant.L3_Generic_3, checkPoints);
			String transportMode = line.getTransportModeName();
			routes.stream().forEach(route -> {
				validator.validate(context, route, parameters, transportMode);
			});

			checkNoReports(context, line.getObjectId());
			Map<String, Map<String, DataLocation>> generic3Context = (Map<String, Map<String, DataLocation>>) context
					.get(Constant.ATTRIBUTE_CONTEXT);

			generic3Context.values().stream().forEach(map -> map.clear());
			generic3Context.clear();
			routes.get(0).setName(routes.get(1).getName());
			routes.stream().forEach(route -> {
				validator.validate(context, route, parameters, transportMode);
			});
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Generic_3, "3_generic_3",
					routes.get(0).getName(), OBJECT_STATE.WARNING);
		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "generic" }, description = "3_Generic_1_Footnote", priority = 104)
	public void verifyTest_3_Generic_1_Footnote() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Generic_1 + " on Footnote " + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();
			Referential ref = (Referential) context.get(Constant.REFERENTIAL);
			Footnote note = noteDao.find(Long.valueOf(6));
			Assert.assertNotNull(note, "note id 6 not found");
			LineLite line = ref.findLine(note.getLineId());
			note.setLineLite(line);
			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			FootnoteValidator validator = new FootnoteValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(Constant.CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new GenericCheckpointParameters(CheckPointConstant.L3_Generic_1,
					CheckPointConstant.L3_Generic_1, CheckPointConstant.L3_Generic_1, 0L, false, null, null,
					"^[\\d ]+$", "Footnote", "code");
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(CheckPointConstant.L3_Generic_1, checkPoints);
			String transportMode = line.getTransportModeName();
			validator.validate(context, note, parameters, transportMode);

			checkNoReports(context, line.getObjectId());
			note.setCode("ça va pas!");
			validator.validate(context, note, parameters, transportMode);
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Generic_1, "3_generic_1", note.getCode(),
					OBJECT_STATE.WARNING);
		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test(groups = { "generic" }, description = "3_Generic_3_Footnote", priority = 105)
	public void verifyTest_3_Generic_3_Footnote() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Generic_3 + " on Footnote " + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();
			Referential ref = (Referential) context.get(Constant.REFERENTIAL);
			List<Footnote> notes = noteDao.findByLineId(6L);
			Assert.assertNotNull(notes, "notes for line 6 not found");
			Assert.assertEquals(notes.size(), 3, " 3 notes for line 6 ");
			LineLite line = ref.findLine(7L);
			notes.stream().forEach(route -> {
				route.setLineLite(line);
			});

			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			FootnoteValidator validator = new FootnoteValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(Constant.CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new GenericCheckpointParameters(CheckPointConstant.L3_Generic_3,
					CheckPointConstant.L3_Generic_3, CheckPointConstant.L3_Generic_3, 0L, false, null, null, null,
					"Footnote", "code");
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(CheckPointConstant.L3_Generic_3, checkPoints);
			String transportMode = line.getTransportModeName();
			notes.stream().forEach(note -> {
				validator.validate(context, note, parameters, transportMode);
			});

			checkNoReports(context, line.getObjectId());
			Map<String, Map<String, DataLocation>> generic3Context = (Map<String, Map<String, DataLocation>>) context
					.get(Constant.ATTRIBUTE_CONTEXT);

			generic3Context.values().stream().forEach(map -> map.clear());
			generic3Context.clear();
			notes.get(0).setCode(notes.get(1).getCode());
			notes.stream().forEach(note -> {
				validator.validate(context, note, parameters, transportMode);
			});
			checkReports(context, line.getObjectId(), CheckPointConstant.L3_Generic_3, "3_generic_3",
					notes.get(0).getCode(), OBJECT_STATE.WARNING);
		} finally {
			utx.rollback();
		}
	}

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test(groups = { "generic" }, description = "3_Generic_3_Timetable", priority = 106)
	public void verifyTest_3_Generic_3_Timetable() throws Exception {
		log.info(Color.CYAN + " check " + CheckPointConstant.L3_Generic_3 + " on Timetable " + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();
			List<Timetable> tms = timetableDao.findAll();
			Assert.assertNotNull(tms, "timetables not found");
			Assert.assertEquals(tms.size(), 5, " 5 timetables ");

			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, "", OBJECT_TYPE.TIME_TABLE, "", OBJECT_STATE.OK,
					null);
			TimetableValidator validator = new TimetableValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(Constant.CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new GenericCheckpointParameters(CheckPointConstant.L3_Generic_3,
					CheckPointConstant.L3_Generic_3, CheckPointConstant.L3_Generic_3, 0L, false, null, null, null,
					"Timetable", "comment");
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(CheckPointConstant.L3_Generic_3, checkPoints);
			tms.stream().forEach(tm -> {
				tm.setComment(tm.getId() + " " +tm.getComment());
				validator.validate(context, tm, parameters, null);
			});

			checkNoReports(context, OBJECT_TYPE.TIME_TABLE);
			Map<String, Map<String, DataLocation>> generic3Context = (Map<String, Map<String, DataLocation>>) context
					.get(Constant.ATTRIBUTE_CONTEXT);

			generic3Context.values().stream().forEach(map -> map.clear());
			generic3Context.clear();
			tms.get(0).setComment(tms.get(1).getComment());
			tms.stream().forEach(tm -> {
				validator.validate(context, tm, parameters, null);
			});
			checkReports(context, OBJECT_TYPE.TIME_TABLE, CheckPointConstant.L3_Generic_3, "3_generic_3",
					tms.get(0).getComment(), OBJECT_STATE.WARNING, 1);
		} finally {
			utx.rollback();
		}

	}

//	@Test(groups = { "tools" }, description = "quickDistanceFromCoordinates", priority = 107)
//	public void verifyTest_quickDistanceFromCoordinates() throws Exception {
//		log.info(Color.CYAN + " check quickDistanceFromCoordinates " + Color.NORMAL);
//		
//		double lat1 = 49.04854359315094 ;
//		double long1 = 2.012291499430454;
//		double lat2 = 49.014622656688 ;
//		double long2 = 2.0795434259068;
//		
//		double dist = GenericValidator.quickDistanceFromCoordinates( lat1,  lat2,  long1, long2 );
//		
//		log.info("distance = "+Double.toString(dist));
//	}

}
