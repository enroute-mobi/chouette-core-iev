package mobi.chouette.exchange.validator.checkpoints;

import lombok.Getter;
import lombok.Setter;

public class GenericCheckpointParameters extends CheckpointParameters{
	@Getter
	@Setter
	private String className;
	@Getter
	@Setter
	private String attributeName;
}
