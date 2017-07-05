package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.validator.NoticeValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.util.Referential;

@Log4j
public class NoticeParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);

		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		String id = xpp.getAttributeValue(null, ID);
		Long version = (Long) context.get(VERSION);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);
		String text = null;
		String publicCode = null;
		String typeOfNoticeRef = null;

		NoticeValidator validator = (NoticeValidator) ValidatorFactory.getValidator(context, NoticeValidator.class);
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(TYPE_OF_NOTICE_REF)) {
				typeOfNoticeRef = xpp.getAttributeValue(null, REF);
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(TEXT)) {
				text = xpp.nextText();
			} else if (xpp.getName().equals(PUBLIC_CODE)) {
				publicCode = xpp.nextText();
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		// log.info("valid " + validType);
		Footnote footnote = factory.getFootnote(id);
		footnote.setObjectVersion(version);
		footnote.setLabel(text);
		footnote.setCode(publicCode);
		Referential referential = (Referential) context.get(REFERENTIAL);
		referential.getSharedFootnotes().put(id, footnote);
		validator.addTypeOfNoticeRef(context, footnote.getObjectId(), typeOfNoticeRef);
		validator.check2NeTExSTIFNotice1(context, footnote, lineNumber, columnNumber);

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
