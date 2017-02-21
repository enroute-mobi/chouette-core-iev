package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.Referential;

@Stateless
public class ReferentialDAOImpl extends GenericDAOImpl<Referential> implements ReferentialDAO{

	public ReferentialDAOImpl() {
		super(Referential.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
