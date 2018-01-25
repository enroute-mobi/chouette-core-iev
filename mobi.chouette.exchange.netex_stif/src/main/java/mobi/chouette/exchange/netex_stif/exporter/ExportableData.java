package mobi.chouette.exchange.netex_stif.exporter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.sampled.Line;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.type.DateRange;

public class ExportableData extends mobi.chouette.exchange.exporter.ExportableData {
	@Getter
	@Setter
	private Set<Footnote> notices = new HashSet<>();

	@Getter
	@Setter
	private DateRange globalValidityPeriod = new DateRange();

	@Getter
	private List<DateRange> validityPeriods= new ArrayList<>();

	@Getter
	@Setter
	private Set<Line> lines = new HashSet<>();
	
	public void addPeriods(List<DateRange> periods)
	{
		periods.forEach(period -> {
			validityPeriods.add(period);
			globalValidityPeriod.extendTo(period);
		});
	}

	@Override
	public void clear() {
		super.clear();
		notices.clear();
		globalValidityPeriod = new DateRange();
		validityPeriods= new ArrayList<>();
		lines.clear();
	}

}
