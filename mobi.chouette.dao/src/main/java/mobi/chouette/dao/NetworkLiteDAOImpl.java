package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.NetworkLite;
import mobi.chouette.model.NetworkLite_;

@Stateless (name="NetworkLiteDAO")
public class NetworkLiteDAOImpl extends GenericTenantDAOImpl<NetworkLite> implements NetworkLiteDAO {

	public NetworkLiteDAOImpl() {
		super(NetworkLite_.lineReferentialId,NetworkLite.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	
}
