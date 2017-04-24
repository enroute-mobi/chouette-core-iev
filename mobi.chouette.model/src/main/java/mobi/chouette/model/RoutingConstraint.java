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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "routing_constraint_zones")
@NoArgsConstructor
public class RoutingConstraint extends ChouetteIdentifiedObject {

	@Setter
	@Getter
	@GenericGenerator(name = "routing_constraint_zones_id_seq", strategy = "mobi.chouette.persistence.hibernate.ChouetteIdentifierGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "routing_constraint_zones_id_seq"),
			@Parameter(name = "increment_size", value = "50") })
	@GeneratedValue(generator = "routing_constraint_zones_id_seq")

	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

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
		this.route = route;
		if (this.route != null) {
			this.route.getRoutingConstraints().remove(this);
			this.route.getRoutingConstraints().add(this);
		}
	}

	@Getter
	@Setter
	@Transient
	private List<StopPoint> stopPoints = new ArrayList<StopPoint>();
}
