package mobi.chouette.exchange.parameters;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.apache.log4j.Logger;

@NoArgsConstructor
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "importId", "noSave", "cleanRepository" ,"keepObsoleteLines"}, name = "actionImportParameter")
public class AbstractImportParameter extends AbstractParameter {
	
	@XmlElement(name = "import_id")
	@Getter
	@Setter
	private Long importId = null;
	

	@XmlElement(name = "no_save", defaultValue = "false")
	@Getter
	@Setter
	private boolean noSave = false;

	@XmlElement(name = "clean_repository", defaultValue = "false")
	@Getter
	@Setter
	private boolean cleanRepository = false;

	@XmlElement(name = "keep_obsolete_lines", defaultValue = "false")
	@Getter
	@Setter
	private boolean keepObsoleteLines = true;

	@Override
	public boolean isValid(Logger log) {
		return super.isValid(log);
	}

}
