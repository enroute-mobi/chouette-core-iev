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
@EqualsAndHashCode(of = { "objectId" }, callSuper = false)
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
