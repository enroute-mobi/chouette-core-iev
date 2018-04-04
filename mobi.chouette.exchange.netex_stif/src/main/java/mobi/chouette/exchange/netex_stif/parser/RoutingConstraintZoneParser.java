package mobi.chouette.exchange.netex_stif.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.model.RoutingConstraintZone;
import mobi.chouette.exchange.netex_stif.validator.RoutingConstraintZoneValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;
import mobi.chouette.model.Route;
import mobi.chouette.model.RoutingConstraint;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class RoutingConstraintZoneParser implements Parser {


	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(Constant.PARSER);
		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		Referential referential = (Referential) context.get(Constant.REFERENTIAL);
		Long version = (Long) context.get(NetexStifConstant.VERSION);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NetexStifConstant.NETEX_STIF_OBJECT_FACTORY);
		Map<Route, List<StopPoint>> stopPoints = new HashMap<Route, List<StopPoint>>();
		RoutingConstraintZoneValidator validator = (RoutingConstraintZoneValidator) ValidatorFactory.getValidator(context, RoutingConstraintZoneValidator.class);

		String id = xpp.getAttributeValue(null, NetexStifConstant.ID);
		RoutingConstraintZone zone = factory.getRoutingConstraintZone(id);
		String changed = xpp.getAttributeValue(null, NetexStifConstant.CHANGED);
		if (changed != null) {
			zone.setCreationTime(NetexStifUtils.getDate(changed));
		}
		String modification = xpp.getAttributeValue(null, NetexStifConstant.MODIFICATION);
//		LineLite line = (LineLite) context.get(Constant.LINE);
//		if (line != null)
//			NetexStifUtils.uniqueObjectIdOnLine(context,zone, line);
		
		String name = null;
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NetexStifConstant.NAME)) {
				name = xpp.nextText();
				zone.setName(name);
			} else if (xpp.getName().equals(NetexStifConstant.ZONE_USE)) {
				String zoneUse = xpp.nextText();
				zone.setZoneUse(zoneUse);
			} else if (xpp.getName().equals(NetexStifConstant.MEMBERS)) {
				parseScheduledStopPoints(context, xpp, factory, stopPoints, zone, validator);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		// log.info("name : "  + name + " id " + id + " valid " + valid);
		validator.addModification(context, zone.getObjectId(), modification);
		boolean valid = validator.validate(context, zone, lineNumber, columnNumber);
		if (name != null && id != null && valid) {

			String dataSourceRef = (String) context.get(NetexStifConstant.IMPORT_DATA_SOURCE_REF);
			for (Route route : stopPoints.keySet()) {
				List<StopPoint> list = stopPoints.get(route);
				// we need two stop points to have an itl
				if (list != null && list.size() >= 2) {
					String realId = NetexStifUtils.genRoutingConstrainId(id, route);
					RoutingConstraint routingConstraint = ObjectFactory.getRoutingConstraint(referential, realId);
					routingConstraint.setObjectVersion(version);
					routingConstraint.setName(name);
					routingConstraint.setRoute(route);
					routingConstraint.getStopPoints().addAll(list);
					routingConstraint.setDataSourceRef(dataSourceRef);
				}
			}

		}
	}

	private void parseScheduledStopPoints(Context context, XmlPullParser xpp, NetexStifObjectFactory factory,
			Map<Route, List<StopPoint>> stopPoints, RoutingConstraintZone zone, RoutingConstraintZoneValidator validator) throws XmlPullParserException, IOException {
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NetexStifConstant.SCHEDULED_STOP_POINT_REF)) {
				int columnNumber = xpp.getColumnNumber();
				int lineNumber = xpp.getLineNumber();
				String ref = xpp.getAttributeValue(null, NetexStifConstant.REF);
				String attr_version = xpp.getAttributeValue(null, NetexStifConstant.VERSION);
				String content = xpp.nextText();
				// check internal reference
				boolean checked = validator.checkNetexRef(context, zone, NetexStifConstant.SCHEDULED_STOP_POINT_REF, ref, lineNumber,
						columnNumber);
				if (checked)
					checked = validator.checkInternalRef(context, zone, NetexStifConstant.SCHEDULED_STOP_POINT_REF, ref,
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
