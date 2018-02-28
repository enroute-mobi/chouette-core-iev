package mobi.chouette.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import lombok.extern.log4j.Log4j;
import mobi.chouette.model.ChouetteIdentifiedObject;
import mobi.chouette.model.ChouetteLocalizedObject;
import mobi.chouette.model.ChouetteObject;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Organisation;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopArea;
import mobi.chouette.model.StopPoint;

@Log4j
public abstract class ChouetteModelUtil {

	public static final String FUNCTIONAL_SCOPE = "functional_scope";

	/**
	 * Build a list of Neptune Ids (ObjectId) from a list of Neptune Objects
	 * 
	 * @param neptuneObjects
	 *            the list to parse
	 * @return the object ids list
	 */
	public static List<String> extractObjectIds(Collection<? extends ChouetteIdentifiedObject> neptuneObjects) {
		List<String> objectIds = new ArrayList<>();
		if (neptuneObjects != null) {
			for (ChouetteIdentifiedObject neptuneObject : neptuneObjects) {
				if (neptuneObject != null) {
					String objectId = neptuneObject.getObjectId();
					if (objectId != null) {
						objectIds.add(objectId);
					}
				}
			}
		}

		return objectIds;
	}

	/**
	 * Build a map of objectIds (Id) from a list of Neptune Identified Objects
	 * 
	 * @param neptuneObjects
	 *            the list to parse
	 * @return the ids map
	 */
	public static <T extends ChouetteIdentifiedObject> Map<String, T> mapOnObjectIds(Collection<T> neptuneObjects) {
		Map<String, T> map = new HashMap<>();
		if (neptuneObjects != null) {
			for (T neptuneObject : neptuneObjects) {
				if (neptuneObject != null) {
					String id = neptuneObject.getObjectId();
					if (id != null) {
						map.put(id, neptuneObject);
					}
				}
			}
		}
		return map;
	}

	/**
	 * Build a list of internal Ids (Id) from a list of Neptune Objects
	 * 
	 * @param neptuneObjects
	 *            the list to parse
	 * @return the ids list
	 */
	public static List<Long> extractIds(Collection<? extends ChouetteObject> neptuneObjects) {
		List<Long> ids = new ArrayList<>();
		if (neptuneObjects != null) {
			for (ChouetteObject neptuneObject : neptuneObjects) {
				if (neptuneObject != null) {
					Long id = neptuneObject.getId();
					if (id != null) {
						ids.add(id);
					}
				}
			}
		}

		return ids;
	}

	/**
	 * Build a map of internal Ids (Id) from a list of Neptune Objects
	 * 
	 * @param neptuneObjects
	 *            the list to parse
	 * @return the ids map
	 */
	public static <T extends ChouetteObject> Map<Long, T> mapOnIds(Collection<T> neptuneObjects) {
		Map<Long, T> map = new HashMap<>();
		if (neptuneObjects != null) {
			for (T neptuneObject : neptuneObjects) {
				if (neptuneObject != null) {
					Long id = neptuneObject.getId();
					if (id != null) {
						map.put(id, neptuneObject);
					}
				}
			}
		}
		return map;
	}

	/**
	 * project latitude and longitude on x and y if not already set<br>
	 * clears projection if no projection is given
	 * 
	 * @param projectionType
	 *            type of projection (EPSG:xxx)
	 */
	public static void toProjection(ChouetteLocalizedObject object, String projectionType) {
		if (!object.hasCoordinates())
			return;

		String projection = null;
		if (projectionType == null || projectionType.isEmpty()) {
			object.setX(null);
			object.setY(null);
			object.setProjectionType(null);
			return;
		}
		if (object.hasProjection())
			return;
		projection = projectionType.toUpperCase();

		Coordinate p = new Coordinate(object.getLongitude(), object.getLatitude());
		Coordinate coordinate = CoordinateUtil.transform(Coordinate.WGS84, projection, p);
		if (coordinate != null) {
			object.setX(coordinate.x);
			object.setY(coordinate.y);
			object.setProjectionType(projection);
		}
	}

	/**
	 * get stop areas from route using stop point list
	 * 
	 * @param route
	 * @return
	 */
	public static List<StopArea> getStopAreaOfRoute(Route route) {
		ArrayList<StopArea> areas = new ArrayList<>();
		ArrayList<StopPoint> points = new ArrayList<>(route.getStopPoints());
		for (Iterator<StopPoint> iterator = points.iterator(); iterator.hasNext();) {
			StopPoint stopPoint = iterator.next();
			if (stopPoint == null)
				iterator.remove();

		}
		points.sort((arg0, arg1) -> arg0.getPosition().intValue() - arg1.getPosition().intValue());

		for (StopPoint point : points) {
			areas.add(point.getContainedInStopArea());
		}
		return areas;
	}

	/**
	 * replace prefix value in an object id (Neptune)
	 * 
	 * @param objectId
	 * @param prefix
	 * @return
	 */
	public static String changePrefix(String objectId, String prefix) {
		String[] tokens = objectId.split(":");
		StringBuilder changed = new StringBuilder(prefix);
		for (int i = 1; i < tokens.length; i++) {
			changed.append(":");
			changed.append(tokens[i]);
		}
		return changed.toString();
	}

	/**
	 * replace suffix value in an object id (Neptune)
	 * 
	 * @param objectId
	 * @param suffix
	 * @return
	 */
	public static String changeSuffix(String objectId, String suffix) {
		String[] tokens = objectId.split(":");
		if (tokens.length < 2)
			return objectId;
		StringBuilder changed = new StringBuilder();
		for (int i = 0; i < 2; i++) {
			changed.append(tokens[i]);
			changed.append(":");
		}
		changed.append(suffix);
		for (int i = 3; i < tokens.length; i++) {
			changed.append(":");
			changed.append(tokens[i]);
		}
		return changed.toString();
	}

	/**
	 * update departure and arrival of JourneyPattern <br>
	 * to be used after stopPoints update
	 *
	 * @param jp
	 */
	public static void refreshDepartureArrivals(JourneyPattern jp) {
		List<StopPoint> stopPoints = jp.getStopPoints();
		if (stopPoints == null || stopPoints.isEmpty()) {
			jp.setDepartureStopPoint(null);
			jp.setArrivalStopPoint(null);
		} else {
			for (StopPoint stopPoint : stopPoints) {
				if (stopPoint.getPosition() == null) {
					log.warn("stopPoint without position " + stopPoint.getObjectId());
					return;
				}
			}
			jp.getStopPoints().sort((arg0, arg1) -> arg0.getPosition().intValue() - arg1.getPosition().intValue());
			jp.setDepartureStopPoint(stopPoints.get(0));
			jp.setArrivalStopPoint(stopPoints.get(stopPoints.size() - 1));
		}

	}

	/**
	 * compare 2 objects even if null
	 * 
	 * @param obj1
	 * @param obj2
	 * @return true if objects are equals or both null
	 */
	public static boolean sameValue(Object obj1, Object obj2) {
		if (obj1 == null)
			return obj2 == null;
		return obj1.equals(obj2);
	}


	/**
	 * extract lines object id from organization
	 * 
	 * @param organisation
	 * @return object ids of affected lines 
	 */
	public static List<String> getAuthorizedLines(Organisation organisation) {
		List<String> ret = new ArrayList<>();
		String codes = organisation.getSsoAttributes().get(FUNCTIONAL_SCOPE);
		if (codes != null) {
			try {
				JSONArray json = new JSONArray(codes);
				for (int i = 0; i < json.length(); i++) {
					String code = json.getString(i);
					if (code.contains(":Line:"))
						ret.add(code);
				}
			} catch (JSONException e) {
				log.error("JSON Error : " + e.getMessage() + " unable to decode data " + codes);
			}
		}
		return ret;

	}

	public static final String classNameforRubyName(String rubyName)
	{
		switch (rubyName)
		{
		case "routing_constraint_zone" : return "RoutingConstraint";
		case "time_table" : return "Timetable";
		default : return toCamelCase(rubyName);
		}
	}
	/**
	 * @param underscore
	 * @return
	 */
	public static String toCamelCase(String underscore) {
		StringBuilder b = new StringBuilder();
		boolean underChar = false;
		boolean first = true;
		for (char c : underscore.toCharArray()) {
			if (first) {
				b.append(Character.toUpperCase(c));
				first = false;
			} else if (c == '_') {
				underChar = true;
			} else {
				if (underChar) {
					b.append(Character.toUpperCase(c));
					underChar = false;
				} else
					b.append(c);
			}
		}
		return b.toString();
	}

}
