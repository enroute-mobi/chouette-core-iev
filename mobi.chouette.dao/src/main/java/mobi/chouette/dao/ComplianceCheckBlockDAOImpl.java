package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.compliance.ComplianceCheckBlock;

@Stateless
public class ComplianceCheckBlockDAOImpl extends GenericDAOImpl<ComplianceCheckBlock>
		implements ComplianceCheckBlockDAO {

	public ComplianceCheckBlockDAOImpl() {
		super(ComplianceCheckBlock.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
