package mobi.chouette.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j;
import mobi.chouette.model.converter.HstoreConverter;

@Entity
@Table(name = "organisations")
@NoArgsConstructor
@ToString(callSuper = true, exclude = { "ssoAttributes", "lines" })
@Log4j
public class Organisation extends ChouetteObject {
	private static final long serialVersionUID = 4952690444017749057L;

	public static final String SSO_FUNCTIONAL_SCOPE = "functional_scope";
	@Getter
	@Setter
	@SequenceGenerator(name = "organisations_id_seq", sequenceName = "organisations_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organisations_id_seq")
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
	@Column(name = "sso_attributes")
	@Convert(converter = HstoreConverter.class)
	private Map<String, String> ssoAttributes = new HashMap<String, String>();

	@Transient
	private Set<String> lines;

	public Set<String> getLines() {
		if (lines == null) {
			lines = new HashSet<>();
			String lineString = ssoAttributes.get(SSO_FUNCTIONAL_SCOPE);
			if (lineString != null && !lineString.isEmpty()) {
				try {
					JSONArray array = new JSONArray(lineString);
					for (int i = 0; i < array.length(); i++) {
						lines.add(array.getString(i));
					}
				} catch (JSONException e) {
					log.error("unable to parse string " + lineString);
				}
			}
		}
		return lines;
	}

}
