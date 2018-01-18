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
import mobi.chouette.model.importer.ImportTask;
import mobi.chouette.model.importer.ImportTask_;

@Log4j
@Stateless(name = "ImportTaskDAO")
public class ImportTaskDAOImpl extends GenericDAOImpl<ImportTask> implements ImportTaskDAO {

	public ImportTaskDAOImpl() {
		super(ImportTask.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<ImportTask> getTasks(String status) throws DaoException {
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<ImportTask> criteria = builder.createQuery(type);
			Root<ImportTask> root = criteria.from(type);
			Predicate predicate = builder.and(builder.isNotNull(root.get(ImportTask_.referential)),
					builder.equal(root.get(ImportTask_.status), status));
			criteria.where(predicate);
			criteria.orderBy(builder.asc(root.get(ImportTask_.createdAt)));
			TypedQuery<ImportTask> query = em.createQuery(criteria);
			return query.getResultList();
		} catch (EntityNotFoundException ex) {
			log.fatal("database corrupted on imports with status "+status); 
			throw new DaoException(DaoExceptionCode.MISSING_FOREIGN_KEY, "imports : " + ex.getMessage());
		}
	}

}
