package mobi.chouette.exchange.validation.checkpoints;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.testng.annotations.Test;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.AnsiText;
import mobi.chouette.dao.RouteDAO;
import mobi.chouette.exchange.validator.checkpoints.CheckPointConstant;

@Log4j
public class ShapeValidatorTests extends AbstractTestValidation {

	@EJB
	RouteDAO dao;

	@Deployment
	public static EnterpriseArchive createDeployment() {

		return buildDeployment(ShapeValidatorTests.class);
	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "shape" }, description = "3_Shape_1", priority = 81)
	public void verifyTest_3_Shape_1() throws Exception {
		String checkPointName = CheckPointConstant.L3_Shape_1;
		// log.info(Color.CYAN + " check " + checkPointName + Color.NORMAL);
		log.info(AnsiText.create(" check " + checkPointName + " NOT YET IMPLEMENTED !!!").fg(AnsiText.Color.WHITE)
				.bg(AnsiText.Color.RED));

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "shape" }, description = "3_Shape_2", priority = 82)
	public void verifyTest_3_Shape_2() throws Exception {
		String checkPointName = CheckPointConstant.L3_Shape_2;
		// log.info(Color.CYAN + " check " + checkPointName + Color.NORMAL);
		log.info(AnsiText.create(" check " + checkPointName + " NOT YET IMPLEMENTED !!!").fg(AnsiText.Color.WHITE)
				.bg(AnsiText.Color.RED));

	}

	/**
	 * @throws Exception
	 */
	@Test(groups = { "shape" }, description = "3_Shape_2", priority = 83)
	public void verifyTest_3_Shape_3() throws Exception {
		String checkPointName = CheckPointConstant.L3_Shape_3;
		// log.info(Color.CYAN + " check " + checkPointName + Color.NORMAL);
		log.info(AnsiText.create(" check " + checkPointName + " NOT YET IMPLEMENTED !!!").fg(AnsiText.Color.WHITE)
				.bg(AnsiText.Color.RED));

	}

}
