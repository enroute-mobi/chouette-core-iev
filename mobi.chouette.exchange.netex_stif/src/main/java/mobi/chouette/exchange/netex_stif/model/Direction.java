package mobi.chouette.exchange.netex_stif.model;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.model.ChouetteIdentifiedObject;

public class Direction extends ChouetteIdentifiedObject{

	/**
	 * Direction Object
	 */
	private static final long serialVersionUID = -3171106219663785290L;

	@Getter @Setter
	private Long id;
	
	@Getter @Setter
	private String name;

	@Getter @Setter
	private boolean filled = false;
}
