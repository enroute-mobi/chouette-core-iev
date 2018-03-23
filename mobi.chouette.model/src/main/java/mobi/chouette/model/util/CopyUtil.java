package mobi.chouette.model.util;

import java.sql.Date;
import java.util.ArrayList;

import org.hibernate.cfg.NotYetImplementedException;

import mobi.chouette.model.AccessLink;
import mobi.chouette.model.AccessPoint;
import mobi.chouette.model.CalendarDay;
import mobi.chouette.model.Company;
import mobi.chouette.model.ConnectionLink;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.GroupOfLine;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Line;
import mobi.chouette.model.Network;
import mobi.chouette.model.Period;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopArea;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;

public class CopyUtil {

	private static final String MESSAGE = "not implemented";

	public static Network copy(Network object) {
		throw new NotYetImplementedException(MESSAGE);
	}

	public static Company copy(Company object) {
		throw new NotYetImplementedException(MESSAGE);
	}

	public static GroupOfLine copy(GroupOfLine object) {
		throw new NotYetImplementedException(MESSAGE);
	}

	public static StopArea copy(StopArea object) {
		throw new NotYetImplementedException(MESSAGE);
	}

	public static AccessPoint copy(AccessPoint object) {
		throw new NotYetImplementedException(MESSAGE);
	}

	public static AccessLink copy(AccessLink object) {
		throw new NotYetImplementedException(MESSAGE);
	}

	public static ConnectionLink copy(ConnectionLink object) {
		throw new NotYetImplementedException(MESSAGE);
	}

	public static Timetable copy(Timetable object) {
		Timetable tm = new Timetable();
		tm.setObjectId(object.getObjectId());
		tm.setObjectVersion(object.getObjectVersion());
		tm.setDataSourceRef(object.getDataSourceRef());
		tm.setComment(object.getComment());
		tm.setVersion(object.getVersion());
		tm.setIntDayTypes(object.getIntDayTypes());
		tm.setStartOfPeriod(object.getStartOfPeriod());
		tm.setEndOfPeriod(object.getEndOfPeriod());
		tm.setPeriods(new ArrayList<Period>());
		for (Period period : object.getPeriods()) {
			if (period != null && period.getStartDate() != null && period.getEndDate() != null)
				tm.addPeriod(new Period((Date) (period.getStartDate().clone()), (Date) (period.getEndDate().clone())));
		}
		tm.setCalendarDays(new ArrayList<CalendarDay>());
		for (CalendarDay day : object.getCalendarDays()) {
			if (day != null && day.getDate() != null && day.getIncluded() != null)
				tm.addCalendarDay(new CalendarDay((Date) (day.getDate().clone()), day.getIncluded()));
		}
		return tm;

	}

	public static StopPoint copy(StopPoint object) {
		throw new NotYetImplementedException(MESSAGE);
	}

	public static Line copy(Line object) {
		throw new NotYetImplementedException(MESSAGE);
	}

	public static Route copy(Route object) {
		throw new NotYetImplementedException(MESSAGE);
	}

	public static JourneyPattern copy(JourneyPattern object) {
		throw new NotYetImplementedException(MESSAGE);
	}

	public static VehicleJourney copy(VehicleJourney object) {
		throw new NotYetImplementedException(MESSAGE);
	}

	public static Footnote copy(Footnote object) {
		Footnote ft = new Footnote();
		ft.setKey(object.getKey());
		ft.setLabel(object.getLabel());
		ft.setDataSourceRef(object.getDataSourceRef());
		return ft;
	}
	
	private CopyUtil(){}
}
