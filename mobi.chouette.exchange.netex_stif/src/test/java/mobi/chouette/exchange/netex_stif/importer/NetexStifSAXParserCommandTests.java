package mobi.chouette.exchange.netex_stif.importer;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.BasicConfigurator;
import org.testng.Assert;
import org.testng.annotations.Test;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.JobDataTest;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter.FILE_STATE;
import mobi.chouette.exchange.report.FileReport;
import mobi.chouette.exchange.report.ReportConstant;
import mobi.chouette.exchange.validation.report.CheckPointErrorReport;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.util.Referential;
import mobi.chouette.persistence.hibernate.ContextHolder;
@Log4j
public class NetexStifSAXParserCommandTests implements Constant, ReportConstant {

	private static final String path = "src/test/data/";

	protected static InitialContext initialContext;

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

	protected Context initImportContext() {
		init();
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

		test.setPathName("target/referential/test");
		File f = new File("target/referential/test");
		if (f.exists())
			try {
				FileUtils.deleteDirectory(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		f.mkdirs();
		test.setReferential("chouette_gui");
		test.setAction(JobData.ACTION.importer);
		test.setType("netex_stif");
		context.put(TESTNG, "true");
		context.put(OPTIMIZED, Boolean.FALSE);
		return context;

	}


	@Test(groups = { "XML" }, description = "bad xml file", priority = 3)
	public void verifiyXMLSyntax() throws Exception{
		
		Context context = initImportContext();

		NetexStifSAXParserCommand parser = (NetexStifSAXParserCommand) CommandFactory.create(initialContext,
				NetexStifSAXParserCommand.class.getName());
		
		File f = new File(path, "badxmlfile.xml");
		parser.setFileURL("file://" + f.getAbsolutePath());
		parser.execute(context);
		// check error report
		ActionReport report = (ActionReport) context.get(REPORT);
		ValidationReport valReport = (ValidationReport) context.get(VALIDATION_REPORT);
		log.info(report);
		log.info(valReport);
		Assert.assertEquals(report.getResult(), "OK", "result");
		Assert.assertEquals(report.getFiles().size(), 1, "file reported size ");
		FileReport file = report.getFiles().get(0);
		Assert.assertEquals(file.getStatus(),FILE_STATE.ERROR,"file status reported");
		Assert.assertEquals(file.getCheckPointErrorCount(),1,"file error reported");
		CheckPointErrorReport error = valReport.getCheckPointErrors().get(file.getCheckPointErrorKeys().get(0).intValue());
		Assert.assertEquals(error.getTestId(),"1-NeTExStif-2","checkpoint code");
		Assert.assertEquals(error.getKey(),"1_netexstif_2","message code");
		Assert.assertTrue(error.getValue().contains("netex:ParticipantRef"),"value");
		Assert.assertEquals(error.getSource().getFile().getFilename(),"badxmlfile.xml","source filename");
		Assert.assertEquals(error.getSource().getFile().getLineNumber(),Integer.valueOf(190),"source line number");
		Assert.assertEquals(error.getSource().getFile().getColumnNumber(),Integer.valueOf(3),"source column number");		
		
	}
	@Test(groups = { "XML" }, description = "bad xsd file", priority = 4)
	public void verifiyXSDSyntax() throws Exception{
		
		Context context = initImportContext();

		NetexStifSAXParserCommand parser = (NetexStifSAXParserCommand) CommandFactory.create(initialContext,
				NetexStifSAXParserCommand.class.getName());
		
		File f = new File(path, "badxsdfile.xml");
		parser.setFileURL("file://" + f.getAbsolutePath());
		parser.execute(context);
		// check error report
		ActionReport report = (ActionReport) context.get(REPORT);
		ValidationReport valReport = (ValidationReport) context.get(VALIDATION_REPORT);
		log.info(report);
		log.info(valReport);
		Assert.assertEquals(report.getResult(), "OK", "result");
		Assert.assertEquals(report.getFiles().size(), 1, "file reported size ");
		FileReport file = report.getFiles().get(0);
		Assert.assertEquals(file.getStatus(),FILE_STATE.ERROR,"file status reported");
		Assert.assertEquals(file.getCheckPointErrorCount(),1,"file error reported");
		CheckPointErrorReport error = valReport.getCheckPointErrors().get(file.getCheckPointErrorKeys().get(0).intValue());
		Assert.assertEquals(error.getTestId(),"1-NeTExStif-2","checkpoint code");
		Assert.assertEquals(error.getKey(),"1_netexstif_2","message code");
		Assert.assertTrue(error.getValue().startsWith("cvc-complex-type"),"value");
		Assert.assertEquals(error.getSource().getFile().getFilename(),"badxsdfile.xml","source filename");
		Assert.assertEquals(error.getSource().getFile().getLineNumber(),Integer.valueOf(21),"source line number");
		Assert.assertEquals(error.getSource().getFile().getColumnNumber(),Integer.valueOf(62),"source column number");		
		
	}
}
