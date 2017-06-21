package mobi.chouette.exchange.netex_stif.importer;

import java.io.IOException;

import javax.naming.InitialContext;

import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.importer.UncompressCommand;
import mobi.chouette.exchange.report.ReportConstant;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;

/**
 * 
 * Encapsulate uncompress command to add validation reporting
 * 
 * execute use in context : 
 * <ul>
 * <li>REPORT</li>
 * <li>JOB_DATA</li>
 * </ul>
 * 
 * @author michel
 *
 */
public class NetexStifUncompressCommand implements Command, ReportConstant {

	public static final String COMMAND = "NetexStifUncompressCommand";
	
	public static final String L1_NetexStif_1 = "1-NeTExStif-1";

	@Override
	public boolean execute(Context context) throws Exception {

		boolean result = ERROR;
		InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
		Command uncompressCommand = CommandFactory.create(initialContext, UncompressCommand.class.getName());
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.addItemToValidationReport(context, L1_NetexStif_1, "E");
		validationReporter.updateCheckPointReportState(context, L1_NetexStif_1,ValidationReporter.RESULT.OK );
		
		result = uncompressCommand.execute(context);
		if (result == ERROR)
		{
			// add validation message to report
			JobData jobData = (JobData) context.get(JOB_DATA);
			String fileName = jobData.getInputFilename(); 
			validationReporter.addCheckPointReportError(context, L1_NetexStif_1, "1" , new DataLocation(fileName));
		}
		
		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = new NetexStifUncompressCommand();
			return result;
		}
	}

	static {
		CommandFactory.factories
				.put(NetexStifUncompressCommand.class.getName(), new DefaultCommandFactory());
	}
}
