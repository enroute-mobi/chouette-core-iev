package mobi.chouette.exchange.netex_stif;

import java.io.File;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.testng.annotations.Test;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.netex_stif.importer.NetexStifParserCommand;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.util.Referential;

public class NetexStifParserTest {

	private InitialContext initialContext ;

	
	private void init()
	{
		if (initialContext == null)
		{
			try {
				initialContext = new InitialContext();
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void valid() throws Exception {
		init();
		Context context = new Context();
		context.put(Constant.INITIAL_CONTEXT, initialContext);
		NetexStifParserCommand command = (NetexStifParserCommand) CommandFactory.create(initialContext, NetexStifParserCommand.class.getName());
		File f = new File("src/test/data/valid/line_test.xml");
		JobDataImpl job = new JobDataImpl();
		context.put(Constant.JOB_DATA, job);
		job.setAction(JobData.ACTION.importer);
		job.setType("netex");
		job.setPathName("target/referential/test");
		job.setReferential("chouette_gui");
		
		ActionReport report = new ActionReport();
		ValidationReport validationReport = new ValidationReport();
		command.setFileURL("file://"+f.getAbsolutePath());
		context.put(Constant.REPORT, report);
		context.put(Constant.REFERENTIAL, new Referential());
		context.put(Constant.VALIDATION_REPORT, validationReport);
		command.execute(context);
	}
}
