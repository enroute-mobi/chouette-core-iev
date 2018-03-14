package mobi.chouette.exchange.netex_stif.exporter;

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
import mobi.chouette.exchange.netex_stif.exporter.producer.NetexStifCalendriersProducer;
import mobi.chouette.exchange.netex_stif.exporter.producer.NetexStifCommunProducer;

@Log4j
public class NetexStifSharedOperatorDataProducerCommand implements Command {
	public static final String COMMAND = "NetexStifSharedOperatorDataProducerCommand";

	@Override
	public boolean execute(Context context) throws Exception {

		boolean result = Constant.ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);

		try {

			log.info("procesing common data for company");

			ExportableData collection = (ExportableData) context.get(Constant.EXPORTABLE_DATA);

			NetexStifCalendriersProducer producer1 = new NetexStifCalendriersProducer();
			producer1.produce(context);

			if (!collection.getNotices().isEmpty()) {
				NetexStifCommunProducer producer2 = new NetexStifCommunProducer();
				producer2.produce(context);
			}

			collection.clearCompany();
			result = Constant.SUCCESS;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}
		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = new NetexStifSharedOperatorDataProducerCommand();
			return result;
		}
	}

	static {
		CommandFactory.register(NetexStifSharedOperatorDataProducerCommand.class.getName(), new DefaultCommandFactory());
	}

}
