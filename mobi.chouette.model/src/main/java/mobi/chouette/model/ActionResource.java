package mobi.chouette.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.common.JobData;
import mobi.chouette.model.converter.HstoreConverter;

@MappedSuperclass
@ToString(callSuper = true)
public abstract class ActionResource implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4385137259926047441L;

	public abstract Long getId();
	
	public abstract JobData.ACTION getAction();
	
	public abstract Long getTaskId();
	public abstract void setTaskId(Long id);
	
	@Getter
	@Setter
	@Column(name = "status")
	private String status;

	
	@Getter
	@Setter
	@Column(name = "name")
	private String name;
	
	@Getter
	@Setter
	@Column(name = "reference")
	private String reference;
	
	@Getter
	@Setter
	@Column(name = "resource_type")
	private String type;
	

	@Getter
	@Setter
   	@Column(name="metrics")
   	@Convert(converter = HstoreConverter.class)
    private Map<String, String> metrics = new HashMap<String,String>();
	
	@Getter
	@Setter
	@Column(name = "created_at")
	private java.sql.Timestamp createdAt;

	@Getter
	@Setter
	@Column(name = "updated_at")
	private java.sql.Timestamp updatedAt;
	
}
