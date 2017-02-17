package mobi.chouette.exchange.netex_stif.parser;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.type.DayTypeEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class DayTypeParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);
		xpp.require(XmlPullParser.START_TAG, null, DAY_TYPE);
		String id = xpp.getAttributeValue(null, ID);
		Timetable timeTable = ObjectFactory.getTimetable(referential, id);
		Integer version = Integer.valueOf(xpp.getAttributeValue(null, VERSION));
		timeTable.setObjectVersion(version != null ? version : 0);

		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NAME)) {
				timeTable.setComment(xpp.nextText());
			} else if (xpp.getName().equals(PROPERTIES)) {
				parseProperties(xpp, context, timeTable);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
	}

	private void parseProperties(XmlPullParser xpp, Context context, Timetable timetable) throws Exception {
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(PROPERTY_OF_DAY)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(DAYS_OF_WEEK)) {
						String day = xpp.nextText();
						DayTypeEnum dayType = daytypes.get(day);
						if (dayType != null) {
							timetable.addDayType(dayType);
						}
					}
				}
			}
		}
	}

	private static Map<String, DayTypeEnum> daytypes = new HashedMap();

	static {
		daytypes.put("Monday", DayTypeEnum.Monday);
		daytypes.put("Tuesday", DayTypeEnum.Tuesday);
		daytypes.put("Wednesday", DayTypeEnum.Wednesday);
		daytypes.put("Thursday", DayTypeEnum.Thursday);
		daytypes.put("Friday", DayTypeEnum.Friday);
		daytypes.put("Saturday", DayTypeEnum.Saturday);
		daytypes.put("Sunday", DayTypeEnum.Sunday);

		ParserFactory.register(DayTypeParser.class.getName(), new ParserFactory() {
			private DayTypeParser instance = new DayTypeParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
