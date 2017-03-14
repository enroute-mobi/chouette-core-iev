package mobi.chouette.dao;

import java.util.List;

import mobi.chouette.model.ImportTask;

public interface ImportTaskDAO extends GenericDAO<ImportTask> {

	List<ImportTask> getTasks(String status);
}
