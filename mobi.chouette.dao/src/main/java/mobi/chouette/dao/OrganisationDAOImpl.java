package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.Organisation;

@Stateless
public class OrganisationDAOImpl extends GenericDAOImpl<Organisation> implements OrganisationDAO{

	public OrganisationDAOImpl() {
		super(Organisation.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
