package mobi.chouette.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mobi.chouette.model.ImportTask;
import mobi.chouette.model.ImportTask_;
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
	
	
	public List<ComplianceCheckTask> getTasks(String status)
	{
		List<ComplianceCheckTask> result = null;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ComplianceCheckTask> criteria = builder.createQuery(type);
		Root<ComplianceCheckTask> root = criteria.from(type);
		Predicate predicate = builder.and(builder.isNotNull(root.get(ImportTask_.referential)),builder.equal(root.get(ImportTask_.status), status));
		criteria.where(predicate);
		criteria.orderBy(builder.asc(root.get(ImportTask_.createdAt)));
		TypedQuery<ComplianceCheckTask> query = em.createQuery(criteria);
		result = query.getResultList();
		return result;
	}

}
