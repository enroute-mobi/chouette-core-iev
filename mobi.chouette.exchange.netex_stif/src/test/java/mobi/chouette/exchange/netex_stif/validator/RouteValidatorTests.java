package mobi.chouette.exchange.netex_stif.validator;

import org.testng.Assert;
import org.testng.annotations.Test;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.validation.report.ValidationReport;

public class RouteValidatorTests {

	@Getter
	@Setter
	class ReportAndResult {
		ValidationReport report;
		boolean result;

	}

	private ReportAndResult validateId(String directionType) {
		// RouteValidator
		// étend
		// AbstractParsingValidator
		Context context = new Context();
		int lineNumber = 0;
		int columnNumber = 0;
		ActionReport actionReport = new ActionReport();

		ValidationReport report = new ValidationReport();
		context.put(Constant.FILE_NAME, "fakeFilename");
		context.put(Constant.REPORT, actionReport);
		context.put(Constant.VALIDATION_REPORT, report);
		RouteValidator validator = new RouteValidator(context); // --

		boolean result = validator.check2NeTExSTIFRoute1(context, directionType, lineNumber, columnNumber);
		System.out.println("Report ===>" + report.toString());
		System.out.println("REPORT Result = " + report.getResult());

		ReportAndResult rar = new ReportAndResult();
		rar.setReport(report);
		rar.setResult(result);
		return rar;
	}

	@Test(groups = { "Cas erreur 1" }, description = "Route DirectionType incorrect ", priority = 3)
	public void verifyRouteDirectionTypeIncorrect() throws Exception {

		String directionType = "xxxx";
		ReportAndResult rar= validateId(directionType);

		Assert.assertTrue(!rar.isResult());
	}

	@Test(groups = { "Cas erreur 1" }, description = "Route DirectionType incorrect (null) ", priority = 3)
	public void verifyRouteDirectionTypeNull() throws Exception {

		String directionType = null;
		ReportAndResult rar = validateId(directionType);

		Assert.assertTrue(!rar.isResult());
	}

	@Test(groups = { "Nominal 1" }, description = "Route DirectionType is inbound", priority = 3)
	public void verifyRouteDirectionTypeWithInbound() throws Exception {

		String directionType = "inbound";
		ReportAndResult rar = validateId(directionType);

		Assert.assertTrue(!rar.isResult());
	}

	@Test(groups = { "Nominal 2" }, description = "Route DirectionType is outbound", priority = 3)
	public void verifyRouteDirectionTypeWithOutbound() throws Exception {

		String directionType = "outbound";
		ReportAndResult rar = validateId(directionType);

		Assert.assertTrue(!rar.isResult());
	}
}
