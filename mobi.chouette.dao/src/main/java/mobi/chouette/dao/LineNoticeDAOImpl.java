package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.LineNotice;
import mobi.chouette.model.LineNotice_;

@Stateless
public class LineNoticeDAOImpl extends GenericTenantDAOImpl<LineNotice> implements LineNoticeDAO{

	public LineNoticeDAOImpl() {
		super(LineNotice_.lineReferentialId,LineNotice.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
