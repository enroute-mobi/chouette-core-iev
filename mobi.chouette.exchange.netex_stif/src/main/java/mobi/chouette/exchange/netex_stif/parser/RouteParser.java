package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.Direction;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.model.Line;
import mobi.chouette.model.Route;
import mobi.chouette.model.type.PTDirectionEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class RouteParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {

		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);
		Integer version = (Integer) context.get(VERSION);
		xpp.require(XmlPullParser.START_TAG, null, ROUTE);
				
		String id = xpp.getAttributeValue(null, ID);
		Route route = ObjectFactory.getRoute(referential, id);
		route.setObjectVersion(version);
				
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			log.info("RouteParser  tag: " + xpp.getName());
			if (xpp.getName().equals(NAME)) {
				route.setName(xpp.nextText());
			} else if (xpp.getName().equals(LINE_REF)) {
				String tmp = xpp.getAttributeValue(null, REF);
				Line line = ObjectFactory.getLine(referential, tmp);
				route.setLine(line);
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(DIRECTION_TYPE)) {
				
				String tmpDirType = xpp.nextText();
				if (tmpDirType.equals(DIRECTION_INBOUND)) {
					route.setDirection(PTDirectionEnum.R);
				} else if (tmpDirType.equals(DIRECTION_OUTBOUND)) {
					route.setDirection(PTDirectionEnum.A);
				} else {
					// todo error
				}
			} else if (xpp.getName().equals(DIRECTION_REF)) {
				NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);
				String tmp = xpp.getAttributeValue(null, REF);
				Direction direction = factory.getDirection(tmp);
				route.setPublishedName(direction.getName());
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(INVERSE_ROUTE_REF)) {
				String tmp = xpp.getAttributeValue(null, REF);
				Route wayBackRoute = ObjectFactory.getRoute(referential, tmp);
				if (wayBackRoute != null) {
					wayBackRoute.setOppositeRoute(route);
				}
				XPPUtil.skipSubTree(log, xpp);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		log.info("end of route");
		route.setFilled(true);
	}

	static {
		ParserFactory.register(RouteParser.class.getName(), new ParserFactory() {
			private RouteParser instance = new RouteParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
