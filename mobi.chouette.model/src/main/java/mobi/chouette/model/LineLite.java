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
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Chouette Line readonly lite version: a group of Routes which is generally
 * known to the public by a similar name or number
 * <p>
 * Neptune mapping : Line <br>
 * Gtfs mapping : Line <br>
 */
@Entity
@Table(name = "lines", schema = "public")
@Immutable
@EqualsAndHashCode(of = { "objectId" }, callSuper = false)
@NoArgsConstructor
@ToString(callSuper = true)
public class LineLite extends ChouetteIdentifiedObject {
	private static final String OLD_FASHION_PREFIX = "STIF:CODIFLIGNE";

	private static final long serialVersionUID = 8809993452599427585L;

	@Getter
	@Setter
	@Id
	@Column(name = "id")
	protected Long id;

	@Getter
	@Column(name = "deactivated")
	protected boolean deactivated;

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
	 * number or short name
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "number")
	protected String number;

	/**
	 * published name
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "published_name")
	protected String publishedName;

	/**
	 * Transport mode (default value = Bus)
	 * 
	 * @param transportModeName
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Column(name = "transport_mode")
	protected String transportModeName;

	/**
	 * Transport mode (default value = Bus)
	 * 
	 * @param transportModeName
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Column(name = "transport_submode")
	protected String transportSubModeName;

	/**
	 * line referential reference
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "line_referential_id")
	protected Long lineReferentialId;

	/**
	 * network reference
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "network_id")
	protected Long networkId;
	
	/**
	 * primary company reference
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "company_id")
	protected Long companyId;
	
	/**
	 * organization 
	 * 
	 * should be filled manualy
	 * 
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Transient
	protected Organisation organisation;

	/**
	 * secondary companies references
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "secondary_company_ids", columnDefinition = "bigint[]")
	@Type(type = "mobi.chouette.model.usertype.LongArrayUserType")
	private Long[] secondaryCompanyIds = new Long[0];

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
