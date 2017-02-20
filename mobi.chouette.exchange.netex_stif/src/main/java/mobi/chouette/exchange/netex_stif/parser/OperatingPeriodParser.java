package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.model.util.Referential;

@Log4j
public class OperatingPeriodParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);

		String from;
		String to;
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(FROM_DATE)) {
					from = xpp.nextText();	
			}else if (xpp.getName().equals(TO_DATE)) {
				to = xpp.nextText();
			}
		}
	}

}
