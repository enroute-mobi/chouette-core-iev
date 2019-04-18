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
import mobi.chouette.model.Timetable;

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
public class DateRange implements Serializable, Cloneable{
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
	 * create an empty DateRange
	 */
	public DateRange() {}
	
	

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

	/**
	 * Include DateRange into another
	 * 
	 * @param limit
	 */
	public void extendTo(DateRange limit)
	{
		if (first == null || first.after(limit.getFirst())) first = new Date(limit.getFirst().getTime());
		if (last == null || last.before(limit.getLast())) last = new Date(limit.getLast().getTime());
	}
	/**
	 * Include Timetable limit into DateRange
	 * @param limit
	 */
	public void extendTo(Timetable limit)
	{
		if (limit.getStartOfPeriod() == null || limit.getEndOfPeriod() == null) return;
		if (first == null || first.after(limit.getStartOfPeriod())) first = new Date(limit.getStartOfPeriod().getTime());
		if (last == null || last.before(limit.getEndOfPeriod())) last = new Date(limit.getEndOfPeriod().getTime());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
		String firstDate= (first == null  ? "unset" : format.format(first));
		String lastDate= (last == null  ? "unset" : format.format(last));
		return "DateRange [" + firstDate + "," + lastDate + "]";
	}

	public boolean intersects(DateRange another)
	{
		return !(this.first.after(another.last) || this.last.before(another.first));
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
	    if (this == o) return true;
	    if (!(o instanceof DateRange))return false;
	    DateRange otherDateRange = (DateRange) o;
	    return ((otherDateRange.getFirst() == this.first) && (otherDateRange.getLast() == this.last));
	}
	
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + first.hashCode();
	    result = prime * result + last.hashCode();
	    return result;
	}

	public DateRange clone()
	{
		DateRange dateRangeCloned = null;
		try {
			dateRangeCloned = (DateRange) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error("Can not clone DateRange variable", e);
		}
		return dateRangeCloned;
	}
}
