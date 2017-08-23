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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.type.PTDirectionEnum;

/**
 * Chouette Route : An ordered list of StopPoints defining one single path
 * through the network. <br>
 * When a route pass through the same physical point more than once, one stop
 * point must be created for each occurrence.
 * <p>
 * Neptune mapping : ChouetteRoute, PTLink <br>
 * Gtfs mapping : none <br>
 * 
 */
@Entity
@Table(name = "routes")
@NoArgsConstructor
@ToString(callSuper = true, exclude = { "line", "oppositeRoute" })
public class Route extends ChouetteIdentifiedObject implements SignedChouetteObject {

	private static final long serialVersionUID = -2249654966081042738L;

	@Getter
	@Setter
	@GenericGenerator(name = "routes_id_seq", strategy = "mobi.chouette.persistence.hibernate.ChouetteTenantIdentifierGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "routes_id_seq"),
			@Parameter(name = "increment_size", value = "50") })
	@GeneratedValue(generator = "routes_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	@Getter
	@Setter
	@Transient
	private String checksum ;
	
	@Getter
	@Setter 
	@Transient
	private String checksumSource;

	
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
	 * opposite route identifier <br>
	 * an opposite route must have it's wayBack attribute on reverse value<br>
	 * 
	 * the model doesn't map this relationship as object as facility on saving
	 * in database
	 * 
	 * @return The actual value
	 */

	@Getter
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "opposite_route_id")
	private Route oppositeRoute;

	/**
	 * opposite route identifier <br>
	 * 
	 * @param oppositeRoute
	 *            new value
	 */
	public void setOppositeRoute(Route oppositeRoute) {

		if (this.oppositeRoute != oppositeRoute) {
			if (this.oppositeRoute != null) {
				Route tmp = this.oppositeRoute;
				this.oppositeRoute = null;
				tmp.setOppositeRoute(null);
			}
			this.oppositeRoute = oppositeRoute;
			if (oppositeRoute != null) {
				oppositeRoute.setOppositeRoute(this);
			}
		}
	}
	
	/**
	 * opposite route identifier <br>
	 * for error use-cases only
	 * 
	 * @param oppositeRoute
	 *            new value
	 */
	public void forceOppositeRoute(Route oppositeRoute) {

		if (this.oppositeRoute != oppositeRoute) {
			this.oppositeRoute = oppositeRoute;
		}
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
	 * number
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
	 * direction
	 * 
	 * @param direction
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Enumerated(EnumType.STRING)
	@Column(name = "direction")
	private PTDirectionEnum direction;

	/**
	 * wayback <br>
	 * possible values :
	 * <ul>
	 * <li>A : outBound</li>
	 * <li>R : inBound</li>
	 * </ul>
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "wayback")
	private String wayBack;

	/**
	 * set wayBack <br>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setWayBack(String value) {
		wayBack = StringUtils.abbreviate(value, 255);
	}

	/**
	 * line reverse reference
	 * 
	 * @return The actual value
	 */
	@Getter
	@Transient
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "line_id")
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

	/**
	 * set line reverse reference
	 * 
	 * @param line
	 */
	public void setLine(Line line) {
		if (this.line != null) {
			this.line.getRoutes().remove(this);
		}
		this.line = line;
		if (line != null) {
			line.getRoutes().add(this);
		}
	}

	/**
	 * journeyPatterns
	 * 
	 * @param journeyPatterns
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@OneToMany(mappedBy = "route", cascade = { CascadeType.PERSIST })
	private List<JourneyPattern> journeyPatterns = new ArrayList<JourneyPattern>(0);

	/**
	 * routingConstraints
	 * 
	 * @param RoutingConstraints
	 *            New value
	 * @return The actual value
	 */

	@Getter
	@Setter
	@OneToMany(mappedBy = "route", cascade = { CascadeType.PERSIST })
	private List<RoutingConstraint> routingConstraints = new ArrayList<RoutingConstraint>(0);

	/**
	 * stopPoints
	 * 
	 * @param stopPoints
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@OneToMany(mappedBy = "route", cascade = { CascadeType.PERSIST })
	@OrderColumn(name = "position", nullable = false)
	private List<StopPoint> stopPoints = new ArrayList<StopPoint>(0);

	@Setter
	@Getter
	@Column(name = "line_id")
	private Long lineId;

}
