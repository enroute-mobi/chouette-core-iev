package mobi.chouette.exchange.netex_stif.parser;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.model.Period;

@Log4j
public class OperatingPeriodParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		String id = xpp.getAttributeValue(null, ID);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);
		Period period = factory.getOperatingPeriod(id);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			// log.info("OperatingPeriodParser tag : " + xpp.getName());
			if (xpp.getName().equals(FROM_DATE)) {
				Date date = sdf.parse(xpp.nextText());
				period.setStartDate(new java.sql.Date(date.getTime()));
			} else if (xpp.getName().equals(TO_DATE)) {
				Date date = sdf.parse(xpp.nextText());
				period.setEndDate(new java.sql.Date(date.getTime()));
			}
		}

	}
	
	static {

			ParserFactory.register(OperatingPeriodParser.class.getName(), new ParserFactory() {
			private OperatingPeriodParser instance = new OperatingPeriodParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
