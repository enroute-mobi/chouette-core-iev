package mobi.chouette.exchange.netex_stif.exporter;

import java.util.HashSet;
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
	private Set<DateRange> periods = new HashSet<>();

	@Getter
	@Setter
	private Set<Line> lines = new HashSet<>();

	@Override
	public void clear() {
		super.clear();
		notices.clear();
		periods.clear();
		lines.clear();
	}

}
