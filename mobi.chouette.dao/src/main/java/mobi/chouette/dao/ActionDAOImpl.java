package mobi.chouette.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import mobi.chouette.common.JobData;
import mobi.chouette.model.ActionTask;
import mobi.chouette.model.compliance.ComplianceCheckTask;
import mobi.chouette.model.importer.ImportTask;

@Stateless(name = "ActionDAO")
public class ActionDAOImpl implements ActionDAO {

	@EJB
	ImportTaskDAO importTaskDAO;

	@EJB
	ComplianceCheckTaskDAO complianceCheckTaskDAO;

	// @EJB
	// ExportTaskDAO exportTaskDAO;
	//

	@Override
	public ActionTask getTask(JobData job) {
		switch (job.getAction()) {
		case importer:
			return importTaskDAO.find(job.getId());
		case exporter:
			// return exportTaskDAO.find(job.getId());
			break;
		case validator:
			return complianceCheckTaskDAO.find(job.getId());
		}
		return null;
	}

	@Override
	public void saveTask(ActionTask task) {
		switch (task.getAction()) {
		case exporter:
			break;
		case importer:
			ImportTask iTask = (ImportTask) task;
			importTaskDAO.update(iTask);
			break;
		case validator:
			ComplianceCheckTask vTask = (ComplianceCheckTask) task;
			complianceCheckTaskDAO.update(vTask);
			break;

		}
	}

	@Override
	public ActionTask find(JobData.ACTION actionType, Long id) {
		switch (actionType) {
		case exporter:
			break;
		case importer:
			return importTaskDAO.find(id);
		case validator:
			return complianceCheckTaskDAO.find(id);
		}
		return null;
	}

	@Override
	public void update(ActionTask task) {
		switch (task.getAction()) {
		case exporter:
			break;
		case importer:
			ImportTask iTask = (ImportTask) task;
			importTaskDAO.update(iTask);
			break;
		case validator:
			ComplianceCheckTask vTask = (ComplianceCheckTask) task;
			complianceCheckTaskDAO.update(vTask);
			break;
		}
	}

	@Override
	public List<ActionTask> getTasks(String status) {
		List<ActionTask> result = new ArrayList<>();
		result.addAll(importTaskDAO.getTasks(status));
		result.addAll(complianceCheckTaskDAO.getTasks(status));
		// result.addAll(publicationTaskDAO.getTasks(status));

		result.sort((o1,o2) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()));
		return result;
	}
}
