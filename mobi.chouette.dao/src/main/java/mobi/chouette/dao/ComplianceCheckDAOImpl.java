package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mobi.chouette.model.compliance.ComplianceCheck;
import mobi.chouette.model.compliance.ComplianceCheck_;

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
