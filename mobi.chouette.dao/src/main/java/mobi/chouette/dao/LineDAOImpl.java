package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.Line;
import mobi.chouette.model.Line_;

@Stateless (name="LineDAO")
public class LineDAOImpl extends GenericTenantDAOImpl<Line> implements LineDAO {

	public LineDAOImpl() {
		super(Line_.lineReferentialId,Line.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
}
