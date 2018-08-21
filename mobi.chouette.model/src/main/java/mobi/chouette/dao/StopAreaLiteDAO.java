package mobi.chouette.dao;

import java.util.List;

import mobi.chouette.model.StopAreaLite;

public interface StopAreaLiteDAO extends GenericTenantDAO<StopAreaLite> {

	List<StopAreaLite> findByType(Long tenantId, String areaType);
	 List<StopAreaLite> findNonCommercialStopAreas();
}
