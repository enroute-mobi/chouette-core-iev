package mobi.chouette.exchange.validator.checkpoints;

import javax.xml.bind.annotation.XmlType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlType(propOrder = { "className", "attributeName" })
@AllArgsConstructor
@NoArgsConstructor
public class GenericCheckpointParameters extends CheckpointParameters {
	@Getter
	@Setter
	private String className;
	@Getter
	@Setter
	private String attributeName;
}
