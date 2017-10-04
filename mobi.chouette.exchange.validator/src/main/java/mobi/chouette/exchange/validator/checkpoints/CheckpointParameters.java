package mobi.chouette.exchange.validator.checkpoints;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@XmlType(propOrder = { "code", "checkId", "errorType", "minimumValue", "maximumValue", "patternValue" })
@AllArgsConstructor
@NoArgsConstructor
public class CheckpointParameters {
	@XmlElement(name = "code")
	private String code;
	@XmlElement(name = "check_id")
	private Long checkId;
	@XmlElement(name = "error_type")
	private boolean errorType = false;
	@XmlElement(name = "minimum_value")
	private String minimumValue;
	@XmlElement(name = "maximum_value")
	private String maximumValue;
	@XmlElement(name = "pattern_value")
	private String patternValue;

}
