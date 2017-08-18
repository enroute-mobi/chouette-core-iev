package mobi.chouette.exchange.validator.checkpoints;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.ChouetteIdentifiedObject;
import mobi.chouette.model.ChouetteLocalizedObject;

/**
 * @author michel
 *
 * @param <T>
 */
public abstract class GenericValidator<T extends ChouetteIdentifiedObject> {

	private static final String[] genericCodes = { "3-Generic-1", "3-Generic-2", "3-Generic-3" };

	public abstract void validate(Context context, T object, ValidateParameters parameters, String transportMode);

	/**
	 * @param context
	 * @param object
	 * @param parameters
	 * @param transportMode
	 * @param codes
	 */
	public void validate(Context context, T object, ValidateParameters parameters, String transportMode,
			String[] codes) {

		ControlParameters controlParameters = parameters.getControlParameters();
		String className = object.getClass().getSimpleName();
		// first check generic codes for class
		for (String code : genericCodes) {
			// find checkpoint for code
			Collection<CheckpointParameters> checkParams = null;
			// in transportModes
			Map<String, Collection<CheckpointParameters>> modeParameters = controlParameters
					.getTransportModeCheckpoints().get(transportMode);
			if (modeParameters != null) {
				checkParams = modeParameters.get(code);
			}
			// else in global
			if (isEmpty(checkParams)) {
				checkParams = controlParameters.getGlobalCheckPoints().get(code);
			}
			// if tests are found
			if (!isEmpty(checkParams)) {
				for (CheckpointParameters checkParam : checkParams) {
					if (checkParam instanceof GenericCheckpointParameters) {
						GenericCheckpointParameters param = (GenericCheckpointParameters) checkParam;
						if (param.getClassName().equals(className)) {
							Method method = findCheckMethod(this.getClass(), param.getCode());
							if (method != null) {
								try {
									method.invoke(this, context, object, param);
								} catch (IllegalAccessException | IllegalArgumentException
										| InvocationTargetException e) {
									// TODO : log problem
								}
							} else {
								// TODO : log unknown test
							}
						}
					}
				}
			}
		}

		// then check specific codes
		for (String code : codes) {
			// find checkpoint for code
			Collection<CheckpointParameters> checkParams = null;
			// in transportModes
			Map<String, Collection<CheckpointParameters>> modeParameters = controlParameters
					.getTransportModeCheckpoints().get(transportMode);
			if (modeParameters != null) {
				checkParams = modeParameters.get(code);
			}
			// else in global
			if (isEmpty(checkParams)) {
				checkParams = controlParameters.getGlobalCheckPoints().get(code);
			}
			// if tests are found
			if (!isEmpty(checkParams)) {
				for (CheckpointParameters checkParam : checkParams) {

					Method method = findCheckMethod(this.getClass(), checkParam.getCode());
					if (method != null) {
						try {
							method.invoke(this, context, object, checkParam);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							// TODO : log problem
						}
					} else {
						// TODO : log unknown test
					}
				}
			}
		}
	}

	/**
	 * @param underscore
	 * @return
	 */
	protected static String toCamelCase(String underscore) {
		StringBuffer b = new StringBuffer();
		boolean underChar = false;
		for (char c : underscore.toCharArray()) {
			if (c == '_') {
				underChar = true;
				continue;
			}
			if (underChar) {
				b.append(Character.toUpperCase(c));
				underChar = false;
			} else
				b.append(c);
		}
		return b.toString();
	}

	/**
	 * @param camelcase
	 * @return
	 */
	protected static String toUnderscore(String camelcase) {

		return camelcase.replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
	}

	/**
	 * @param beans
	 * @return
	 */
	protected static boolean isEmpty(Collection<?> beans) {
		return beans == null || beans.isEmpty();
	}

	/**
	 * @param string
	 * @return
	 */
	protected static boolean isEmpty(String string) {
		return string == null || string.isEmpty();
	}

	protected static final double R = 6371008.8; // Earth radius
	protected static final double toRad = 0.017453292519943; // degree/rad ratio

	/**
	 * calculate distance on spheroid
	 * 
	 * return 0 if one object has no coordinate
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	protected static double distance(ChouetteLocalizedObject obj1, ChouetteLocalizedObject obj2) {
		if (obj1.hasCoordinates() && obj2.hasCoordinates()) {
			return computeHaversineFormula(obj1.getLongitude().doubleValue(), obj1.getLatitude().doubleValue(),
					obj2.getLongitude().doubleValue(), obj2.getLatitude().doubleValue());
		} else
			return 0;
	}

	/**
	 * @see http://mathforum.org/library/drmath/view/51879.html
	 */
	private static double computeHaversineFormula(double dlon1, double dlat1, double dlon2, double dlat2) {

		double lon1 = dlon1 * toRad;
		double lat1 = dlat1 * toRad;
		double lon2 = dlon2 * toRad;
		double lat2 = dlat2 * toRad;

		double dlon = Math.sin((lon2 - lon1) / 2);
		double dlat = Math.sin((lat2 - lat1) / 2);
		double a = (dlat * dlat) + Math.cos(lat1) * Math.cos(lat2) * (dlon * dlon);
		double c = 2. * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;
		return d;
	}

	protected static final double A = 111322.; // Length of a degree in meter on
												// equator

	/**
	 * get distance for nearby objects (max 2 kms)
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	protected static double quickDistance(ChouetteLocalizedObject obj1, ChouetteLocalizedObject obj2) {
		if (obj1.hasCoordinates() && obj2.hasCoordinates()) {
			double dlon = (obj2.getLongitude().doubleValue() - obj1.getLongitude().doubleValue()) * A;
			dlon *= Math.cos((obj2.getLatitude().doubleValue() + obj1.getLatitude().doubleValue()) * toRad / 2.);
			double dlat = (obj2.getLatitude().doubleValue() - obj1.getLatitude().doubleValue()) * A;
			double ret = Math.sqrt(dlon * dlon + dlat * dlat);
			if (ret > 2000.)
				return distance(obj1, obj2);
			return ret;
		} else
			return 0.;

	}

	/**
	 * get distance between two coordinates
	 * 
	 * @param lat1
	 * @param lat2
	 * @param long1
	 * @param long2
	 * @return
	 */
	public static double quickDistanceFromCoordinates(double lat1, double lat2, double long1, double long2) {

		double dlon = (long2 - long1) * A;
		dlon *= Math.cos((lat2 + lat1) * toRad / 2.);
		double dlat = (lat2 - lat1) * A;
		double ret = Math.sqrt(dlon * dlon + dlat * dlat);
		if (ret > 2000.)
			return computeHaversineFormula(long1, lat1, long1, lat1);
		return ret;

	}

	/**
	 * @param class1
	 * @param attribute
	 * @return
	 */
	private Method findGetter(Class<? extends ChouetteIdentifiedObject> class1, String attribute) {
		String methodName = "get" + attribute;
		Method[] methods = class1.getMethods();
		Method accessor = null;
		for (Method method : methods) {
			if (method.getName().equalsIgnoreCase(methodName)) {
				accessor = method;
				break;
			}
		}
		return accessor;
	}

	/**
	 * @param class1
	 * @param checkPoint
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Method findCheckMethod(Class<? extends GenericValidator> class1, String checkPoint) {
		String methodName = "check" + checkPoint.replaceAll("-", "");
		Method[] methods = class1.getMethods();
		Method check = null;
		for (Method method : methods) {
			if (method.getName().equalsIgnoreCase(methodName)) {
				check = method;
				break;
			}
		}
		return check;
	}

}
