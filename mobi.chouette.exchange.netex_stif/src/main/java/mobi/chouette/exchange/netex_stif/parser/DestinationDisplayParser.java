package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

public class DestinationDisplayParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);
		xpp.require(XmlPullParser.START_TAG, null, DAY_TYPE);
		String id = xpp.getAttributeValue(null, ID);
	
	}

}
