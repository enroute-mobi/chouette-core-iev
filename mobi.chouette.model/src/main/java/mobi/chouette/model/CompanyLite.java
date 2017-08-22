/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */
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
 * Chouette Company : a company providing public transport services.
 * <p>
 * Neptune mapping : Company <br>
 * Gtfs mapping : Agency <br>
 */

@Entity
@Table(name = "companies",schema="public")
@Immutable
@NoArgsConstructor
@ToString(callSuper=true)
public class CompanyLite extends ChouetteIdentifiedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6913288375372751999L;

	@Getter
	@Setter
	@Id
	@Column(name = "id")
	protected Long id;
	
	/**
	 * name
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "name")
	protected String name;

	/**
	 * line referential reference
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "line_referential_id")
	protected Long lineReferentialId;
	
	@Override
	public String objectIdPrefix() {
		if (objectId.startsWith("STIF:CODIFLIGNE")) return "STIF:CODIFLIGNE";
		return super.objectIdPrefix();
	}

	@Override
	public String objectIdSuffix() {
		if (objectId.startsWith("STIF:CODIFLIGNE") )
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
