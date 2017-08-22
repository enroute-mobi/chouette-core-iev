package mobi.chouette.model;

import java.math.BigDecimal;

import com.vividsolutions.jts.geom.LineString;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Chouette Shape : geographic route between to stop areas
 * <p>
 * Neptune mapping : no <br>
 * Gtfs mapping : used for shapes <br>
 * Netex mapping : RouteLink <br>
 * 
 * @since 4.0.0
 * 
 */

// @Entity
// @Table(name = "shape")
@NoArgsConstructor
@ToString
public class Shape extends ChouetteIdentifiedObject {

	private static final long serialVersionUID = -2822152162994971162L;

	@Getter
	@Setter
//	@GenericGenerator(name = "shape_id_seq", strategy = "mobi.chouette.persistence.hibernate.ChouetteTenantIdentifierGenerator", parameters = {
//			@Parameter(name = "sequence_name", value = "shape_id_seq"),
//			@Parameter(name = "increment_size", value = "50") })
//	@GeneratedValue(generator = "shape_id_seq")
//	@Id
//	@Column(name = "id", nullable = false)
	protected Long id;

	/**
	 * length in meters
	 * 
	 * @param distance
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
//	@Column(name = "distance")
	private BigDecimal distance;

	/**
	 * polyline in WGS84 referential
	 * 
	 * @param geometry
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
//	@Column(name = "geometry")
//	@Type(type = "org.hibernate.spatial.GeometryType")
	private LineString geometry;

	/**
	 * last stop area connected to link
	 * 
	 * @param arrivalId new reference
	 * @return The actual value
	 */
	@Getter
	@Setter
//	@Column(name = "arrival_id")
	private Long arrivalId;

	/**
	 * first stop area connected to route section
	 * @param departureId new reference
	 * @return The actual value
	 */
	@Getter
	@Setter
//	@Column(name = "departure_id")
	private Long departureId;


}
