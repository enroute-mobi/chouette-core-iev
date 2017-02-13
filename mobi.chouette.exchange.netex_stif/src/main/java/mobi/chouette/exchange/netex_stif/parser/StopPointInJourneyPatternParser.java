package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

public class StopPointInJourneyPatternParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);
		//ObjectFactory.getStopPoint(referential, objectId)
	}

}
