package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.model.DestinationDisplay;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.validator.ServiceJourneyPatternValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.type.AlightingPossibilityEnum;
import mobi.chouette.model.type.BoardingPossibilityEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class ServiceJourneyPatternParser implements Parser {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(Constant.PARSER);
		Referential referential = (Referential) context.get(Constant.REFERENTIAL);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NetexStifConstant.NETEX_STIF_OBJECT_FACTORY);
		xpp.require(XmlPullParser.START_TAG, null, NetexStifConstant.SERVICE_JOURNEY_PATTERN);
		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		ServiceJourneyPatternValidator validator = (ServiceJourneyPatternValidator) ValidatorFactory
				.getValidator(context, ServiceJourneyPatternValidator.class);
		String id = xpp.getAttributeValue(null, NetexStifConstant.ID);
		JourneyPattern journeyPattern = ObjectFactory.getJourneyPattern(referential, id);
		String changed = xpp.getAttributeValue(null, NetexStifConstant.CHANGED);
		if (changed != null) {
			journeyPattern.setCreationTime(NetexStifUtils.getDate(changed));
		}
		String modification = xpp.getAttributeValue(null, NetexStifConstant.MODIFICATION);
		Long version = (Long) context.get(NetexStifConstant.VERSION);
		// LineLite line = (LineLite) context.get(Constant.LINE);
		// if (line != null)
		// NetexStifUtils.uniqueObjectIdOnLine(context,journeyPattern, line);

		journeyPattern.setObjectVersion(version);

		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NetexStifConstant.NAME)) {
				journeyPattern.setName(xpp.nextText());
			} else if (xpp.getName().equals(NetexStifConstant.DESTINATION_DISPLAY_REF)) {
				String ref = xpp.getAttributeValue(null, NetexStifConstant.REF);
				String attrVersion = xpp.getAttributeValue(null, NetexStifConstant.VERSION);
				String content = xpp.nextText();
				// check external reference
				boolean checked = validator.checkNetexRef(context, journeyPattern, NetexStifConstant.DESTINATION_DISPLAY_REF, ref,
						lineNumber, columnNumber);
				if (checked)
					validator.checkInternalRef(context, journeyPattern, NetexStifConstant.DESTINATION_DISPLAY_REF, ref,
							attrVersion, content, lineNumber, columnNumber);
				DestinationDisplay display = factory.getDestinationDisplay(ref);
				if (display.isFilled()) {
					journeyPattern.setPublishedName(display.getFrontText());
					journeyPattern.setRegistrationNumber(display.getPublicCode());
				} else {
					factory.addJourneyPatternDestination(id, ref);
				}
			} else if (xpp.getName().equals(NetexStifConstant.POINTS_IN_SEQUENCE)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(NetexStifConstant.STOP_POINT_IN_JOURNEY_PATTERN)) {

						parseStopPointInJourneyPattern(context, journeyPattern);
					} else {
						XPPUtil.skipSubTree(log, xpp);
					}
				}
			} else if (xpp.getName().equals(NetexStifConstant.SERVICE_JOURNEY_PATTERN_TYPE)) {
				validator.addPatternType(context, journeyPattern.getObjectId(), xpp.nextText());
				// journeyPattern.setPatternType(xpp.nextText());
			} else if (xpp.getName().equals(NetexStifConstant.ROUTE_REF)) {
				String ref = xpp.getAttributeValue(null, NetexStifConstant.REF);
				String attrVersion = xpp.getAttributeValue(null, NetexStifConstant.VERSION);
				String content = xpp.nextText();
				// check external reference
				boolean checked = validator.checkNetexRef(context, journeyPattern, NetexStifConstant.ROUTE_REF, ref, lineNumber,
						columnNumber);
				if (checked)
					checked = validator.checkInternalRef(context, journeyPattern, NetexStifConstant.ROUTE_REF, ref, attrVersion, content,
							lineNumber, columnNumber);
				Route route = ObjectFactory.getRoute(referential, ref);
				journeyPattern.setRoute(route);
				context.put(NetexStifConstant.ROUTE_FROM_SERVICE_JOURNEY_PATTERN, route);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		validator.addModification(context, journeyPattern.getObjectId(), modification);
		validator.validate(context, journeyPattern, lineNumber, columnNumber);
		if (context.contains(NetexStifConstant.ROUTE_FROM_SERVICE_JOURNEY_PATTERN)) {
			context.remove(NetexStifConstant.ROUTE_FROM_SERVICE_JOURNEY_PATTERN);
		}
	}

	public void parseStopPointInJourneyPattern(Context context, JourneyPattern journeyPattern) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(Constant.PARSER);
		Referential referential = (Referential) context.get(Constant.REFERENTIAL);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NetexStifConstant.NETEX_STIF_OBJECT_FACTORY);
		ServiceJourneyPatternValidator validator = (ServiceJourneyPatternValidator) ValidatorFactory
				.getValidator(context, ServiceJourneyPatternValidator.class);
		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		Long version = (Long) context.get(NetexStifConstant.VERSION);
		String scheduledStopPointId = null;
		String order = xpp.getAttributeValue(null, NetexStifConstant.ORDER);
		Boolean forAlighting = null;
		Boolean forBoarding = null;
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NetexStifConstant.FOR_ALIGHTING)) {
				forAlighting = Boolean.parseBoolean(xpp.nextText());
			} else if (xpp.getName().equals(NetexStifConstant.FOR_BOARDING)) {
				forBoarding = Boolean.parseBoolean(xpp.nextText());

			} else if (xpp.getName().equals(NetexStifConstant.SCHEDULED_STOP_POINT_REF)) {
				scheduledStopPointId = xpp.getAttributeValue(null, NetexStifConstant.REF);
				String attrVersion = xpp.getAttributeValue(null, NetexStifConstant.VERSION);
				String content = xpp.nextText();
				// check external reference
				boolean checked = validator.checkNetexRef(context, journeyPattern, NetexStifConstant.SCHEDULED_STOP_POINT_REF,
						scheduledStopPointId, lineNumber, columnNumber);
				if (checked)
					validator.checkInternalRef(context, journeyPattern, NetexStifConstant.SCHEDULED_STOP_POINT_REF,
							scheduledStopPointId, attrVersion, content, lineNumber, columnNumber);

			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		if (scheduledStopPointId != null) {
			Route route = (Route) context.get(NetexStifConstant.ROUTE_FROM_SERVICE_JOURNEY_PATTERN);
			StopPoint stopPoint = findStopPoint(route, Integer.parseInt(order));
			String objectId = NetexStifUtils.genStopPointId(scheduledStopPointId, order, route);
			if (stopPoint == null) {
				stopPoint = ObjectFactory.getStopPoint(referential, objectId);
				stopPoint.setObjectVersion(version);
				stopPoint.setRoute(route);
				stopPoint.setPosition(Integer.parseInt(order));
			}
			// log.info("set position : " + order);
			if (forAlighting != null) {
				validator.addStopPointAlighting(context, journeyPattern.getObjectId(), stopPoint.getPosition(),
						forAlighting);
				stopPoint.setForAlighting(
						forAlighting ? AlightingPossibilityEnum.normal : AlightingPossibilityEnum.forbidden);
			} else {
				validator.addStopPointAlighting(context, journeyPattern.getObjectId(), stopPoint.getPosition(),
						Boolean.TRUE);
			}

			if (forBoarding != null) {
				validator.addStopPointBoarding(context, journeyPattern.getObjectId(), stopPoint.getPosition(),
						forBoarding);
				stopPoint.setForBoarding(
						forBoarding ? BoardingPossibilityEnum.normal : BoardingPossibilityEnum.forbidden);
			} else {
				validator.addStopPointBoarding(context, journeyPattern.getObjectId(), stopPoint.getPosition(),
						Boolean.TRUE);
			}
			factory.addStopPoint(scheduledStopPointId, stopPoint);
			journeyPattern.addStopPoint(stopPoint);
			validator.addStopPointOrder(context, journeyPattern.getObjectId(), stopPoint.getPosition(), objectId);
		}
	}

	private StopPoint findStopPoint(Route route, Integer pos) {
        for (StopPoint sp : route.getStopPoints())
        {
        	if (sp.getPosition().equals(pos)) return sp;
        }
		return null;
	}

	static {
		ParserFactory.register(ServiceJourneyPatternParser.class.getName(), new ParserFactory() {
			private ServiceJourneyPatternParser instance = new ServiceJourneyPatternParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
