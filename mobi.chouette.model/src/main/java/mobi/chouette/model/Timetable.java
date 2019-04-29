package mobi.chouette.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.Cacheable;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.type.DayMaskPair;
import mobi.chouette.model.type.DayTypeEnum;
import lombok.extern.log4j.Log4j;
import java.util.Map;



/**
 * Chouette Timetable
 * <p>
 * Neptune mapping : Timetable <br>
 * Gtfs mapping : service in calendar and calendar_dates <br>
 */
@Log4j
@Entity
@Table(name = "time_tables")
@Cacheable
@EqualsAndHashCode(of = { "objectId" }, callSuper = false)
@NoArgsConstructor
@ToString(callSuper = true, exclude = { "vehicleJourneys" })
public class Timetable extends ChouetteIdentifiedObject implements SignedChouetteObject, DataSourceRefObject {
	private static final long serialVersionUID = -1598554061982685113L;
	public static final long ONE_DAY = 3600000L * 24;


	/** Week day index ranges from 0 to 6, Sunday to Saturday**/
	public static final ArrayList<DayMaskPair> weekDayIndexToMask;
    static {
    	weekDayIndexToMask = new ArrayList<DayMaskPair>();
    	weekDayIndexToMask.add(new DayMaskPair(256, DayTypeEnum.Sunday));
    	weekDayIndexToMask.add(new DayMaskPair(4, DayTypeEnum.Monday));
    	weekDayIndexToMask.add(new DayMaskPair(8, DayTypeEnum.Tuesday));
        weekDayIndexToMask.add(new DayMaskPair(16, DayTypeEnum.Wednesday));
        weekDayIndexToMask.add(new DayMaskPair(32, DayTypeEnum.Thursday));
        weekDayIndexToMask.add(new DayMaskPair(64, DayTypeEnum.Friday));
        weekDayIndexToMask.add(new DayMaskPair(128, DayTypeEnum.Saturday));
    }

	@Getter
	@Setter
	@GenericGenerator(name = "time_tables_id_seq", strategy = "mobi.chouette.persistence.hibernate.ChouetteTenantIdentifierGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "time_tables_id_seq"),
			@Parameter(name = "increment_size", value = "100") })
	@GeneratedValue(generator = "time_tables_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;

	/**
	 * Neptune object id <br>
	 * composed of 3 items separated by a colon
	 * <ol>
	 * <li>prefix : an alphanumerical value (underscore accepted)</li>
	 * <li>type : a camelcase name describing object type</li>
	 * <li>technical id: an alphanumerical value (underscore and minus accepted)
	 * </li>
	 * </ol>
	 * This data must be unique in dataset
	 *
	 * @return The actual value
	 */
	@Getter
	@NaturalId(mutable = true)
	@Column(name = "objectid", nullable = false, unique = true)
	protected String objectId;

	public void setObjectId(String value) {
		objectId = StringUtils.abbreviate(value, 255);
	}

	/**
	 * object version
	 *
	 * @param objectVersion
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "object_version")
	protected Long objectVersion = 1L;

	@Getter
	@Setter
	@Column(name = "checksum")
	private String checksum;

	@Getter
	@Setter
	@Column(name = "checksum_source")
	private String checksumSource;

	/**
	 * comment <br>
	 * Note : should be rename as name in next release
	 *
	 * @return The actual value
	 */
	@Getter
	@Column(name = "comment")
	private String comment;

	/**
	 * set comment <br>
	 * truncated to 255 characters if too long
	 *
	 * @param value
	 *            New value
	 */
	public void setComment(String value) {
		comment = StringUtils.abbreviate(value, 255);
	}

	/**
	 * version <br>
	 * Note : should be rename as short name in next release
	 *
	 * @return The actual value
	 */
	@Getter
	@Column(name = "version")
	private String version;

	/**
	 * set version <br>
	 * truncated to 255 characters if too long
	 *
	 * @param value
	 *            New value
	 */
	public void setVersion(String value) {
		version = StringUtils.abbreviate(value, 255);
	}

	/**
	 * data source ref
	 *
	 * @return The actual value
	 */
	@Getter
	@Column(name = "data_source_ref")
	private String dataSourceRef;

	/**
	 * set data source ref <br>
	 * truncated to 255 characters if too long
	 *
	 * @param value
	 *            New value
	 */
	public void setDataSourceRef(String value) {
		dataSourceRef = StringUtils.abbreviate(value, 255);
	}

	/**
	 * day of week as bit mask
	 *
	 * @param intDayTypes
	 *            New value
	 * @return The actual value
	 */

	@Getter
	@Setter
	@Column(name = "int_day_types")
	private Integer intDayTypes = 0;

	public List<DayTypeEnum> getDayTypes() {
		List<DayTypeEnum> result = new ArrayList<DayTypeEnum>();
		if (getIntDayTypes() != null && getIntDayTypes().intValue() != 0) {
			for (DayMaskPair pair : weekDayIndexToMask) {
				if ((this.intDayTypes & pair.getKey()) == pair.getKey()){
					result.add(pair.getValue());
				}
			}
		}
		return result;
	}

	public void setDayTypes(List<DayTypeEnum> arrayList) {
		int value = 0;
		for (DayTypeEnum dayType : arrayList) {
			int mask = 1 << dayType.ordinal();
			value |= mask;
		}
		this.intDayTypes = value;
	}

	/**
	 * add a dayType if not already present
	 *
	 * @param dayType
	 */
	public void addDayType(DayTypeEnum dayType) {
		if (dayType != null) {
			int mask = 1 << dayType.ordinal();
			this.intDayTypes |= mask;
		}
	}

	/**
	 * remove a daytype
	 *
	 * @param dayType
	 */
	public void removeDayType(DayTypeEnum dayType) {
		if (dayType != null) {
			int mask = 1 << dayType.ordinal();
			this.intDayTypes &= ~mask;
		}
	}

	/**
	 * first valid day in timetable
	 *
	 * @param startOfPeriod
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "start_date")
	private Date startOfPeriod;

	/**
	 * last valid day in timetable
	 *
	 * @param endOfPeriod
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "end_date")
	private Date endOfPeriod;

	/**
	 * list of peculiar days
	 *
	 * @param calendarDays
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@CollectionTable(name = "time_table_dates", joinColumns = @JoinColumn(name = "time_table_id"))
	@OrderColumn(name = "position", nullable = false)
	private List<CalendarDay> calendarDays = new ArrayList<CalendarDay>(0);

	/**
	 * list of periods
	 *
	 * @param periods
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@CollectionTable(name = "time_table_periods", joinColumns = @JoinColumn(name = "time_table_id"))
	@OrderColumn(name = "position", nullable = false)
	private List<Period> periods = new ArrayList<Period>(0);

	/**
	 * list of vehicleJourneys
	 *
	 * @param vehicleJourneys
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@ManyToMany(mappedBy = "timetables", fetch = FetchType.LAZY)
	private List<VehicleJourney> vehicleJourneys = new ArrayList<VehicleJourney>(0);

	/**
	 * add a day if not already present
	 *
	 * @param calendarDay
	 */
	public void addCalendarDay(CalendarDay calendarDay) {
		if (calendarDays == null)
			calendarDays = new ArrayList<CalendarDay>();
		if (calendarDay != null && !calendarDays.contains(calendarDay)) {
			calendarDays.add(calendarDay);
		}
	}

	/**
	 * remove a day
	 *
	 * @param calendarDay
	 */
	public void removeCalendarDay(CalendarDay calendarDay) {
		if (calendarDays == null)
			calendarDays = new ArrayList<CalendarDay>();
		if (calendarDay != null) {
			calendarDays.remove(calendarDay);
		}
	}

	/**
	 * add a period if not already present
	 *
	 * @param period
	 */
	public void addPeriod(Period period) {
		if (periods == null)
			periods = new ArrayList<Period>();
		if (period != null && !periods.contains(period)) {
			periods.add(period);
		}

	}

	/**
	 * remove a period
	 *
	 * @param period
	 */
	public void removePeriod(Period period) {
		if (periods == null)
			periods = new ArrayList<Period>();
		if (period != null) {
			periods.remove(period);
		}
	}

	/**
	 * add a vehicle journey if not already present
	 *
	 * @param vehicleJourney
	 */
	public void addVehicleJourney(VehicleJourney vehicleJourney) {
		if (!getVehicleJourneys().contains(vehicleJourney)) {
			getVehicleJourneys().add(vehicleJourney);
		}
		if (!vehicleJourney.getTimetables().contains(this)) {
			vehicleJourney.getTimetables().add(this);
		}
	}

	/**
	 * remove a vehicle journey
	 *
	 * @param vehicleJourney
	 */
	public void removeVehicleJourney(VehicleJourney vehicleJourney) {
		getVehicleJourneys().remove(vehicleJourney);
		vehicleJourney.getTimetables().remove(this);
	}

	public static int buildDayTypeMask(ArrayList<Integer> weekDays) {
		int value = 0;
		if (weekDays == null) { return value; }
		for (Integer weekday : weekDays) {
			value += weekDayIndexToMask.get(weekday).getKey();
		}
		return value;
	}


	/**
	 * get peculiar dates
	 *
	 * @return a list of active dates and periods converted to dates if
	 *         exclusion present
	 */
	public List<Date> getEffectiveDates() {
		List<Date> ret = getPeculiarDates();
		if (!getExcludedDates().isEmpty()) {
			for (Period period : periods) {
				List<Date> added = toDates(period);
				for (Date date : added) {
					if (!ret.contains(date))
						ret.add(date);
				}
			}
		}
		Collections.sort(ret);
		return ret;
	}

	/**
	 * get peculiar dates
	 *
	 * @return a list of active dates
	 */
	public List<Date> getPeculiarDates() {
		List<Date> ret = new ArrayList<>();
		for (CalendarDay day : getCalendarDays()) {
			if (day.getIncluded())
				ret.add(new Date(day.getDate().getTime()));
		}
		Collections.sort(ret);
		return ret;
	}

	/**
	 * get excluded dates
	 *
	 * @return a list of excluded dates
	 */
	public List<Date> getExcludedDates() {
		List<Date> ret = new ArrayList<>();
		for (CalendarDay day : getCalendarDays()) {
			if (!day.getIncluded())
				ret.add(new Date(day.getDate().getTime()));
		}
		return ret;
	}

	public boolean isActiveOn(final Date aDay) {
		if (getCalendarDays() != null && !getCalendarDays().isEmpty()) {
			CalendarDay includedDay = new CalendarDay(aDay, true);
			if (getCalendarDays().contains(includedDay)) {
				return true;
			}
		}
		if (getIntDayTypes() != null && getIntDayTypes().intValue() != 0 && getPeriods() != null && !getPeriods().isEmpty()) {
			Calendar c = Calendar.getInstance();
			c.setTime(aDay);
			int weekDayMask = weekDayIndexToMask.get(c.get(Calendar.DAY_OF_WEEK)-1).getKey();
			if ((getIntDayTypes() & weekDayMask) == weekDayMask) {
				/** Checks if day is in a period **/
				for (Period period : getPeriods()) {
					if (period.contains(aDay)) { return true; }
				}
			}
		}
		return false;
	}

	public boolean isActiveBefore(final Date aDay) {
		return isActiveOnPeriod(getStartOfPeriod(), aDay);
	}

	public boolean isActiveAfter(final Date aDay) {
		return isActiveOnPeriod(aDay, getEndOfPeriod());
	}

	public boolean isActiveOnPeriod(final Date start, final Date end) {
		if (start == null || end == null) {
			return false;
		} else {
			Date day = new Date(start.getTime());
			while (day.before(end)) {
				if (isActiveOn(day))
					return true;
				day.setTime(day.getTime() + ONE_DAY);
			}
			return isActiveOn(end);
		}
	}

	private List<Date> toDates(Period period) {
		List<Date> dates = new ArrayList<>();
		List<Date> excluded = getExcludedDates();

		if (getIntDayTypes() != null && getIntDayTypes().intValue() != 0) {
			Calendar c = Calendar.getInstance();
			c.setTime(period.getStartDate());

			while (!c.getTime().after(period.getEndDate())) {
				int weekDayMask = weekDayIndexToMask.get(c.get(Calendar.DAY_OF_WEEK)-1).getKey();
				if ((getIntDayTypes() & weekDayMask) == weekDayMask) {
					Date d = new Date(c.getTime().getTime());
					if (!excluded.contains(d)) { dates.add(d);}
				}
				c.add(Calendar.DATE, 1);
			}
		}
		return dates;
	}

	/**
	 * calculate startOfPeriod and endOfPeriod form dates and periods
	 */
	public void computeLimitOfPeriods() {
		Date startOfPeriod = null;
		Date endOfPeriod = null;
		for (Period period : getPeriods()) {
			if (startOfPeriod == null || startOfPeriod.after(period.getStartDate())) {
				startOfPeriod = (Date) period.getStartDate().clone();
			}
			if (endOfPeriod == null || endOfPeriod.before(period.getEndDate())) {
				endOfPeriod = (Date) period.getEndDate().clone();
			}
		}
		// check DayType
		Calendar c = Calendar.getInstance();
		if (startOfPeriod != null && endOfPeriod != null) {
			while (startOfPeriod.before(endOfPeriod) && !isActiveOn(startOfPeriod)) {
				c.setTime(startOfPeriod);
				c.add(Calendar.DATE, 1);
				startOfPeriod.setTime(c.getTimeInMillis());
			}
			while (endOfPeriod.after(startOfPeriod) && !isActiveOn(endOfPeriod)) {
				c.setTime(endOfPeriod);
				c.add(Calendar.DATE, -1);
				endOfPeriod.setTime(c.getTimeInMillis());
			}
		}
		for (CalendarDay calendarDay : getCalendarDays()) {
			Date date = calendarDay.getDate();
			if (calendarDay.getIncluded()) {
				if (startOfPeriod == null || date.before(startOfPeriod))
					startOfPeriod = (Date) date.clone();
				if (endOfPeriod == null || date.after(endOfPeriod))
					endOfPeriod = (Date) date.clone();
			}
		}
		setStartOfPeriod(startOfPeriod);
		setEndOfPeriod(endOfPeriod);
	}
}
