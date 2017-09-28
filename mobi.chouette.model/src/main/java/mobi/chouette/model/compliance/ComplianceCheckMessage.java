package mobi.chouette.model.compliance;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.common.JobData;
import mobi.chouette.model.ActionMessage;
import mobi.chouette.model.converter.HstoreConverter;

@Entity
@Table(name = "compliance_check_results")
@NoArgsConstructor
@ToString(callSuper = true)
public class ComplianceCheckMessage extends ActionMessage {

	// @formatter:off
	/**
	 * TODO : compliance_check_results -> classe ComplianceCheckMessage extends ActionMessage 
	 * attributs : 
	 * 		id : 
	 * 		name : pour les logs ? 
	 * 		condition_attributes : hstore , demander les clés à AF
	 * 
	 * relations : ComplianceCheckResource
	 */
	// @formatter:on

	/**
	 * 
	 */
	private static final long serialVersionUID = -9013179381489826803L;

	public JobData.ACTION getAction() {
		return JobData.ACTION.validator;
	}

	@Getter
	@Setter
	@GenericGenerator(name = "compliance_check_results_id_seq", strategy = "mobi.chouette.persistence.hibernate.ChouettePublicIdentifierGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "public.compliance_check_results_id_seq"),
			@Parameter(name = "increment_size", value = "100") })
	@GeneratedValue(generator = "compliance_check_results_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	@Getter
	@Setter
	@Column(name = "compliance_check_id")
	private Long complianceCheckId;
	
	
	@Getter
	@Setter
	private Long taskId;
	//TODO : taskId => compliance_check_sets ???

	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compliance_check_resource_id")
	private ComplianceCheckResource resource;

	@Getter
	@Setter
	@Column(name = "condition_attributes")
	@CollectionType(type = "java.util.HashMap")
	@Convert(converter = HstoreConverter.class)
	private Map<String, String> conditionAttributs = new HashMap<String, String>();
}
