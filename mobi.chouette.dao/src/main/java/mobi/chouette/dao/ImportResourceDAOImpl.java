package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.importer.ImportResource;

@Stateless  (name="ImportResourceDAO")
public class ImportResourceDAOImpl extends GenericDAOImpl<ImportResource> implements ImportResourceDAO {

	public ImportResourceDAOImpl() {
		super(ImportResource.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}


}
