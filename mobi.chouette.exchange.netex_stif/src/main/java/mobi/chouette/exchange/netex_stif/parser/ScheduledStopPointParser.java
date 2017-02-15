package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
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
		Referential referential = (Referential) context.get(REFERENTIAL);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);

		Integer version = (Integer) context.get(VERSION);

		String id = xpp.getAttributeValue(null, ID);
		String order = xpp.getAttributeValue(null, ORDER);
		StopPoint stopPoint = ObjectFactory.getStopPoint(referential, NetexStifParserUtils.genStopPointId(id, order));
		stopPoint.setObjectVersion(version);
		// save ScheduledStopPoint to use it latter, call get to create the
		// point if not exists
		ScheduledStopPoint scheduledStopPoint = factory.getScheduledStopPoint(id);
		scheduledStopPoint.setOrder(order);
		XPPUtil.skipSubTree(log, xpp);
	}

}
