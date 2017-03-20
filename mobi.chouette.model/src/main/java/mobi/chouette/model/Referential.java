package mobi.chouette.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "referentials")
@NoArgsConstructor
@ToString(callSuper = true)
public class Referential {
	@Getter
	@Setter
	@SequenceGenerator(name="referentials_id_seq", sequenceName="referentials_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="referentials_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	@Getter
	@Setter
	@Column(name = "name")
	private String name;
	
	@Getter
	@Setter
	@Column(name = "slug")
	private String schemaName;
	
	@Getter
	@Setter
	@Column(name="line_referential_id")
	private Long lineReferentialId;
	
	@Getter
	@Setter
	@Column(name="stop_area_referential_id")
	private Long stopAreaReferentialId;
	
	@Getter
	@Setter
	@OneToMany (fetch = FetchType.EAGER,cascade={CascadeType.ALL})
	@JoinColumn(name="referential_id")
	private List<ReferentialMetadata> metadatas = new ArrayList<>();
	
	@Getter
	@Setter
	@Column(name="ready")
	private boolean ready;
	
	@Getter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisation_id")
	private Organisation organisation;
	

}
