package mobi.chouette.exchange.netex_stif.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.model.RoutingConstraintZone;
import mobi.chouette.exchange.netex_stif.validator.RoutingConstraintZoneValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;
import mobi.chouette.model.LineLite;
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
		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		Referential referential = (Referential) context.get(REFERENTIAL);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);
		Map<Route, List<StopPoint>> stopPoints = new HashMap<Route, List<StopPoint>>();
		RoutingConstraintZoneValidator validator = (RoutingConstraintZoneValidator) ValidatorFactory.getValidator(context, RoutingConstraintZoneValidator.class);

		String id = xpp.getAttributeValue(null, ID);
		RoutingConstraintZone zone = factory.getRoutingConstraintZone(id);
		String changed = xpp.getAttributeValue(null, CHANGED);
		if (changed != null) {
			zone.setCreationTime(NetexStifUtils.getDate(changed));
		}
		String modification = xpp.getAttributeValue(null, MODIFICATION);
		LineLite line = (LineLite) context.get(LINE);
		if (line != null)
			NetexStifUtils.uniqueObjectIdOnLine(zone, line);
		
		String name = null;
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NAME)) {
				name = xpp.nextText();
				zone.setName(name);
			} else if (xpp.getName().equals(ZONE_USE)) {
				String zoneUse = xpp.nextText();
				zone.setZoneUse(zoneUse);
			} else if (xpp.getName().equals(MEMBERS)) {
				parseScheduledStopPoints(context, xpp, factory, stopPoints, zone, validator);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		// log.info("name : "  + name + " id " + id + " valid " + valid);
		validator.addModification(context, zone.getObjectId(), modification);
		boolean valid = validator.validate(context, zone, lineNumber, columnNumber);
		if (name != null && id != null && valid) {

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

	private void parseScheduledStopPoints(Context context, XmlPullParser xpp, NetexStifObjectFactory factory,
			Map<Route, List<StopPoint>> stopPoints, RoutingConstraintZone zone, RoutingConstraintZoneValidator validator) throws XmlPullParserException, IOException {
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(SCHEDULED_STOP_POINT_REF)) {
				int columnNumber = xpp.getColumnNumber();
				int lineNumber = xpp.getLineNumber();
				String ref = xpp.getAttributeValue(null, REF);
				String attr_version = xpp.getAttributeValue(null, VERSION);
				String content = xpp.nextText();
				// check internal reference
				boolean checked = validator.checkNetexRef(context, zone, SCHEDULED_STOP_POINT_REF, ref, lineNumber,
						columnNumber);
				if (checked)
					checked = validator.checkInternalRef(context, zone, SCHEDULED_STOP_POINT_REF, ref,
							attr_version, content, lineNumber, columnNumber);
				zone.getStopPointsRef().add(ref);
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
