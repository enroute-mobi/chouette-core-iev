package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.StopArea;
import mobi.chouette.model.StopArea_;

@Stateless
public class StopAreaDAOImpl extends GenericTenantDAOImpl<StopArea> implements StopAreaDAO{
	public StopAreaDAOImpl() {
		super(StopArea_.stopAreaReferentialId,StopArea.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
