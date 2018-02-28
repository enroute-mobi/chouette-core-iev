package mobi.chouette.exchange.validator.checkpoints;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Time;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.hibernate.metamodel.ValidationException;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.report.DataLocation;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.exchange.validator.Constant;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.ChouetteIdentifiedObject;
import mobi.chouette.model.ChouetteLocalizedObject;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.util.Referential;

/**
 * @author michel
 *
 * @param <T>
 */
@Log4j
public abstract class GenericValidator<T extends ChouetteIdentifiedObject> {

	private static final String GETTER_FOR = "getter for ";
	private static final String FOR = " for ";
	private static final String UNKNOWN_COLUMN = "unknown column ";
	private static final String NOT_FOUND = " not found";
	private static final String FAILED = " failed ";
	private static final String NOT_ACCESSIBLE = " not accessible";
	private static final String METHOD_FOR = "method for ";
	private static final String[] genericCodes = { CheckPointConstant.L3_Generic_1, CheckPointConstant.L3_Generic_2,
			CheckPointConstant.L3_Generic_3 };

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
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		ControlParameters controlParameters = parameters.getControlParameters();
		String className = object.getClass().getSimpleName();
		// first check generic codes for class
		Arrays.stream(genericCodes).forEach(code -> {
			// find checkpoint for code
			Collection<CheckpointParameters> checkParams = null;
			// in transportModes
			if (transportMode != null) {
				Map<String, Collection<CheckpointParameters>> modeParameters = controlParameters
						.getTransportModeCheckpoints().get(transportMode);
				if (modeParameters != null) {
					checkParams = modeParameters.get(code);
				}
			}
			// else in global
			if (isEmpty(checkParams)) {
				checkParams = controlParameters.getGlobalCheckPoints().get(code);
			}
			// if tests are found
			if (!isEmpty(checkParams)) {
				checkParams.stream().forEach(checkParam -> {
					if (checkParam instanceof GenericCheckpointParameters) {
						GenericCheckpointParameters param = (GenericCheckpointParameters) checkParam;
						if (param.getClassName().equals(className)) {
							Method method = findCheckMethod(this.getClass(), param.getOriginCode());
							if (method != null) {
								try {
									validationReporter.addItemToValidationReport(context, checkParam.getSpecificCode(),
											checkParam.isErrorType() ? "E" : "W");
									method.invoke(this, context, object, param);
								} catch (ValidationException ve) {
									throw ve;
								} catch (IllegalAccessException | IllegalArgumentException e) {
									log.error(METHOD_FOR + checkParam.getOriginCode() + NOT_ACCESSIBLE, e);
								} catch (InvocationTargetException e) {
									log.error(METHOD_FOR + checkParam.getOriginCode() + FAILED, e);
								}
							} else {
								log.error(METHOD_FOR + checkParam.getOriginCode() + NOT_FOUND);
							}
						}
					}
				});
			}
		});

		// then check specific codes
		Arrays.stream(codes).forEach(code -> {
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
				checkParams.stream().forEach(checkParam -> {

					Method method = findCheckMethod(this.getClass(), checkParam.getOriginCode());
					if (method != null) {
						try {
							validationReporter.addItemToValidationReport(context, checkParam.getSpecificCode(),
									checkParam.isErrorType() ? "E" : "W");
							method.invoke(this, context, object, checkParam);
						} catch (ValidationException ve) {
							throw ve;
						} catch (IllegalAccessException | IllegalArgumentException e) {
							log.error(METHOD_FOR + checkParam.getOriginCode() + NOT_ACCESSIBLE, e);
						} catch (InvocationTargetException e) {
							log.error("target exception :" + e.getTargetException());
							if (e.getTargetException() instanceof mobi.chouette.exchange.validator.ValidationException) {
								throw (mobi.chouette.exchange.validator.ValidationException) e.getTargetException();
							} else {
								log.error(METHOD_FOR + checkParam.getOriginCode() + FAILED, e);
							}
						}
					} else {
						log.error(METHOD_FOR + checkParam.getOriginCode() + NOT_FOUND);
					}
				});
			}
		});
	}

	/**
	 * <b>Titre</b> :[Génériques] Contrôle du contenu selon un pattern
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2192">Cartes
	 * #2192</a>
	 * <p>
	 * <b>Code</b> :3-Generique-1
	 * <p>
	 * <b>Variables</b> : * attribut<br>
	 * * expression régulière
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : l'attribut de l'objet doit respecter un motif
	 * (expression régulière)
	 * <p>
	 * <b>Message</b> : {objet} : l'attribut {nom attribut} à une valeur
	 * {valeur} qui ne respecte pas le motif {expression régulière}
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3Generic1(Context context, T object, GenericCheckpointParameters parameters) {
		String javaAttribute = toCamelCase(parameters.getAttributeName());
		Method getter = findGetter(object.getClass(), javaAttribute);
		if (getter == null) {
			log.error(UNKNOWN_COLUMN + parameters.getAttributeName() + FOR + object.getClass().getSimpleName());
			return;
		}
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, parameters.getSpecificCode());
		try {
			Object objVal = getter.invoke(object);
			if (objVal != null) {
				String value = objVal.toString();
				String regex = parameters.getPatternValue();
				if (!Pattern.matches(regex, value)) {
					// pattern don't matches
					DataLocation source = new DataLocation(object, parameters.getAttributeName());
					validationReporter.addCheckPointReportError(context, parameters.getCheckId(),
							parameters.getSpecificCode(), CheckPointConstant.L3_Generic_1, source, value, regex);
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException e) {
			log.error(GETTER_FOR + parameters.getAttributeName() + NOT_ACCESSIBLE, e);
		} catch (InvocationTargetException e) {
			log.error(GETTER_FOR + parameters.getAttributeName() + FAILED, e);
		}

	}

	/**
	 * <b>Titre</b> :[Génériques] Valeur min
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2193">Cartes
	 * #2193</a>
	 * <p>
	 * <b>Code</b> :3-Generique-2
	 * <p>
	 * <b>Variables</b> : * attribut de type numérique<br>
	 * * valeur minimale (optionnelle)<br>
	 * * valeur maximale (optionnelle)
	 * <p>
	 * <b>Prérequis</b> : Néant
	 * <p>
	 * <b>Prédicat</b> : la valeur numérique de l'attribut doit rester comprise
	 * entre 2 valeurs
	 * <p>
	 * <b>Message</b> : {objet} : l'attribut {nom attribut} à une valeur
	 * {valeur} supérieure à la valeur maximale autorisée {max}<br>
	 * {objet} : l'attribut {nom attribut} à une valeur {valeur} inférieure à la
	 * valeur minimale autorisée {min}
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	protected void check3Generic2(Context context, T object, GenericCheckpointParameters parameters) {
		String javaAttribute = toCamelCase(parameters.getAttributeName());
		Method getter = findGetter(object.getClass(), javaAttribute);
		if (getter == null) {
			log.error(UNKNOWN_COLUMN + parameters.getAttributeName() + FOR + object.getClass().getSimpleName());
			return;
		}
		if (!Number.class.isAssignableFrom(getter.getReturnType())) {
			log.error("column " + parameters.getAttributeName() + FOR + object.getClass().getSimpleName()
					+ " is not numeric");
			return;
		}
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, parameters.getSpecificCode());
		try {
			Object objVal = getter.invoke(object);
			if (objVal != null) {
				long value = Long.parseLong(objVal.toString());
				if (parameters.getMinimumValue() != null) {
					long minVal = Long.parseLong(parameters.getMinimumValue());
					if (value < minVal) {
						DataLocation source = new DataLocation(object, parameters.getAttributeName());
						validationReporter.addCheckPointReportError(context, parameters.getCheckId(),
								parameters.getSpecificCode(), CheckPointConstant.L3_Generic_2, "2", source,
								objVal.toString(), parameters.getMinimumValue());
					}
				}
				if (parameters.getMaximumValue() != null) {
					long maxVal = Long.parseLong(parameters.getMaximumValue());
					if (value > maxVal) {
						DataLocation source = new DataLocation(object, parameters.getAttributeName());
						validationReporter.addCheckPointReportError(context, parameters.getCheckId(),
								parameters.getSpecificCode(), CheckPointConstant.L3_Generic_2, "1", source,
								objVal.toString(), parameters.getMaximumValue());
					}
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException e) {
			log.error(GETTER_FOR + parameters.getAttributeName() + NOT_ACCESSIBLE, e);
		} catch (InvocationTargetException e) {
			log.error(GETTER_FOR + parameters.getAttributeName() + FAILED, e);
		}

	}

	/**
	 * <b>Titre</b> :[Génériques] Unicité d'un attribut d'un objet dans une
	 * ligne
	 * <p>
	 * <b>Référence Redmine</b> :
	 * <a target="_blank" href="https://projects.af83.io/issues/2194">Cartes
	 * #2194</a>
	 * <p>
	 * <b>Code</b> :3-Generique-3
	 * <p>
	 * <b>Variables</b> : * attribut
	 * <p>
	 * <b>Prérequis</b> : Néant
	 * <p>
	 * <b>Prédicat</b> : la valeur de l'attribut doit être unique au sein des
	 * objets de la ligne
	 * <p>
	 * <b>Message</b> : {objet} : l'attribut {nom attribut} de {ref X} à une
	 * valeur {valeur} en conflit avec {ref Y}
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 *            context de validation
	 * @param object
	 *            objet à contrôler
	 * @param parameters
	 *            paramètres du point de contrôle
	 */
	@SuppressWarnings("unchecked")
	protected void check3Generic3(Context context, T object, GenericCheckpointParameters parameters) {
		String javaAttribute = toCamelCase(parameters.getAttributeName());
		Method getter = findGetter(object.getClass(), javaAttribute);
		if (getter == null) {
			log.error(UNKNOWN_COLUMN + parameters.getAttributeName() + FOR + object.getClass().getSimpleName());
			return;
		}

		// save data for iterative check
		// TODO : reset ATTRIBUTE_CONTEXT after validation completed
		Map<String, Map<String, DataLocation>> generic3Context = (Map<String, Map<String, DataLocation>>) context
				.computeIfAbsent(Constant.ATTRIBUTE_CONTEXT, k -> new HashMap<>());
		String attributeKey = parameters.getClassName() + ":" + parameters.getAttributeName();
		Map<String, DataLocation> attributeContext = generic3Context.computeIfAbsent(attributeKey,
				k -> new HashMap<>());
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.prepareCheckPointReport(context, parameters.getSpecificCode());
		try {
			Object objVal = getter.invoke(object);
			if (objVal != null) {
				String value = objVal.toString();
				DataLocation source = new DataLocation(object, parameters.getAttributeName());
				if (attributeContext.containsKey(value)) {
					// duplicate value
					DataLocation target = attributeContext.get(value);
					validationReporter.addCheckPointReportError(context, parameters.getCheckId(),
							parameters.getSpecificCode(), CheckPointConstant.L3_Generic_3, source, value, null, target);
				} else {
					attributeContext.put(value, source);
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException e) {
			log.error(GETTER_FOR + parameters.getAttributeName() + NOT_ACCESSIBLE, e);
		} catch (InvocationTargetException e) {
			log.error(GETTER_FOR + parameters.getAttributeName() + FAILED, e);
		}

	}

	/**
	 * @param underscore
	 * @return
	 */
	public static String toCamelCase(String underscore) {
		StringBuilder b = new StringBuilder();
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

	/**
	 * check if to objects are null or equals
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	protected boolean isEqual(Object obj1, Object obj2) {
		if (obj1 == null)
			return obj2 == null;
		return obj1.equals(obj2);
	}

	protected static final double R = 6371008.8; // Earth radius
	protected static final double TO_RAD = 0.017453292519943; // degree/rad
																// ratio

	/**
	 * calculate distance on spheroid
	 * 
	 * return 0 if one object has no coordinate
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	protected static Double distance(ChouetteLocalizedObject obj1, ChouetteLocalizedObject obj2) {
		if (obj1.hasCoordinates() && obj2.hasCoordinates()) {
			return computeHaversineFormula(obj1.getLongitude().doubleValue(), obj1.getLatitude().doubleValue(),
					obj2.getLongitude().doubleValue(), obj2.getLatitude().doubleValue());
		} else
			return Double.valueOf(0.);
	}

	/**
	 * @see http://mathforum.org/library/drmath/view/51879.html
	 */
	private static Double computeHaversineFormula(double dlon1, double dlat1, double dlon2, double dlat2) {

		double lon1 = dlon1 * TO_RAD;
		double lat1 = dlat1 * TO_RAD;
		double lon2 = dlon2 * TO_RAD;
		double lat2 = dlat2 * TO_RAD;

		double dlon = Math.sin((lon2 - lon1) / 2);
		double dlat = Math.sin((lat2 - lat1) / 2);
		double a = (dlat * dlat) + Math.cos(lat1) * Math.cos(lat2) * (dlon * dlon);
		double c = 2. * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;
		return Double.valueOf(d);
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
	protected static Double quickDistance(ChouetteLocalizedObject obj1, ChouetteLocalizedObject obj2) {
		if (obj1.hasCoordinates() && obj2.hasCoordinates()) {
			double dlon = (obj2.getLongitude().doubleValue() - obj1.getLongitude().doubleValue()) * A;
			dlon *= Math.cos((obj2.getLatitude().doubleValue() + obj1.getLatitude().doubleValue()) * TO_RAD / 2.);
			double dlat = (obj2.getLatitude().doubleValue() - obj1.getLatitude().doubleValue()) * A;
			double ret = Math.sqrt(dlon * dlon + dlat * dlat);
			if (ret > 2000.)
				return distance(obj1, obj2);
			return Double.valueOf(ret);
		} else
			return Double.valueOf(0.);

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
		dlon *= Math.cos((lat2 + lat1) * TO_RAD / 2.);
		double dlat = (lat2 - lat1) * A;
		double ret = Math.sqrt(dlon * dlon + dlat * dlat);
		if (ret > 2000.)
			return computeHaversineFormula(long1, lat1, long1, lat1);
		return ret;

	}

	/**
	 * calculate duration in second beetween times <br>
	 * if last time before first time, assume a change of day
	 * 
	 * @param first
	 *            first time (earlyer one)
	 * @param last
	 *            last time
	 * @return difference in seconds
	 */
	protected long diffTime(Time first, Time last) {
		if (first == null || last == null)
			return Long.MIN_VALUE; // TODO diffTime => action when first & last
									// time are null

		long lastTime = (last.getTime() / 1000L);
		long firstTime = (first.getTime() / 1000L);

		long delta = lastTime - firstTime;
		if (last.before(first)) {
			delta += 86400L;
		}

		return delta;
	}

	/**
	 * @param class1
	 * @param attribute
	 * @return
	 */
	private Method findGetter(Class<? extends ChouetteIdentifiedObject> class1, String attribute) {
		String methodName = "get" + attribute;
		Method[] methods = class1.getMethods();
		Optional<Method> result = Arrays.stream(methods).filter(method -> method.getName().equalsIgnoreCase(methodName))
				.findFirst();
		return result.orElse(null);
	}

	/**
	 * @param class1
	 * @param checkPoint
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Method findCheckMethod(Class<? extends GenericValidator> class1, String checkPoint) {
		String methodName = "check" + checkPoint.replaceAll("-", "");
		Method[] methods = class1.getDeclaredMethods();
		Optional<Method> result = Arrays.stream(methods).filter(method -> method.getName().equalsIgnoreCase(methodName))
				.findFirst();
		if (!result.isPresent()) {
			methods = class1.getSuperclass().getDeclaredMethods();
			result = Arrays.stream(methods).filter(method -> method.getName().equalsIgnoreCase(methodName)).findFirst();
		}
		return result.orElse(null);
	}

	@SuppressWarnings("unchecked")
	protected Double getDistance(Context context, ChouetteLocalizedObject stop1, ChouetteLocalizedObject stop2) {
		Map<String, Double> distances = (Map<String, Double>) context.get(Constant.DISTANCE_CONTEXT);
		if (distances == null) {
			distances = new HashMap<>();
			context.put(Constant.DISTANCE_CONTEXT, distances);
		}

		String key = stop1.getObjectId() + "#" + stop2.getObjectId();
		if (distances.containsKey(key)) {
			return distances.get(key);
		}
		key = stop2.getObjectId() + "#" + stop1.getObjectId();
		if (distances.containsKey(key)) {
			return distances.get(key);
		}
		Double distance = quickDistance(stop1, stop2);
		distances.put(key, distance);
		return distance;
	}

	/**
	 * calculate speed between 2 passing times in Km/h
	 * 
	 * @param context
	 *            context
	 * @param passingTime1
	 *            first passing time
	 * @param passingTime2
	 *            last passing time
	 * @return speed
	 */
	protected double getSpeedBetweenStops(Context context, VehicleJourneyAtStop passingTime1,
			VehicleJourneyAtStop passingTime2) {
		Referential r = (Referential) context.get(Constant.REFERENTIAL);
		// TODO find distance with shapes if present

		// else use flying distance
		StopAreaLite stop1 = r.findStopArea(passingTime1.getStopPoint().getStopAreaId());
		if (stop1 == null)
			throw new ValidationException("unknown StopArea for id " + passingTime1.getStopPoint().getStopAreaId());
		StopAreaLite stop2 = r.findStopArea(passingTime2.getStopPoint().getStopAreaId());
		if (stop2 == null)
			throw new ValidationException("unknown StopArea for id " + passingTime2.getStopPoint().getStopAreaId());

		Double distance = getDistance(context, stop1, stop2);
		// security if arrival time is missing
		Time arrivalTime = getArrivalTime(passingTime2);
		long time = diffTime(passingTime1.getDepartureTime(), arrivalTime);
		// if (duration less than 1 minute, force one minute
		if (time < 60L)
			time = 60l;
		double speed = distance / (double) time * 3.6; // (km/h)
		return speed;
	}

	/**
	 * gives arrivalTime or departuretime if null
	 * 
	 * @param passingTime
	 * @return
	 */
	protected Time getArrivalTime(VehicleJourneyAtStop passingTime) {
		return passingTime.getArrivalTime() == null ? passingTime.getDepartureTime() : passingTime.getArrivalTime();
	}

}
