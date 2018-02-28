package mobi.chouette.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NaturalId;

import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(of = { "objectId" }, callSuper = false)
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
	@Setter
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
	@Setter
	@Column(name = "area_type", nullable = false)
	protected String areaType;


	/**
	 * stop area parent<br>
	 * 
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "parent_id")
	protected Long parentId;
	
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
		// TODO conditions à définir
		return deletedTime != null;
	}

}
