package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.model.Line;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class PublicationDeliveryParser implements Parser, Constant {

	

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);
		String version = xpp.getAttributeValue(null, VERSION);
		// TODO que fait on de version 
		
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(DATA_OBJECTS)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(COMPOSITE_FRAME)) {
						Parser compositeFrameParser = ParserFactory.create(CompositeFrameParser.class.getName());
						compositeFrameParser.parse(context);
					}
				}
			} else if (xpp.getName().equals(PUBLICATION_TIMESTAMP)) {

			} else if (xpp.getName().equals(PARTICIPANT_REF)) {

			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
	}

}
