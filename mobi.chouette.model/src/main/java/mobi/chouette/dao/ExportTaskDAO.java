package mobi.chouette.dao;

import java.util.List;

import mobi.chouette.model.exporter.ExportTask;

public interface ExportTaskDAO extends GenericDAO<ExportTask> {

	List<ExportTask> getTasks(String status) throws DaoException;
}
