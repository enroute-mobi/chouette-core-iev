package mobi.chouette.dao;

import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.StopAreaLite_;

@Stateless (name="StopAreaLiteDAO")
public class StopAreaLiteDAOImpl extends GenericTenantDAOImpl<StopAreaLite> implements StopAreaLiteDAO{
	public StopAreaLiteDAOImpl() {
		super(StopAreaLite_.stopAreaReferentialId, StopAreaLite.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<StopAreaLite> findByType(Long tenantId, String areaType) {
		
		if (areaType == null || areaType.isEmpty())
		{
			return Collections.emptyList();
		}
		List<StopAreaLite> result = null;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<StopAreaLite> criteria = builder.createQuery(type);
		Root<StopAreaLite> root = criteria.from(type);
		Predicate predicate = builder.equal(root.get(StopAreaLite_.areaType.getName()),areaType);
		criteria.where(predicate);
		TypedQuery<StopAreaLite> query = em.createQuery(criteria);
		result = query.getResultList();
		return result;
	}
	
	@Override
	public List<StopAreaLite> findNonCommercialStopAreas() {
		
		List<StopAreaLite> result = null;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<StopAreaLite> criteria = builder.createQuery(type);
		Root<StopAreaLite> root = criteria.from(type);
		Predicate predicate = builder.not(root.get(StopAreaLite_.areaType.getName()).in("zdlp","zdep"));
		criteria.where(predicate);
		TypedQuery<StopAreaLite> query = em.createQuery(criteria);
		result = query.getResultList();
		return result;
	}
}
