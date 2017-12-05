/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */
package mobi.chouette.dao;

import mobi.chouette.core.ChouetteRuntimeException;

@SuppressWarnings("serial")
public class DaoRuntimeException extends ChouetteRuntimeException {
	private static final String PREFIX = "DAO";
	private DaoExceptionCode code;

	public DaoRuntimeException(DaoExceptionCode code, String message) {
		super(message);
		this.code = code;
	}

	public DaoRuntimeException(DaoExceptionCode code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public DaoExceptionCode getExceptionCode() {
		return code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.certu.chouette.common.ChouetteRuntimeException#getCode()
	 */
	public String getCode() {
		return code.name();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.certu.chouette.common.ChouetteRuntimeException#getPrefix()
	 */
	@Override
	public String getPrefix() {
		return PREFIX;
	}

}
