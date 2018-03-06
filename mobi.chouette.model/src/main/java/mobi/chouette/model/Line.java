/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */
package mobi.chouette.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.NaturalId;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.type.UserNeedEnum;

/**
 * Chouette Line : a group of Routes which is generally known to the public by a
 * similar name or number
 * <p>
 * Neptune mapping : Line <br>
 * Gtfs mapping : Line <br>
 */
@Entity
@Table(name = "lines",schema="public")
@NoArgsConstructor
@EqualsAndHashCode(of = { "objectId" }, callSuper = false)
@ToString(callSuper = true)
public class Line extends ChouetteIdentifiedObject {
	private static final long serialVersionUID = -8086291270595894778L;

	@Getter
	@Setter
	@SequenceGenerator(name="lines_id_seq", sequenceName="public.lines_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="lines_id_seq")
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
	@Column(name = "name")
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
	 * number or short name
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "number")
	private String number;

	/**
	 * set number <br>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setNumber(String value) {
		number = StringUtils.abbreviate(value, 255);
	}

	/**
	 * published name
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "published_name")
	private String publishedName;

	/**
	 * stable id
	 * 
	 * @return The actual value
	 * 
	 * @since 3.1.0
	 */
	@Getter
	@Column(name = "stable_id")
	private String stableId;

	/**
	 * set published name <br>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setPublishedName(String value) {
		publishedName = StringUtils.abbreviate(value, 255);
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
	 * Transport mode (default value = Bus)
	 * 
	 * @param transportModeName
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "transport_mode")
	private String transportModeName;

	/**
	 * Transport sub mode (default value = Bus)
	 * 
	 * @param transportSubModeName
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "transport_submode")
	protected String transportSubModeName;
	/**
	 * mobility restriction indicator (such as wheel chairs) <br>
	 * 
	 * <ul>
	 * <li>null if information n is unknown for this line</li>
	 * <li>true if wheel chairs can use this line</li>
	 * <li>false if wheel chairs can't use this line</li>
	 * </ul>
	 * 
	 * @param mobilityRestrictedSuitable
	 *            New state for mobility restriction indicator
	 * @return The actual mobility restriction indicator
	 */
	@Getter
	@Setter
	@Column(name = "mobility_restricted_suitability")
	private Boolean mobilityRestrictedSuitable;

	/**
	 * coded user needs as binary map<br>
	 * 
	 * use following methods for easier access :
	 * <ul>
	 * <li>getUserNeeds</li>
	 * <li>setUserNeeds</li>
	 * <li>addUserNeed</li>
	 * <li>addAllUserNeed</li>
	 * <li>removeUserNeed</li>
	 * </ul>
	 * 
	 * @param intUserNeeds
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "int_user_needs")
	private Integer intUserNeeds = 0;

	public List<UserNeedEnum> getUserNeeds() {
		List<UserNeedEnum> result = new ArrayList<UserNeedEnum>();
		if (intUserNeeds == null) return result;
		for (UserNeedEnum userNeed : UserNeedEnum.values()) {
			int mask = 1 << userNeed.ordinal();
			if ((this.intUserNeeds & mask) == mask) {
				result.add(userNeed);
			}
		}
		return result;
	}

	public void setUserNeeds(List<UserNeedEnum> userNeeds) {
		int value = 0;
		for (UserNeedEnum userNeed : userNeeds) {
			int mask = 1 << userNeed.ordinal();
			value |= mask;
		}
		this.intUserNeeds = value;
	}

	/**
	 * flexible service <br>
	 * 
	 * <ul>
	 * <li>null if unknown</li>
	 * <li>true for flexible service</li>
	 * <li>false for regular service</li>
	 * </ul>
	 * 
	 * @param flexibleService
	 *           New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "flexible_service")
	private Boolean flexibleService;

	/**
	 * web site url
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "url")
	private String url;

	/**
	 * set web site url <br>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setUrl(String value) {
		url = StringUtils.abbreviate(value, 255);
	}

	/**
	 * line drawing color <br>
	 * should be used also on label background
	 * 
	 * @return The actual value in RRGGBB hexadecimal format
	 */
	@Getter
	@Column(name = "color", length = 6)
	private String color;

	/**
	 * set line drawing color <br>
	 * truncated to 6 characters if too long
	 * 
	 * @param value
	 *            New value in RRGGBB hexadecimal format
	 */
	public void setColor(String value) {
		color = StringUtils.abbreviate(value, 6);
	}

	/**
	 * line text color
	 * 
	 * @return The actual value in RRGGBB hexadecimal format
	 */
	@Getter
	@Column(name = "text_color", length = 6)
	private String textColor;

	/**
	 * set line text color <br>
	 * truncated to 6 characters if too long
	 * 
	 * @param value
	 *            New value in RRGGBB hexadecimal format
	 */
	public void setTextColor(String value) {
		textColor = StringUtils.abbreviate(value, 6);
	}

	/**
	 * network reference
	 * 
	 * @return The actual value
	 */
	@Getter
	@ManyToOne(cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "network_id")
	private Network network;

	/**
	 * set network
	 * 
	 * @param network New value
	 */
	public void setNetwork(Network network) {
		if (this.network != null) {
			this.network.getLines().remove(this);
		}
		this.network = network;
		if (network != null) {
			network.getLines().add(this);
		}
	}

	@Getter
	@Setter
	@Column(name = "deactivated")
	protected boolean deactivated;


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
	 * company reference
	 * 
	 * @return The actual value
	 */
	@Getter
	@ManyToOne(cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "company_id")
	private Company company;

	/**
	 * set company
	 * 
	 * @param company New value
	 */
	public void setCompany(Company company) {
		if (this.company != null) {
			this.company.getLines().remove(this);
		}
		this.company = company;
		if (company != null) {
			company.getLines().add(this);
		}
	}

	/**
	 * list of routes
	 * 
	 * @param routes
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Transient
// 	@OneToMany(mappedBy = "line", cascade = { CascadeType.PERSIST })
	private List<Route> routes = new ArrayList<Route>(0);

	/**
	 * list of footnotes
	 * 
	 * @param footnotes
	 *            New value
	 * @return The actual value
	 * 
	 * @since 2.5.3
	 */
	@Getter
	@Setter
	@Transient
	// @OneToMany(mappedBy = "line", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Footnote> footnotes = new ArrayList<>(0);

	/**
	 * groups of lines reverse reference
	 * 
	 * @param groupOfLines
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@ManyToMany(cascade = { CascadeType.PERSIST })
	@JoinTable(name = "group_of_lines_lines", joinColumns = { @JoinColumn(name = "line_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "group_of_line_id", nullable = false, updatable = false) })
	private List<GroupOfLine> groupOfLines = new ArrayList<GroupOfLine>(0);



}
