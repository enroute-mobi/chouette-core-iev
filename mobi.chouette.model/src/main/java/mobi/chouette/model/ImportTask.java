package mobi.chouette.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "imports")
@NoArgsConstructor
@ToString(callSuper = true)
public class ImportTask {
	@Getter
	@Setter
	@SequenceGenerator(name="imports_id_seq", sequenceName="imports_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="imports_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;
	
	@Getter
	@Setter
	@Column(name = "name")
	private String name;
	
	@Getter
	@Setter
	@Column(name = "status")
	private String status;
	
	@Getter
	@Setter
	@Transient
	private String format = "netex_stif";
	
	@Getter
	@Setter
	@Column(name = "current_step_id")
	private String currentStepId;
	
	@Getter
	@Setter
	@Column(name="current_step_progress")
	private double currentStepProgress;
	
	@Getter
	@Setter
	@Column(name="workbench_id")
	private Long workbenchId;
	
	@Getter
	@Setter
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(name="referential_id", insertable=false, updatable=false)
	private Referential referential;
	
	@Getter
	@Setter
	@Column(name="created_at")
	private java.sql.Timestamp createdAt;
	
	@Getter
	@Setter
	@Column(name="updated_at")
	private java.sql.Timestamp updatedAt;
	
	@Getter
	@Setter
	@Column(name="started_at")
	private java.sql.Timestamp startedAt;
	
	@Getter
	@Setter
	@Column(name="ended_at")
	private java.sql.Timestamp endedAt;

	@Getter
	@Setter
	@Column(name="file")
	private String file;
	

}
