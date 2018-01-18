package mobi.chouette.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.extern.log4j.Log4j;
import mobi.chouette.model.compliance.ComplianceCheckTask;
import mobi.chouette.model.compliance.ComplianceCheckTask_;

@Log4j
@Stateless
public class ComplianceCheckTaskDAOImpl extends GenericDAOImpl<ComplianceCheckTask> implements ComplianceCheckTaskDAO {

	public ComplianceCheckTaskDAOImpl() {
		super(ComplianceCheckTask.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<ComplianceCheckTask> getTasks(String status) throws DaoException {
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<ComplianceCheckTask> criteria = builder.createQuery(type);
			Root<ComplianceCheckTask> root = criteria.from(type);
			Predicate predicate = builder.and(builder.isNotNull(root.get(ComplianceCheckTask_.referential)),
					builder.equal(root.get(ComplianceCheckTask_.status), status));
			criteria.where(predicate);
			criteria.orderBy(builder.asc(root.get(ComplianceCheckTask_.createdAt)));
			TypedQuery<ComplianceCheckTask> query = em.createQuery(criteria);
			return query.getResultList();
		} catch (EntityNotFoundException ex) {
			log.fatal("database corrupted on compliance_checks with status " + status);
			throw new DaoException(DaoExceptionCode.MISSING_FOREIGN_KEY,
					"compliance_checks : " + ex.getMessage());
		}
	}

}
