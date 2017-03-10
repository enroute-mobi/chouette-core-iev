package mobi.chouette.dao;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;

import com.google.common.collect.Iterables;

public abstract class GenericTenantDAOImpl<T> implements GenericTenantDAO<T> {

	protected EntityManager em;

	protected Class<T> type;

	protected Attribute<T, Long> tenantAttribute;

	public GenericTenantDAOImpl(Attribute<T, Long> tenantAttribute, Class<T> type) {
		this.type = type;
		this.tenantAttribute = tenantAttribute;
	}

	private Long getTenantValue(T object) {
		Member m = tenantAttribute.getJavaMember();
		String methodName = m.getName();
		if (!m.getName().startsWith("get")) {
			methodName = "get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
		}
		try {
			Method method = type.getMethod(methodName);
			Long value = (Long) method.invoke(object);
			return value;
		} catch (Exception e) {
			throw new RuntimeException("unable to access to tenant id " + tenantAttribute.getName(), e);
		}

	}

	public T find(final Long tenantId, final Object id) {
		T result = em.find(type, id);
		if (result == null)
			return null;
		if (getTenantValue(result).equals(tenantId))
			return result;
		return null;
	}

	public List<T> find(final String hql, final List<Object> values) {
		List<T> result = null;
		if (values.isEmpty()) {
			TypedQuery<T> query = em.createQuery(hql, type);
			result = query.getResultList();
		} else {
			TypedQuery<T> query = em.createQuery(hql, type);
			int pos = 0;
			for (Object value : values) {
				query.setParameter(pos++, value);
			}
			result = query.getResultList();
		}
		return result;
	}

	public List<T> findAll(final Long tenantId, final Collection<Long> ids) {
		if (ids == null || ids.size() == 0) {
			return Collections.emptyList();
		}
		List<T> result = null;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(type);
		Root<T> root = criteria.from(type);
		Predicate predicate1 = builder.equal(root.get(tenantAttribute.getName()), tenantId);
		Predicate predicate2 = builder.in(root.get("id")).value(ids);
		criteria.where(builder.and(predicate1, predicate2));
		TypedQuery<T> query = em.createQuery(criteria);
		result = query.getResultList();
		return result;
	}

	public List<T> findAll(final Long tenantId) {
		List<T> result = null;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(type);
		Root<T> root = criteria.from(type);
		Predicate predicate1 = builder.equal(root.get(tenantAttribute.getName()), tenantId);
		criteria.where(predicate1);
		TypedQuery<T> query = em.createQuery(criteria);
		result = query.getResultList();
		return result;
	}

	public T findByObjectId(final Long tenantId, final String objectId) {
		if (objectId == null || objectId.isEmpty()) {
			return null;
		}
		List<T> result = null;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(type);
		Root<T> root = criteria.from(type);
		Predicate predicate1 = builder.equal(root.get(tenantAttribute.getName()), tenantId);
		Predicate predicate2 = builder.equal(root.get("objectid"), objectId);
		criteria.where(builder.and(predicate1, predicate2));
		TypedQuery<T> query = em.createQuery(criteria);
		result = query.getResultList();
		if (result.isEmpty())
			return null;
		return result.get(0);
	}

	public List<T> findByObjectId(final Long tenantId, final Collection<String> objectIds) {
		// System.out.println("GenericDAOImpl.findByObjectId() : " + objectIds);
		List<T> result = null;
		if (objectIds.isEmpty())
			return result;

		Iterable<List<String>> iterator = Iterables.partition(objectIds, 32000);
		for (List<String> ids : iterator) {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<T> criteria = builder.createQuery(type);
			Root<T> root = criteria.from(type);
			Predicate predicate1 = builder.equal(root.get(tenantAttribute.getName()), tenantId);
			Predicate predicate2 = builder.in(root.get("objectId")).value(ids);
			criteria.where(builder.and(predicate1, predicate2));
			TypedQuery<T> query = em.createQuery(criteria);
			if (result == null)
				result = query.getResultList();
			else
				result.addAll(query.getResultList());
		}
		return result;
	}

	public void create(final T entity) {
		em.persist(entity);
	}

	public T update(final T entity) {
		return em.merge(entity);
	}

	public void delete(final T entity) {
		em.remove(entity);
	}

	public void detach(final T entity) {
		em.detach(entity);
	}

	public int deleteAll(final Long tenantId) {
		int result = 0;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaDelete<T> criteria = builder.createCriteriaDelete(type);
		Root<T> root = criteria.from(type);
		Predicate predicate1 = builder.equal(root.get(tenantAttribute.getName()), tenantId);
		criteria.where(predicate1);
    	Query query = em.createQuery(criteria);
		result = query.executeUpdate();
		return result;
	}

	// public int truncate() {
	// String query = new StringBuilder("TRUNCATE TABLE
	// ").append(type.getAnnotation(Table.class).name())
	// .append(" CASCADE").toString();
	// return em.createNativeQuery(query).executeUpdate();
	// }

	public void evictAll() {
		EntityManagerFactory factory = em.getEntityManagerFactory();
		Cache cache = factory.getCache();
		cache.evictAll();
	}

	public void flush() {
		em.flush();
	}

	@Override
	public void clear() {
		em.clear();
	}

	@Override
	public void detach(Collection<?> list) {
		for (Object object : list) {
			em.detach(object);
		}
	}

}
