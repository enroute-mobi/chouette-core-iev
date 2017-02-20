package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class DayTypeAssignementParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential  referential = (Referential) context.get(REFERENTIAL);

		Timetable timetable = null;
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(OPERATING_PERIOD_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(DAY_TYPE_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				timetable = ObjectFactory.getTimetable(referential, ref);
				XPPUtil.skipSubTree(log, xpp);
			}else if (xpp.getName().equals(IS_AVAILABLE)) {
				String isAvailable = xpp.nextText();
			}
			else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		
	}

	static {

		ParserFactory.register(DayTypeAssignementParser.class.getName(), new ParserFactory() {
			private DayTypeAssignementParser instance = new DayTypeAssignementParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}
}
