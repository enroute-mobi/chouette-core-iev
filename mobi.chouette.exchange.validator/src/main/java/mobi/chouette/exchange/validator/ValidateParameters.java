package mobi.chouette.exchange.validator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.exchange.validator.checkpoints.ControlParameters;

@XmlRootElement(name = "validate")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "referencesType", "ids", "controlParameters" })
@ToString
public class ValidateParameters extends AbstractParameter {

	@Getter
	@Setter
	@XmlElement(name = "control_parameters")
	private ControlParameters controlParameters = new ControlParameters();

}
