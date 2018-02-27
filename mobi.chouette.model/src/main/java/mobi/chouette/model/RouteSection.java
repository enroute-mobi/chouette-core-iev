package mobi.chouette.model;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.vividsolutions.jts.geom.LineString;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Chouette Route Section : geographic route between to stop areas
 * <p>
 * Neptune mapping : no <br>
 * Gtfs mapping : used for shapes
 * 
 * @since 3.2.0
 * 
 */

// @Entity
// @Table(name = "route_sections",schema="public")
@EqualsAndHashCode(of = { "objectId" }, callSuper = false)
@NoArgsConstructor
@ToString
public class RouteSection extends ChouetteIdentifiedObject {
	private static final long serialVersionUID = 8490105295077539089L;

	@Getter
	@Setter
// 	@SequenceGenerator(name="route_sections_id_seq", sequenceName="public.route_sections_id_seq", allocationSize=1)
//     @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="route_sections_id_seq")
// 	@Id
// 	@Column(name = "id", nullable = false)
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
//	@NaturalId(mutable=true)
//	@Column(name = "objectid", nullable = false, unique = true)
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
//	@Column(name = "object_version")
	protected Long objectVersion = 1L;

	/**
	 * length in meters
	 * 
	 * @param distance
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
// 	@Column(name = "distance")
	private BigDecimal distance;

	/**
	 * <br>
	 * 
	 * <ul>
	 * <li>true if</li>
	 * <li>false if</li>
	 * </ul>
	 * 
	 * @param noProcessing
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
// 	@Column(name = "no_processing")
	private Boolean noProcessing = false;

	@Getter
	@Setter
// 	@Column(name = "input_geometry")
// 	@Type(type = "org.hibernate.spatial.GeometryType")
	private LineString inputGeometry;

	@Getter
	@Setter
// 	@Column(name = "processed_geometry")
// 	@Type(type = "org.hibernate.spatial.GeometryType")
	private LineString processedGeometry;

	/**
	 * first stop area connected to route section
	 * 
	 * @return The actual value
	 */
	@Getter
// 	@ManyToOne(fetch = FetchType.LAZY)
// 	@JoinColumn(name = "departure_id")
	private StopArea departure;

	/**
	 * set departure
	 * 
	 * @param stopArea
	 */
	public void setDeparture(StopArea stopArea) {
		this.departure = stopArea;
	}

	/**
	 * last stop area connected to link
	 * 
	 * @return The actual value
	 */
	@Getter
// 	@ManyToOne(fetch = FetchType.LAZY)
// 	@JoinColumn(name = "arrival_id")
	private StopArea arrival;

	/**
	 * set stopArea
	 * 
	 * @param stopArea
	 */
	public void setArrival(StopArea stopArea) {
		this.arrival = stopArea;
	}

}
