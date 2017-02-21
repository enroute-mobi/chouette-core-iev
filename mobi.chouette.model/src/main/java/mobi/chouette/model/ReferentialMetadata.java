package mobi.chouette.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import mobi.chouette.model.type.DateRange;

@Entity
@Table(name = "referential_metadata")
@NoArgsConstructor
@ToString(callSuper = true)
public class ReferentialMetadata {
	@Getter
	@Setter
	@SequenceGenerator(name="referential_metadata_id_seq", sequenceName="referential_metadata_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="referential_metadata_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	@Getter
	@Setter
	@Column(name = "line_ids",columnDefinition="int[]")
	@Type(type = "mobi.chouette.model.usertype.IntegerArrayUserType")
	private Integer[] lineIds;

	@Getter
	@Setter
	@Column(name = "periodes",columnDefinition="daterange[]")
	@Type(type = "mobi.chouette.model.usertype.DateRangeArrayUserType")
	private DateRange[] periods;
	

}
