package mobi.chouette.exchange.netex_stif.validator;

import org.testng.annotations.Test;

import com.vividsolutions.jts.util.Assert;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.netex_stif.validator.RouteValidator;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.validation.report.ValidationReport;

public class RouteValidatorTests {

	private boolean validateId(String directionType) {
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
		return result;
	}

	@Test(groups = { "Cas erreur 1" }, description = "Route DirectionType incorrect ", priority = 3)
	public void verifyRouteDirectionTypeIncorrect() throws Exception {

		String directionType = "xxxx";
		boolean result = validateId(directionType);

		Assert.isTrue(!result);
	}

	@Test(groups = { "Cas erreur 1" }, description = "Route DirectionType incorrect (null) ", priority = 3)
	public void verifyRouteDirectionTypeNull() throws Exception {

		String directionType = null;
		boolean result = validateId(directionType);

		Assert.isTrue(!result);
	}

	@Test(groups = { "Nominal 1" }, description = "Route DirectionType is inbound", priority = 3)
	public void verifyRouteDirectionTypeWithInbound() throws Exception {

		String directionType = "inbound";
		boolean result = validateId(directionType);

		Assert.isTrue(result);
	}

	@Test(groups = { "Nominal 2" }, description = "Route DirectionType is outbound", priority = 3)
	public void verifyRouteDirectionTypeWithOutbound() throws Exception {

		String directionType = "outbound";
		boolean result = validateId(directionType);

		Assert.isTrue(result);
	}
}
