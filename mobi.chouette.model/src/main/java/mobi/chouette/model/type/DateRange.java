package mobi.chouette.model.type;

import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.postgresql.util.PGobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * Java model for Postgresql DateRange object
 * 
 * @author michel
 * 
 * @see mobi.chouette.model.usertype.DateRangeArrayUserType
 * 
 * @since 4.0
 *
 */
@AllArgsConstructor
@Log4j
public class DateRange implements Serializable {
	private static final String YYYY_MM_DD = "yyyy-MM-dd";
	private static final long serialVersionUID = -4771648000594466515L;
	/**
	 * first date in range (included)
	 * 
	 * @return The first date in range (included)
	 */
	@Getter
	@Setter
	private Date first;
	/**
	 * last date in range (excluded)
	 * 
	 * @return The last date in range (excluded)
	 */
	@Getter
	@Setter
	private Date last;

	/**
	 * create a DateRange from Postgresql object
	 * @param obj Postgresql object
	 */
	public DateRange(PGobject obj) {
		if (obj.getType().equals("daterange")) {
			// value starts with [ and ends with ) : last is excluded from range
			String value = obj.getValue().substring(1, obj.getValue().length() - 1);
			String[] tok = value.split(",");
			SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
			try {
				first = new Date(format.parse(tok[0]).getTime());
			} catch (ParseException e) {
				log.error(e.getMessage(),e);
			}
			try {
				last = new Date(format.parse(tok[1]).getTime());
			} catch (ParseException e) {
				log.error(e.getMessage(),e);
			}
		}
	}

	/**
	 * convert to Postgresql object for saving
	 * 
	 * @return Postgresql object instance
	 * @throws SQLException
	 */
	public PGobject toPGObject() throws SQLException {
		PGobject obj = new PGobject();
		obj.setType("daterange");
		SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
		// use parenthesis at end to set excluded value
		obj.setValue("[" + format.format(first) + "," + format.format(last) + ")");
		return obj;
	}

	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
		return "DateRange [" + format.format(first) + "," + format.format(last) + "]";
	}

}
