package mobi.chouette.dao;

import mobi.chouette.common.JobData;
import mobi.chouette.model.ActionResource;

public interface ActionResourceDAO {
	
	ActionResource createResource(JobData job);
	
    void saveResource(ActionResource resource);

}
