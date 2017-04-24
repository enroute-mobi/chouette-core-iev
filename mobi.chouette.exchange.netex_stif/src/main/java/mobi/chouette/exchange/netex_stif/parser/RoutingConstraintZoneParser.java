package mobi.chouette.exchange.netex_stif.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.model.Route;
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
		Map<Route, List<StopPoint>> stopPoints = new HashMap<Route, List<StopPoint>>();

		String id = xpp.getAttributeValue(null, ID);
		String name = null;
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NAME)) {
				name = xpp.getText();
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(MEMBERS)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(SCHEDULED_STOP_POINT_REF)) {
						String ref = xpp.getAttributeValue(null, REF);
						List<StopPoint> list = factory.getStopPoints(ref);
						if (list != null) {
							for (StopPoint stopPoint : list) {
								// routingList.add(stopPoint);
								Route route = stopPoint.getRoute();
								List<StopPoint> savedList = stopPoints.get(route);
								if (savedList == null) {
									savedList = new ArrayList<StopPoint>();
								}
								if (!savedList.contains(stopPoint)) {
									savedList.add(stopPoint);
								}
								stopPoints.put(route, savedList);
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
		if (name != null && id != null) {

			for (Route route : stopPoints.keySet()) {
				List<StopPoint> list = stopPoints.get(route);
				// we need to stop points to have an itl
				if (list != null && list.size() >= 2) {
					String realId = NetexStifUtils.genRoutingConstrainId(id, route);
					RoutingConstraint routingConstraint = ObjectFactory.getRoutingConstraint(referential, realId);
					routingConstraint.setName(name);
					routingConstraint.setRoute(route);
					routingConstraint.getStopPoints().addAll(list);
				}
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
