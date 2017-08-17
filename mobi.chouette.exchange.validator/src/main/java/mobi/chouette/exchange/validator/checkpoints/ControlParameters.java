package mobi.chouette.exchange.validator.checkpoints;

import java.util.Collection;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class ControlParameters {
	@Getter
	@Setter
	private Collection<CheckpointParameters> globalCheckPoints;

	@Getter
	@Setter
	private Map<String, Collection<CheckpointParameters>> transportModeCheckpoints;
}
