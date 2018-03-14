package mobi.chouette.dao;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import mobi.chouette.common.JobData;
import mobi.chouette.model.ActionResource;
import mobi.chouette.model.compliance.ComplianceCheckResource;
import mobi.chouette.model.exporter.ExportResource;
import mobi.chouette.model.importer.ImportResource;

@Stateless  (name="ActionResourceDAO")
public class ActionResourceDAOImpl implements ActionResourceDAO {

	@EJB
	ImportResourceDAO importResourceDAO;
	
	@EJB
	ComplianceCheckResourceDAO complianceCheckResourceDAO;
	
	@EJB
	ExportResourceDAO exportResourceDAO;
	
	@Override
	public ActionResource createResource(JobData job) {
		ActionResource resource = null;
		switch (job.getAction()) {
		case importer:
			resource = new ImportResource(job.getId());
			break;
		case exporter:
			resource = new ExportResource(job.getId());
			break;
		case validator:
			resource = new ComplianceCheckResource(job.getId());
			break;
		}
		return resource;
	}

	@Override
	public void saveResource(ActionResource resource) {
		switch (resource.getAction())
		{
		case importer:
			ImportResource importResource = (ImportResource) resource;
			importResourceDAO.create(importResource);
			break;
		case exporter:
			ExportResource exportResource = (ExportResource) resource;
			exportResourceDAO.create(exportResource);
			break;
		case validator:
			ComplianceCheckResource checkResource = (ComplianceCheckResource) resource;
			complianceCheckResourceDAO.create(checkResource);
			break;
		
		}
		
	}

}
