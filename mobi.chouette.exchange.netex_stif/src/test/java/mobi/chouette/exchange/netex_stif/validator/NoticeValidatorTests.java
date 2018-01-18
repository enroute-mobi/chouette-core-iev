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
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.FILE_STATE;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.Line;

@Log4j
public class NoticeValidatorTests extends AbstractTest {

	public static String TEST_FILENAME = NetexStifConstant.COMMUN_FILE_NAME;

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

		public void addTypeOfNoticeRef(String typeOfNoticeRef) {
			noticeValidator.addTypeOfNoticeRef(context, fakeFootnote.getObjectId(), typeOfNoticeRef);

		}

	}

	@BeforeSuite
	public void init() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		Locale.setDefault(Locale.ENGLISH);
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

	@Test(groups = { "Notice", "Label" }, description = "Nominal : Footnote label is set", priority = 801)
	public void verifyNoticeLabelIsSet() throws Exception {
		TestContext tc = new TestContext();
		tc.getFakeFootnote().setLabel("not-empty-label");
		validateLabelInFootNote(tc);
		Assert.assertTrue(tc.isResult());
		checkNoReports(tc.getContext(), TEST_FILENAME);
	}

	@Test(groups = { "Notice", "Label" }, description = "Error : Footnote label is not set or empty", priority = 802)
	public void verifyNoticeLabelIsNotSetOrEmpty() throws Exception {
		TestContext tc = new TestContext();

		// case of null value
		tc.getFakeFootnote().setLabel(null); //
		validateLabelInFootNote(tc);
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Notice_1, "2_netexstif_notice_1",
				null, FILE_STATE.ERROR);

		// case of empty value
		tc = new TestContext();
		tc.getFakeFootnote().setLabel(""); //
		validateLabelInFootNote(tc);
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Notice_1, "2_netexstif_notice_1",
				null, FILE_STATE.ERROR);
	}

	private void validateTypeOfNoticeRefOfFootNote(TestContext tc, String typeOfNoticeRef) {

		int lineNumber = 1;
		int columnNumber = 2;

		NoticeValidator validator = tc.getNoticeValidator();

		if (typeOfNoticeRef != null) {
			tc.addTypeOfNoticeRef(typeOfNoticeRef);
		}

		boolean result = validator.check2NeTExSTIFNotice2(tc.getContext(), tc.getFakeFootnote(), lineNumber,
				columnNumber);
		log.info("Validation Report ===>" + tc.getValidationReport().toString());
		log.info("Validation Report Result = " + tc.getValidationReport().getResult());
		log.info("Action Report ===>" + tc.getActionReport().toString());
		log.info("Action Report Result = " + tc.getActionReport().getResult());

		tc.setResult(result);
	}

	@Test(groups = { "Notice",
			"TypeOfNoticeRef" }, description = "Nominal : Footnote  TypeOfNoticeRef is incorrect", priority = 803)
	public void verifyTypeOfNoticeRefForGivenFootNoteCorrect() throws Exception {
		TestContext tc = new TestContext();

		validateTypeOfNoticeRefOfFootNote(tc, NetexStifConstant.SERVICE_JOURNEY_NOTICE);
		Assert.assertTrue(tc.isResult());
		checkNoReports(tc.getContext(), TEST_FILENAME);
	}

	@Test(groups = { "Notice",
			"TypeOfNoticeRef" }, description = "Error : Footnote  TypeOfNoticeRef is incorrect", priority = 804)
	public void verifyTypeOfNoticeRefForGivenFootNoteNotCorrect() throws Exception {
		TestContext tc = new TestContext();

		// case TypeOfNoticeRef null
		validateTypeOfNoticeRefOfFootNote(tc, null);
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Notice_2, "2_netexstif_notice_2",
				"null", FILE_STATE.WARNING);

		// case TypeOfNoticeRef empty
		tc = new TestContext();
		validateTypeOfNoticeRefOfFootNote(tc, "");
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Notice_2, "2_netexstif_notice_2", "",
				FILE_STATE.WARNING);

		// case TypeOfNoticeRef is not SERVICE_JOURNEY_NOTICE
		tc = new TestContext();
		validateTypeOfNoticeRefOfFootNote(tc, "bidule");
		Assert.assertFalse(tc.isResult());
		checkReports(tc.getContext(), TEST_FILENAME, NetexCheckPoints.L2_NeTExSTIF_Notice_2, "2_netexstif_notice_2",
				"bidule", FILE_STATE.WARNING);
	}
}
