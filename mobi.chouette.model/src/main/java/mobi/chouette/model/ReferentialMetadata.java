package mobi.chouette.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.type.DateRange;

@Entity
@Immutable
@Table(name = "referential_metadata")
@NoArgsConstructor
@ToString(callSuper = true)
public class ReferentialMetadata extends ChouetteObject{
	private static final long serialVersionUID = 2598860486989210732L;

	@Getter
	@Setter
	@SequenceGenerator(name="referential_metadata_id_seq", sequenceName="referential_metadata_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="referential_metadata_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	@Getter
	@Setter
	@Column(name = "line_ids",columnDefinition="bigint[]")
	@Type(type = "mobi.chouette.model.usertype.LongArrayUserType")
	private Long[] lineIds;

	@Getter
	@Setter
	@Column(name = "periodes",columnDefinition="daterange[]")
	@Type(type = "mobi.chouette.model.usertype.DateRangeArrayUserType")
	private DateRange[] periods;
	
	

}
