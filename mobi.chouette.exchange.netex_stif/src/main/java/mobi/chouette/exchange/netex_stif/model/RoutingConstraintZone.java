package mobi.chouette.exchange.netex_stif.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.model.ChouetteIdentifiedObject;

public class RoutingConstraintZone extends ChouetteIdentifiedObject {


	private static final long serialVersionUID = 6448383238063669281L;

	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter 
	private String objectId;
	
	@Getter 
	@Setter
	private Long objectVersion;

	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private String zoneUse;
	
	@Getter @Setter 
    private Set<String> stopPointsRef = new HashSet<>();
	
	public void clear()
	{
		objectId = null;
		name = null;
		zoneUse = null;
		stopPointsRef.clear();
		creationTime = new Date();
	}

}
