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
import mobi.chouette.model.RoutingConstraint;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class RoutingConstraintZoneParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);

		String id = xpp.getAttributeValue(null, ID);
		RoutingConstraint routingConstraint = ObjectFactory.getRoutingConstraint(referential, id);
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NAME)) {
				String name = xpp.getText();
				routingConstraint.setName(name);
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(MEMBERS)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(SCHEDULED_STOP_POINT_REF)) {
						String ref = xpp.getAttributeValue(null, REF);
						List<StopPoint> list = factory.getStopPoints(ref);
						if (list != null) {
							List<StopPoint> routingList = routingConstraint.getStopPoints();
							for (StopPoint stopPoint : list) {
								if (!routingList.contains(stopPoint)) {
									routingList.add(stopPoint);
								}
							}
						}
						XPPUtil.skipSubTree(log, xpp);
					} else {
						XPPUtil.skipSubTree(log, xpp);
					}
				}
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}

	}

	static {
		ParserFactory.register(RoutingConstraintZoneParser.class.getName(), new ParserFactory() {
			private RoutingConstraintZoneParser instance = new RoutingConstraintZoneParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
