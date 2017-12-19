package mobi.chouette.exchange;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import mobi.chouette.dao.CompanyDAO;
import mobi.chouette.dao.GroupOfLineDAO;
import mobi.chouette.dao.LineDAO;
import mobi.chouette.dao.NetworkDAO;
import mobi.chouette.model.Company;
import mobi.chouette.model.GroupOfLine;
import mobi.chouette.model.Line;
import mobi.chouette.model.Network;

@Stateless
public class DaoReader {

	@EJB
	protected LineDAO lineDAO;

	@EJB
	protected NetworkDAO ptNetworkDAO;

	@EJB
	protected CompanyDAO companyDAO;

	@EJB
	protected GroupOfLineDAO groupOfLineDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Set<Long> loadLines(String type, List<Long> ids) {
		Set<Line> lines = new HashSet<>();
		Set<Long> lineIds = new HashSet<>();
		if (ids == null || ids.isEmpty()) {
			lines.addAll(lineDAO.findAll());
		} else {
			switch (type) {
			case "line":
				lines.addAll(lineDAO.findAll(ids));
				break;
			case "network":
				List<Network> nlist = ptNetworkDAO.findAll(ids);
				for (Network ptNetwork : nlist) {
					lines.addAll(ptNetwork.getLines());
				}
				break;
			case "company":
				List<Company> clist = companyDAO.findAll(ids);
				for (Company company : clist) {
					lines.addAll(company.getLines());
				}
				break;
			case "group_of_line":
				List<GroupOfLine> glist = groupOfLineDAO.findAll(ids);
				for (GroupOfLine groupOfLine : glist) {
					lines.addAll(groupOfLine.getLines());
				}
				break;
			}
		}
		for (Line line : lines) {
			lineIds.add(line.getId());
		}
		return lineIds;
	}

}
