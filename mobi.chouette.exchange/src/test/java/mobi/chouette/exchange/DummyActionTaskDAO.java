package mobi.chouette.exchange;

import lombok.Getter;
import mobi.chouette.common.JobData;
import mobi.chouette.dao.ActionDAO;
import mobi.chouette.model.ActionTask;

public class DummyActionTaskDAO implements ActionDAO  {

	@Getter
	ActionTask saved;
	
	@Override
	public ActionTask getTask(JobData job) {
		saved = null;
		return new DummyActionTask();
	}

	@Override
	public void saveTask(ActionTask task) {
		saved = task;
		
	}


}
