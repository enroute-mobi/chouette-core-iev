package mobi.chouette.model;

import java.io.Serializable;

import mobi.chouette.common.JobData;

public abstract class ActionResource implements Serializable{

	public abstract Long getId();
	
	public abstract JobData.ACTION getAction();
}
