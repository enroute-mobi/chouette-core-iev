package mobi.chouette.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
 * Chouette StopArea
 * <p/>
 * StopArea may be on 5 areaTypes :
 * <ul>
 * <li>BOARDINGPOSITION for physical stops on roads</li>
 * <li>QUAY for physical stops on rails</li>
 * <li>COMMERCIALSTOPPOINT to group physical stops</li>
 * <li>STOPPLACE to group commercials stops and stop places in bigger ones</li>
 * </ul>
 * theses objects have internal dependency rules :
 * <ol>
 * <li>boarding positions and quays cannot have {@link StopArea} children</li>
 * <li>commercial stop points can have only boarding position and quay children</li>
 * <li>stop places can have only stop place and commercial stop point children</li>
 * </ol>
 * <p>
 * Neptune mapping : ChouetteStopArea, AreaCentroid <br>
 * Gtfs mapping : Stop (only for BoardingPosition, Quay and CommercialStopPoint
 * types) <br>
 */

@Entity
@Table(name = "stop_areas",schema="public")
@Cacheable
@EqualsAndHashCode(of = { "objectId" }, callSuper = false)
@NoArgsConstructor
@ToString(callSuper = true, exclude = { "accessLinks", "accessPoints",
		"containedStopAreas",
		 })
public class StopArea extends ChouetteLocalizedObject {
	private static final long serialVersionUID = 4548672479038099240L;

	@Getter
	@Setter
	@SequenceGenerator(name="stop_areas_id_seq", sequenceName="public.stop_areas_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="stop_areas_id_seq")
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
	 * area type
	 * 
	 * @param areaType
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "area_type", nullable = false)
	protected String areaType;

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
	 * nearest topic name
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "nearest_topic_name")
	private String nearestTopicName;

	/**
	 * set nearest topic name <br>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setNearestTopicName(String value) {
		nearestTopicName = StringUtils.abbreviate(value, 255);

	}

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
	 * timezone
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "time_zone")
	private String timeZone;

	/**
	 * set timezone <br>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setTimeZone(String value) {
		timeZone = StringUtils.abbreviate(value, 255);
	}

	/**
	 * fare code
	 * 
	 * @param fareCode
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "fare_code")
	private Integer fareCode;

	/**
	 * lift indicator <br>
	 * 
	 * <ul>
	 * <li>true if a lift is available at this stop</li>
	 * <li>false if no lift is available at this stop</li>
	 * </ul>
	 * 
	 * @param liftAvailable
	 *            New state for lift indicator
	 * @return The actual lift indicator
	 */
	@Getter
	@Setter
	@Column(name = "lift_availability")
	private Boolean liftAvailable = false;

	/**
	 * mobility restriction indicator (such as wheel chairs) <br>
	 * 
	 * <ul>
	 * <li>true if wheel chairs can access this stop</li>
	 * <li>false if wheel chairs can't access this stop</li>
	 * </ul>
	 * 
	 * @param mobilityRestrictedSuitable
	 *            New state for mobility restriction indicator
	 * @return The actual mobility restriction indicator
	 */
	@Getter
	@Setter
	@Column(name = "mobility_restricted_suitability")
	private Boolean mobilityRestrictedSuitable = false;

	/**
	 * stairs indicator <br>
	 * 
	 * <ul>
	 * <li>true if a stairs are presents at this stop</li>
	 * <li>false if no stairs are presents at this stop</li>
	 * </ul>
	 * 
	 * @param stairsAvailable
	 *            New state for stairs indicator
	 * @return The actual stairs indicator
	 */
	@Getter
	@Setter
	@Column(name = "stairs_availability")
	private Boolean stairsAvailable = false;

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

	/**
	 * return UserNeeds as Enum list
	 * 
	 * @return UserNeeds
	 */
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

	/**
	 * update UserNeeds as Enum list
	 * 
	 * @param userNeeds
	 */
	public void setUserNeeds(List<UserNeedEnum> userNeeds) {
		int value = 0;
		for (UserNeedEnum userNeed : userNeeds) {
			int mask = 1 << userNeed.ordinal();
			value |= mask;
		}
		this.intUserNeeds = value;
	}

	
	/**
	 * stop area parent<br>
	 * unavailable for areaType = ITL
	 * 
	 * @return The actual value
	 */
	@Getter
	@ManyToOne(fetch = FetchType.LAZY ,cascade = { CascadeType.PERSIST})
	@JoinColumn(name = "parent_id")
	private StopArea parent;

	/**
	 * set stop area parent
	 * 
	 * @param stopArea
	 */
	public void setParent(StopArea stopArea) {
		if (this.parent != null) {
			this.parent.getContainedStopAreas().remove(this);
		}
		this.parent = stopArea;
		if (stopArea != null) {
			stopArea.getContainedStopAreas().add(this);
		}
	}
	
	public void forceParent(StopArea stopArea) {
		this.parent = stopArea;
	}

	/**
	 * xml data
	 */
	@Getter
	@Setter
	@Column(name="import_xml",columnDefinition = "text")
	private String importXml;
	
	
	/**
	 * StopArea referential reference
	 * 
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "stop_area_referential_id")
	protected Long stopAreaReferentialId;

	/**
	 * delete time
	 * 
	 * @param deletedTime
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "deleted_at")
	protected Date deletedTime;


	@Override
	public String objectIdPrefix() {
		if (objectId.startsWith("FR:")) return "FR";
		return super.objectIdPrefix();
	}

	@Override
	public String objectIdSuffix() {
		if (objectId.startsWith("FR:") )
		{
			String[] tokens = objectIdArray();
			if (tokens.length > 3)
				return tokens[3].trim();
			else
				return ""; 
		}
		return super.objectIdSuffix();
	}

	public boolean isDesactivated()
	{
		return deletedTime != null;
	}

	/**
	 * stop area children<br>
	 * unavailable for areaType = ITL, BoardingPosition and Quay
	 * 
	 * @param containedStopAreas
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@OneToMany(mappedBy = "parent",cascade = { CascadeType.PERSIST })
	private List<StopArea> containedStopAreas = new ArrayList<StopArea>(0);

	/**
	 * access links<br>
	 * only for areaType != ITL
	 * 
	 * @param accessLinks
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Transient
//	@OneToMany(cascade = { CascadeType.PERSIST })
//	@JoinColumn(name = "stop_area_id", updatable = false)
	private List<AccessLink> accessLinks = new ArrayList<AccessLink>(0);


	/**
	 * access points<br>
	 * only for areaType != ITL
	 * 
	 * @param accessPoints
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Transient
	// @OneToMany(mappedBy = "containedIn", cascade = { CascadeType.PERSIST })
	private List<AccessPoint> accessPoints = new ArrayList<AccessPoint>(0);


}
