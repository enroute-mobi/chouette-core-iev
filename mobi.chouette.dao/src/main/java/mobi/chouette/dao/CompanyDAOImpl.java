package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.Company;
import mobi.chouette.model.Company_;

@Stateless
public class CompanyDAOImpl extends GenericTenantDAOImpl<Company> implements CompanyDAO{

	public CompanyDAOImpl() {
		super(Company_.lineReferentialId,Company.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
