package mobi.chouette.model.exporter;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.common.JobData;
import mobi.chouette.model.ActionMessage;

@Entity
@Table(name = "test_export_messages")
@NoArgsConstructor
@ToString(callSuper = true)
public class ExportMessage extends ActionMessage {
	private static final long serialVersionUID = -2708006192840323555L;

	public JobData.ACTION getAction() {
		return JobData.ACTION.importer;
	}

	@Getter
	@Setter
	@GenericGenerator(name = "test_export_messages_id_seq", strategy = "mobi.chouette.persistence.hibernate.ChouettePublicIdentifierGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "public.test_export_messages_id_seq"),
			@Parameter(name = "increment_size", value = "100") })
	@GeneratedValue(generator = "test_export_messages_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	@Getter
	@Setter
	@Column(name = "export_id")
	private Long taskId;
	
	@Getter
	@Setter
	@Column(name = "resource_id")
	private Long resourceId;
	
	@Getter
	@Setter
	@Transient
	private Long checkPointId;

	@Getter
	@Setter
	@Column(name = "criticity")
	@Enumerated(EnumType.STRING)
	private CRITICITY criticity;


	public ExportMessage(Long taskId, Long resouceId) {
		this.taskId = taskId;
		setResourceId(resouceId);
		Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());
		this.setCreationTime(now);

	}

}
