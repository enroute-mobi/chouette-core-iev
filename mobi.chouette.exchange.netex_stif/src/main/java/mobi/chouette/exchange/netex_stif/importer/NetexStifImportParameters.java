package mobi.chouette.exchange.netex_stif.importer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.NoArgsConstructor;
import lombok.ToString;
import mobi.chouette.exchange.parameters.AbstractImportParameter;

@XmlRootElement(name = "netex-import")
@NoArgsConstructor
@ToString(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(propOrder={})
public class NetexStifImportParameters extends AbstractImportParameter {

}
