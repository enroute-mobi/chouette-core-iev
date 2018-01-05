package mobi.chouette.exchange.validation.report;

import java.io.PrintStream;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import mobi.chouette.exchange.report.AbstractReport;
import mobi.chouette.model.AccessLink;
import mobi.chouette.model.AccessPoint;
import mobi.chouette.model.Company;
import mobi.chouette.model.CompanyLite;
import mobi.chouette.model.ConnectionLink;
import mobi.chouette.model.GroupOfLine;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Line;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Network;
import mobi.chouette.model.Route;
import mobi.chouette.model.RoutingConstraint;
import mobi.chouette.model.StopArea;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class ObjectReference extends AbstractReport {

	public enum TYPE {
		NETWORK("Network","networks"), 
		COMPANY("Company","companies"), 
		GROUP_OF_LINE("GroupOfLine","groups_of_lines"), 
		STOP_AREA("StopArea","stop_areas"), 
		STOP_POINT("StopPoint","stop_points"), 
		CONNECTION_LINK("ConnectionLink","connection_links"), 
		ACCESS_POINT("AccessPoint","access_points"),
		ACCESS_LINK("AccessLink","access_links"), 
		TIME_TABLE("Timetable","time_tables"), 
		LINE("Line","lines"), 
		ROUTE("Route","routes"), 
		JOURNEY_PATTERN("JourneyPattern",""), 
		VEHICLE_JOURNEY("VehicleJourney",""),
		ROUTING_CONSTRAINT("RoutingConstraint","routing_constraint_zones");

		private java.lang.String value;
		@Getter
		private java.lang.String guiValue;

		private TYPE(final java.lang.String value,String guiValue) {
			this.value = value;
			this.guiValue = guiValue;
		}

		public static TYPE fromValue(final java.lang.String value) {
			String v=value;
			if (value.endsWith("Lite"))
				v=value.replace("Lite", "");
			for (TYPE c : TYPE.values()) {
				if (c.value.equals(v)) {
					return c;
				}
			}
			throw new IllegalArgumentException(value);
		}
	}

	private TYPE type;

	private Long id;

	private String objectId;

	public ObjectReference(Network object) {
		this.type = TYPE.NETWORK;
		this.id = object.getId();
		if (id == null)
			this.objectId = object.getObjectId();

	}

	public ObjectReference(Company object) {
		this.type = TYPE.COMPANY;
		this.id = object.getId();
		if (id == null)
			this.objectId = object.getObjectId();
	}
	public ObjectReference(CompanyLite object) {
		this.type = TYPE.COMPANY;
		this.id = object.getId();
		if (id == null)
			this.objectId = object.getObjectId();
	}

	public ObjectReference(GroupOfLine object) {
		this.type = TYPE.GROUP_OF_LINE;
		this.id = object.getId();
		if (id == null)
			this.objectId = object.getObjectId();
	}

	public ObjectReference(StopArea object) {
		this.type = TYPE.STOP_AREA;
		this.id = object.getId();
		if (id == null)
			this.objectId = object.getObjectId();
	}
	public ObjectReference(StopAreaLite object) {
		this.type = TYPE.STOP_AREA;
		this.id = object.getId();
		if (id == null)
			this.objectId = object.getObjectId();
	}

	public ObjectReference(ConnectionLink object) {
		this.type = TYPE.CONNECTION_LINK;
		this.id = object.getId();
		if (id == null)
			this.objectId = object.getObjectId();
	}

	public ObjectReference(AccessPoint object) {
		this.type = TYPE.ACCESS_POINT;
		this.id = object.getId();
		if (id == null)
			this.objectId = object.getObjectId();
	}

	public ObjectReference(AccessLink object) {
		this.type = TYPE.ACCESS_LINK;
		this.id = object.getId();
		if (id == null)
			this.objectId = object.getObjectId();
	}

	public ObjectReference(Timetable object) {
		this.type = TYPE.TIME_TABLE;
		this.id = object.getId();
		if (id == null)
			this.objectId = object.getObjectId();
	}

	public ObjectReference(Line object) {
		this.type = TYPE.LINE;
		this.id = object.getId();
		if (id == null)
			this.objectId = object.getObjectId();
	}
	public ObjectReference(LineLite object) {
		this.type = TYPE.LINE;
		this.id = object.getId();
		if (id == null)
			this.objectId = object.getObjectId();
	}

	public ObjectReference(Route object) {
		this.type = TYPE.ROUTE;
		this.id = object.getId();
		if (id == null)
			this.objectId = object.getObjectId();
	}

	public ObjectReference(JourneyPattern object) {
		this.type = TYPE.JOURNEY_PATTERN;
		this.id = object.getId();
		if (id == null)
			this.objectId = object.getObjectId();
	}

	public ObjectReference(VehicleJourney object) {
		this.type = TYPE.VEHICLE_JOURNEY;
		this.id = object.getId();
		if (id == null)
			this.objectId = object.getObjectId();
	}
	
	public ObjectReference(RoutingConstraint object) {
		this.type = TYPE.ROUTING_CONSTRAINT;
		this.id = object.getId();
		if (id == null)
			this.objectId = object.getObjectId();
	}

	public ObjectReference(StopPoint object)
	{
		this.type = TYPE.STOP_POINT;
		this.id = object.getId();
	}

	public ObjectReference(String className, String id) {
		this.type = TYPE.fromValue(className);
		this.objectId = id;
	}

	@Override
	public void print(PrintStream out, StringBuilder ret, int level, boolean first) {
		ret.setLength(0);
		out.print(addLevel(ret, level).append('{'));
		out.print(toJsonString(ret, level + 1, "type", type.name().toLowerCase(), true));
		if (id != null)
			out.print(toJsonString(ret, level + 1, "id", id, false));
		else
			out.print(toJsonString(ret, level + 1, "objectId", objectId, false));

		ret.setLength(0);
		out.print(addLevel(ret.append('\n'), level).append('}'));

	}

	public static boolean isEligible(String className, String objectId) {
		if (objectId == null || objectId.isEmpty())
			return false;
		try {
			TYPE.fromValue(className);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

}
