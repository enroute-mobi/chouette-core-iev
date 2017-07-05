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
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.model.OperatingPeriod;
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
		Timetable timetable = null;
		OperatingPeriod period = null;
		CalendarDay day = null;
		String isAvailable = null;
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(OPERATING_PERIOD_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				period = factory.getOperatingPeriod(ref);
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(DATE)) {
				String value = isAvailable = xpp.nextText();
				Date date = ParserUtils.getSQLDate(value);
				day = new CalendarDay();
				day.setDate(date);
			}else if (xpp.getName().equals(DAY_TYPE_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				timetable = ObjectFactory.getTimetable(referential, ref);
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(IS_AVAILABLE)) {
				isAvailable = xpp.nextText();
				if (day != null)
				{
					Boolean included = ParserUtils.getBoolean(isAvailable);
					day.setIncluded(included);
				}
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
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
