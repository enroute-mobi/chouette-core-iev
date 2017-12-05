package mobi.chouette.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import com.google.common.collect.Iterables;

public abstract class GenericDAOImpl<T> extends DAOImpl<T> implements GenericDAO<T> {

	public GenericDAOImpl(Class<T> type) {
		super(type);
	}
	
	public T find(final Object id) {
		return em.find(type, id);
	}
	
	public List<T> find(final String hql, final List<Object> values) {
		TypedQuery<T> query = em.createQuery(hql, type);
		if (!values.isEmpty()) {
			int pos = 0;
			for (Object value : values) {
				query.setParameter(pos++, value);
			}
		}
		return query.getResultList();
	}

	
	public List<T> findAll(final Collection<Long> ids) {
		if (ids == null || ids.isEmpty()){
			return Collections.emptyList();
		}
		List<T> result = null;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(type);
		Root<T> root = criteria.from(type);
		Predicate predicate = builder.in(root.get("id")).value(ids);
		criteria.where(predicate);
		TypedQuery<T> query = em.createQuery(criteria);
		result = query.getResultList();
		return result;
	}

	public List<T> findAll() {
		List<T> result = null;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(type);
		criteria.from(type);
		TypedQuery<T> query = em.createQuery(criteria);
		result = query.getResultList();
		return result;
	}

	@SuppressWarnings("unchecked")
	
	public T findByObjectId(final String objectId) {
		Session session = em.unwrap(Session.class);
		return (T) session.bySimpleNaturalId(type).load(objectId);
	}

	public List<T> findByObjectId(final Collection<String> objectIds) {
		List<T> result = null;
		if (objectIds.isEmpty())
			return result;

		Iterable<List<String>> iterator = Iterables.partition(objectIds, 32000);
		for (List<String> ids : iterator) {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<T> criteria = builder.createQuery(type);
			Root<T> root = criteria.from(type);
			Predicate predicate = builder.in(root.get("objectId")).value(ids);
			criteria.where(predicate);
			TypedQuery<T> query = em.createQuery(criteria);
			if (result == null)
				result = query.getResultList();
			else
				result.addAll(query.getResultList());
		}
		return result;
	}
	
	public int deleteAll() {
		int result = 0;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaDelete<T> criteria = builder.createCriteriaDelete(type);
		criteria.from(type);
		Query query = em.createQuery(criteria);
		result = query.executeUpdate();
		return result;
	}
	
	public int truncate() {
		String query = new StringBuilder("TRUNCATE TABLE ").append(type.getAnnotation(Table.class).name())
				.append(" CASCADE").toString();
		return em.createNativeQuery(query).executeUpdate();
	}
	
}
