package mobi.chouette.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mobi.chouette.model.GroupOfLine;
import mobi.chouette.model.GroupOfLine_;

@Stateless
public class GroupOfLineDAOImpl extends GenericTenantDAOImpl<GroupOfLine> implements GroupOfLineDAO{

	public GroupOfLineDAOImpl() {
		super(GroupOfLine_.lineReferentialId, GroupOfLine.class);
	}

	@PersistenceContext(unitName = "public")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
