package mobi.chouette.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mobi.chouette.model.util.NamingUtil;

/**
 * Chouette Footnote : a note for vehicle journeys
 * <p>
 * Neptune mapping : non (extension in comments <br>
 * Gtfs mapping : none <br>
 * Hub mapping : 
 * 
 * @since 2.5.3
 */

@Entity
@Table(name = "footnotes")
@EqualsAndHashCode(of = { "objectId" }, callSuper = false)
@NoArgsConstructor
public class Footnote extends ChouetteIdentifiedObject implements SignedChouetteObject  {
	/**
    * 
    */
	private static final long serialVersionUID = -6223882293500225313L;

	@Getter
	@Setter
	@GenericGenerator(name = "footnotes_id_seq", strategy = "mobi.chouette.persistence.hibernate.ChouetteTenantIdentifierGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "footnotes_id_seq"),
			@Parameter(name = "increment_size", value = "10") })
	@GeneratedValue(generator = "footnotes_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	@Getter
	@Setter
	@Column(name = "checksum")
	private String checksum ;
	
	@Getter
	@Setter 
	@Column(name = "checksum_source")
	private String checksumSource;


//	@Getter 
	@Setter 
	@Transient 
	private String objectId;
	
	public String getObjectId()
	{
		if (objectId == null && lineLite != null)
		{
			objectId = lineLite.objectIdPrefix()+":Notice:"+lineLite.objectIdSuffix()+"_"+NamingUtil.getName(this)+":LOC";
		}
		return objectId;
	}
	
	
	@Getter 
	@Setter 
	@Transient 
	private Long objectVersion;


	/**
	 * referenced line
	 * 
	 * @param line
	 *            new line
	 * @return The actual line
	 */
	@Getter
	@Setter
	@Transient
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "line_id")
	private Line line;
	
	/**
	 * line reverse reference
	 * 
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Transient
	private LineLite lineLite;

	@Getter
	@Setter
	@Column(name = "line_id")
	private Long lineId;
	

	/**
	 * label
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "label")
	private String label;

	/**
	 * set label <br>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setLabel(String value) {
		label = StringUtils.abbreviate(value, 255);
	}

	/**
	 * code
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "code")
	private String code;

	/**
	 * set code <br>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setCode(String value) {
		code = StringUtils.abbreviate(value, 255);
	}


	/**
	 * relative key for import/export
	 * 
	 * should be unique for each line
	 * 
	 * @param key
	 *            new key
	 * @return The actual key
	 */
	@Getter
	@Setter
	@Transient
	private String key;

}
