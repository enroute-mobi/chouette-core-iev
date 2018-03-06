package mobi.chouette.exchange.netex_stif.exporter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.type.DateRange;

@Log4j
public class ExportableData extends mobi.chouette.exchange.exporter.ExportableData {
	@Getter
	@Setter
	private Set<Footnote> notices = new HashSet<>();

	@Getter
	@Setter
	private DateRange globalValidityPeriod = new DateRange();

	@Getter
	private List<DateRange> validityPeriods= new ArrayList<>();

	public void addPeriods(List<DateRange> periods)
	{
		periods.forEach(period -> {
			validityPeriods.add(period);
			globalValidityPeriod.extendTo(period);
		});
		log.info("global validity period " + globalValidityPeriod);
	}

	@Override
	public void clear() {
		super.clear();
		notices.clear();
		globalValidityPeriod = new DateRange();
		validityPeriods= new ArrayList<>();
	}

	@Override
	public void clearLine() {
		super.clearLine();
	}


}
