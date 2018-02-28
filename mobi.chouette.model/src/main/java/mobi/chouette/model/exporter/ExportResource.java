package mobi.chouette.model.exporter;

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
//TODO : vérifier l'adéquation avec la table créée par RAILS

@Entity
@Table(name = "test_export_resources")
@NoArgsConstructor
@ToString(callSuper = true)
public class ExportResource extends ActionResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1257345220758519489L;

	public JobData.ACTION getAction() {
		return JobData.ACTION.exporter;
	}

	@Getter
	@Setter
	@GenericGenerator(name = "test_export_resources_id_seq", strategy = "mobi.chouette.persistence.hibernate.ChouettePublicIdentifierGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "public.test_export_resources_id_seq"),
			@Parameter(name = "increment_size", value = "100") })
	@GeneratedValue(generator = "test_export_resources_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	@Getter
	@Setter
	@Column(name = "import_id")
	private Long taskId;

	public ExportResource(Long taskId) {
		this.taskId = taskId;
		Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());

		this.setCreationTime(now);
		this.setUpdatedTime((Timestamp) now.clone());
	}

}
