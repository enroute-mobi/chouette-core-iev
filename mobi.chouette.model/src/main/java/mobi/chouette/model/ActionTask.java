package mobi.chouette.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.common.JobData;

@SuppressWarnings("serial")
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class ActionTask extends BasicActionTask {

	public abstract Long getId();

	public abstract JobData.ACTION getAction();

	@Getter
	@Setter
	@Column(name = "name")
	private String name;

	@Getter
	@Setter
	@Column(name = "current_step_id")
	private String currentStepId;

	@Getter
	@Setter
	@Column(name = "current_step_progress")
	private Double currentStepProgress;

	@Getter
	@Setter
	@Column(name = "started_at")
	private java.sql.Timestamp startedAt;

	@Getter
	@Setter
	@Column(name = "ended_at")
	private java.sql.Timestamp endedAt;

}
