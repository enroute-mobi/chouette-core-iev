package mobi.chouette.model;

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
import mobi.chouette.model.converter.HstoreConverter;

@Entity
@Table(name = "import_messages")
@NoArgsConstructor
@ToString(callSuper = true)
public class ImportMessage {
	@Getter
	@Setter
	@SequenceGenerator(name="import_messages_id_seq", sequenceName="import_messages_id_seq", allocationSize=100)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="import_messages_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	@Getter
	@Setter
	@Column(name = "criticity")
	private Integer criticity;
	
	@Getter
	@Setter
	@Column(name = "message_key")
	private String messageKey;
	
	@Getter
	@Setter
	@Column(name="message_attributs")
	@CollectionType(type="java.util.HashMap")
   	@Convert(converter = HstoreConverter.class)
	private Map<String, String> messageAttributs = new HashMap<String, String>();
	
	@Getter
	@Setter
	@Column(name="import_id")
	private Integer importId;
	
	@Getter
	@Setter
	@Column(name="resource_id")
	private Integer resourceId;
	
	@Getter
	@Setter
	@Column(name="created_at")
	private java.sql.Timestamp createdAt;
	
}
