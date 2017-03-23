package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

public class StopPointParserOld implements Parser, Constant{

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);
		xpp.require(XmlPullParser.START_TAG, null, STOP_POINT);
		String id = xpp.getAttributeValue(null, ID);
		StopPoint stopPoint = ObjectFactory.getStopPoint(referential, id);
		Long version = (Long)context.get(VERSION);
		stopPoint.setObjectVersion(version);
		
	}
	
	static {
		ParserFactory.register(StopPointParserOld.class.getName(), new ParserFactory() {
			private StopPointParserOld instance = new StopPointParserOld();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
