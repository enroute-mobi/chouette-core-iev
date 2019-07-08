package mobi.chouette.exchange.netex_stif.exporter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.LineDAO;
import mobi.chouette.dao.StopAreaDAO;
import mobi.chouette.exchange.netex_stif.exporter.producer.NetexStifArretsProducer;
import mobi.chouette.exchange.netex_stif.exporter.producer.NetexStifLignesProducer;
import mobi.chouette.model.Line;
import mobi.chouette.model.StopArea;

@Log4j
@Stateless(name = DaoNetexStifSharedDataProducerCommand.COMMAND)
public class DaoNetexStifSharedDataProducerCommand implements Command {
	public static final String COMMAND = "DaoNetexStifSharedDataProducerCommand";

	@EJB
	private LineDAO lineDao;

	@EJB
	private StopAreaDAO stopAreaDao;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean execute(Context context) throws Exception {

		boolean result = Constant.ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);

		try {

			log.info("procesing global data ");
			ExportableData collection = (ExportableData) context.get(Constant.EXPORTABLE_DATA);

			collectCodifLigneData(context);

			NetexStifLignesProducer producer1 = new NetexStifLignesProducer();
			producer1.produce(context);
			collection.clearLineReferential();

			if (collection.getMappedLines().size() > 1) {
				collectReflexData(context);
				NetexStifArretsProducer producer2 = new NetexStifArretsProducer();
				producer2.produce(context);
				collection.clearStopAreaReferential();
			}

			collection.clearAll();
			result = Constant.SUCCESS;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}
		return result;
	}

	private void collectReflexData(Context context) {
		ExportableData collection = (ExportableData) context.get(Constant.EXPORTABLE_DATA);
		collection.getStopAreas().clear();
		collection.getQuays().clear();
		collection.getCommercialStops().clear();
		collection.getStopPlaces().clear();
		NetexStifExportParameters parameters = (NetexStifExportParameters) context.get(Constant.CONFIGURATION);
		List<StopArea> stops = stopAreaDao.findAll(parameters.getStopAreaReferentialId());
		collection.getStopAreas().addAll(stops);
		for (StopArea l : stops) {
			if (l.getAreaType()!=null) {
				switch (l.getAreaType()) {
					case "zder":
						collection.getZders().add(l);
						break;
					case "zdep":
						collection.getZdeps().add(l);
						break;
					case "zdlp":
						collection.getZdlps().add(l);
						break;
					case "zdlr":
						collection.getZdlrs().add(l);
						break;
					case "lda":
						collection.getLdas().add(l);
						break;
					case "gdl":
						collection.getGdls().add(l);
						break;
				}
			}
		}
	}

	private void collectCodifLigneData(Context context) {
		ExportableData collection = (ExportableData) context.get(Constant.EXPORTABLE_DATA);
		Set<Long> lineIds = collection.getMappedLines().keySet();

		NetexStifExportParameters parameters = (NetexStifExportParameters) context.get(Constant.CONFIGURATION);
		List<Line> lines = lineDao.findAll(parameters.getLineReferentialId(), lineIds);
		collection.getLines().addAll(lines);
		lines.forEach(l -> {
			collection.getNetworks().add(l.getNetwork());
			collection.getCompanies().add(l.getCompany());
			collection.getLineNotices().addAll(l.getLineNotices());
			collection.getGroupOfLines().addAll(l.getGroupOfLines());
		});

	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = null;
			try {
				String name = "java:app/mobi.chouette.exchange.netex_stif/" + COMMAND;
				result = (Command) context.lookup(name);
			} catch (NamingException e) {
				// try another way on test context
				String name = "java:module/" + COMMAND;
				try {
					result = (Command) context.lookup(name);
				} catch (NamingException e1) {
					log.error(e);
				}
			}
			return result;
		}
	}

	static {
		CommandFactory.register(DaoNetexStifSharedDataProducerCommand.class.getName(), new DefaultCommandFactory());
	}

}
