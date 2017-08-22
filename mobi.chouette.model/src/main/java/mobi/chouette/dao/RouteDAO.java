package mobi.chouette.dao;

import java.util.List;

import mobi.chouette.model.Route;

public interface RouteDAO extends GenericDAO<Route> {
	public List<Route> findByLineId(final long lineId);
}
