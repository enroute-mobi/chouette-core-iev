package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;

@Log4j
public class NetexHoraireParser implements Parser, Constant {

	// level members

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(SERVICE_JOURNEYS)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(SERVICE_JOURNEY)) {
						Parser parser = ParserFactory.create(ServiceJourneyParser.class.getName());
						parser.parse(context);
					} else {
						XPPUtil.skipSubTree(log, xpp);
					}
				}
			}else if (xpp.getName().equals(SERVICE_JOURNEY)){
				Parser parser = ParserFactory.create(ServiceJourneyParser.class.getName());
				parser.parse(context);
			}
			else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
	}
	

	static{
		ParserFactory.register(NetexHoraireParser.class.getName(), new ParserFactory() {
			private NetexHoraireParser instance = new NetexHoraireParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
