package mobi.chouette.dao;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;

@Local
public interface GenericTenantDAO<T> {

	T find(Long tenantId, Object id);

	T findByObjectId(Long tenantId, String id);

	List<T> findByObjectId(Long tenantId, Collection<String> objectIds);

	List<T> findAll(Long tenantId );

	List<T> findAll(Long tenantId, Collection<Long> ids);

	void create(T entity);

	T update(T entity);

	void delete(T entity);

	int deleteAll(Long tenantId);

	void detach(T entity);

	void evictAll();

	void flush();
	
	void clear();

	void detach(Collection<?> list);
	

}
