package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.LineLite;
import mobi.chouette.model.LineLite_;

@Stateless (name="LineLiteDAO")
public class LineLiteDAOImpl extends GenericTenantDAOImpl<LineLite> implements LineLiteDAO {

	public LineLiteDAOImpl() {
		super(LineLite_.lineReferentialId,LineLite.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	
}
