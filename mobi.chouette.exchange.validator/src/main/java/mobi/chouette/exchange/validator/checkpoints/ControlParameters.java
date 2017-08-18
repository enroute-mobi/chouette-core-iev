package mobi.chouette.exchange.validator.checkpoints;

import java.util.Collection;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "globalCheckPoints", "transportModeCheckpoints" })

public class ControlParameters {
	private Map<String, Collection<CheckpointParameters>> globalCheckPoints;

	private Map<String, Map<String, Collection<CheckpointParameters>>> transportModeCheckpoints;
}
