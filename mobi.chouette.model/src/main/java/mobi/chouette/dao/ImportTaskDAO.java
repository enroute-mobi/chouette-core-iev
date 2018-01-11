package mobi.chouette.dao;

import java.util.List;

import mobi.chouette.model.importer.ImportTask;

public interface ImportTaskDAO extends GenericDAO<ImportTask> {

	List<ImportTask> getTasks(String status) throws DaoException;
}
