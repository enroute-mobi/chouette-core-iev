package mobi.chouette.exchange.netex_stif.model;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.model.ChouetteIdentifiedObject;

public class StopPointInJourneyPattern extends ChouetteIdentifiedObject {

	private static final long serialVersionUID = -8417177660319602749L;

	@Getter
	@Setter
	private Long id;

	@Getter 
	@Setter
	private Integer order;
	
    public StopPointInJourneyPattern(String objectId,Integer order)
    {
    	this.objectId = objectId;
    	this.order = order;
    }
}
