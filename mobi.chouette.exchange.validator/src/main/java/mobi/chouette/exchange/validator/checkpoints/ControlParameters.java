package mobi.chouette.exchange.validator.checkpoints;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import lombok.Data;
import lombok.ToString;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "globalCheckPoints", "transportModeCheckpoints" })
@ToString
public class ControlParameters {
	private Map<String, Collection<CheckpointParameters>> globalCheckPoints = new HashMap<>();

	private Map<String, Map<String, Collection<CheckpointParameters>>> transportModeCheckpoints = new HashMap<>();

	public void clean() {
		globalCheckPoints.values().forEach(col -> col.clear());
		globalCheckPoints.clear();
		transportModeCheckpoints.values().forEach(map -> map.values().forEach(col -> col.clear()));
		transportModeCheckpoints.values().forEach(map -> map.clear());
		transportModeCheckpoints.clear();
	}
}
