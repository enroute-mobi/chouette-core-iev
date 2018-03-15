package mobi.chouette.exchange.netex_stif.exporter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.exchange.parameters.AbstractExportParameter;
import mobi.chouette.model.type.DateRange;


@XmlRootElement(name = "netex-export")
@XmlType(propOrder={"validityPeriods", "mode"})
@XmlAccessorType(XmlAccessType.FIELD)
public class NetexStifExportParameters  extends AbstractExportParameter{
	
	@Getter @Setter
	@XmlElement(name = "mode")
	private String mode;

    @Getter @Setter 
    @XmlElement(name = "validity_periods")
	private List<DateRange> validityPeriods = new ArrayList<>();

}
