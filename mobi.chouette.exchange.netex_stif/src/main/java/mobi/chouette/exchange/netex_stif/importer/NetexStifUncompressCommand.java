package mobi.chouette.exchange.netex_stif.importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.naming.InitialContext;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.FileUtil;
import mobi.chouette.common.JobData;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.importer.UncompressCommand;
import mobi.chouette.exchange.netex_stif.validator.NetexCheckPoints;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.ERROR_CODE;
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
@Log4j
public class NetexStifUncompressCommand implements Command, ReportConstant,NetexCheckPoints {

	public static final String COMMAND = "NetexStifUncompressCommand";

	@Override
	public boolean execute(Context context) throws Exception {

		boolean result = ERROR;
		InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
		Command uncompressCommand = CommandFactory.create(initialContext, UncompressCommand.class.getName());
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.addItemToValidationReport(context, L1_NetexStif_1, "E");
		validationReporter.updateCheckPointReportState(context, L1_NetexStif_1, ValidationReporter.RESULT.OK);

		result = uncompressCommand.execute(context);
		if (result == ERROR) {
			// add validation message to report
			JobData jobData = (JobData) context.get(JOB_DATA);
			String fileName = jobData.getInputFilename();
			validationReporter.addCheckPointReportError(context, L1_NetexStif_1, "1", new DataLocation(fileName));
		} else {
			JobData jobData = (JobData) context.get(JOB_DATA);
			String zipName = jobData.getInputFilename();
			// check mandatory files
			String pathName = jobData.getPathName();
			// Calendar
			File file = new File(pathName + "/" + INPUT + "/calendrier.xml");
			if (!file.exists()) {
                log.error("missing file : calendrier.xml");
				ActionReporter reporter = ActionReporter.Factory.getInstance();
				reporter.setActionError(context, ERROR_CODE.INVALID_PARAMETERS, "no calendrier.xml file");
				validationReporter.addCheckPointReportError(context, L1_NetexStif_1, "2",
						new DataLocation(zipName), "calendrier.xml");
				result = ERROR;
			}
			Path path = Paths.get(jobData.getPathName(), INPUT);
			List<Path> stream = FileUtil.listFiles(path, "offre*.xml", "*metadata*");
			if (stream.isEmpty()) {
                log.error("missing files : no offer file found");
				ActionReporter reporter = ActionReporter.Factory.getInstance();
				reporter.setActionError(context, ERROR_CODE.INVALID_PARAMETERS, "no offer data");
				validationReporter.addCheckPointReportError(context, L1_NetexStif_1, "3",
						new DataLocation(zipName));
				result = ERROR;
			}
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
		CommandFactory.factories.put(NetexStifUncompressCommand.class.getName(), new DefaultCommandFactory());
	}
}
