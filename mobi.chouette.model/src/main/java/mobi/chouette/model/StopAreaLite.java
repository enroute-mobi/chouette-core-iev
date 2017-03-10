package mobi.chouette.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Chouette StopArea
 * <p/>
 * StopArea may be on 5 areaTypes :
 * <ul>
 * <li>BOARDINGPOSITION for physical stops on roads</li>
 * <li>QUAY for physical stops on rails</li>
 * <li>COMMERCIALSTOPPOINT to group physical stops</li>
 * <li>STOPPLACE to group commercials stops and stop places in bigger ones</li>
 * <li>ITL to group any other type for routing constraint purpose</li>
 * </ul>
 * theses objects have internal dependency rules :
 * <ol>
 * <li>only boarding positions and quays can have {@link StopPoint} children</li>
 * <li>boarding positions and quays cannot have {@link StopArea} children</li>
 * <li>commercial stop points can have only boarding position and quay children</li>
 * <li>stop places can have only stop place and commercial stop point children</li>
 * <li>routing constraint stops can't have routing constraint stops children</li>
 * </ol>
 * <p/>
 * Neptune mapping : ChouetteStopArea, AreaCentroid <br/>
 * Gtfs mapping : Stop (only for BoardingPosition, Quay and CommercialStopPoint
 * types) <br/>
 */

/**
 * @author dsuru
 * 
 */
@Entity
@Table(name = "stop_areas",schema="public")
@Immutable
@NoArgsConstructor
@ToString(callSuper = true, exclude = { "parent" })
public class StopAreaLite extends ChouetteLocalizedObject {
	private static final long serialVersionUID = 4548672479038099240L;

	@Getter
	@Setter
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;
	
	/**
	 * line referential reference
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "stop_area_referential_id")
	protected Long stopAreaReferentialId;

	/**
	 * name
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "name")
	protected String name;

	/**
	 * area type
	 * 
	 * @param areaType
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Column(name = "area_type", nullable = false)
	protected String areaType;


	/**
	 * stop area parent<br/>
	 * unavailable for areaType = ITL
	 * 
	 * @return The actual value
	 */
	@Getter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	protected StopAreaLite parent;


}
