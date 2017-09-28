package mobi.chouette.model.compliance;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.common.JobData;
import mobi.chouette.model.ActionResource;

@Entity
@Table(name = "compliance_check_resources")
@NoArgsConstructor
@ToString(callSuper = true)
public class ComplianceCheckResource extends ActionResource {

	// @formatter:off
	/**
	 * TODO : 
	 * compliance_check_resources  -> classe ComplianceCheckResource extends ActionResource
     * attributs : 
     *     id : 
     *     name : 
     *     type : 
     *     reference : 
     *     status : 
     *     metrics : hstore : comptage des tests effectués ventilés par non fait, ok ,warning ou erreur 
	 *
     * relations : ComplianceCheckTask
	 *
	 */
	// @formatter:on

	private static final long serialVersionUID = 1075916620758048284L;

	@Getter
	@Setter
	@GenericGenerator(name = "compliance_check_resources_id_seq", strategy = "mobi.chouette.persistence.hibernate.ChouettePublicIdentifierGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "public.compliance_check_resources_id_seq"),
			@Parameter(name = "increment_size", value = "100") })
	@GeneratedValue(generator = "compliance_check_resources_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	@Getter
	@Setter
	@Column(name = "check_id")
	private Long taskId;

	@Getter
	@Setter
	private ComplianceCheckTask complianceCheckTask;

	public JobData.ACTION getAction() {
		return JobData.ACTION.validator;
	}

	public ComplianceCheckResource(Long taskId) {
		this.taskId = taskId;
		Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());
		this.setCreationTime(now);
		this.setUpdatedTime((Timestamp) now.clone());
	}

}
