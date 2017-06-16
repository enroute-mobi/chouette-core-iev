package mobi.chouette.dao;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import mobi.chouette.model.ActionMessage;
import mobi.chouette.model.ActionResource;
import mobi.chouette.model.ImportMessage;

@Stateless  (name="ActionMessageDAO")
public class ActionMessageDAOImpl implements ActionMessageDAO {

	@EJB
	ImportMessageDAO importMessageDAO;
	
	@Override
	public ActionMessage createMessage(ActionResource resource) {
		ActionMessage message = null;
		switch (resource.getAction()) {
		case importer:
			message = new ImportMessage(resource.getTaskId(),resource.getId());
			break;
		case exporter:
//			resource = new ExportResource(job.getId());
			break;
		case validator:
//			resource = new ValidationResource(job.getId());
			break;
		}
		return message;
	}

	@Override
	public void saveMessage(ActionMessage message) {
		switch (message.getAction())
		{
		case exporter:
			break;
		case importer:
			ImportMessage importMessage = (ImportMessage) message;
			importMessageDAO.create(importMessage);
			break;
		case validator:
			break;
		
		}
		
	}

}
