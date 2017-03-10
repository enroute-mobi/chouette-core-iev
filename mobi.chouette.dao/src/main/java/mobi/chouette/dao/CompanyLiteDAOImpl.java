package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.CompanyLite;

@Stateless (name="CompanyLiteDAO")

public class CompanyLiteDAOImpl extends GenericDAOImpl<CompanyLite> implements CompanyLiteDAO{

	public CompanyLiteDAOImpl() {
		super(CompanyLite.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
