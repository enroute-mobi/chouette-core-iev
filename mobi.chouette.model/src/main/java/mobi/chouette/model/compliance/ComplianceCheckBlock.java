package mobi.chouette.model.compliance;

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

import org.hibernate.annotations.CollectionType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.ChouetteDatedObject;
import mobi.chouette.model.converter.HstoreConverter;

@Entity
@Table(name = "compliance_check_blocks")
@NoArgsConstructor
@ToString(callSuper = true)
public class ComplianceCheckBlock extends ChouetteDatedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2134086879316324827L;
	/**
	 * 
	 * compliance_check_blocks -> classe ComplianceCheckBlock attributs : id : name : pour les logs ?
	 * condition_attributes : hstore , demander les clés à AF
	 *
	 * relations : ComplianceCheckTask propriétaire liste de ComplianceCheck (contrôles associés au mode/sous-mode)
	 *
	 */

	@Getter
	@Setter
	@SequenceGenerator(name = "compliance_check_blocks_id_seq", sequenceName = "compliance_check_blocks_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "compliance_check_blocks_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;
	
	

	@Getter
	@Setter
	@Column(name = "condition_attributes")
	@CollectionType(type = "java.util.HashMap")
	@Convert(converter = HstoreConverter.class)
	private Map<String, String> conditionAttributes = new HashMap<String, String>();
	// TODO: voir avec AF pour cohérence avec le type de condition_attributes dans les autres tables...(!)
	

	@Getter
	@Setter
	@Column(name = "compliance_check_set_id")
	private Long taskId;
	
	
	
	

}
