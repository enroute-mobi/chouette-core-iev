package mobi.chouette.exchange.netex_stif.exporter;

import java.io.IOException;
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
import mobi.chouette.exchange.report.ActionReporter;

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
		ActionReporter reporter = ActionReporter.Factory.getInstance();

		try {

			log.info("procesing global data ");
			ExportableData collection = (ExportableData) context.get(Constant.EXPORTABLE_DATA);

			collectCodifLigneData(context);
			
			NetexStifLignesProducer producer1 = new NetexStifLignesProducer();
			producer1.produce(context);

			if (collection.getMappedLines().size() > 1) {
				collectReflexData(context);
				NetexStifArretsProducer producer2 = new NetexStifArretsProducer();
				producer2.produce(context);
			}

			collection.clear();
			result = Constant.SUCCESS;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}
		return result;
	}

	private void collectReflexData(Context context) {
		// TODO Auto-generated method stub
		
	}

	private void collectCodifLigneData(Context context) {
		ExportableData collection = (ExportableData) context.get(Constant.EXPORTABLE_DATA);
        Set<Long> lineIds = collection.getMappedLines().keySet();
        
        NetexStifExportParameters parameters = (NetexStifExportParameters) context.get(Constant.CONFIGURATION);
        lineDao.findAll(parameters.getLineReferentialId(),lineIds);
		// TODO add llineReferential ID !!! 
        
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
