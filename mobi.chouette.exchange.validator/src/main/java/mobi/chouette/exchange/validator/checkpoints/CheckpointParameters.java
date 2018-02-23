package mobi.chouette.exchange.validator.checkpoints;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@XmlType(propOrder = { "originCode", "specificCode", "name", "checkId", "errorType", "minimumValue", "maximumValue", "patternValue" })
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CheckpointParameters {
	@XmlElement(name = "origin_code")
	private String originCode;
	@XmlElement(name = "specific_code")
	private String specificCode;
	@XmlElement(name = "name")
	private String name;
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
