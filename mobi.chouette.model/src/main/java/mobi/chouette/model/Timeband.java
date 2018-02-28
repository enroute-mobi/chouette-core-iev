package mobi.chouette.model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;

/**
 * Models the time band for journies in timesheet category.
 * 
 * @author zbouziane
 * @since 3.2.0
 * 
 */
@Entity
@Table(name = "timebands")
@NoArgsConstructor
@EqualsAndHashCode(of = { "objectId" }, callSuper = false)
@ToString(callSuper = true, exclude = { "journeyFrequencies" })
public class Timeband extends ChouetteIdentifiedObject {

	private static final long serialVersionUID = 3366941607519552650L;

	@Getter
	@Setter
	@GenericGenerator(name = "timebands_id_seq", strategy = "mobi.chouette.persistence.hibernate.ChouetteTenantIdentifierGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "timebands_id_seq"),
			@Parameter(name = "increment_size", value = "100") })
	@GeneratedValue(generator = "timebands_id_seq")
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
	 * Timeband name
	 * 
	 * @return The name of this time band
	 */
	@Getter
	@Column(name = "name")
	private String name;

	/**
	 * set name <br/>
	 * truncated to 255 characters if too long
	 * 
	 * @param name
	 *            The new name of this time band
	 */
	public void setName(String value) {
		name = StringUtils.abbreviate(value, 255);
	}

	/**
	 * start time
	 * 
	 * @param startTime
	 *            The new start time of this time band
	 * @return The start time of this time band
	 */
	@Getter
	@Setter
	@Column(name = "start_time", nullable = false)
	private Time startTime;

	/**
	 * end time
	 * 
	 * @param endTime
	 *            The new end time of this time band
	 * @return The end time of this time band
	 */
	@Getter
	@Setter
	@Column(name = "end_time", nullable = false)
	private Time endTime;

	@Getter
	@Setter
	@OneToMany(mappedBy = "timeband", cascade = { CascadeType.PERSIST })
	private List<JourneyFrequency> journeyFrequencies = new ArrayList<>(0);
}
