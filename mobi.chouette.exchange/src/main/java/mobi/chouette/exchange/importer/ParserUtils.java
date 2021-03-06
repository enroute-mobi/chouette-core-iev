package mobi.chouette.exchange.importer;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import lombok.extern.log4j.Log4j;

@Log4j
public class ParserUtils {

	private static DatatypeFactory factory = null;

	static {
		try {
			factory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
            log.error("unable to load DatatypeFactory");
		}
	}

	public static String getText(String value) {
		String result = null;
		if (value != null) {
			result = value.trim();
			result = (result.length() == 0 ? null : result);
		}
		return result;
	}

	public static Integer getInt(String value) {
		Integer result = null;
		if (value != null) {
			result = Integer.valueOf(value);
		}
		return result;
	}

	public static Long getLong(String value) {
		Long result = null;
		if (value != null) {
			result = Long.valueOf(value);
		}
		return result;
	}

	public static Boolean getBoolean(String value) {
		Boolean result = null;
		if (value != null) {
			result = Boolean.valueOf(value);
		}
		return result;
	}

	public static <T extends Enum<T>> T getEnum(Class<T> type, String value) {
		T result = null;
		if (value != null) {
			try {
				result = Enum.valueOf(type, value);
			} catch (Exception ignored) {
	            log.debug("enum not found for value "+value);
			}
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	public static Time getSQLDuration(String value) {
		Time result = null;

		if (value != null) {
			try {
				Duration duration = factory.newDuration(value);
				result = new Time(duration.getHours(), duration.getMinutes(), duration.getSeconds());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return result;
	}

	public static Time getSQLTime(String value) throws ParseException {
		Time result = null;
		if (value != null) {
			DateFormat timeFormat = null;
			if (value.contains(".")) {
				timeFormat = new SimpleDateFormat("HH:mm:ss.sss");
			} else {
				timeFormat = new SimpleDateFormat("HH:mm:ss");
			}
			result = new Time(getDate(timeFormat, value).getTime());

		}
		return result;
	}

	public static Date getSQLDateTime(String value) {
		Date result = null;

		if (value != null) {
			XMLGregorianCalendar calendar = factory.newXMLGregorianCalendar(value);
			result = new Date(calendar.toGregorianCalendar().getTimeInMillis());
		}
		return result;
	}

	public static Date getSQLDate(String value) throws ParseException {
		Date result = null;

		if (value != null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			long time = getDate(dateFormat, value).getTime();
			result = new Date(time);
		}
		return result;
	}

	public static java.util.Date getDate(DateFormat format, String value) throws ParseException {
		java.util.Date result = null;

		if (value != null) {
			result = format.parse(value);
		}
		return result;
	}

	public static java.util.Date getDate(String value) throws ParseException {
		String format = "yyyy-MM-dd'T'HH:mm:ss";
		if (value != null && value.endsWith("Z")) {
			format = format + "'Z'";
		}

		DateFormat dateFormat = new SimpleDateFormat(format);
		return getDate(dateFormat, value);
	}

	public static BigDecimal getBigDecimal(String value) {
		BigDecimal result = null;
		if (value != null) {
			try {
				result = BigDecimal.valueOf(Double.valueOf(value));
			} catch (Exception ignored) {
	            log.debug("unable to convetr to BigDecimal "+value);
			}
		}
		return result;
	}

	public static BigDecimal getBigDecimal(String value, String pattern) {
		BigDecimal result = null;

		if (value != null) {
			Matcher m = Pattern.compile(pattern).matcher(value.trim());
			if (m.matches()) {
				result = getBigDecimal(m.group(1));

			}
		}
		return result;
	}

	public static BigDecimal getX(String value) {
		return ParserUtils.getBigDecimal(value, "([\\d\\.]+) [\\d\\.]+");
	}

	public static BigDecimal getY(String value) {
		return ParserUtils.getBigDecimal(value, "[\\d\\.]+ ([\\d\\.]+)");
	}

	public static String objectIdPrefix(String objectId) {
		if (objectIdArray(objectId).length > 2) {
			return objectIdArray(objectId)[0].trim();
		} else
			return "";
	}

	public static String objectIdSuffix(String objectId) {
		if (objectIdArray(objectId).length > 2)
			return objectIdArray(objectId)[2].trim();
		else
			return "";
	}

	private static String[] objectIdArray(String objectId) {
		return objectId.split(":");
	}

}
