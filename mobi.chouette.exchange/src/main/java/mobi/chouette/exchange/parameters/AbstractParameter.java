package mobi.chouette.exchange.parameters;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"name","userName","organisationName","organisationCode","referentialName","referentialId","lineReferentialId","stopAreaReferentialId","referencesType", "ids","test"},name="actionParameters")
public class AbstractParameter {

	@Getter@Setter
	@XmlElement(name = "name", required=true)
	private String name;

	@Getter@Setter
	@XmlElement(name = "user_name", required=true)
	private String userName;

	@Getter@Setter
	@XmlElement(name = "organisation_name")
	private String organisationName;

	@Getter@Setter
	@XmlElement(name = "organisation_code")
	private String organisationCode;

	@Getter@Setter
	@XmlElement(name = "referential_name")
	private String referentialName;
	
	@XmlElement(name = "referential_id")
	@Getter
	@Setter
	private Long referentialId = null;
	
	@XmlElement(name = "line_referential_id")
	@Getter
	@Setter
	private Long lineReferentialId = null;
	
	@XmlElement(name = "stop_area_referential_id")
	@Getter
	@Setter
	private Long stopAreaReferentialId = null;
	

	
	@Getter
	@Setter
	@XmlElement(name = "references_type", required = true)
	private String referencesType;

	@Getter
	@Setter
	@XmlElement(name = "reference_ids")
	private Set<Long> ids = new HashSet<>();
	
	@Getter@Setter
	@XmlElement(name = "test")
	private long test = 0;
	
	public boolean isValid(Logger log)
	{
		return true;
	}

}
