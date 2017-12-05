package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.model.DestinationDisplay;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;

@Log4j
public class DestinationDisplayParser implements Parser {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(Constant.PARSER);
		xpp.require(XmlPullParser.START_TAG, null, NetexStifConstant.DESTINATION_DISPLAY);
		String id = xpp.getAttributeValue(null, NetexStifConstant.ID);
		// to be checked?
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NetexStifConstant.NETEX_STIF_OBJECT_FACTORY);
		DestinationDisplay destinationDisplay = factory.getDestinationDisplay(id);
		Long version = (Long)context.get(NetexStifConstant.VERSION);
		destinationDisplay.setObjectVersion(version);
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NetexStifConstant.FRONT_TEXT)) {
				destinationDisplay.setFrontText(xpp.nextText());
			}else if (xpp.getName().equals(NetexStifConstant.PUBLIC_CODE)){
				destinationDisplay.setPublicCode(xpp.nextText());
			}else{
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		destinationDisplay.setFilled(true);
		
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
