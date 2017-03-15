package mobi.chouette.exchange.importer;

import java.io.IOException;

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
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.JourneyPatternDAO;
import mobi.chouette.dao.RouteDAO;
import mobi.chouette.dao.StopPointDAO;
import mobi.chouette.dao.TimetableDAO;
import mobi.chouette.dao.VehicleJourneyAtStopDAO;
import mobi.chouette.dao.VehicleJourneyDAO;

@Log4j
@Stateless(name = CleanRepositoryCommand.COMMAND)
public class CleanRepositoryCommand implements Command {

	public static final String COMMAND = "CleanRepositoryCommand";


	@EJB
	private JourneyPatternDAO journeyPatternDAO;

	@EJB
	private RouteDAO routeDAO;

	@EJB
	private StopPointDAO stopPointDAO;

	@EJB
	private TimetableDAO timetableDAO;

	@EJB
	private VehicleJourneyDAO vehicleJourneyDAO;

	@EJB
	private VehicleJourneyAtStopDAO vehicleJourneyAtStopDAO;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean execute(Context context) throws Exception {

		boolean result = ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);
		log.debug("aaa");
		try {
			log.debug("nnnn");
			
			log.debug("mm");
			log.debug("oo");
			journeyPatternDAO.truncate();
			routeDAO.truncate();
			stopPointDAO.truncate();
			timetableDAO.truncate();
			vehicleJourneyDAO.truncate();
			vehicleJourneyAtStopDAO.truncate();

			result = SUCCESS;
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
		log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = null;
			try {
				String name = "java:app/mobi.chouette.exchange/" + COMMAND;
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
		CommandFactory.factories.put(CleanRepositoryCommand.class.getName(), new DefaultCommandFactory());
	}
}
