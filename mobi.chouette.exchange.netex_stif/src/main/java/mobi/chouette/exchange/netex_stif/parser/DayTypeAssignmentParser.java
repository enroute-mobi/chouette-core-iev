package mobi.chouette.exchange.netex_stif.parser;

import java.sql.Date;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.importer.ParserUtils;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.DayTypeAssignment;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.model.OperatingPeriod;
import mobi.chouette.exchange.netex_stif.validator.DayTypeAssignmentValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;
import mobi.chouette.model.CalendarDay;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class DayTypeAssignmentParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);

		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		DayTypeAssignmentValidator validator = (DayTypeAssignmentValidator) ValidatorFactory.getValidator(context, DayTypeAssignmentValidator.class);
		String id = xpp.getAttributeValue(null, ID);
		DayTypeAssignment dayTypeAssignment = factory.getDayTypeAssignment(id); 
		validator.checkNetexId(context, DAY_TYPE_ASSIGNMENT, id, lineNumber, columnNumber);
		String changed = xpp.getAttributeValue(null, CHANGED);
		if (changed != null) {
			dayTypeAssignment.setCreationTime(NetexStifUtils.getDate(changed));
		}
		String modification = xpp.getAttributeValue(null, MODIFICATION);
		validator.addModification(context, id, modification);
		Timetable timetable = null;
		OperatingPeriod period = null;
		CalendarDay day = null;
		String isAvailable = null;
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(OPERATING_PERIOD_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				String attr_version = xpp.getAttributeValue(null, VERSION);
				String content = xpp.nextText();
				// check internal reference
				boolean checked = validator.checkNetexRef(context, dayTypeAssignment, OPERATING_PERIOD_REF, ref, lineNumber,
						columnNumber);
				if (checked)
					checked = validator.checkInternalRef(context, dayTypeAssignment, OPERATING_PERIOD_REF, ref,
							attr_version, content, lineNumber, columnNumber);
				dayTypeAssignment.setOperatingPeriodRef(ref);
				period = factory.getOperatingPeriod(ref);
			} else if (xpp.getName().equals(OPERATING_DAY_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				dayTypeAssignment.setOperationDayRef(ref);
			}else if (xpp.getName().equals(DATE)) {
				String value = xpp.nextText();
				Date date = ParserUtils.getSQLDate(value);
				day = new CalendarDay();
				day.setDate(date);
				dayTypeAssignment.setDay(day);
			}else if (xpp.getName().equals(DAY_TYPE_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				String attr_version = xpp.getAttributeValue(null, VERSION);
				String content = xpp.nextText();
				// check internal reference
				boolean checked = validator.checkNetexRef(context, dayTypeAssignment, DAY_TYPE_REF, ref, lineNumber,
						columnNumber);
				if (checked)
					checked = validator.checkInternalRef(context, dayTypeAssignment, DAY_TYPE_REF, ref,
							attr_version, content, lineNumber, columnNumber);
				timetable = ObjectFactory.getTimetable(referential, ref);
			} else if (xpp.getName().equals(IS_AVAILABLE)) {
				isAvailable = xpp.nextText();
				Boolean included = ParserUtils.getBoolean(isAvailable);
				dayTypeAssignment.setIsAvailable(included);
				if (day != null)
				{
					day.setIncluded(included);
				}
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		validator.validate(context,dayTypeAssignment,lineNumber,columnNumber);
		if (timetable != null) {
			if (period != null) {
				// log.info("add period to timetable "+timetable.getObjectId());
				// bypass method add to avoid identical content for uninitialized period 
				timetable.getPeriods().add(period.getPeriod());
			}
			if (day != null) {
				// log.info("add date to timetable "+timetable.getObjectId());
				timetable.addCalendarDay(day);
			}
		}
	}

	static {

		ParserFactory.register(DayTypeAssignmentParser.class.getName(), new ParserFactory() {
			private DayTypeAssignmentParser instance = new DayTypeAssignmentParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}
}
