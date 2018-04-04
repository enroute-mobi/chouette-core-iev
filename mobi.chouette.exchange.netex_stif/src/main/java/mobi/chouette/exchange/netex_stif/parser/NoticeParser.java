package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.validator.NoticeValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.util.Referential;

@Log4j
public class NoticeParser implements Parser {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(Constant.PARSER);

		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		String id = xpp.getAttributeValue(null, NetexStifConstant.ID);
		Long version = (Long) context.get(NetexStifConstant.VERSION);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NetexStifConstant.NETEX_STIF_OBJECT_FACTORY);
		String text = null;
		String publicCode = null;
		String typeOfNoticeRef = null;

		NoticeValidator validator = (NoticeValidator) ValidatorFactory.getValidator(context, NoticeValidator.class);
		validator.checkNetexId(context, NetexStifConstant.NOTICE, id, lineNumber, columnNumber);
		String modification = xpp.getAttributeValue(null, NetexStifConstant.MODIFICATION);
		String changed = xpp.getAttributeValue(null, NetexStifConstant.CHANGED);
		
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NetexStifConstant.TYPE_OF_NOTICE_REF)) {
				typeOfNoticeRef = xpp.getAttributeValue(null, NetexStifConstant.REF);
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(NetexStifConstant.TEXT)) {
				text = xpp.nextText();
			} else if (xpp.getName().equals(NetexStifConstant.PUBLIC_CODE)) {
				publicCode = xpp.nextText();
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		// log.info("valid " + validType);
		Footnote footnote = factory.getFootnote(id);
		footnote.setObjectVersion(version);
		String dataSourceRef = (String) context.get(NetexStifConstant.IMPORT_DATA_SOURCE_REF);
		footnote.setDataSourceRef(dataSourceRef);
		if (changed != null) {
			footnote.setCreationTime(NetexStifUtils.getDate(changed));
		}
		validator.addModification(context, id, modification);
		if (text != null) text = text.trim();
		if (publicCode != null) publicCode = publicCode.trim();
		footnote.setLabel(text);
		footnote.setCode(publicCode);
		Referential referential = (Referential) context.get(Constant.REFERENTIAL);
		validator.addTypeOfNoticeRef(context, footnote.getObjectId(), typeOfNoticeRef);
//		boolean result3 = validator.checkModification(context, NOTICE, footnote, lineNumber, columnNumber);
//
//		validator.check2NeTExSTIFNotice1(context, footnote, lineNumber, columnNumber);
//		if (validator.check2NeTExSTIFNotice2(context, footnote, lineNumber, columnNumber) && result3) {
//			// notice accepted
//			referential.getSharedFootnotes().put(id, footnote);
//		}

		boolean result = validator.validate(context, footnote, lineNumber, columnNumber);
		if (result) {
			// notice accepted
			referential.getSharedFootnotes().put(id, footnote);
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
