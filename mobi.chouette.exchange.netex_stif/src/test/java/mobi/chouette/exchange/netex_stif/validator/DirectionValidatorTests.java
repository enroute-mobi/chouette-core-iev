package mobi.chouette.exchange.netex_stif.validator;

import java.util.Locale;

import org.apache.log4j.BasicConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.Direction;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.FILE_STATE;
import mobi.chouette.exchange.report.FileReport;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.exchange.validation.report.CheckPointErrorReport;
import mobi.chouette.exchange.validation.report.ValidationReport;

@Log4j
public class DirectionValidatorTests implements Constant {

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

	private void validateAbsenceOfDirectionAttributes(TestContext tc, String directionType,
			String oppositeDirectionRef) {

		int lineNumber = 1;
		int columnNumber = 2;

		tc.getFakeDirection().setOppositeDirectionRef(oppositeDirectionRef);
		tc.getFakeDirection().setDirectionType(directionType);

		DirectionValidator validator = tc.getDirectionValidator();

		boolean result = validator.check2NeTExSTIFDirection2(tc.getContext(), tc.getFakeDirection(), lineNumber,
				columnNumber);
		log.info("Validation Report ===>" + tc.getValidationReport().toString());
		log.info("Validation Report Result = " + tc.getValidationReport().getResult());
		log.info("Action Report ===>" + tc.getActionReport().toString());
		log.info("Action Report Result = " + tc.getActionReport().getResult());

		tc.setResult(result);
	}

	private void checkNoReports(Context context, String fileName) {
		ActionReport report = (ActionReport) context.get(REPORT);

		ValidationReport valReport = (ValidationReport) context.get(VALIDATION_REPORT);
		log.info(report);
		log.info(valReport);
		Assert.assertEquals(report.getResult(), "OK", "result");
		Assert.assertEquals(report.getFiles().size(), 1, "file reported size ");
		FileReport file = report.getFiles().get(0);
		Assert.assertEquals(file.getStatus(), FILE_STATE.OK, "file status reported");
		Assert.assertEquals(file.getCheckPointErrorCount(), 0, "no file error reported");

	}

	private void checkReports(Context context, String fileName, String checkPointCode, String messageCode, String value,
			FILE_STATE fileState) {
		ActionReport report = (ActionReport) context.get(REPORT);

		ValidationReport valReport = (ValidationReport) context.get(VALIDATION_REPORT);
		log.info(report);
		log.info(valReport.getCheckPointErrors());
		Assert.assertEquals(report.getResult(), "OK", "result");
		Assert.assertEquals(report.getFiles().size(), 1, "file reported size ");
		FileReport file = report.getFiles().get(0);
		CheckPointErrorReport error = null;
		if (fileState.equals(FILE_STATE.ERROR)) {
			Assert.assertEquals(file.getStatus(), FILE_STATE.ERROR, "file status reported");
			Assert.assertEquals(file.getCheckPointErrorCount(), 1, "file error reported");
			error = valReport.getCheckPointErrors().get(file.getCheckPointErrorKeys().get(0).intValue());
		} else {
			Assert.assertEquals(file.getStatus(), FILE_STATE.OK, "file status reported");
			Assert.assertEquals(file.getCheckPointWarningCount(), 1, "file warning reported");
			error = valReport.getCheckPointErrors().get(file.getCheckPointWarningKeys().get(0).intValue());
		}
		Assert.assertEquals(error.getTestId(), checkPointCode, "checkpoint code");
		Assert.assertEquals(error.getKey(), messageCode, "message code");
		if (value == null)
			Assert.assertNull(error.getValue(), "value");
		else
			Assert.assertEquals(error.getValue(), value, "value");
		Assert.assertNotNull(error.getSource().getObjectId(), "source objectId");
		Assert.assertEquals(error.getSource().getFile().getFilename(), fileName, "source filename");
		Assert.assertEquals(error.getSource().getFile().getLineNumber(), Integer.valueOf(1), "source line number");
		Assert.assertEquals(error.getSource().getFile().getColumnNumber(), Integer.valueOf(2), "source column number");

	}

	@Test(groups = { "Direction",
			"Attributes DirectionType OppositeDirectionRef" }, description = "Error 1: Direction incorrect : neither 'DirectionType', nor 'OppositeDirectionRef' ", priority = 1)
	public void verifyDirectionAttributeIncorrect() throws Exception {
		String directionType = "directionType-should-be-null-but-is-not";
		String oppositeDirectionRef = "oppositeDirectionRef-should-be-null-but-is-not";

		TestContext tc = null;

		// -- DirectionType OppositeDirectionRef are both incorrect
		tc = new TestContext();
		validateAbsenceOfDirectionAttributes(tc, directionType, oppositeDirectionRef);
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Direction_2,
				"2_netexstif_direction_2", oppositeDirectionRef, FILE_STATE.ERROR);

		// -- DirectionType is incorrect
		tc = new TestContext();
		validateAbsenceOfDirectionAttributes(tc, directionType, null);
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Direction_2,
				"2_netexstif_direction_2", directionType, FILE_STATE.ERROR);

		// -- OppositeDirectionRef is incorrect
		tc = new TestContext();
		validateAbsenceOfDirectionAttributes(tc, null, oppositeDirectionRef);
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Direction_2,
				"2_netexstif_direction_2", oppositeDirectionRef, FILE_STATE.ERROR);
	}

	@Test(groups = { "Direction",
			"Attributes DirectionType OppositeDirectionRef" }, description = "Nominal : Direction correct : 'DirectionType' and 'OppositeDirectionRef' are not set ", priority = 1)
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

}
