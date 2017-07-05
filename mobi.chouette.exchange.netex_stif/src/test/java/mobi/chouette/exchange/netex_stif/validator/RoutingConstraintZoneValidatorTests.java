package mobi.chouette.exchange.netex_stif.validator;

import java.util.Locale;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.BasicConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.JobDataTest;
import mobi.chouette.exchange.netex_stif.importer.NetexStifImportParameters;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.model.RoutingConstraintZone;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.FILE_STATE;
import mobi.chouette.exchange.report.FileReport;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.exchange.validation.report.CheckPointErrorReport;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.util.Referential;
import mobi.chouette.persistence.hibernate.ContextHolder;

@Log4j
public class RoutingConstraintZoneValidatorTests extends AbstractTest {

	protected static InitialContext initialContext;


	protected Context initImportContext() {

		ContextHolder.setContext("chouette_gui"); // set tenant schema

		Context context = new Context();
		context.put(INITIAL_CONTEXT, initialContext);
		context.put(REPORT, new ActionReport());
		context.put(VALIDATION_REPORT, new ValidationReport());
		NetexStifImportParameters configuration = new NetexStifImportParameters();
		context.put(CONFIGURATION, configuration);
		context.put(REFERENTIAL, new Referential());
		context.put(NETEX_STIF_OBJECT_FACTORY, new NetexStifObjectFactory());
		configuration.setName("name");
		configuration.setUserName("userName");
		configuration.setNoSave(true);
		configuration.setOrganisationName("organisation");
		configuration.setReferentialName("test");
		JobDataTest test = new JobDataTest();
		context.put(JOB_DATA, test);
		context.put(FILE_NAME, "offre_xxx.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "offre_xxx.xml", IO_TYPE.INPUT);

		test.setAction(JobData.ACTION.importer);
		test.setType("netex_stif");
		context.put(TESTNG, "true");
		context.put(OPTIMIZED, Boolean.FALSE);
		return context;
	}

	@BeforeSuite
	public void init() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		Locale.setDefault(Locale.ENGLISH);
		if (initialContext == null) {
			try {
				initialContext = new InitialContext();
			} catch (NamingException e) {
				e.printStackTrace();
			}

		}
	}

	@Test(groups = { "RoutingConstraintZone" }, description = "not enought ScheduledStopPoints", priority = 1)
	public void validateNotEnoughStops() throws Exception {
		Context context = initImportContext();
		RoutingConstraintZone zone = new RoutingConstraintZone();
		zone.setObjectId("CITYWAY:RoutingConstraintZone:1234:LOC");
		zone.setName("nom de la zone");
		zone.getStopPointsRef().add("st1");
		RoutingConstraintZoneValidator validator = (RoutingConstraintZoneValidator) ValidatorFactory.getValidator(context,
				RoutingConstraintZoneValidator.class);

		boolean res = validator.check2NeTExSTIFRoutingConstraintZone1(context, zone, 1, 2);
		Assert.assertFalse(res, "validation should be not ok");
		checkReports(context, "offre_xxx.xml", NetexCheckPoints.L2_NeTExSTIF_RoutingConstraintZone_1,
				"2_netexstif_routingconstraintzone_1", null);

	}


	@Test(groups = { "RoutingConstraintZone" }, description = "missing zoneUse", priority = 1)
	public void validateMissingZoneUse() throws Exception {
		Context context = initImportContext();
		RoutingConstraintZone zone = new RoutingConstraintZone();
		zone.setObjectId("CITYWAY:RoutingConstraintZone:1234:LOC");
		zone.setName("nom de la zone");
		RoutingConstraintZoneValidator validator = (RoutingConstraintZoneValidator) ValidatorFactory.getValidator(context,
				RoutingConstraintZoneValidator.class);

		boolean res = validator.check2NeTExSTIFRoutingConstraintZone2(context, zone, 1, 2);
		Assert.assertFalse(res, "validation should be not ok");
		checkReports(context, "offre_xxx.xml", NetexCheckPoints.L2_NeTExSTIF_RoutingConstraintZone_2,
				"2_netexstif_routingconstraintzone_2", "null");

	}

	@Test(groups = { "RoutingConstraintZone" }, description = "wrong zone use", priority = 2)
	public void validatewrongZoneUse() throws Exception {

		Context context = initImportContext();
		RoutingConstraintZone zone = new RoutingConstraintZone();
		zone.setObjectId("CITYWAY:RoutingConstraintZone:1234:LOC");
		zone.setName("nom de la zone");
		zone.setZoneUse("autre");
		RoutingConstraintZoneValidator validator = (RoutingConstraintZoneValidator) ValidatorFactory.getValidator(context,
				RoutingConstraintZoneValidator.class);

		boolean res = validator.check2NeTExSTIFRoutingConstraintZone2(context, zone, 1, 2);
		Assert.assertFalse(res, "validation should be not ok");
		checkReports(context, "offre_xxx.xml", NetexCheckPoints.L2_NeTExSTIF_RoutingConstraintZone_2,
				"2_netexstif_routingconstraintzone_2", "autre");

	}


}
