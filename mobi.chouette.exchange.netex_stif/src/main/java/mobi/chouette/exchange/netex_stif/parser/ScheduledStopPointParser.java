package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.model.ScheduledStopPoint;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class ScheduledStopPointParser implements Parser, Constant {

	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);

		String id = xpp.getAttributeValue(null, ID);
		// save ScheduledStopPoint to use it latter, call get to create the
		// point if not exists
		factory.getScheduledStopPoint(id);
		// scheduledStopPoint.setOrder(order);
		XPPUtil.skipSubTree(log, xpp);
	}

	static {
		ParserFactory.register(ScheduledStopPointParser.class.getName(), new ParserFactory() {
			private ScheduledStopPointParser instance = new ScheduledStopPointParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}
}
