package mobi.chouette.exchange.netex_stif.importer;

import java.io.IOException;

import javax.naming.InitialContext;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.validation.ValidationData;
import mobi.chouette.model.util.Referential;

@Log4j
public class NetexStifInitImportCommand implements Command {

	public static final String COMMAND = "NetexStifInitImportCommand";

	@Override
	public boolean execute(Context context) throws Exception {
		boolean result = Constant.ERROR;

		Monitor monitor = MonitorFactory.start(COMMAND);

		try {
			context.put(Constant.REFERENTIAL, new Referential());
			if (context.get(Constant.VALIDATION) != null)
			   context.put(Constant.VALIDATION_DATA, new ValidationData());
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
			Command result = new NetexStifInitImportCommand();
			return result;
		}
	}

	static {
		CommandFactory.register(NetexStifInitImportCommand.class.getName(), new DefaultCommandFactory());
	}

}
