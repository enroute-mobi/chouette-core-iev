package mobi.chouette.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mobi.chouette.model.ImportResource;
import mobi.chouette.model.ImportResource_;

@Stateless  (name="ImportResourceDAO")
public class ImportResourceDAOImpl extends GenericDAOImpl<ImportResource> implements ImportResourceDAO {

	public ImportResourceDAOImpl() {
		super(ImportResource.class);
	}

	@Override
	public List<ImportResource> getResources(String status) {
		List<ImportResource> result = null;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ImportResource> criteria = builder.createQuery(type);
		Root<ImportResource> root = criteria.from(type);
		Predicate predicate = builder.equal(root.get(ImportResource_.status), status);
		criteria.where(predicate);
		criteria.orderBy(builder.asc(root.get(ImportResource_.createdAt)));
		TypedQuery<ImportResource> query = em.createQuery(criteria);
		result = query.getResultList();
		return result;
	}

}
