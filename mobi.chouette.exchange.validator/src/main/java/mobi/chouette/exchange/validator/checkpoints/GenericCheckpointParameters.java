package mobi.chouette.exchange.validator.checkpoints;

import javax.xml.bind.annotation.XmlType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlType(propOrder = { "code", "errorType", "firstValue", "secondValue" ,"className", "attributeName" })
@NoArgsConstructor
public class GenericCheckpointParameters extends CheckpointParameters {
	@Getter
	@Setter
	private String className;
	@Getter
	@Setter
	private String attributeName;
	public GenericCheckpointParameters(String code, boolean errorType, String firstValue, String secondValue,
			String className, String attributeName) {
		super(code, errorType, firstValue, secondValue);
		this.className = className;
		this.attributeName = attributeName;
	}
	
	
}
