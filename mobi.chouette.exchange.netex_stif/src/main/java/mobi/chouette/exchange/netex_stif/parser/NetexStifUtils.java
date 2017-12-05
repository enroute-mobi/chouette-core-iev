package mobi.chouette.exchange.netex_stif.parser;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.ParserUtils;
import mobi.chouette.exchange.parameters.AbstractImportParameter;
import mobi.chouette.model.ChouetteIdentifiedObject;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.type.ConnectionLinkTypeEnum;
import mobi.chouette.model.type.DayTypeEnum;
import mobi.chouette.model.type.PTDirectionEnum;
import mobi.chouette.model.util.ChouetteModelUtil;

@Log4j
public class NetexStifUtils extends ParserUtils {

	public static String fromPTDirectionType(PTDirectionEnum type) {
		if (type == null)
			return null;
		return type.toString();
	}

	public static PTDirectionEnum toPTDirectionType(String value) {
		if (value == null)
			return null;
		PTDirectionEnum result = null;
		try {
			result = PTDirectionEnum.valueOf(StringUtils.capitalize(value));
		} catch (Exception e) {
			log.error("unable to translate " + value + " as PTDirection");
		}
		return result;
	}

	public static String fromConnectionLinkType(ConnectionLinkTypeEnum type) {
		if (type == null)
			return null;
		switch (type) {
		case Underground:
			return "indoors";
		case Overground:
			return "outdoors";
		case Mixed:
			return "mixed";
		default:
			return "unknown";
		}
	}

	public static ConnectionLinkTypeEnum toConnectionLinkType(String value) {
		if (value == null)
			return null;
		if (value.equals("indoors"))
			return ConnectionLinkTypeEnum.Underground;
		else if (value.equals("outdoors"))
			return ConnectionLinkTypeEnum.Overground;
		else if (value.equals("mixed"))
			return ConnectionLinkTypeEnum.Mixed;
		else
			return null;
	}

	public static List<DayTypeEnum> getDayTypes(List<String> values) {
		List<DayTypeEnum> result = new ArrayList<DayTypeEnum>();
		for (String dayType : values) {
			try {
				result.add(DayTypeEnum.valueOf(dayType));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return result;

	}

	public static List<Date> getCalendarDays(List<String> values) {
		List<Date> result = new ArrayList<Date>();
		for (String value : values) {
			try {
				result.add(NetexStifUtils.getSQLDate(value));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return result;
	}

	public static Long getTrainNumber(String value) {
		try {
			String number = value.replaceFirst("Local:TrainNumber:", "");
			return Long.parseLong(number);
		} catch (Exception e) {
			return null;
		}
	}

	public final static String ID_SEPARATOR = ":";

	public static String genStopPointId(String id, String order, Route route) {
		String suffix =  id.split(ID_SEPARATOR)[2];
		String routeSuffix = route.objectIdSuffix();
		// if (!suffix.startsWith(routeSuffix) ) {
			suffix = routeSuffix+"-"+suffix;
		// }
		suffix += "-"+order;
		
		return ChouetteModelUtil.changeSuffix(id,suffix);
	}
	
	public static String genRoutingConstrainId(String id, Route route) {
		String suffix =  id.split(ID_SEPARATOR)[2];
		String routeSuffix = route.objectIdSuffix();
		// if (!suffix.startsWith(routeSuffix) ) {
			suffix = routeSuffix+"-"+suffix;
		// }
		
		return ChouetteModelUtil.changeSuffix(id,suffix);
	}

//	public static String genStopPointId(StopPoint stopPoint) {
//		return genStopPointId(stopPoint.getObjectId(), stopPoint.getPosition().toString(), stopPoint.getRoute());
//
//	}

	public static void uniqueObjectIdOnLine(Context context, ChouetteIdentifiedObject object, LineLite line) {
		String suffix = object.objectIdSuffix();
		String lineSuffix = line.objectIdSuffix();
		if (suffix.startsWith(lineSuffix) && !suffix.equals(lineSuffix))
			return;
		String objectId = ChouetteModelUtil.changeSuffix(object.getObjectId(), lineSuffix + "-" + suffix);
		object.setObjectId(objectId);
		uniqueObjectId(context, object);
	}
	
	public static void uniqueObjectId(Context context, ChouetteIdentifiedObject object) {
		String suffix = object.objectIdSuffix();
		AbstractImportParameter parameters = (AbstractImportParameter) context.get(Constant.CONFIGURATION);
		Long refId = parameters.getReferentialId();
		String objectId = ChouetteModelUtil.changeSuffix(object.getObjectId(), refId.toString() + "-" + suffix);
		object.setObjectId(objectId);
	}
	
//	public static void stopPointObjectId(ChouetteIdentifiedObject object, Route route, String order) {
//		String suffix = object.objectIdSuffix();
//		String routeSuffix = route.objectIdSuffix();
//		if (suffix.startsWith(routeSuffix) && !suffix.equals(routeSuffix)) {
//			return;
//		}
//		String objectId = ChouetteModelUtil.changeSuffix(object.getObjectId(),
//				routeSuffix + "-" + suffix + "-" + order);
//		object.setObjectId(objectId);
//	}

}
