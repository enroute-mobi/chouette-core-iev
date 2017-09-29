package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.compliance.ComplianceCheckTask;

@Stateless
public class ComplianceCheckTaskDAOImpl extends GenericDAOImpl<ComplianceCheckTask> implements ComplianceCheckTaskDAO {

	public ComplianceCheckTaskDAOImpl() {
		super(ComplianceCheckTask.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
