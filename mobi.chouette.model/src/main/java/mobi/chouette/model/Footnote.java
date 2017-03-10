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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Chouette Footnote : a note for vehicle journeys
 * <p/>
 * Neptune mapping : non (extension in comments <br/>
 * Gtfs mapping : none <br/>
 * Hub mapping : 
 * 
 * @since 2.5.3
 */

@Entity
@Table(name = "footnotes")
@NoArgsConstructor
public class Footnote extends ChouetteIdentifiedObject {
	/**
    * 
    */
	private static final long serialVersionUID = -6223882293500225313L;

	@Getter
	@Setter
	@GenericGenerator(name = "footnotes_id_seq", strategy = "mobi.chouette.persistence.hibernate.ChouetteIdentifierGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "footnotes_id_seq"),
			@Parameter(name = "increment_size", value = "10") })
	@GeneratedValue(generator = "footnotes_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

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
	 * label
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "label")
	private String label;

	/**
	 * set label <br/>
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
	 * set code <br/>
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
