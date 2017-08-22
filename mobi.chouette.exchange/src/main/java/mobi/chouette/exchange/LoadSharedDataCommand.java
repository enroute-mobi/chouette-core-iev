package mobi.chouette.exchange;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
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
import mobi.chouette.dao.CompanyLiteDAO;
import mobi.chouette.dao.LineLiteDAO;
import mobi.chouette.dao.StopAreaLiteDAO;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.model.CompanyLite;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.util.Referential;

@Log4j
@Stateless(name = LoadSharedDataCommand.COMMAND)
public class LoadSharedDataCommand implements Command, Constant {

	public static final String COMMAND = "LoadSharedDataCommand";

	@EJB
	private LineLiteDAO lineDAO;

	@EJB
	private CompanyLiteDAO companyDAO;

	@EJB
	private StopAreaLiteDAO stopAreaDAO;

	@Override
	public boolean execute(Context context) throws Exception {
		boolean result = ERROR;

		Monitor monitor = MonitorFactory.start(COMMAND);

		try {
			Referential referential = (Referential) context.get(REFERENTIAL);
			AbstractParameter parameters = (AbstractParameter) context.get(CONFIGURATION);
			List<Long> ids = parameters.getIds();
			Set<Long> companyIds = new HashSet<>();
			for (LineLite line : lineDAO.findAll(parameters.getLineReferentialId(), ids)) {
				referential.getSharedReadOnlyLines().put(line.getObjectId(), line);
				companyIds.add(line.getCompanyId());
				if (line.getSecondaryCompanyIds() != null && line.getSecondaryCompanyIds().length > 0)
					companyIds.addAll(Arrays.asList(line.getSecondaryCompanyIds()));
				lineDAO.detach(line);
			}

			for (CompanyLite company : companyDAO.findAll(parameters.getLineReferentialId(), companyIds)) {
				referential.getSharedReadOnlyCompanies().put(company.getObjectId(), company);
				companyDAO.detach(company);
			}

			for (StopAreaLite stopArea : stopAreaDAO.findByType(parameters.getStopAreaReferentialId(), "zdep")) {
				referential.getSharedReadOnlyStopAreas().put(stopArea.getObjectId(), stopArea);
				stopAreaDAO.detach(stopArea);
			}
			for (StopAreaLite stopArea : stopAreaDAO.findByType(parameters.getStopAreaReferentialId(), "zdlp")) {
				referential.getSharedReadOnlyStopAreas().put(stopArea.getObjectId(), stopArea);
				stopAreaDAO.detach(stopArea);
			}

			result = SUCCESS;

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
		CommandFactory.factories.put(LoadSharedDataCommand.class.getName(), new DefaultCommandFactory());
	}

}
