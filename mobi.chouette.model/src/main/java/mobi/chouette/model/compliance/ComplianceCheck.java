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

@Entity
@Table(name = "compliance_check")
@NoArgsConstructor
@ToString(callSuper = true)
public class ComplianceCheck {
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
	@SequenceGenerator(name = "compliances_id_seq", sequenceName = "compliances_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "compliances_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	@Getter
	@Setter
	@Column(name = "name")
	protected String name;

	@Getter
	@Setter
	@Id
	@Column(name = "type", nullable = false)
	protected Object type; // TODO : demander à AF !

	@Getter
	@Setter
	@Id
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
	@Id
	@Column(name = "comment", nullable = false)
	protected String comment;

}
