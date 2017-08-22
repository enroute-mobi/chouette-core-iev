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
@XmlType(propOrder = { "code", "errorType", "firstValue", "secondValue" })
@AllArgsConstructor
@NoArgsConstructor
public class CheckpointParameters {
	@XmlElement(name = "code")
	private String code;
	@XmlElement(name = "error_type")
	private boolean errorType = false;
	@XmlElement(name = "first_value")
	private String firstValue;
	@XmlElement(name = "second_value")
	private String secondValue;

}
