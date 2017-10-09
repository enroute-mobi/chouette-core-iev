package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.compliance.ComplianceCheckMessage;

@Stateless
public class ComplianceCheckMessageDAOImpl extends GenericDAOImpl<ComplianceCheckMessage>
		implements ComplianceCheckMessageDAO {

	public ComplianceCheckMessageDAOImpl() {
		super(ComplianceCheckMessage.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
