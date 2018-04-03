package mobi.chouette.exchange.netex_stif.exporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.Organisation;
import mobi.chouette.model.StopArea;
import mobi.chouette.model.type.DateRange;

public class ExportableData extends mobi.chouette.exchange.exporter.ExportableData {
	@Getter
	@Setter
	private Set<Footnote> notices = new HashSet<>();

	@Getter
	@Setter
	private Set<String> dataSourceRefs = new HashSet<>();
	
	@Getter
	@Setter 
	private Map<String,Organisation> organisations = new HashMap<>();

	@Getter
	@Setter
	private Set<StopArea> zdeps = new HashSet<>();

	@Getter
	@Setter
	private Set<StopArea> zders = new HashSet<>();

	@Getter
	@Setter
	private Set<StopArea> zdlps = new HashSet<>();

	@Getter
	@Setter
	private Set<StopArea> zdlrs = new HashSet<>();

	@Getter
	@Setter
	private Set<StopArea> ldas = new HashSet<>();

	@Getter
	@Setter
	private Set<StopArea> gdls = new HashSet<>();

	@Getter
	@Setter
	private DateRange globalValidityPeriod = new DateRange();

	@Getter
	private List<DateRange> validityPeriods= new ArrayList<>();

	public void addPeriods(List<DateRange> periods)
	{
		periods.forEach(period -> {
			validityPeriods.add(period);
		});
	}

	@Override
	public void clearCompany() {
		super.clearCompany();
		notices.clear();
		dataSourceRefs.clear();
		globalValidityPeriod = new DateRange();
		validityPeriods= new ArrayList<>();
	}

	@Override
	public void clearStopAreaReferential()
	{
		super.clearStopAreaReferential();
		zdeps.clear();
		zders.clear();
		zdlps.clear();
		zdlrs.clear();
		ldas.clear();
		gdls.clear();
	}
	
	@Override
	public void clearLineReferential()
	{
		super.clearLineReferential();
		organisations.clear();
	}


}
