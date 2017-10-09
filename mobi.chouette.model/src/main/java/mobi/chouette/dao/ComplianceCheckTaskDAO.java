package mobi.chouette.dao;

import java.util.List;

import mobi.chouette.model.compliance.ComplianceCheckTask;

public interface ComplianceCheckTaskDAO extends GenericDAO<ComplianceCheckTask> {
	public List<ComplianceCheckTask> getTasks(String status);
}
