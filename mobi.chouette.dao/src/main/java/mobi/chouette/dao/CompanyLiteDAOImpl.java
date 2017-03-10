package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.CompanyLite;
import mobi.chouette.model.CompanyLite_;

@Stateless (name="CompanyLiteDAO")

public class CompanyLiteDAOImpl extends GenericTenantDAOImpl<CompanyLite> implements CompanyLiteDAO{

	public CompanyLiteDAOImpl() {
		super(CompanyLite_.lineReferentialId,CompanyLite.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
