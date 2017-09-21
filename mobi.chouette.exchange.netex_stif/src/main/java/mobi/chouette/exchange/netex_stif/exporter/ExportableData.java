package mobi.chouette.exchange.netex_stif.exporter;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.model.Footnote;

public class ExportableData extends mobi.chouette.exchange.exporter.ExportableData {
	@Getter
	@Setter
	private Set<Footnote> notices = new HashSet<>();

	@Override
	public void clear() {
		super.clear();
		notices.clear();
	}
	
	
}
