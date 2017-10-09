package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.compliance.ComplianceCheckResource;

@Stateless
public class ComplianceCheckResourceDAOImpl extends GenericDAOImpl<ComplianceCheckResource>
		implements ComplianceCheckResourceDAO {

	public ComplianceCheckResourceDAOImpl() {
		super(ComplianceCheckResource.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
