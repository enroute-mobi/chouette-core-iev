package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.compliance.ComplianceCheck;

@Stateless
public class ComplianceCheckDAOImpl extends GenericDAOImpl<ComplianceCheck> implements ComplianceCheckDAO {

	public ComplianceCheckDAOImpl() {
		super(ComplianceCheck.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
