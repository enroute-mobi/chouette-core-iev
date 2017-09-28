package mobi.chouette.model.compliance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.ChouetteDatedObject;

@Entity
@Table(name = "compliance_checks")
@NoArgsConstructor
@ToString(callSuper = true)
public class ComplianceCheck extends ChouetteDatedObject {

	private static final long serialVersionUID = -6471679594238033395L;

	// @formatter:off
	/**
	 * compliance_checks -> classe ComplianceCheck 
     *  attributs : 
     *     id : 
     *     name : pour les logs ? 
     *     type : ??? (type d'objet concerné? ) 
     *     code : vérifier avec AF si c'est le code des cartes Redmine ou le code redéfini ; dans le second cas, demander le champs du code Redmine
     *     attributes : hstore , demander les clés à AF 
     *     criticity : enum
     *     comment : inutile
     *
     * relations : ComplianceCheckTask propriétaire
     *            optionnel ComplianceCheckBlock propriétaire
	 */
	// @formatter:on

	public enum CRITICITY {
		WARNING, ERROR
	};

	@Getter
	@Setter
	@SequenceGenerator(name = "compliance_checks_id_seq", sequenceName = "compliance_checks_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "compliance_checks_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	@Getter
	@Setter
	@Column(name = "compliance_check_set_id")
	private Long taskId;

	@Getter
	@Setter
	@Column(name = "compliance_check_block_id")
	private Long blockId;

	@Getter
	@Setter
	@Column(name = "type", nullable = false)
	protected String type; 

	//control attribute json ?!!
	//voir avec AF
	
	@Getter
	@Setter
	@Column(name = "name")
	protected String name;
	
	@Getter
	@Setter
	@Column(name = "code", nullable = false)
	protected String code; // TODO : demander à AF : code des cartes Redmine ou le code redéfini ; dans le second cas,
							// demander le champs du code Redmine

	@Getter
	@Setter
	@Column(name = "criticity")
	@Enumerated(EnumType.ORDINAL)
	private CRITICITY criticity;

	@Getter
	@Setter
	@Column(name = "comment", nullable = false)
	protected String comment;
	
	
	

}
