package mobi.chouette.exchange.validator.checkpoints;

import javax.xml.bind.annotation.XmlType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlType(propOrder = { "code", "errorType", "minimumValue", "maximumValue", "patternValue" ,"className", "attributeName" })
@NoArgsConstructor
public class GenericCheckpointParameters extends CheckpointParameters {
	@Getter
	@Setter
	private String className;
	@Getter
	@Setter
	private String attributeName;
	public GenericCheckpointParameters(String code, boolean errorType, String minimumValue, String maximumValue,  String patternValue,
			String className, String attributeName) {
		super(code, errorType, minimumValue, maximumValue,patternValue);
		this.className = className;
		this.attributeName = attributeName;
	}
	
	
}
