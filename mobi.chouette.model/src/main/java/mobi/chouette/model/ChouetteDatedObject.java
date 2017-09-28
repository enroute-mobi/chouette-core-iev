/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */
package mobi.chouette.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Abstract object used for all Dated Chouette Object
 * <p>
 */
@SuppressWarnings("serial")
@MappedSuperclass
@ToString(callSuper = true)
public abstract class ChouetteDatedObject extends ChouetteObject {


	/**
	 * creation time
	 * 
	 * @param creationTime
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "created_at")
	protected Date creationTime = new Date();

	/**
	 * update time
	 * 
	 * @param updateTime
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "updated_at")
	protected Date updatedTime = new Date();

}
