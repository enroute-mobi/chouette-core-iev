package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.DestinationDisplay;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class DestinationDisplayParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);
		xpp.require(XmlPullParser.START_TAG, null, DESTINATION_DISPLAY);
		String id = xpp.getAttributeValue(null, ID);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);
		DestinationDisplay destinationDisplay = factory.getDestinationDisplay(id);
		String version = (String)context.get(VERSION);
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(FRONT_TEXT)) {
				destinationDisplay.setFrontText(xpp.nextText());
			}else if (xpp.getName().equals(PUBLIC_CODE)){
				destinationDisplay.setPublicCode(xpp.nextText());
			}else{
				XPPUtil.skipSubTree(log, xpp);
			}
		}
	}
	

	static {
		ParserFactory.register(DestinationDisplayParser.class.getName(),
				new ParserFactory() {
					private DestinationDisplayParser instance = new DestinationDisplayParser();

					@Override
					protected Parser create() {
						return instance;
					}
				});
	}

}
