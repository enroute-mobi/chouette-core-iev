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

import mobi.chouette.model.Footnote;
import mobi.chouette.model.Footnote_;

@Stateless
public class FootnoteDAOImpl extends GenericDAOImpl<Footnote> implements FootnoteDAO {

	public FootnoteDAOImpl() {
		super(Footnote.class);
	}
	@PersistenceContext(unitName = "referential")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	public List<Footnote> findByLineId(final long lineId) {
		List<Footnote> result = null;

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Footnote> criteria = builder.createQuery(type);
		Root<Footnote> root = criteria.from(type);
		Predicate predicate = builder.in(root.get(Footnote_.lineId)).value(lineId);
		criteria.where(predicate);
		TypedQuery<Footnote> query = em.createQuery(criteria);
		result = query.getResultList();
		return result;
	}

}
