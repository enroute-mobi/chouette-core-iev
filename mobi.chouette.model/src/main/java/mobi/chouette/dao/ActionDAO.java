package mobi.chouette.dao;

import mobi.chouette.common.JobData;
import mobi.chouette.model.ActionTask;

public interface ActionDAO  {

	ActionTask getTask(JobData job);
	
	void saveTask(ActionTask task);
}
