package mobi.chouette.dao;

import java.util.Collection;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

abstract class DAOImpl<T> {

	protected EntityManager em;

	protected Class<T> type;

	public DAOImpl(Class<T> type) {
		this.type = type;
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

	public void evictAll() {
		EntityManagerFactory factory = em.getEntityManagerFactory();
		Cache cache = factory.getCache();
		cache.evictAll();
	}

	public void flush() {
		em.flush();
	}

	public void clear() {
		em.clear();
	}

	public void detach(Collection<?> list) {
		for (Object object : list) {
			em.detach(object);
		}
	}

}
