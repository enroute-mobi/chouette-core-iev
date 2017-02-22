package mobi.chouette.exchange.netex_stif.model;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.model.ChouetteIdentifiedObject;

public class ScheduledStopPoint extends ChouetteIdentifiedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2873067342890284756L;

	@Getter
	@Setter
	private Long id;

	@Getter @Setter
	private String stopArea;
	

}
