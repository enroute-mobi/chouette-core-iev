package mobi.chouette.exchange.netex_stif.validator;

import org.testng.Assert;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter.FILE_STATE;
import mobi.chouette.exchange.report.FileReport;
import mobi.chouette.exchange.validation.report.CheckPointErrorReport;
import mobi.chouette.exchange.validation.report.ValidationReport;

@Log4j

public abstract class AbstractTest implements Constant {

	protected void checkReports(Context context, String fileName, String checkPointCode, String messageCode, String value,
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


	protected final void checkNoReports(Context context, String fileName) {
		ActionReport report = (ActionReport) context.get(REPORT);

		ValidationReport valReport = (ValidationReport) context.get(VALIDATION_REPORT);
		log.info(report);
		log.info(valReport.getCheckPointErrors());
		Assert.assertEquals(report.getResult(), "OK", "result");
		Assert.assertEquals(report.getFiles().size(), 1, "file reported size ");
		FileReport file = report.getFiles().get(0);
		Assert.assertEquals(file.getStatus(), FILE_STATE.OK, "file status reported");
		Assert.assertEquals(file.getCheckPointErrorCount(), 0, "no file error reported");

	}


}
