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

	public ComplianceCheck findByCode(final String code) {
		ComplianceCheck result = null;

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ComplianceCheck> criteria = builder.createQuery(type);
		Root<ComplianceCheck> root = criteria.from(type);
		Predicate predicate = builder.in(root.get(ComplianceCheck_.code)).value(code);
		criteria.where(predicate);
		TypedQuery<ComplianceCheck> query = em.createQuery(criteria);
		result = query.getSingleResult();
		return result;
	}
}
