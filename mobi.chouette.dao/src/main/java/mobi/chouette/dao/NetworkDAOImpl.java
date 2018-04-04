package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.Network;
import mobi.chouette.model.Network_;

@Stateless
public class NetworkDAOImpl extends GenericTenantDAOImpl<Network> implements NetworkDAO{

	public NetworkDAOImpl() {
		super(Network_.lineReferentialId,Network.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
