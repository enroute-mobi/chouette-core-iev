package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.compliance.ComplianceCheckMessage;

@Stateless
public class ComplianceCheckResourceDAOImpl extends GenericDAOImpl<ComplianceCheckMessage>
		implements ComplianceCheckMessageDAO {

	public ComplianceCheckResourceDAOImpl() {
		super(ComplianceCheckMessage.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
