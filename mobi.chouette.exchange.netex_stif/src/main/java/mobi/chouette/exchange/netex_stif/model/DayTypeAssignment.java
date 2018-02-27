package mobi.chouette.exchange.netex_stif.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.model.CalendarDay;
import mobi.chouette.model.ChouetteIdentifiedObject;

public class DayTypeAssignment extends ChouetteIdentifiedObject {

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
	private String operationDayRef;
	
	@Getter @Setter
	private String operatingPeriodRef;
	
	@Getter @Setter
	private CalendarDay day;
	
	@Getter @Setter
	private Boolean isAvailable;
	
	public void clear()
	{
		objectId = null;
		operationDayRef = null;
		operatingPeriodRef = null;
		day = null;
		isAvailable = null;
		creationTime = new Date();
	}


}
