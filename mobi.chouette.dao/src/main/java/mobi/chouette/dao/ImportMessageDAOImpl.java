package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.ImportMessage;

@Stateless  (name="ImportMessageDAO")
public class ImportMessageDAOImpl extends GenericDAOImpl<ImportMessage> implements ImportMessageDAO {

	public ImportMessageDAOImpl() {
		super(ImportMessage.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	

}
