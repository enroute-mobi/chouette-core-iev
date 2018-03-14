package mobi.chouette.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import mobi.chouette.common.JobData;
import mobi.chouette.model.ActionTask;
import mobi.chouette.model.compliance.ComplianceCheckTask;
import mobi.chouette.model.exporter.ExportTask;
import mobi.chouette.model.importer.ImportTask;

@Stateless(name = "ActionDAO")
public class ActionDAOImpl implements ActionDAO {

	@EJB
	ImportTaskDAO importTaskDAO;

	@EJB
	ComplianceCheckTaskDAO complianceCheckTaskDAO;

	 @EJB
	 ExportTaskDAO exportTaskDAO;
	

	/* (non-Javadoc)
	 * @see mobi.chouette.dao.ActionDAO#getTask(mobi.chouette.common.JobData)
	 */
	@Override
	public ActionTask getTask(JobData job) {
		switch (job.getAction()) {
		case importer:
			return importTaskDAO.find(job.getId());
		case exporter:
			return exportTaskDAO.find(job.getId());
		case validator:
			return complianceCheckTaskDAO.find(job.getId());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see mobi.chouette.dao.ActionDAO#saveTask(mobi.chouette.model.ActionTask)
	 */
	@Override
	public void saveTask(ActionTask task) {
		switch (task.getAction()) {
		case importer:
			ImportTask iTask = (ImportTask) task;
			importTaskDAO.update(iTask);
			break;
		case exporter:
			ExportTask eTask = (ExportTask) task;
			exportTaskDAO.update(eTask);
			break;
		case validator:
			ComplianceCheckTask vTask = (ComplianceCheckTask) task;
			complianceCheckTaskDAO.update(vTask);
			break;

		}
	}

	/* (non-Javadoc)
	 * @see mobi.chouette.dao.ActionDAO#find(mobi.chouette.common.JobData.ACTION, java.lang.Long)
	 */
	@Override
	public ActionTask find(JobData.ACTION actionType, Long id) {
		switch (actionType) {
		case importer:
			return importTaskDAO.find(id);
		case exporter:
			return exportTaskDAO.find(id);
		case validator:
			return complianceCheckTaskDAO.find(id);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see mobi.chouette.dao.ActionDAO#update(mobi.chouette.model.ActionTask)
	 */
	@Override
	public void update(ActionTask task) {
		switch (task.getAction()) {
		case importer:
			ImportTask iTask = (ImportTask) task;
			importTaskDAO.update(iTask);
			break;
		case exporter:
			ExportTask eTask = (ExportTask) task;
			exportTaskDAO.update(eTask);
			break;
		case validator:
			ComplianceCheckTask vTask = (ComplianceCheckTask) task;
			complianceCheckTaskDAO.update(vTask);
			break;
		}
	}

	/* (non-Javadoc)
	 * @see mobi.chouette.dao.ActionDAO#getTasks(java.lang.String)
	 */
	@Override
	public List<ActionTask> getTasks(String status) throws DaoException {
		List<ActionTask> result = new ArrayList<>();
		result.addAll(importTaskDAO.getTasks(status));
		result.addAll(complianceCheckTaskDAO.getTasks(status));
		result.addAll(exportTaskDAO.getTasks(status));

		result.sort((o1,o2) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()));
		return result;
	}
}
