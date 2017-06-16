package mobi.chouette.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CollectionType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.common.JobData;
import mobi.chouette.model.converter.HstoreConverter;

@MappedSuperclass
@ToString(callSuper = true)
public abstract class ActionMessage implements Serializable{

    public enum CRITICITY { INFO, WARNING, ERROR };
	
	private static final long serialVersionUID = -4385137259926047441L;

	public abstract Long getId();
	
	public abstract JobData.ACTION getAction();
	
	public abstract Long getTaskId();
	public abstract void setTaskId(Long id);
	
	@Getter
	@Setter
	@Column(name = "criticity")
	@Enumerated(EnumType.ORDINAL)
	private CRITICITY criticity;
	
	@Getter
	@Setter
	@Column(name = "message_key")
	private String messageKey;
	
	@Getter
	@Setter
	@Column(name="message_attributs")
	@CollectionType(type="java.util.HashMap")
   	@Convert(converter = HstoreConverter.class)
	private Map<String, String> messageAttributs = new HashMap<String, String>();
	
	@Getter
	@Setter
	@Column(name="resource_id")
	private Long resourceId;

	@Getter
	@Setter
	@Column(name = "created_at")
	private java.sql.Timestamp createdAt;

//	@Getter
//	@Setter
//	@Column(name = "updated_at")
//	private java.sql.Timestamp updatedAt;
	
}
