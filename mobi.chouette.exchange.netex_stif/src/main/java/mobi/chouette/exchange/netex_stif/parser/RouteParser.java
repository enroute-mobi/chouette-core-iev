package mobi.chouette.exchange.netex_stif.parser;

import java.util.Map;
import java.util.Properties;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.util.XmlPullUtil;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.model.Line;
import mobi.chouette.model.Route;
import mobi.chouette.model.type.PTDirectionEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class RouteParser implements Parser, Constant {

	private Map<String, Properties> directions;

	@Override
	public void parse(Context context) throws Exception {

		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);

		xpp.require(XmlPullParser.START_TAG, null, ROUTE);
		String id = xpp.getAttributeValue(null, ID);
		Route route = ObjectFactory.getRoute(referential, id);
		Integer version = Integer.valueOf(xpp.getAttributeValue(null, VERSION));
		route.setObjectVersion(version != null ? version : 0);

		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NAME)) {
				route.setName(xpp.nextText());
			} else if (xpp.getName().equals(LINE_REF)) {
				String tmp = xpp.getAttributeValue(null, ID);
				Line line = ObjectFactory.getLine(referential, tmp);
				route.setLine(line);
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

			} else if (xpp.getName().equals(INVERSE_ROUTE_REF)) {
				String tmp = xpp.getAttributeValue(null, ID);
				Route wayBackRoute = ObjectFactory.getRoute(referential, tmp);
				if (wayBackRoute != null) {
					wayBackRoute.setOppositeRoute(route);
					route.setOppositeRoute(wayBackRoute);
				}
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		route.setFilled(true);
	}

	private void parseRoute(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);

		xpp.require(XmlPullParser.START_TAG, null, "Route");
		context.put(COLUMN_NUMBER, xpp.getColumnNumber());
		context.put(LINE_NUMBER, xpp.getLineNumber());

		String id = xpp.getAttributeValue(null, ID);
		Route route = ObjectFactory.getRoute(referential, id);

		Integer version = Integer.valueOf(xpp.getAttributeValue(null, VERSION));
		route.setObjectVersion(version != null ? version : 0);

		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals("keyList")) {
				parseKeyValues(context, route);
			} else if (xpp.getName().equals("Name")) {
				route.setName(xpp.nextText());
			} else if (xpp.getName().equals("ShortName")) {
				route.setPublishedName(xpp.nextText());
			} else if (xpp.getName().equals("DirectionRef")) {
				String ref = xpp.getAttributeValue(null, REF);
				Properties properties = directions.get(ref);
				if (properties != null) {

					String directionName = properties.getProperty(NAME);
					if (directionName != null) {
						route.setDirection(NetexStifUtils.toPTDirectionType(directionName));
					}
					String directionType = properties.getProperty("DirectionType");
					if (directionType != null) {
						route.setWayBack((directionType.equals("outbound")) ? "A" : "R");
					}
				}
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals("InverseRouteRef")) {
				String ref = xpp.getAttributeValue(null, REF);
				Route wayBackRoute = ObjectFactory.getRoute(referential, ref);
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

	private void parseKeyValues(Context context, Route route) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);

		xpp.require(XmlPullParser.START_TAG, null, "keyList");
		context.put(COLUMN_NUMBER, xpp.getColumnNumber());
		context.put(LINE_NUMBER, xpp.getLineNumber());

		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals("KeyValue")) {
				XmlPullUtil.nextStartTag(xpp, "Key");
				String key = xpp.nextText();
				if (key.equals("Comment")) {
					XmlPullUtil.nextStartTag(xpp, "Value");
					String value = xpp.nextText();
					route.setComment(value);
					XmlPullUtil.nextEndTag(xpp);
				} else if (key.equals("Number")) {
					XmlPullUtil.nextStartTag(xpp, "Value");
					String value = xpp.nextText();
					route.setNumber(value);
					XmlPullUtil.nextEndTag(xpp);
				} else {
					XPPUtil.skipSubTree(log, xpp);
				}
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
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
