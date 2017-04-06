package mobi.chouette.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.common.JobData;

@Entity
@Table(name = "import_resources")
@NoArgsConstructor
@ToString(callSuper = true)
public class ImportResource extends ActionResource{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1257345220758519489L;

	public JobData.ACTION getAction()
	{
		return JobData.ACTION.importer;
	}
	
	@Getter
	@Setter
	@SequenceGenerator(name = "import_resources_id_seq", sequenceName = "import_resources_id_seq", allocationSize = 100)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "import_resources_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	@Getter
	@Setter
	@Column(name = "status")
	private String status;

	@Getter
	@Setter
	@Column(name = "import_id")
	private Integer importId;

	@Getter
	@Setter
	@Column(name = "created_at")
	private java.sql.Timestamp createdAt;

	@Getter
	@Setter
	@Column(name = "updated_at")
	private java.sql.Timestamp updatedAt;

}
