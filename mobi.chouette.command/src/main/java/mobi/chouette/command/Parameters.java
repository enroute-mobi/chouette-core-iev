package mobi.chouette.command;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Data;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.exchange.validation.parameters.ValidationParameters;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"configuration","validation"})
@Data
public class Parameters {


	@XmlElements(value = {
			@XmlElement(name = "netex-import", type = mobi.chouette.exchange.netex.importer.NetexImportParameters.class),
			@XmlElement(name = "netex-export", type = mobi.chouette.exchange.netex.exporter.NetexExportParameters.class),
        	@XmlElement(name = "validate", type = mobi.chouette.exchange.validator.ValidateParameters.class) })
	private AbstractParameter configuration;

	@XmlElement(name = "validation")
	private ValidationParameters validation;

}
