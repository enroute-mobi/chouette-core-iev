/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */
package mobi.chouette.model;

import java.util.regex.Pattern;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Abstract object used for all Identified Chouette Object
 * <p>
 */
@SuppressWarnings("serial")
@MappedSuperclass
@ToString(callSuper = true)
public abstract class ChouetteIdentifiedObject extends ChouetteDatedObject {

	public abstract String getObjectId();
	public abstract void setObjectId(String objectId);

	public abstract Long getObjectVersion();
	public abstract void setObjectVersion(Long objectVersion);

 
	@Getter
	@Setter
	@Transient
	private boolean saved = false;

	@Getter
	@Setter
	@Transient
	private boolean isFilled = false;

	/**
	 * to be overrided; facility to check registration number on any object
	 * 
	 * @return null : when object has no registration number
	 */
	public String getRegistrationNumber() {
		return null;
	}

	/**
	 * check if an objectId is conform to Trident
	 * 
	 * @param oid
	 *            objectId to check
	 * @return true if valid, false otherwise
	 */
	public static boolean checkObjectId(String oid) {
		if (oid == null)
			return false;

		Pattern p = Pattern.compile("(\\w|_)+:\\w+:([0-9A-Za-z]|_|-)+");
		return p.matcher(oid).matches();
	}

	protected String[] objectIdArray() {
		return getObjectId().split(":");
	}

	/**
	 * return prefix of objectId
	 * 
	 * @return String
	 */
	public String objectIdPrefix() {
		String[] tokens = objectIdArray();
		if (tokens.length > 2) {
			return tokens[0].trim();
		} else
			return "";
	}

	/**
	 * return suffix of objectId
	 * 
	 * @return String
	 */
	public String objectIdSuffix() {
		String[] tokens = objectIdArray();
		if (tokens.length > 2)
			return tokens[2].trim();
		else
			return "";
	}
}
