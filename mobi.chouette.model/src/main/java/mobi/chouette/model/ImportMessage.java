package mobi.chouette.model;

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

@Entity
@Table(name = "import_messages")
@NoArgsConstructor
@ToString(callSuper = true)
public class ImportMessage extends ActionMessage {
	private static final long serialVersionUID = -2708006192840323555L;

	public JobData.ACTION getAction() {
		return JobData.ACTION.importer;
	}

	@Getter
	@Setter
	@GenericGenerator(name = "import_messages_id_seq", strategy = "mobi.chouette.persistence.hibernate.ChouettePublicIdentifierGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "public.import_messages_id_seq"),
			@Parameter(name = "increment_size", value = "100") })
	@GeneratedValue(generator = "import_messages_id_seq")
	// @SequenceGenerator(name="import_messages_id_seq", sequenceName="import_messages_id_seq", allocationSize=1)
	// @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="import_messages_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	@Getter
	@Setter
	@Column(name = "import_id")
	private Long taskId;

	public ImportMessage(Long taskId, Long resouceId) {
		this.taskId = taskId;
		setResourceId(resouceId);
		Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());

		// this.setCreatedAt(now);
		this.setCreationTime(now);

		// this.setUpdatedAt((Timestamp) now.clone());
	}

}
