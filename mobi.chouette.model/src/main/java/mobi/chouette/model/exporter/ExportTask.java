package mobi.chouette.model.exporter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.common.JobData;
import mobi.chouette.model.ActionTask;

@Entity
@Table(name = "test_export")
@NoArgsConstructor
@ToString(callSuper = true)
public class ExportTask extends ActionTask {
	private static final long serialVersionUID = 9177879600144123687L;
	
	@Getter
	@Setter
	@SequenceGenerator(name="test_exports_id_seq", sequenceName="test_exports_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="test_exports_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;
	
	// used by service module 
	@Getter
	@Setter
	@Transient
	private String format = "netex_stif";
	
	@Getter
	@Setter
	@Column(name="file")
	private String file;


	public JobData.ACTION getAction()
	{
		return JobData.ACTION.exporter;
	}
	

}
