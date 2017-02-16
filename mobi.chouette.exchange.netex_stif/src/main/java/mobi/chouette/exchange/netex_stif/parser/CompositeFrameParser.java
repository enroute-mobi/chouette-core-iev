package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;

@Log4j
public class CompositeFrameParser implements Constant, Parser {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		xpp.require(XmlPullParser.START_TAG, null, COMPOSITE_FRAME);

		boolean isTypeValid = false;
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(TYPE_OF_FRAME_REF)) {
				String val = xpp.getAttributeValue(null, REF);
				if (val.equals(NETEX_OFFRE_LIGNE)) {
					isTypeValid = true;
				}
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(FRAMES) && isTypeValid) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(GENERAL_FRAME)) {
						Parser parser = ParserFactory.create(GeneralFrameParser.class.getName());
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
		ParserFactory.register(CompositeFrameParser.class.getName(), new ParserFactory() {
			private CompositeFrameParser instance = new CompositeFrameParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
