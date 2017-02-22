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
public class PassengerStopAssignementParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);

		ScheduledStopPoint scheduledStopPoint = null;
		String quayRef = null;
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(SCHEDULED_STOP_POINT_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				scheduledStopPoint = factory.getScheduledStopPoint(ref);
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(QUAY_REF)) {
				quayRef = xpp.nextText();
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		if (quayRef != null && scheduledStopPoint != null) {
			// stopPoint.setStopArea(quayRef);
			scheduledStopPoint.setStopArea(quayRef);
		}

	}

	static {
		ParserFactory.register(PassengerStopAssignementParser.class.getName(), new ParserFactory() {
			private PassengerStopAssignementParser instance = new PassengerStopAssignementParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
