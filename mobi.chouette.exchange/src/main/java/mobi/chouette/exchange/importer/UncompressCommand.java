package mobi.chouette.exchange.importer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.naming.InitialContext;

import org.apache.commons.io.FilenameUtils;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.FileUtil;
import mobi.chouette.common.JobData;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.IO_TYPE;

/**
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
public class UncompressCommand implements Command {

	public static final String COMMAND = "UncompressCommand";

	@Override
	public boolean execute(Context context) throws Exception {

		boolean result = Constant.ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		JobData jobData = (JobData) context.get(Constant.JOB_DATA);

		String path = jobData.getPathName();
		String file = jobData.getInputFilename(); 
		if (file == null)
		{
			reporter.setActionError(context, ActionReporter.ERROR_CODE.INVALID_PARAMETERS,"Missing input file");
			return result;
		}
		Path filename = Paths.get(path, file);
		Path target = Paths.get(path, Constant.INPUT);
		if (!target.toFile().exists()) {
			Files.createDirectories(target);
		}
		if (FilenameUtils.getExtension(filename.toString()).equalsIgnoreCase("zip"))
		{
            reporter.addZipReport(context, file,IO_TYPE.INPUT);
			try {
				FileUtil.uncompress(filename.toString(), target.toString());
				result = Constant.SUCCESS;
			} catch (Exception e) {
				log.error(e.getMessage());
				reporter.addZipErrorInReport(context, file, ActionReporter.FILE_ERROR_CODE.READ_ERROR, e.getMessage());
				reporter.setActionError(context, ActionReporter.ERROR_CODE.INVALID_PARAMETERS,"invalid_zip");
			}
		}
		else
		{
			org.apache.commons.io.FileUtils.copyFileToDirectory(filename.toFile(), target.toFile());
			result = Constant.SUCCESS;
		}

		log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			return new UncompressCommand();
		}
	}

	static {
		CommandFactory.register(UncompressCommand.class.getName(), new DefaultCommandFactory());
	}
}
