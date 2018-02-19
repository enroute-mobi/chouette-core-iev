package mobi.chouette.exchange.netex_stif.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.BasicConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.exchange.netex_stif.JobDataImpl;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.importer.NetexStifImportParameters;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.model.StopPointInJourneyPattern;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.FILE_STATE;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.Referential;
import mobi.chouette.persistence.hibernate.ContextHolder;

public class ServiceJourneyPatternValidatorTests extends AbstractTest {

	protected static InitialContext initialContext;

	protected Context initImportContext() {

		ContextHolder.setContext("chouette_gui"); // set tenant schema

		Context context = new Context();
		context.put(Constant.INITIAL_CONTEXT, initialContext);
		context.put(Constant.REPORT, new ActionReport());
		context.put(Constant.VALIDATION_REPORT, new ValidationReport());
		NetexStifImportParameters configuration = new NetexStifImportParameters();
		context.put(Constant.CONFIGURATION, configuration);
		context.put(Constant.REFERENTIAL, new Referential());
		context.put(NetexStifConstant.NETEX_STIF_OBJECT_FACTORY, new NetexStifObjectFactory());
		configuration.setName("name");
		configuration.setUserName("userName");
		configuration.setNoSave(true);
		configuration.setOrganisationName("organisation");
		configuration.setReferentialName("test");
		JobDataImpl test = new JobDataImpl();
		context.put(Constant.JOB_DATA, test);
		context.put(Constant.FILE_NAME, "offre_xxx.xml");
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		reporter.addFileReport(context, "offre_xxx.xml", IO_TYPE.INPUT);

		test.setAction(JobData.ACTION.importer);
		test.setType("netex_stif");
		context.put(Constant.TESTNG, "true");
		context.put(Constant.OPTIMIZED, Boolean.FALSE);
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


	@Test(groups = { "RouteRef" }, description = "test routeRef", priority = 1201)
	public void validateRouteRef() throws Exception {
		Context context = initImportContext();
		ServiceJourneyPatternValidator validator = (ServiceJourneyPatternValidator) ValidatorFactory.getValidator(context,
				ServiceJourneyPatternValidator.class);
		JourneyPattern jp = new JourneyPattern();
		String jpId = "CITYWAY:ServiceJourneyPattern:1234:LOC";
		jp.setObjectId(jpId);
		jp.setRoute(new Route());
		boolean res = validator.check2NeTExSTIFServiceJourneyPattern1 (context, jp, 1, 2);
		Assert.assertTrue(res, "validation should be ok");
		jp.setRoute(null);
		res = validator.check2NeTExSTIFServiceJourneyPattern1(context, jp, 1, 2);
		Assert.assertFalse(res, "validation should be nok");
		checkReports(context, "offre_xxx.xml", NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_1,
				"2_netexstif_servicejourneypattern_1", null, FILE_STATE.ERROR);
	}


	@Test(groups = { "PatternType" }, description = "test patternType", priority = 1202)
	public void validatePatternType() throws Exception {
		Context context = initImportContext();
		ServiceJourneyPatternValidator validator = (ServiceJourneyPatternValidator) ValidatorFactory.getValidator(context,
				ServiceJourneyPatternValidator.class);
		JourneyPattern jp = new JourneyPattern();
		String jpId = "CITYWAY:ServiceJourneyPattern:1234:LOC";
		jp.setObjectId(jpId);
		boolean res = validator.check2NeTExSTIFServiceJourneyPattern3(context, jp, 1, 2);
		Assert.assertFalse(res, "validation should be nok");
		context = initImportContext();
		validator = (ServiceJourneyPatternValidator) ValidatorFactory.getValidator(context,
				ServiceJourneyPatternValidator.class);
		validator.addPatternType(context, jpId, "");
		res = validator.check2NeTExSTIFServiceJourneyPattern3(context, jp, 1, 2);
		Assert.assertFalse(res, "validation should be nok");
		checkReports(context, "offre_xxx.xml", NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_3,
				"2_netexstif_servicejourneypattern_3_1", null, FILE_STATE.ERROR);
		context = initImportContext();
		validator = (ServiceJourneyPatternValidator) ValidatorFactory.getValidator(context,
				ServiceJourneyPatternValidator.class);
		validator.addPatternType(context, jpId, "other");
		res = validator.check2NeTExSTIFServiceJourneyPattern3(context, jp, 1, 2);
		Assert.assertFalse(res, "validation should be nok");
		checkReports(context, "offre_xxx.xml", NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_3,
				"2_netexstif_servicejourneypattern_3_2", "other", FILE_STATE.ERROR);
		context = initImportContext();
		validator = (ServiceJourneyPatternValidator) ValidatorFactory.getValidator(context,
				ServiceJourneyPatternValidator.class);
		validator.addPatternType(context, jpId, "passenger");
		checkNoReports(context, "offre_xxx.xml");
		res = validator.check2NeTExSTIFServiceJourneyPattern3(context, jp, 1, 2);
		Assert.assertTrue(res, "validation should be ok");
	}

	
	@Test(groups = { "Order" }, description = "test order", priority = 1203)
	public void validateOrder() throws Exception {
		Context context = initImportContext();
		ServiceJourneyPatternValidator validator = (ServiceJourneyPatternValidator) ValidatorFactory.getValidator(context,
				ServiceJourneyPatternValidator.class);
		JourneyPattern jp = new JourneyPattern();
		jp.setObjectId("CITYWAY:ServiceJourneyPattern:1234:LOC");
		List<StopPointInJourneyPattern> spjps = new ArrayList<>();
		for (int i = 0; i < 100; ++i){
			StopPoint p = new StopPoint();
			p.setObjectId("CITYWAY:StopPoint:"+i+":LOC");
			p.setPosition(i);
			jp.addStopPoint(p);
			StopPointInJourneyPattern spjp = new StopPointInJourneyPattern("CITYWAY:StopPointInJourneyPattern:"+i+":LOC", Integer.valueOf(i));
			validator.addStopPointInJourneyPattern(context, jp.getObjectId(), spjp);
			spjps.add(spjp);
		}
		boolean res = validator.check2NeTExSTIFServiceJourneyPattern4(context, jp, 1, 2);
		Assert.assertTrue(res, "validation should be ok");
		spjps.get(50).setOrder(3);
		res = validator.check2NeTExSTIFServiceJourneyPattern4(context, jp, 1, 2);
		Assert.assertFalse(res, "validation should be nok");
		checkReports(context, "offre_xxx.xml", NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_4,
				"2_netexstif_servicejourneypattern_4", null, FILE_STATE.ERROR);
		context = initImportContext();
	    validator = (ServiceJourneyPatternValidator) ValidatorFactory.getValidator(context,
				ServiceJourneyPatternValidator.class);
		jp = new JourneyPattern();
		spjps.clear();
		jp.setObjectId("CITYWAY:ServiceJourneyPattern:1234:LOC");
		for (int i = 0; i < 100; ++i){
			StopPoint p = new StopPoint();
			p.setObjectId("CITYWAY:StopPoint:"+i+":LOC");
			p.setPosition(i);
			jp.addStopPoint(p);
			StopPointInJourneyPattern spjp = new StopPointInJourneyPattern("CITYWAY:StopPointInJourneyPattern:"+i+":LOC", Integer.valueOf(i));
			validator.addStopPointInJourneyPattern(context, jp.getObjectId(), spjp);
			spjps.add(spjp);
			i++;
		}
		res = validator.check2NeTExSTIFServiceJourneyPattern4(context, jp, 1, 2);
		Assert.assertTrue(res, "validation should be ok");
		spjps.get(2).setOrder(2);
		res = validator.check2NeTExSTIFServiceJourneyPattern4(context, jp, 1, 2);
		Assert.assertFalse(res, "validation should be nok");
		checkReports(context, "offre_xxx.xml", NetexCheckPoints.L2_NeTExSTIF_ServiceJourneyPattern_4,
				"2_netexstif_servicejourneypattern_4", null, FILE_STATE.ERROR);
	}
		
		
}
