package mobi.chouette.exchange.netex_stif.parser;

import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.model.StopArea;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class PassengerStopAssignementParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);
		Referential referential = (Referential) context.get(REFERENTIAL);

		String quayRef = null;
		String id = null;
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			log.info("PassengerStopAssignementParser : " + xpp.getName());
			if (xpp.getName().equals(SCHEDULED_STOP_POINT_REF)) {
				id = xpp.getAttributeValue(null, REF);
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(QUAY_REF)) {
				quayRef = xpp.getAttributeValue(null, REF);
				XPPUtil.skipSubTree(log, xpp);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		if (quayRef != null && id != null) {
			List<StopPoint> list = factory.getStopPoints(id);
			log.info("id :" + id + " list : " + list);
			if (list != null) {
				StopArea stopArea = ObjectFactory.getStopArea(referential, quayRef);
				for (StopPoint stopPoint : list) {
					log.info("stop point" + stopPoint);
					stopPoint.setContainedInStopArea(stopArea);
				}
			}
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
