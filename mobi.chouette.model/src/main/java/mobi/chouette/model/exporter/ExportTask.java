package mobi.chouette.model.exporter;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.common.JobData;
import mobi.chouette.model.ActionTask;
import mobi.chouette.model.converter.HstoreConverter;

@Entity
@Table(name = "exports")
@NoArgsConstructor
@ToString(callSuper = true)
public class ExportTask extends ActionTask {
	private static final long serialVersionUID = 9177879600144123687L;
	
	@Getter
	@Setter
	@SequenceGenerator(name="exports_id_seq", sequenceName="exports_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="exports_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;
	
	// used by service module 
	// TODO : should be persistent to call any export format (gtfs, neptune, netex_france,...)
	@Getter
	@Setter
	@Transient
	private String format = "netex_stif";
	
	
	@Getter
	@Setter
	@Column(name="type")
	private String type;
	
	@Getter
	@Setter
	@Column(name="file")
	private String file;

	@Getter
	@Setter
	@Column(name="token_upload")
	private String url;
	
	@Getter
	@Setter
	@Column(name = "options")
	@CollectionType(type = "java.util.HashMap")
	@Convert(converter = HstoreConverter.class)
	private Map<String, String> options = new HashMap<String, String>();

	public JobData.ACTION getAction()
	{
		return JobData.ACTION.exporter;
	}
	

}
