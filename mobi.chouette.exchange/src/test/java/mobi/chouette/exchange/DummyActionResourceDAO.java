package mobi.chouette.exchange;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import mobi.chouette.common.JobData;
import mobi.chouette.dao.ActionResourceDAO;
import mobi.chouette.model.ActionResource;

public class DummyActionResourceDAO implements ActionResourceDAO  {

	@Getter
	List<ActionResource> saved = new ArrayList<>();

	private long nextId = 1;
	@Override
	public ActionResource createResource(JobData job) {
		return new DummyActionResource(nextId++);
	}

	@Override
	public void saveResource(ActionResource resource) {
		saved.add(resource);
	}

}
