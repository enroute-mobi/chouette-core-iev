package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.util.Referential;

@Log4j
public class NoticeParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);

		String id = xpp.getAttributeValue(null, ID);
		Long version = (Long) context.get(VERSION);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);
		String text = null;
		String publicCode = null;
		boolean validType = false;
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(TYPE_OF_NOTICE_REF)) {
				String type = xpp.nextText();
				if (type.equals(SERVICE_JOURNEY_NOTICE)) {
					validType = true;
				}
			} else if (xpp.getName().equals(TEXT)) {
				text = xpp.nextText();
			} else if (xpp.getName().equals(PUBLIC_CODE)) {
				publicCode = xpp.nextText();
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		if (validType) {
			Footnote footnote = factory.getFootnote(id);
			footnote.setObjectVersion(version);
			footnote.setLabel(text);
			footnote.setCode(publicCode);
			Referential referential = (Referential) context.get(REFERENTIAL);
			referential.getFootnotes().put(id, footnote);
		}
	}

	static {
		ParserFactory.register(NoticeParser.class.getName(), new ParserFactory() {
			private NoticeParser instance = new NoticeParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}
}
