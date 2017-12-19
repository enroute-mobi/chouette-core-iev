package mobi.chouette.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j;
import mobi.chouette.model.ChouetteIdentifiedObject;
import mobi.chouette.model.ChouetteLocalizedObject;
import mobi.chouette.model.ChouetteObject;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopArea;
import mobi.chouette.model.StopPoint;

@Log4j
public abstract class ChouetteModelUtil {

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

	public static List<StopArea> getStopAreaOfRoute(Route route) {
		ArrayList<StopArea> areas = new ArrayList<>();
		ArrayList<StopPoint> points = new ArrayList<>(route.getStopPoints());
		for (Iterator<StopPoint> iterator = points.iterator(); iterator.hasNext();) {
			StopPoint stopPoint = iterator.next();
			if (stopPoint == null)
				iterator.remove();

		}
		points.sort((arg0,arg1) -> arg0.getPosition().intValue() - arg1.getPosition().intValue());
		
		for (StopPoint point : points) {
			areas.add(point.getContainedInStopArea());
		}
		return areas;
	}

	public static String changePrefix(String objectId, String prefix) {
		String[] tokens = objectId.split(":");
		StringBuilder changed = new StringBuilder(prefix);
		for (int i = 1; i < tokens.length; i++) {
			changed.append(":");
			changed.append(tokens[i]);
		}
		return changed.toString();
	}

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
			jp.getStopPoints().sort((arg0,arg1) -> arg0.getPosition().intValue() - arg1.getPosition().intValue());
			jp.setDepartureStopPoint(stopPoints.get(0));
			jp.setArrivalStopPoint(stopPoints.get(stopPoints.size() - 1));
		}

	}

	public static boolean sameValue(Object obj1, Object obj2) {
		if (obj1 == null)
			return obj2 == null;
		return obj1.equals(obj2);
	}

}
