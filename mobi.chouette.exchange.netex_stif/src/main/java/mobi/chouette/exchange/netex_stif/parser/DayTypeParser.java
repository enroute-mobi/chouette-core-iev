package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.model.Route;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

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
			}else if (xpp.getName().equals(PROPERTY_OF_DAY)){
				xpp.require(XmlPullParser.START_TAG, null, PROPERTY_OF_DAY);
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					// TODO
				}
			}
			else {
				
			}
		}
	}

}
