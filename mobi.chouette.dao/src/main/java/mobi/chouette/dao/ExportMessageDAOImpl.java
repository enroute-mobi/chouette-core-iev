package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.exporter.ExportMessage;

@Stateless  (name="ExportMessageDAO")
public class ExportMessageDAOImpl extends GenericDAOImpl<ExportMessage> implements ExportMessageDAO {

	public ExportMessageDAOImpl() {
		super(ExportMessage.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
