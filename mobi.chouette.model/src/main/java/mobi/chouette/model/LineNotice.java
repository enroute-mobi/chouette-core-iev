package mobi.chouette.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.NaturalId;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "line_notices",schema="public")
@Cacheable
@NoArgsConstructor
@EqualsAndHashCode(of = { "objectId" }, callSuper = false)
@ToString(callSuper=true)
public class LineNotice extends ChouetteIdentifiedObject {

	private static final long serialVersionUID = -8086291270595894778L;

	@Getter
	@Setter
	@SequenceGenerator(name="line_notices_id_seq", sequenceName="public.line_notices_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="line_notices_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;


	@Getter
	@NaturalId(mutable=true)
	@Column(name = "objectid", nullable = false, unique = true)
	protected String objectId;

	public void setObjectId(String value) {
		objectId = StringUtils.abbreviate(value, 255);
	}

	@Getter
	@Setter
	@Column(name = "object_version")
	protected Long objectVersion = 1L;

	@Getter
	@Setter
	@Column(name = "title")
	private String title;

	@Getter
	@Setter
	@Column(name="content",columnDefinition = "text")
	private String content;

	@Getter
	@Setter
	@Column(name="import_xml",columnDefinition = "text")
	private String importXml;

	@Getter
	@Setter
	@Column(name = "line_referential_id")
	protected Long lineReferentialId;
}
