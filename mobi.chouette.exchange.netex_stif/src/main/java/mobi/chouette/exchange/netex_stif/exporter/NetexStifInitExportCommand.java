package mobi.chouette.exchange.netex_stif.exporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.naming.InitialContext;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class NetexStifInitExportCommand implements Command {

	public static final String COMMAND = "NetexStifInitExportCommand";

	@Override
	public boolean execute(Context context) throws Exception {
		boolean result = Constant.ERROR;

		Monitor monitor = MonitorFactory.start(COMMAND);

		try {
			JobData jobData = (JobData) context.get(Constant.JOB_DATA);

			context.put(Constant.REFERENTIAL, new Referential());
			// prepare exporter
			Path path = Paths.get(jobData.getPathName(), Constant.OUTPUT);
			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}
			result = Constant.SUCCESS;

		} catch (Exception e) {
			log.error(e, e);
			throw e;
		} finally {
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}

		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = new NetexStifInitExportCommand();
			return result;
		}
	}

	static {
		CommandFactory.register(NetexStifInitExportCommand.class.getName(), new DefaultCommandFactory());
	}

}
