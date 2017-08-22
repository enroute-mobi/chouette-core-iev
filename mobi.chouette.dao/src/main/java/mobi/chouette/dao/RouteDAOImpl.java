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

import mobi.chouette.model.Route;
import mobi.chouette.model.Route_;

@Stateless
public class RouteDAOImpl extends GenericDAOImpl<Route> implements RouteDAO {

	public RouteDAOImpl() {
		super(Route.class);
	}

	@PersistenceContext(unitName = "referential")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	public List<Route> findByLineId(final long lineId) {
		// System.out.println("GenericDAOImpl.findByObjectId() : " + objectIds);
		List<Route> result = null;

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Route> criteria = builder.createQuery(type);
		Root<Route> root = criteria.from(type);
		Predicate predicate = builder.in(root.get(Route_.lineId)).value(lineId);
		criteria.where(predicate);
		TypedQuery<Route> query = em.createQuery(criteria);
		result = query.getResultList();
		return result;
	}
}
