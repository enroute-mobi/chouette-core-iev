package mobi.chouette.dao;

import mobi.chouette.model.ActionResource;

public interface ActionResourceDAO {

	ActionResource getResource();
	
	void saveResource (ActionResource actionResource);
}
