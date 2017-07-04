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
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.FILE_STATE;
import mobi.chouette.exchange.report.FileReport;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.exchange.validation.report.CheckPointErrorReport;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.Line;

@Log4j
public class NoticeValidatorTests implements Constant {

	public static String TEST_FILENAME = "offre_xxx.xml";

	@Getter
	@Setter
	class TestContext {
		NoticeValidator noticeValidator;
		Context context = new Context();
		ValidationReport validationReport = new ValidationReport();
		ActionReport actionReport = new ActionReport();
		Footnote fakeFootnote = new Footnote();
		boolean result = false;

		public TestContext() {
			context.put(Constant.REPORT, actionReport);
			context.put(Constant.VALIDATION_REPORT, validationReport);
			context.put(Constant.FILE_NAME, TEST_FILENAME);

			ActionReporter reporter = ActionReporter.Factory.getInstance();
			reporter.addFileReport(context, TEST_FILENAME, IO_TYPE.INPUT);

			noticeValidator = (NoticeValidator) ValidatorFactory.getValidator(context, NoticeValidator.class);

			Line fakeLine = new Line();
			fakeLine.setObjectId("STIF:line:1234:LOC");
			fakeFootnote.setLine(fakeLine);
			fakeFootnote.setObjectId("CITYWAY:Footnote:1234:LOC");
		}

	}

	@BeforeSuite
	public void init() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		Locale.setDefault(Locale.ENGLISH);
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

	private void validateLabelInFootNote(TestContext tc) {

		int lineNumber = 1;
		int columnNumber = 2;

		NoticeValidator validator = tc.getNoticeValidator();

		boolean result = validator.check2NeTExSTIFNotice1(tc.getContext(), tc.getFakeFootnote(), lineNumber,
				columnNumber);
		log.info("Validation Report ===>" + tc.getValidationReport().toString());
		log.info("Validation Report Result = " + tc.getValidationReport().getResult());
		log.info("Action Report ===>" + tc.getActionReport().toString());
		log.info("Action Report Result = " + tc.getActionReport().getResult());

		tc.setResult(result);
	}

	@Test(groups = { "Notice", "Label" }, description = "Nominal : Footnote label is set", priority = 1)
	public void verifyNoticeLabelIsSet() throws Exception {
		TestContext tc = new TestContext();
		tc.getFakeFootnote().setLabel("not-empty-label");
		validateLabelInFootNote(tc);
		Assert.assertTrue(tc.isResult());
		checkNoReports(tc.getContext(), TEST_FILENAME);
	}

	@Test(groups = { "Notice", "Label" }, description = "Error : Footnote label is not set or empty", priority = 1)
	public void verifyNoticeLabelIsNotSetOrEmpty() throws Exception {
		TestContext tc = new TestContext();
		tc.getFakeFootnote().setLabel(null); //
		validateLabelInFootNote(tc);
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Notice_1, "2_netexstif_notice_1",
				null, FILE_STATE.ERROR);
	}

}
