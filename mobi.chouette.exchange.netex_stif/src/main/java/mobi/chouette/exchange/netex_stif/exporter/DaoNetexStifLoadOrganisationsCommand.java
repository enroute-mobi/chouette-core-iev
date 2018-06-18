package mobi.chouette.exchange.netex_stif.exporter;

import java.io.IOException;
import java.util.List;

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
import mobi.chouette.dao.OrganisationDAO;
import mobi.chouette.model.Organisation;

@Log4j
@Stateless(name = DaoNetexStifLoadOrganisationsCommand.COMMAND)
public class DaoNetexStifLoadOrganisationsCommand implements Command {

	public static final String COMMAND = "DaoNetexStifLoadOrganisationsCommand";

	@EJB
	private OrganisationDAO organisationDAO;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean execute(Context context) throws Exception {

		boolean result = Constant.SUCCESS;
		Monitor monitor = MonitorFactory.start(COMMAND);

		try {
			ExportableData collection = (ExportableData) context.computeIfAbsent(Constant.EXPORTABLE_DATA,
					c -> new ExportableData());
			List<Organisation> list = organisationDAO.findAll();
			/* If there is no organisation fetched */
			if (list.isEmpty()) {
				result = Constant.ERROR;
			}
			
			list.forEach(l -> {
				if (l.getCode() != null)
					collection.getOrganisations().put(l.getCode(), l);
			});

		} catch (Exception e) {
			result = Constant.ERROR;
			log.error(e.getMessage(), e);
		} finally {
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}
		return result;
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
		CommandFactory.register(DaoNetexStifLoadOrganisationsCommand.class.getName(), new DefaultCommandFactory());
	}
}
