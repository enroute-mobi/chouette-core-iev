package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class StopPointInJourneyPatternParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);

		Integer version = (Integer) context.get(VERSION);

		String id = xpp.getAttributeValue(null, ID);
		String order = xpp.getAttributeValue(null, ORDER);
		String objectId = id + ":" + order;
		StopPoint stopPoint = ObjectFactory.getStopPoint(referential, objectId);
		stopPoint.setObjectVersion(version);
		XPPUtil.skipSubTree(log, xpp);

	}
	
	static {
		ParserFactory.register(StopPointInJourneyPatternParser.class.getName(), new ParserFactory() {
			private StopPointInJourneyPatternParser instance = new StopPointInJourneyPatternParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}
	

}
