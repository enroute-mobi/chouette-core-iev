package mobi.chouette.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import org.hibernate.annotations.Type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "routing_constraint_zones")
@EqualsAndHashCode(of = { "objectId" }, callSuper = false)
@NoArgsConstructor
public class RoutingConstraint extends ChouetteIdentifiedObject implements SignedChouetteObject {

	private static final long serialVersionUID = -2247411121328805905L;

	@Setter
	@Getter
	@GenericGenerator(name = "routing_constraint_zones_id_seq", strategy = "mobi.chouette.persistence.hibernate.ChouetteTenantIdentifierGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "routing_constraint_zones_id_seq"),
			@Parameter(name = "increment_size", value = "50") })
	@GeneratedValue(generator = "routing_constraint_zones_id_seq")

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

	@Getter
	@Setter
	@Column(name = "checksum")
	private String checksum ;
	
	@Getter
	@Setter 
	@Column(name = "checksum_source")
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
	 * set name <br/>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setName(String value) {
		name = StringUtils.abbreviate(value, 255);
	}

	@Getter
	@Setter
	@Column(name = "stop_point_ids", columnDefinition = "bigint[]")
	@Type(type = "mobi.chouette.model.usertype.LongArrayUserType")
	private Long[] stopPointIds;

	@Getter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "route_id")
	private Route route;

	public void setRoute(Route route) {
		if (this.route != null) {
			this.route.getRoutingConstraints().remove(this);
		}
		this.route = route;
		if (this.route != null) {
			this.route.getRoutingConstraints().add(this);
		}
	}

	@Getter
	@Setter
	@Transient
	private List<StopPoint> stopPoints = new ArrayList<StopPoint>();
}
