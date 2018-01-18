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

	public static final String ID_SEPARATOR = ":";

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
		switch (value) {
		case "indoors":
			return ConnectionLinkTypeEnum.Underground;
		case "outdoors":
			return ConnectionLinkTypeEnum.Overground;
		case "mixed":
			return ConnectionLinkTypeEnum.Mixed;
		default:
			return null;
		}
	}

	public static List<DayTypeEnum> getDayTypes(List<String> values) {
		List<DayTypeEnum> result = new ArrayList<>();
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
		List<Date> result = new ArrayList<>();
		for (String value : values) {
			try {
				result.add(NetexStifUtils.getSQLDate(value));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return result;
	}

	public static String genStopPointId(String id, String order, Route route) {
		String suffix = route.objectIdSuffix() + "-" + id.split(ID_SEPARATOR)[2]+ "-" + order;

		return ChouetteModelUtil.changeSuffix(id, suffix);
	}

	public static String genRoutingConstrainId(String id, Route route) {
		String suffix = route.objectIdSuffix() + "-" + id.split(ID_SEPARATOR)[2];

		return ChouetteModelUtil.changeSuffix(id, suffix);
	}

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

}
