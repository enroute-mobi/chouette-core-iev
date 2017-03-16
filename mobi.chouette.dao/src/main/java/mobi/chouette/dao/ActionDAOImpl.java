package mobi.chouette.dao;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import mobi.chouette.common.JobData;
import mobi.chouette.model.ActionTask;
import mobi.chouette.model.ImportTask;

@Stateless  (name="ActionDAO")
public class ActionDAOImpl {

	@EJB
	ImportTaskDAO importTaskDAO;
	
//	@EJB
//	ExportTaskDAO exportTaskDAO;
//	
//	@EJB
//	ValidationTaskDAO validationTaskDAO;

	public ActionTask getTask(JobData job) {
		ActionTask task = null;
		switch (job.getAction()) {
		case importer:
			task = importTaskDAO.find(job.getId());
			break;
		case exporter:
//			task = exportTaskDAO.find(job.getId());
			break;
		case validator:
//			task = validationTaskDAO.find(job.getId());
			break;
		}
		return task;
	}
	
	public void saveTask(ActionTask task)
	{
		switch (task.getAction())
		{
		case exporter:
			break;
		case importer:
			ImportTask iTask = (ImportTask) task;
			importTaskDAO.update(iTask);
			break;
		case validator:
			break;
		
		}
	}
}
