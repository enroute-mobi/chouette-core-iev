package mobi.chouette.exchange;

import java.util.List;

import lombok.Getter;
import mobi.chouette.common.JobData;
import mobi.chouette.common.JobData.ACTION;
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

	@Override
	public ActionTask find(ACTION actionType, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(ActionTask task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ActionTask> getTasks(String status) {
		// TODO Auto-generated method stub
		return null;
	}


}
