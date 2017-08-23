package mobi.chouette.exchange.validation.report;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import mobi.chouette.model.AccessLink;
import mobi.chouette.model.AccessPoint;
import mobi.chouette.model.ChouetteIdentifiedObject;
import mobi.chouette.model.Company;
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
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.util.NamingUtil;

@Data
@ToString /* (exclude={"object"}) */
public class DataLocation {
	private String objectType; // Line route stop area..
	private String filename;
	private int lineNumber = -1;
	private int columnNumber = -1;
	private String objectId = "";
	private String name = "";
	private ChouetteIdentifiedObject object;
	private LineLite line;
	private String attribute;
	private List<Path> path = new ArrayList<>();

	public DataLocation(String fileName) {
		this.filename = fileName;
	}

	public DataLocation(String fileName, String locationName) {
		this.filename = fileName;
		this.name = locationName;
	}

	public DataLocation(String fileName, String locationName, int lineNumber, String objectId) {
		this.filename = fileName;
		this.lineNumber = lineNumber;
		this.objectId = objectId;
		this.name = locationName;
	}

	public DataLocation(String fileName, String locationName, int lineNumber, int columnNumber, String objectId) {
		this.filename = fileName;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
		this.objectId = objectId;
		this.name = locationName;
	}

	public DataLocation(String fileName, String locationName, int lineNumber) {
		this.filename = fileName;
		this.lineNumber = lineNumber;
		this.name = locationName;
	}

	public DataLocation(String fileName, int lineNumber, int columnNumber) {
		this.filename = fileName;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
	}

	public DataLocation(String fileName, int lineNumber, int columnNumber, String objectId) {
		this.filename = fileName;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
		this.objectId = objectId;
	}

	public DataLocation(String fileName, int lineNumber, int columnNumber, ChouetteIdentifiedObject chouetteObject) {
		this(fileName, lineNumber, columnNumber, null, chouetteObject);
	}

	public DataLocation(String fileName, int lineNumber, int columnNumber, LineLite line,
			ChouetteIdentifiedObject chouetteObject) {
		this(chouetteObject);
		this.line = line;
		this.filename = fileName;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
	}

	public DataLocation(ChouetteIdentifiedObject chouetteObject, LineLite line) {
		this(chouetteObject);
		this.line = line;
	}

	public DataLocation(ChouetteIdentifiedObject chouetteObject, String attribute) {
		this(chouetteObject);
		this.attribute = attribute;
	}

	public DataLocation(ChouetteIdentifiedObject chouetteObject) {
		this.objectId = chouetteObject.getObjectId();
		this.object = chouetteObject;
		this.name = buildName(chouetteObject);
		if (chouetteObject.getObjectId() != null) {
			path.add(new Path(chouetteObject));
			if (chouetteObject instanceof VehicleJourney) {
				VehicleJourney object = (VehicleJourney) chouetteObject;
				if (object.getJourneyPattern() != null) {
					path.add(new Path(object.getJourneyPattern()));
					if (object.getJourneyPattern().getRoute() != null) {
						path.add(new Path(object.getJourneyPattern().getRoute()));
						if (object.getJourneyPattern().getRoute().getLine() != null) {
							path.add(new Path(object.getJourneyPattern().getRoute().getLine()));
						} else if (object.getJourneyPattern().getRoute().getLineLite() != null) {
							path.add(new Path(object.getJourneyPattern().getRoute().getLineLite()));
							this.line = object.getJourneyPattern().getRoute().getLineLite();
						}
					}
				}
			} else if (chouetteObject instanceof JourneyPattern) {
				JourneyPattern object = (JourneyPattern) chouetteObject;
				if (object.getRoute() != null) {
					path.add(new Path(object.getRoute()));
					if (object.getRoute().getLine() != null) {
						path.add(new Path(object.getRoute().getLine()));
					} else if (object.getRoute().getLineLite() != null) {
						path.add(new Path(object.getRoute().getLineLite()));
						this.line = object.getRoute().getLineLite();
					}
				}
			} else if (chouetteObject instanceof RoutingConstraint) {
				RoutingConstraint object = (RoutingConstraint) chouetteObject;
				if (object.getRoute() != null) {
					path.add(new Path(object.getRoute()));
					if (object.getRoute().getLine() != null) {
						path.add(new Path(object.getRoute().getLine()));
					} else if (object.getRoute().getLineLite() != null) {
						path.add(new Path(object.getRoute().getLineLite()));
						this.line = object.getRoute().getLineLite();
					}
				}
			} else if (chouetteObject instanceof Route) {
				Route object = (Route) chouetteObject;
				if (object.getLine() != null) {
					path.add(new Path(object.getLine()));
				} else if (object.getLineLite() != null) {
					path.add(new Path(object.getLineLite()));
					this.line = object.getLineLite();
				}
			} else if (chouetteObject instanceof AccessLink) {
				AccessLink object = (AccessLink) chouetteObject;
				if (object.getAccessPoint() != null) {
					path.add(new Path(object.getAccessPoint()));
					if (object.getAccessPoint().getContainedIn() != null) {
						path.add(new Path(object.getAccessPoint().getContainedIn()));
					}
				}
			} else if (chouetteObject instanceof AccessPoint) {
				AccessPoint object = (AccessPoint) chouetteObject;
				if (object.getContainedIn() != null) {
					path.add(new Path(object.getContainedIn()));
				}
			}
		}
		// addLineLocation(this, chouetteObject);

	}

	public static String buildName(ChouetteIdentifiedObject chouetteObject) {
		if (chouetteObject instanceof VehicleJourney) {
			VehicleJourney object = (VehicleJourney) chouetteObject;
			return NamingUtil.getName(object);
		} else if (chouetteObject instanceof JourneyPattern) {
			JourneyPattern object = (JourneyPattern) chouetteObject;
			return NamingUtil.getName(object);
		} else if (chouetteObject instanceof Route) {
			Route object = (Route) chouetteObject;
			return NamingUtil.getName(object);
		} else if (chouetteObject instanceof Line) {
			Line object = (Line) chouetteObject;
			return NamingUtil.getName(object);
		} else if (chouetteObject instanceof LineLite) {
			LineLite object = (LineLite) chouetteObject;
			return NamingUtil.getName(object);
		} else if (chouetteObject instanceof AccessLink) {
			AccessLink object = (AccessLink) chouetteObject;
			return NamingUtil.getName(object);
		} else if (chouetteObject instanceof AccessPoint) {
			AccessPoint object = (AccessPoint) chouetteObject;
			return NamingUtil.getName(object);
		} else if (chouetteObject instanceof StopArea) {
			StopArea object = (StopArea) chouetteObject;
			return NamingUtil.getName(object);
		} else if (chouetteObject instanceof StopAreaLite) {
			StopAreaLite object = (StopAreaLite) chouetteObject;
			return NamingUtil.getName(object);
		} else if (chouetteObject instanceof ConnectionLink) {
			ConnectionLink object = (ConnectionLink) chouetteObject;
			return NamingUtil.getName(object);
		} else if (chouetteObject instanceof Network) {
			Network object = (Network) chouetteObject;
			return NamingUtil.getName(object);
		} else if (chouetteObject instanceof Company) {
			Company object = (Company) chouetteObject;
			return NamingUtil.getName(object);
		} else if (chouetteObject instanceof GroupOfLine) {
			GroupOfLine object = (GroupOfLine) chouetteObject;
			return NamingUtil.getName(object);
		} else if (chouetteObject instanceof Timetable) {
			Timetable object = (Timetable) chouetteObject;
			return NamingUtil.getName(object);
		}
		return "unnammed";
	}

	// public static void addLineLocation(DataLocation loc,
	// NeptuneIdentifiedObject chouetteObject) {
	// if (loc.getLine() != null)
	// return;
	// Line line = null;
	// try {
	// if (chouetteObject instanceof VehicleJourney) {
	// VehicleJourney object = (VehicleJourney) chouetteObject;
	// line = object.getRoute().getLine();
	// } else if (chouetteObject instanceof JourneyPattern) {
	// JourneyPattern object = (JourneyPattern) chouetteObject;
	// line = object.getRoute().getLine();
	// } else if (chouetteObject instanceof StopPoint) {
	// StopPoint object = (StopPoint) chouetteObject;
	// line = object.getRoute().getLine();
	// } else if (chouetteObject instanceof Route) {
	// Route object = (Route) chouetteObject;
	// line = object.getLine();
	// } else if (chouetteObject instanceof Line) {
	// line = (Line) chouetteObject;
	// }
	// } catch (NullPointerException ex) {
	// // ignore line path
	// }
	// if (line != null)
	// loc.setLine(line);
	//
	// }

	public class Path {
		@Getter
		String objectClass;
		@Getter
		String objectId;

		public Path(String className, String objectId) {
			this.objectClass = className;
			if (this.objectClass.endsWith("Lite")) {
				this.objectClass = this.objectClass.substring(0, this.objectClass.lastIndexOf("Lite"));
			}
			this.objectId = objectId;
		}

		public Path(ChouetteIdentifiedObject object) {
			// protection from proxy class names
			this(object.getClass().getSimpleName().split("_")[0], object.getObjectId());
		}

		public String toString() {
			return "class = " + objectClass + ", id = " + objectId;
		}

	}

}
