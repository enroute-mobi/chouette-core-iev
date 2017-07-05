package mobi.chouette.exchange.importer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.FootnoteDAO;
import mobi.chouette.exchange.parameters.AbstractImportParameter;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.util.Referential;

@Log4j
@Stateless(name = FootnoteRegisterCommand.COMMAND)
public class FootnoteRegisterCommand implements Command {

	public static final String COMMAND = "FootnoteRegisterCommand";

	@EJB
	private FootnoteDAO footnoteDAO;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean execute(mobi.chouette.common.Context context) throws Exception {
		
        // save has been abandoned
		AbstractImportParameter parameters = (AbstractImportParameter) context.get(CONFIGURATION);
		if (parameters.isNoSave()) return false;
		Referential referential = (Referential) context.get(REFERENTIAL);
		Map<String, Footnote> footnotes = referential.getFootnotes();
		log.info("footnotes:" + footnotes.size());
		Iterator<Footnote> iterator = footnotes.values().iterator();
		while (iterator.hasNext()) {
			Footnote footnote = iterator.next();
			footnoteDAO.create(footnote);
			footnoteDAO.flush();
		}
		
		return true;
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
		CommandFactory.factories.put(FootnoteRegisterCommand.class.getName(), new DefaultCommandFactory());
	}

}
