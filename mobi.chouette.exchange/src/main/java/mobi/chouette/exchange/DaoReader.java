package mobi.chouette.exchange;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import mobi.chouette.dao.CompanyDAO;
import mobi.chouette.dao.GroupOfLineDAO;
import mobi.chouette.dao.LineDAO;
import mobi.chouette.dao.LineLiteDAO;
import mobi.chouette.dao.NetworkDAO;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.model.Company;
import mobi.chouette.model.GroupOfLine;
import mobi.chouette.model.Line;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Network;

@Stateless
public class DaoReader {

	@EJB
	protected LineDAO lineDAO;

	@EJB
	protected LineLiteDAO lineLiteDAO;

	@EJB
	protected NetworkDAO ptNetworkDAO;

	@EJB
	protected CompanyDAO companyDAO;

	@EJB
	protected GroupOfLineDAO groupOfLineDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Set<Long> loadLines(AbstractParameter params, String type, List<Long> ids) {
		Set<Line> lines = new HashSet<>();
		Set<Long> lineIds = new HashSet<>();
		if (ids == null || ids.isEmpty()) {
			lines.addAll(lineDAO.findAll(params.getLineReferentialId()));
		} else {
			switch (type) {
			case "line":
				lines.addAll(lineDAO.findAll(params.getLineReferentialId(),ids));
				break;
			case "network":
				List<Network> nlist = ptNetworkDAO.findAll(params.getLineReferentialId(),ids);
				nlist.forEach(n -> lines.addAll(n.getLines()));
				break;
			case "company":
				List<Company> clist = companyDAO.findAll(params.getLineReferentialId(),ids);
				clist.forEach(c -> lines.addAll(c.getLines()));
				break;
			case "group_of_line":
				List<GroupOfLine> glist = groupOfLineDAO.findAll(params.getLineReferentialId(),ids);
				glist.forEach(g -> lines.addAll(g.getLines()));
				break;
			}
		}
		lineIds = lines.stream().map(Line::getId).collect(Collectors.toSet());
		lines.clear();
		return lineIds;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Map<Long, Set<Long>> loadLinesByCompanies(Long lineReferentialId, List<Long> ids) {
		Set<LineLite> lines = new HashSet<>();
		Map<Long, Set<Long>> result = new HashMap<>();
		if (ids == null || ids.isEmpty()) {
			lines.addAll(lineLiteDAO.findAll(lineReferentialId));
		} else {
			lines.addAll(lineLiteDAO.findAll(lineReferentialId,ids));
		}
		lines.forEach(l -> {
			Set<Long> lineIds = result.computeIfAbsent(l.getCompanyId(), s -> new HashSet<>());
			lineIds.add(l.getId());
		});
		return result;
	}
}
