package mobi.chouette.model;

import java.io.Serializable;

import mobi.chouette.common.JobData;

public abstract class ActionResource implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4385137259926047441L;

	public abstract Long getId();
	
	public abstract JobData.ACTION getAction();
}
