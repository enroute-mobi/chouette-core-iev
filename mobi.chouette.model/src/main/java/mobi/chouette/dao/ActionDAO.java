package mobi.chouette.dao;

import java.util.List;

import mobi.chouette.common.JobData;
import mobi.chouette.model.ActionTask;

public interface ActionDAO  {

	ActionTask getTask(JobData job);
	ActionTask find(JobData.ACTION actionType, Long id);
	void saveTask(ActionTask task);
	void update(ActionTask task);
	List<ActionTask> getTasks(String status);
}
