package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.exporter.ExportResource;

@Stateless  (name="ExportResourceDAO")
public class ExportResourceDAOImpl extends GenericDAOImpl<ExportResource> implements ExportResourceDAO {

	public ExportResourceDAOImpl() {
		super(ExportResource.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}


}
