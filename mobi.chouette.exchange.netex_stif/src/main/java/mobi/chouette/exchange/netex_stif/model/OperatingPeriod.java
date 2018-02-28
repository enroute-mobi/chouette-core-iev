package mobi.chouette.exchange.netex_stif.model;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.model.ChouetteIdentifiedObject;
import mobi.chouette.model.Period;

public class OperatingPeriod extends ChouetteIdentifiedObject {


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
	private Period period = new Period();
	

}
