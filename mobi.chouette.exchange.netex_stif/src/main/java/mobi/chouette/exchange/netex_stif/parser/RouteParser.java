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
import mobi.chouette.exchange.netex_stif.validator.RouteValidator;
import mobi.chouette.model.LineLite;
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
		Long version = (Long) context.get(VERSION);
		xpp.require(XmlPullParser.START_TAG, null, ROUTE);

		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();

		String id = xpp.getAttributeValue(null, ID);
		
		RouteValidator routeRalidator = new RouteValidator();
		routeRalidator.checkNetexId(context, ROUTE, id, lineNumber, columnNumber);
		
		Route route = ObjectFactory.getRoute(referential, id);
		route.setObjectVersion(version);

		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			// log.info("RouteParser tag: " + xpp.getName());
			if (xpp.getName().equals(NAME)) {
				route.setName(xpp.nextText());
			} else if (xpp.getName().equals(LINE_REF)) {
				String tmp = xpp.getAttributeValue(null, REF);
				LineLite line = referential.getSharedReadOnlyLines().get(tmp);
				if (line != null) {
					route.setLineId(line.getId());
					NetexStifUtils.uniqueObjectIdOnLine(route, line);
					context.put(LINE, line);
				}
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(DIRECTION_TYPE)) {

				String tmpDirType = xpp.nextText();
				if (tmpDirType.equals(DIRECTION_INBOUND)) {
					route.setDirection(PTDirectionEnum.R);
					route.setWayBack(tmpDirType);
				} else if (tmpDirType.equals(DIRECTION_OUTBOUND)) {
					route.setDirection(PTDirectionEnum.A);
					route.setWayBack(tmpDirType);
				} else {
					// todo error
				}
			} else if (xpp.getName().equals(DIRECTION_REF)) {
				NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);
				String tmp = xpp.getAttributeValue(null, REF);
				Direction direction = factory.getDirection(tmp);
				if (direction.isDetached()) {
					route.setPublishedName(direction.getName());
				} else {
					factory.addRouteDirection(id, tmp);
				}
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
