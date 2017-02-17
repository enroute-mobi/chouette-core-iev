package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;


// we are at members level
@Log4j
public class NetexCommunParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NOTICES)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					log.info("NetexCommunParser tag : " + xpp.getName());
					if (xpp.getName().equals(NOTICE)) {
						Parser parser = ParserFactory.create(NoticeParser.class.getName());
						parser.parse(context);
					} else {
						XPPUtil.skipSubTree(log, xpp);
					}
				}
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
	}

	static {
		ParserFactory.register(NetexCommunParser.class.getName(), new ParserFactory() {
			private NetexCommunParser instance = new NetexCommunParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}
}
