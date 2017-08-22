package mobi.chouette.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Chouette StopAreaLite
 * <p>
 * Neptune mapping : ChouetteStopArea, AreaCentroid <br>
 * Gtfs mapping : Stop (only for BoardingPosition, Quay and CommercialStopPoint
 * types) <br>
 */

@Entity
@Table(name = "stop_areas",schema="public")
@Immutable
@NoArgsConstructor
@ToString(callSuper = true)
public class StopAreaLite extends ChouetteLocalizedObject {
	private static final long serialVersionUID = -4655237357710413470L;

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
	 * stop area parent<br>
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "parent_id")
	protected Long parentId;

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


}
