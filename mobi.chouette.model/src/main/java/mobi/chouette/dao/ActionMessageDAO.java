package mobi.chouette.dao;

import mobi.chouette.model.ActionMessage;
import mobi.chouette.model.ActionResource;

public interface ActionMessageDAO {
		
	ActionMessage createMessage(ActionResource resource);

	void saveMessage (ActionMessage message);
}
