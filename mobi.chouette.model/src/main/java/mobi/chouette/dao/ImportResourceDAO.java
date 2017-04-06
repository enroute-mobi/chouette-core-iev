package mobi.chouette.dao;

import java.util.List;

import mobi.chouette.model.ImportResource;

public interface ImportResourceDAO extends GenericDAO<ImportResource> {

	List<ImportResource> getResources(String status);
}
