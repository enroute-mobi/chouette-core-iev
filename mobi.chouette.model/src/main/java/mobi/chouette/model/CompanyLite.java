/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */
package mobi.chouette.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NaturalId;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Chouette Company : a company providing public transport services.
 * <p>
 * Neptune mapping : Company <br>
 * Gtfs mapping : Agency <br>
 */

@Entity
@Table(name = "companies", schema = "public")
@EqualsAndHashCode(of = { "objectId" }, callSuper = false)
@Immutable
@NoArgsConstructor
@ToString(callSuper = true)
public class CompanyLite extends ChouetteIdentifiedObject {

	private static final String OLD_FASHION_PREFIX = "STIF:CODIFLIGNE";

	/**
	 * 
	 */
	private static final long serialVersionUID = 6913288375372751999L;

	@Getter
	@Setter
	@Id
	@Column(name = "id")
	protected Long id;

	/**
	 * Neptune object id <br>
	 * composed of 3 items separated by a colon
	 * <ol>
	 * <li>prefix : an alphanumerical value (underscore accepted)</li>
	 * <li>type : a camelcase name describing object type</li>
	 * <li>technical id: an alphanumerical value (underscore and minus accepted)
	 * </li>
	 * </ol>
	 * This data must be unique in dataset
	 * 
	 * @return The actual value
	 */
	@Getter
	@NaturalId(mutable=true)
	@Column(name = "objectid", nullable = false, unique = true)
	protected String objectId;

	public void setObjectId(String value) {
		objectId = StringUtils.abbreviate(value, 255);
	}

	/**
	 * object version
	 * 
	 * @param objectVersion
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "object_version")
	protected Long objectVersion = 1L;

	/**
	 * name
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "name")
	protected String name;

	/**
	 * line referential reference
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "line_referential_id")
	protected Long lineReferentialId;

	@Override
	public String objectIdPrefix() {
		if (objectId.startsWith(OLD_FASHION_PREFIX))
			return OLD_FASHION_PREFIX;
		return super.objectIdPrefix();
	}

	@Override
	public String objectIdSuffix() {
		if (objectId.startsWith(OLD_FASHION_PREFIX)) {
			String[] tokens = objectIdArray();
			if (tokens.length > 3)
				return tokens[3].trim();
			else
				return "";
		}
		return super.objectIdSuffix();
	}

}
