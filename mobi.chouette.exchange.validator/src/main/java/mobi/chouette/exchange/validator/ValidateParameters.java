package mobi.chouette.exchange.validator;

import java.util.ArrayList;
import java.util.List;

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

	@XmlElement(name = "references_type")
	@Getter
	@Setter
	private String referencesType;

	@Getter
	@Setter
	@XmlElement(name = "reference_ids")
	private List<Long> ids = new ArrayList<>();

	@Getter
	@Setter
	@XmlElement(name = "control_parameters")
	private ControlParameters controlParameters = new ControlParameters();

}
