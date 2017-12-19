package mobi.chouette.model.compliance;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.common.JobData.ACTION;
import mobi.chouette.model.ActionTask;

@Entity
@Table(name = "compliance_check_sets")
@NoArgsConstructor
@ToString(callSuper = true)
public class ComplianceCheckTask extends ActionTask {

	// @formatter:off
		/**
		 * table des demande de contrôle : 
		 *	compliance_check_sets    -> classe ComplianceCheckTask extends ActionTask 
		 *	                           Contrôler les attributs qui ne serait pas présents dans ActionTask et reboucler abec AF sur pourquoi 
		 *	                           Ajouter les attributs spécifiques utiles au validator 
	     *		
         *	      attributs : 
         *	          id : 
		 *	          referential_id : utile
		 *	          compliance_control_set_id : inutile 
		 *	          status : utile
		 *	          workbench_id : ???
		 *	          creator : inutile
		 *	          ensemble de dates : création, update, started, ended 
		 *	
		 *	     relations : liste de ComplianceCheckBlock (contrôles par mode de tarnsport) 
		 *	                 liste de ComplianceCheck (contrôles hors bloc) 
		 *	                    ---> attention, il faut avoir ici (annotation JPA ou Hibernate) uniquement les ComplianceCheck sans ComplianceCheckBlock
		 *
		 */
	// @formatter:on

	private static final long serialVersionUID = 4080465212210839878L;

	@Getter
	@Setter
	@SequenceGenerator(name = "compliance_check_sets_id_seq", sequenceName = "compliance_check_sets_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "compliance_check_sets_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	// used by service module 
	@Getter
	@Setter
	@Transient
	private String format = null;	

	@Getter
	@Setter
	@OneToMany(mappedBy = "task", cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
	private List<ComplianceCheckBlock> complianceCheckBlocks = new ArrayList<>(0);
	//
	@Getter
	@Setter
	@OneToMany(mappedBy = "task", cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
	private List<ComplianceCheck> complianceChecks = new ArrayList<>(0);

	@Override
	public ACTION getAction() {
		return ACTION.validator;
	}


}
