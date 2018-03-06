package mobi.chouette.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.type.AlightingPossibilityEnum;
import mobi.chouette.model.type.BoardingPossibilityEnum;

/**
 * Chouette StopPoint : a StopPoint on a route
 * <p>
 * Neptune mapping : StopPoint <br>
 * Gtfs mapping : none
 */

@Entity
@Table(name = "stop_points")
@NoArgsConstructor
@EqualsAndHashCode(of = { "objectId" }, callSuper = false)
@ToString(callSuper=true, exclude = { "route" })
public class StopPoint extends ChouetteIdentifiedObject {

	private static final long serialVersionUID = -4913573673645997423L;

	@Getter
	@Setter
	@GenericGenerator(name = "stop_points_id_seq", strategy = "mobi.chouette.persistence.hibernate.ChouetteTenantIdentifierGenerator", 
		parameters = {
			@Parameter(name = "sequence_name", value = "stop_points_id_seq"),
			@Parameter(name = "increment_size", value = "100") })
	@GeneratedValue(generator = "stop_points_id_seq")
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
	 * position on the route
	 * 
	 * @param position
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "position")
	private Integer position;

	   /**
	    * boarding possibility
	    * 
	    * @param forBoarding
	    *           New value
	    * @return The actual value
	    * 
	    * @since 2.5.2
	    */
	   @Getter
	   @Setter
	   @Enumerated(EnumType.STRING)
	   @Column(name = "for_boarding")
	   private BoardingPossibilityEnum forBoarding;

	   /**
	    * alighting possibility
	    * 
	    * @param forAlighting
	    *           New value
	    * @return The actual value
	    * 
	    * @since 2.5.2
	    */
	   @Getter
	   @Setter
	   @Enumerated(EnumType.STRING)
	   @Column(name = "for_alighting")
	   private AlightingPossibilityEnum forAlighting;

	   /**
	 * stop area container
	 * 
	 * @return The actual value
	 */
	@Getter
	@Transient
//	@ManyToOne(fetch = FetchType.LAZY , cascade = { CascadeType.PERSIST})
//	@JoinColumn(name = "stop_area_id")
	private StopArea containedInStopArea;

	/**
	 * set stop area
	 * 
	 * @param stopArea
	 */
	public void setContainedInStopArea(StopArea stopArea) {
		this.containedInStopArea = stopArea;
	}

	/**
	 * route
	 * 
	 * @return The actual value
	 */
	@Getter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "route_id")
	private Route route;

	/**
	 * set route
	 * 
	 * @param route
	 */
	public void setRoute(Route route) {
		if (this.route != null) {
			this.route.getStopPoints().remove(this);
		}
		this.route = route;
		if (route != null) {
			route.getStopPoints().add(this);
		}
	}
	
	/**
	 * comment : not saved, use for extension
	 * 
	 * @param : comment
	 * @return the actual value
	 */
	@Getter
	@Setter
	@Transient
	private String comment;
	
	@Getter @Setter
	@Column(name = "stop_area_id")
	private Long stopAreaId;

}
