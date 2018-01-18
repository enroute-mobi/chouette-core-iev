package mobi.chouette.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.converter.HstoreConverter;

@Entity
@Table(name = "organisations")
@NoArgsConstructor
@ToString(callSuper = true)
public class Organisation extends ChouetteObject {
	private static final long serialVersionUID = 4952690444017749057L;

	@Getter
	@Setter
	@SequenceGenerator(name="organisations_id_seq", sequenceName="organisations_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="organisations_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	@Getter
	@Setter
	@Column(name = "name")
	private String name;
	
	@Getter
	@Setter
	@Column(name = "code")
	private String code;
	
	@Getter
	@Setter
   	@Column(name="sso_attributes")
   	@Convert(converter = HstoreConverter.class)
    private Map<String, String> ssoAttributes = new HashMap<String,String>();

}
