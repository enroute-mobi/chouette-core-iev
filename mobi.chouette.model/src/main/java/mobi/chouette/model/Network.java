/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */
package mobi.chouette.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
// import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
// import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.NaturalId;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.type.PTNetworkSourceTypeEnum;

/**
 * Chouette Public Transport Network : a set of lines
 * <p>
 * Neptune mapping : PTNetwork <br>
 * Gtfs mapping : none
 */
@Entity
@Table(name = "networks",schema="public")
@Cacheable
@NoArgsConstructor
@EqualsAndHashCode(of = { "objectId" }, callSuper = false)
@ToString(callSuper=true, exclude = { "lines" })
public class Network extends ChouetteIdentifiedObject {

	private static final long serialVersionUID = -8986371268064619423L;

	@Getter
	@Setter
	@SequenceGenerator(name="networks_id_seq", sequenceName="public.networks_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="networks_id_seq")
	@Id
	@Column(name = "id", nullable = false)
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
	@Column(name = "name", nullable = false)
	private String name;

	/**
	 * set name <br>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setName(String value) {
		name = StringUtils.abbreviate(value, 255);
	}

	/**
	 * comment
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "comment")
	private String comment;

	/**
	 * set comment <br>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setComment(String value) {
		comment = StringUtils.abbreviate(value, 255);
	}

	/**
	 * version date
	 * 
	 * @param versionDate
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Temporal(TemporalType.DATE)
	@Column(name = "version_date")
	private Date versionDate;

	/**
	 * description
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "description")
	private String description;

	/**
	 * set description <br>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setDescription(String value) {
		description = StringUtils.abbreviate(value, 255);
	}

	/**
	 * registration number
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "registration_number")
	private String registrationNumber;

	/**
	 * set registration number <br>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setRegistrationNumber(String value) {
		registrationNumber = StringUtils.abbreviate(value, 255);

	}

	/**
	 * source type
	 * 
	 * @param sourceType
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Enumerated(EnumType.STRING)
	@Column(name = "source_type")
	private PTNetworkSourceTypeEnum sourceType;

	/**
	 * source name
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "source_name")
	private String sourceName;

	/**
	 * set source name <br>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setSourceName(String value) {
		sourceName = StringUtils.abbreviate(value, 255);
	}

	/**
	 * source identifier
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "source_identifier")
	private String sourceIdentifier;

	/**
	 * set source identifier <br>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setSourceIdentifier(String value) {
		sourceIdentifier = StringUtils.abbreviate(value, 255);

	}

	/**
	 * xml data
	 */
	@Getter
	@Setter
	@Column(name="import_xml",columnDefinition = "text")
	private String importXml;
	
	
	/**
	 * line referential reference
	 * 
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "line_referential_id")
	protected Long lineReferentialId;

	/**
	 * lines
	 * 
	 * @param lines
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@OneToMany(mappedBy = "network")
	private List<Line> lines = new ArrayList<Line>(0);

}
