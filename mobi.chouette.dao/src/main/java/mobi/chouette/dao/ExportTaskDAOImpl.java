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
import mobi.chouette.model.exporter.ExportTask;
import mobi.chouette.model.exporter.ExportTask_;

@Log4j
@Stateless(name = "ExportTaskDAO")
public class ExportTaskDAOImpl extends GenericDAOImpl<ExportTask> implements ExportTaskDAO {

	public ExportTaskDAOImpl() {
		super(ExportTask.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<ExportTask> getTasks(String status) throws DaoException {
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<ExportTask> criteria = builder.createQuery(type);
			Root<ExportTask> root = criteria.from(type);
			Predicate predicate = builder.and(builder.isNotNull(root.get(ExportTask_.referential)),
					builder.equal(root.get(ExportTask_.status), status), 
					builder.equal(root.get(ExportTask_.type), "Export::Netex"));
			criteria.where(predicate);
			criteria.orderBy(builder.asc(root.get(ExportTask_.createdAt)));
			TypedQuery<ExportTask> query = em.createQuery(criteria);
			return query.getResultList();
		} catch (EntityNotFoundException ex) {
			log.fatal("database corrupted on imports with status "+status); 
			throw new DaoException(DaoExceptionCode.MISSING_FOREIGN_KEY, "imports : " + ex.getMessage());
		}
	}

}
