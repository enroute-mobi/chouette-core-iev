package mobi.chouette.exchange.netex_stif.validator;

import java.util.Locale;

import org.apache.log4j.BasicConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.model.Direction;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.FILE_STATE;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.exchange.validation.report.ValidationReport;

@Log4j
public class DirectionValidatorTests extends AbstractTest {

	public static String TEST_FILENAME = "offre_xxx.xml";

	@Getter
	@Setter
	class TestContext {
		DirectionValidator directionValidator;
		Context context = new Context();
		ValidationReport validationReport = new ValidationReport();
		ActionReport actionReport = new ActionReport();
		Direction fakeDirection = new Direction();
		ServiceJourneyPatternValidator serviceJourneyPatternValidator;

		public TestContext() {
			context.put(Constant.REPORT, actionReport);
			context.put(Constant.VALIDATION_REPORT, validationReport);
			context.put(Constant.FILE_NAME, TEST_FILENAME);

			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addFileReport(context, TEST_FILENAME, IO_TYPE.INPUT);

			directionValidator = (DirectionValidator) ValidatorFactory.getValidator(context, DirectionValidator.class);

			fakeDirection.setObjectId("CITYWAY:Direction1234:LOC");
		}

		boolean result = false;

	}

	@BeforeSuite
	public void init() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		Locale.setDefault(Locale.ENGLISH);
	}

	/*
	 * -- check that attributes DirectionType OppositeDirectionRef are both not set
	 */
	private void validateAbsenceOfDirectionAttributes(TestContext tc, String directionType,
			String oppositeDirectionRef) {

		int lineNumber = 1;
		int columnNumber = 2;

		tc.getFakeDirection().setOppositeDirectionRef(oppositeDirectionRef);
		tc.getFakeDirection().setDirectionType(directionType);

		DirectionValidator validator = tc.getDirectionValidator();

		boolean result = validator.check2NeTExSTIFDirection2(tc.getContext(), tc.getFakeDirection(), lineNumber,
				columnNumber);
		log.info("Validation Report ===>" + tc.getValidationReport().getCheckPointErrors());
		log.info("Validation Report Result = " + tc.getValidationReport().getResult());
		log.info("Action Report ===>" + tc.getActionReport().toString());
		log.info("Action Report Result = " + tc.getActionReport().getResult());

		tc.setResult(result);
	}

	@Test(groups = { "Direction",
			"Attributes DirectionType OppositeDirectionRef" }, description = "Error 1: Direction incorrect : neither 'DirectionType', nor 'OppositeDirectionRef' ", priority = 601)
	public void verifyDirectionAttributeIncorrect() throws Exception {
		String directionType = "directionType-should-be-null-but-is-not";
		String oppositeDirectionRef = "oppositeDirectionRef-should-be-null-but-is-not";

		TestContext tc = null;

		// -- DirectionType OppositeDirectionRef are both incorrect
		tc = new TestContext();
		validateAbsenceOfDirectionAttributes(tc, directionType, oppositeDirectionRef);
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Direction_2,
				"2_netexstif_direction_2", NetexStifConstant.OPPOSITE_DIRECTION_REF, FILE_STATE.ERROR,2);

		// -- DirectionType is incorrect
		tc = new TestContext();
		validateAbsenceOfDirectionAttributes(tc, directionType, null);
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Direction_2,
				"2_netexstif_direction_2", NetexStifConstant.DIRECTION_TYPE, FILE_STATE.ERROR);

		// -- OppositeDirectionRef is incorrect
		tc = new TestContext();
		validateAbsenceOfDirectionAttributes(tc, null, oppositeDirectionRef);
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Direction_2,
				"2_netexstif_direction_2", NetexStifConstant.OPPOSITE_DIRECTION_REF, FILE_STATE.ERROR);
	}

	@Test(groups = { "Direction",
			"Attributes DirectionType OppositeDirectionRef" }, description = "Nominal : Direction correct : 'DirectionType' and 'OppositeDirectionRef' are not set ", priority = 602)
	public void verifyDirectionAttributeCorrect() throws Exception {
		String directionType = null;
		String oppositeDirectionRef = null;

		TestContext tc = null;

		// -- DirectionType OppositeDirectionRef are both incorrect
		tc = new TestContext();
		validateAbsenceOfDirectionAttributes(tc, directionType, oppositeDirectionRef);
		Assert.assertTrue(tc.isResult());
		checkNoReports(tc.getContext(), TEST_FILENAME);

	}

	/*
	 * -- check Direction name
	 */

	private void validateDirectionName(TestContext tc) {

		int lineNumber = 1;
		int columnNumber = 2;

		DirectionValidator validator = tc.getDirectionValidator();

		boolean result = validator.check2NeTExSTIFDirection1(tc.getContext(), tc.getFakeDirection(), lineNumber,
				columnNumber);
		log.info("Validation Report ===>" + tc.getValidationReport().toString());
		log.info("Validation Report Result = " + tc.getValidationReport().getResult());
		log.info("Action Report ===>" + tc.getActionReport().toString());
		log.info("Action Report Result = " + tc.getActionReport().getResult());

		tc.setResult(result);
	}

	@Test(groups = { "Direction", "Name" }, description = "Nominal : Direction name is set  ", priority = 603)
	public void verifyDirectionNameCorrect() throws Exception {

		TestContext tc = new TestContext();
		tc.getFakeDirection().setName("direction-name-correctly-set");
		validateDirectionName(tc);
		Assert.assertTrue(tc.isResult());
		checkNoReports(tc.getContext(), TEST_FILENAME);

	}

	@Test(groups = { "Direction", "Name" }, description = "Error : Direction name is not set  ", priority = 604)
	public void verifyDirectionNameNotCorrect() throws Exception {

		TestContext tc = new TestContext();
		tc.getFakeDirection().setName(null); // -- name not correct
		validateDirectionName(tc);
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Direction_1,
				"2_netexstif_direction_1", null, FILE_STATE.ERROR);

	}
}
