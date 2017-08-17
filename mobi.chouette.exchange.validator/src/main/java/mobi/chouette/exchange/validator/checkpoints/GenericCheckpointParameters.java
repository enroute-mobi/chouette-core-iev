package mobi.chouette.exchange.validator.checkpoints;

import javax.xml.bind.annotation.XmlType;

import lombok.Getter;
import lombok.Setter;

@XmlType(propOrder={"className", "attributeName"})
public class GenericCheckpointParameters extends CheckpointParameters{
	@Getter
	@Setter
	private String className;
	@Getter
	@Setter
	private String attributeName;
}
