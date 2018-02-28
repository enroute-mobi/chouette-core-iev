package mobi.chouette.exchange.netex_stif.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.model.ChouetteIdentifiedObject;

public class PassengerStopAssignment extends ChouetteIdentifiedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2873067342890284756L;

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
	private String scheduledStopPointRef;
	
	@Getter @Setter
	private String quayRef;
	
	public void clear()
	{
		objectId = null;
		scheduledStopPointRef = null;
		quayRef = null;
		creationTime = new Date();
	}


}
