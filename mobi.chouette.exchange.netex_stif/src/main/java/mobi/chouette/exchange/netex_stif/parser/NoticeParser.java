package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.model.Footnote;

public class NoticeParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		
		xpp.require(XmlPullParser.START_TAG, null, NOTICE);
		String id = xpp.getAttributeValue(null, ID);
		Integer version = Integer.valueOf(xpp.getAttributeValue(null, VERSION));
		NetexStifObjectFactory factory = (NetexStifObjectFactory)context.get(NETEX_STIF_OBJECT_FACTORY);
		// TODO : est ce que l'on récupère comme ça le type ou est ce que c'est une ref, et auquel cas, il faut aussi les typeOfNotice
		String type = xpp.getAttributeValue(null, TYPE_OF_NOTICE_REF);
		if (type.equals(SERVICE_JOURNEY_NOTICE)){
			Footnote footnote = factory.getFootnote(id);
			footnote.setObjectVersion(version != null ? version : 0);
			footnote.setLabel(xpp.getAttributeValue(null, TEXT));
			footnote.setCode(xpp.getAttributeValue(null, PUBLIC_CODE));
		}
	}

}
