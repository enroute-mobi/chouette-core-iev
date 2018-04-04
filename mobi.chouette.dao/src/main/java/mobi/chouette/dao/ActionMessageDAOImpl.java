package mobi.chouette.dao;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import mobi.chouette.model.ActionMessage;
import mobi.chouette.model.ActionResource;
import mobi.chouette.model.compliance.ComplianceCheckMessage;
import mobi.chouette.model.exporter.ExportMessage;
import mobi.chouette.model.importer.ImportMessage;

@Stateless  (name="ActionMessageDAO")
public class ActionMessageDAOImpl implements ActionMessageDAO {

	@EJB
	ImportMessageDAO importMessageDAO;

	@EJB
	ComplianceCheckMessageDAO complianceCheckMessageDAO;

	@EJB
	ExportMessageDAO exportMessageDAO;

	@Override
	public ActionMessage createMessage(ActionResource resource) {
		ActionMessage message = null;
		switch (resource.getAction()) {
		case importer:
			message = new ImportMessage(resource.getTaskId(),resource.getId());
			break;
		case exporter:
			message = new ExportMessage(resource.getTaskId(),resource.getId());
			break;
		case validator:
			message  = new ComplianceCheckMessage(resource.getTaskId(),resource.getId());
			break;
		}
		return message;
	}

	@Override
	public void saveMessage(ActionMessage message) {
		switch (message.getAction())
		{
		case importer:
			ImportMessage importMessage = (ImportMessage) message;
			importMessageDAO.create(importMessage);
			break;
		case exporter:
			ExportMessage exportMessage = (ExportMessage) message;
			exportMessageDAO.create(exportMessage);
			break;
		case validator:
			ComplianceCheckMessage checkMessage = (ComplianceCheckMessage) message;
			complianceCheckMessageDAO.create(checkMessage);
			break;
		
		}
		
	}

}
