package mobi.chouette.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.NaturalId;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Chouette GroupOfLine : to associate lines with common purpose
 * <p>
 * Neptune mapping : GroupOfLine <br>
 * 
 */
@Entity
@Table(name = "group_of_lines",schema="public")
@Cacheable
@NoArgsConstructor
@EqualsAndHashCode(of = { "objectId" }, callSuper = false)
@ToString(callSuper=true, exclude = {"lines" })
public class GroupOfLine extends ChouetteIdentifiedObject {

	private static final long serialVersionUID = 2900948915585746984L;

	@Getter
	@Setter
	@SequenceGenerator(name="group_of_lines_id_seq", sequenceName="public.group_of_lines_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="group_of_lines_id_seq")
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
	 * registration number
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "registration_number", unique = true)
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
	 * grouped Lines
	 * 
	 * @param lines
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@ManyToMany(mappedBy = "groupOfLines")
	private List<Line> lines = new ArrayList<Line>(0);

	/**
	 * add a line to list only if not already present <br>
	 * do not affect lineIds list
	 * 
	 * @param line
	 *            line to add
	 */
	public void addLine(Line line) {
		if (!getLines().contains(line)) {
			getLines().add(line);
		}
		if (!line.getGroupOfLines().contains(this)) {
			line.getGroupOfLines().add(this);
		}
	}

	/**
	 * remove a line from the group
	 * 
	 * @param line
	 */
	public void removeLine(Line line) {
		getLines().remove(line);
		line.getGroupOfLines().remove(this);
	}

}
