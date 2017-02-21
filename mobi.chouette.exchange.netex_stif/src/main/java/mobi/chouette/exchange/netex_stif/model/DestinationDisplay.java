package mobi.chouette.exchange.netex_stif.model;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.model.ChouetteIdentifiedObject;

public class DestinationDisplay extends ChouetteIdentifiedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2401373066461210407L;

	@Getter @Setter
	private Long id;
	
	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private String frontText;
	
	@Getter @Setter
	private String publicCode;
	
	@Getter @Setter
	private boolean filled=false;

}
