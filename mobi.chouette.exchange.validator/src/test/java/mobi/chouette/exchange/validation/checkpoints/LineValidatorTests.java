package mobi.chouette.exchange.validation.checkpoints;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.testng.annotations.Test;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.dao.RouteDAO;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.exchange.validator.checkpoints.CheckpointParameters;
import mobi.chouette.exchange.validator.checkpoints.LineValidator;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.util.Referential;

@Log4j
public class LineValidatorTests extends AbstractTestValidation {

	@EJB
	RouteDAO dao;

	@Deployment
	public static EnterpriseArchive createDeployment() {

		return buildDeployment(LineValidatorTests.class);
	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "line" }, description = "3_Line_1", priority = 1)
	public void verifyTest_3_Line_1() throws Exception {
		log.info(Color.CYAN + " check " + L3_Route_1 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();

			Referential ref = (Referential) context.get(REFERENTIAL);
			LineLite line = ref.findLine(5L);

			List<Route> routes = dao.findByLineId(line.getId());
			routes.stream().forEach(r -> {
				r.setLineLite(line);
				ref.getRoutes().put(r.getObjectId(), r);
			});

			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			LineValidator validator = new LineValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new CheckpointParameters(L3_Line_1, false, null, null);
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(L3_Line_1, checkPoints);
			String transportMode = line.getTransportModeName();
			validator.validate(context, line, parameters, transportMode);

			checkNoReports(context, line.getObjectId());

		} finally {
			utx.rollback();
		}

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "line" }, description = "3_Line_1 : error no opposite route", priority = 1)
	public void verifyTest_3_Line_1_ErrorNoOppositeRoute() throws Exception {
		log.info(Color.CYAN + " check " + L3_Route_1 + Color.NORMAL);
		initSchema();
		Context context = initValidatorContext();
		loadSharedData(context);
		utx.begin();
		try {
			em.joinTransaction();

			Referential ref = (Referential) context.get(REFERENTIAL);
			LineLite line = ref.findLine(5L);
			
			List<Route> routes = dao.findByLineId(line.getId());
			routes.stream().forEach(r -> {
				r.setLineLite(line);
				r.setOppositeRoute(null);
				ref.getRoutes().put(r.getObjectId(), r);
			});

			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
					null);
			LineValidator validator = new LineValidator();
			ValidateParameters parameters = (ValidateParameters) context.get(CONFIGURATION);
			Collection<CheckpointParameters> checkPoints = new ArrayList<>();
			CheckpointParameters checkPoint = new CheckpointParameters(L3_Line_1, false, null, null);
			checkPoints.add(checkPoint);
			parameters.getControlParameters().getGlobalCheckPoints().put(L3_Line_1, checkPoints);
			String transportMode = line.getTransportModeName();
			validator.validate(context, line, parameters, transportMode);

			checkReports(context, line.getObjectId(), L3_Line_1, "3_line_1", null, OBJECT_STATE.WARNING);

		} finally {
			utx.rollback();
		}

	}



}
