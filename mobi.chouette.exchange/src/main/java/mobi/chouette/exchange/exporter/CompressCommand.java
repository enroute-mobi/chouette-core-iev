package mobi.chouette.exchange.exporter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.naming.InitialContext;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.FileUtil;
import mobi.chouette.common.JobData;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;

import org.apache.commons.io.FileUtils;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

@Log4j
public class CompressCommand implements Command {

	public static final String COMMAND = "CompressCommand";

	@Override
	public boolean execute(Context context) throws Exception {

		boolean result = Constant.ERROR;

		Monitor monitor = MonitorFactory.start(COMMAND);

		try {
			JobData jobData = (JobData) context.get(Constant.JOB_DATA);
			String path = jobData.getPathName();
			String file = jobData.getOutputFilename();
			Path target = Paths.get(path, Constant.OUTPUT);
			Path filename = Paths.get(path, file);
			File outputFile = filename.toFile();
			if (outputFile.exists())
				outputFile.delete();
			FileUtil.compress(target.toString(), filename.toString());
			result = Constant.SUCCESS;
			deleteDirectory(target);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}

		return result;
	}

	private void deleteDirectory(Path dirPath) {
		try {
			FileUtils.deleteDirectory(dirPath.toFile());
		} catch (Exception e) {
			log.warn("cannot purge output directory " + e.getMessage());
		}
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			return new CompressCommand();
		}
	}

	static {
		CommandFactory.register(CompressCommand.class.getName(), new DefaultCommandFactory());
	}
}
