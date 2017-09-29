package mobi.chouette.model;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.common.JobData;

@SuppressWarnings("serial")
@MappedSuperclass
@Access(AccessType.FIELD)
@ToString(exclude={"referential"})
@Data
public abstract class BasicActionTask implements Serializable {
	
	public abstract Long getId();
	
	public abstract JobData.ACTION getAction();
	
	@Getter
	@Setter
	@Column(name = "status")
	private String status;


	@Getter
	@Setter
	@Column(name="workbench_id")
	private Long workbenchId;
	
	@Getter
	@Setter
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn(name="referential_id", updatable=false)
	private Referential referential;
	
	@Getter
	@Setter
	@Column(name="created_at")
	private java.sql.Timestamp createdAt;
	
	@Getter
	@Setter
	@Column(name="updated_at")
	private java.sql.Timestamp updatedAt;
	


	
}
