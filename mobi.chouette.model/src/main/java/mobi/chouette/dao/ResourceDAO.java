package mobi.chouette.dao;

import mobi.chouette.common.JobData;
import mobi.chouette.model.ActionResource;

public interface ResourceDAO {
	
	ActionResource createResource(JobData job);
	
    void saveResource(ActionResource resource);
	 
	
}
