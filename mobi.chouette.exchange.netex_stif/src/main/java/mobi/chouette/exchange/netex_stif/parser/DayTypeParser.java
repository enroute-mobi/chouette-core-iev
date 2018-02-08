package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.validator.DayTypeValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.type.DayTypeEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class DayTypeParser implements Parser {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(Constant.PARSER);
		Referential referential = (Referential) context.get(Constant.REFERENTIAL);
		xpp.require(XmlPullParser.START_TAG, null, NetexStifConstant.DAY_TYPE);
		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		DayTypeValidator validator = (DayTypeValidator) ValidatorFactory.getValidator(context, DayTypeValidator.class);
		String id = xpp.getAttributeValue(null, NetexStifConstant.ID);
		Timetable timeTable = ObjectFactory.getTimetable(referential, id);
		validator.checkNetexId(context, NetexStifConstant.DAY_TYPE, id, lineNumber, columnNumber);
		Long version = (Long) context.get(NetexStifConstant.VERSION);
		timeTable.setObjectVersion(version);
		String changed = xpp.getAttributeValue(null, NetexStifConstant.CHANGED);
		if (changed != null) {
			timeTable.setCreationTime(NetexStifUtils.getDate(changed));
		}
		String modification = xpp.getAttributeValue(null, NetexStifConstant.MODIFICATION);
		validator.addModification(context, id, modification);

		// for post import checkPoints
		validator.addLocation(context, timeTable, lineNumber, columnNumber);

		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			// log.info("DayTypeParser tag : "+ xpp.getName());
			if (xpp.getName().equals(NetexStifConstant.NAME)) {
				timeTable.setComment(xpp.nextText());
			} else if (xpp.getName().equals(NetexStifConstant.PROPERTIES)) {
				parseProperties(xpp, context, timeTable);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
	}

	private void parseProperties(XmlPullParser xpp, Context context, Timetable timetable) throws Exception {
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NetexStifConstant.PROPERTY_OF_DAY)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(NetexStifConstant.DAYS_OF_WEEK)) {
						String day = xpp.nextText();
						try {
							timetable.addDayType(DayTypeEnum.valueOf(day));
						} catch (Exception ex) {
							log.warn("unmanaged daytype " + day);
						}

					}
				}
			}
		}
	}

	static {
		ParserFactory.register(DayTypeParser.class.getName(), new ParserFactory() {
			private DayTypeParser instance = new DayTypeParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
