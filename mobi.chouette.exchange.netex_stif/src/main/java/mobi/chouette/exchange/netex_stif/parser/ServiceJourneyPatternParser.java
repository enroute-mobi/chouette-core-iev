package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.DestinationDisplay;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.validator.ServiceJourneyPatternValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.type.AlightingPossibilityEnum;
import mobi.chouette.model.type.BoardingPossibilityEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class ServiceJourneyPatternParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);
		xpp.require(XmlPullParser.START_TAG, null, SERVICE_JOURNEY_PATTERN);
		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		ServiceJourneyPatternValidator validator = (ServiceJourneyPatternValidator) ValidatorFactory
				.getValidator(context, ServiceJourneyPatternValidator.class);
		String id = xpp.getAttributeValue(null, ID);
		JourneyPattern journeyPattern = ObjectFactory.getJourneyPattern(referential, id);
		String changed = xpp.getAttributeValue(null, CHANGED);
		if (changed != null) {
			journeyPattern.setCreationTime(NetexStifUtils.getDate(changed));
		}
		String modification = xpp.getAttributeValue(null, MODIFICATION);
		Long version = (Long) context.get(VERSION);
		LineLite line = (LineLite) context.get(LINE);
		if (line != null)
			NetexStifUtils.uniqueObjectIdOnLine(journeyPattern, line);

		journeyPattern.setObjectVersion(version);

		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NAME)) {
				journeyPattern.setName(xpp.nextText());
			} else if (xpp.getName().equals(DESTINATION_DISPLAY_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				String att_version = xpp.getAttributeValue(null, VERSION);
				String content = xpp.nextText();
				// check external reference
				boolean checked = validator.checkNetexRef(context, journeyPattern, DESTINATION_DISPLAY_REF, ref,
						lineNumber, columnNumber);
				if (checked)
					checked = validator.checkInternalRef(context, journeyPattern, DESTINATION_DISPLAY_REF, ref,
							att_version, content, lineNumber, columnNumber);
				DestinationDisplay display = factory.getDestinationDisplay(ref);
				if (display.isFilled()) {
					journeyPattern.setPublishedName(display.getFrontText());
					journeyPattern.setRegistrationNumber(display.getPublicCode());
				} else {
					factory.addJourneyPatternDestination(id, ref);
				}
			} else if (xpp.getName().equals(POINTS_IN_SEQUENCE)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(STOP_POINT_IN_JOURNEY_PATTERN)) {

						parseStopPointInJourneyPattern(context, journeyPattern);
					} else {
						XPPUtil.skipSubTree(log, xpp);
					}
				}
			} else if (xpp.getName().equals(SERVICE_JOURNEY_PATTERN_TYPE)) {
				validator.addPatternType(context,journeyPattern.getObjectId(),xpp.nextText());
				// journeyPattern.setPatternType(xpp.nextText());
			} else if (xpp.getName().equals(ROUTE_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				String att_version = xpp.getAttributeValue(null, VERSION);
				String content = xpp.nextText();
				// check external reference
				boolean checked = validator.checkNetexRef(context, journeyPattern, ROUTE_REF, ref, lineNumber,
						columnNumber);
				if (checked)
					checked = validator.checkInternalRef(context, journeyPattern, ROUTE_REF, ref, att_version, content,
							lineNumber, columnNumber);
				Route route = ObjectFactory.getRoute(referential, ref);
				journeyPattern.setRoute(route);
				context.put(ROUTE_FROM_SERVICE_JOURNEY_PATTERN, route);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		validator.addModification(context, journeyPattern.getObjectId(), modification);
		validator.validate(context, journeyPattern, lineNumber, columnNumber);
		if (context.contains(ROUTE_FROM_SERVICE_JOURNEY_PATTERN)) {
			context.remove(ROUTE_FROM_SERVICE_JOURNEY_PATTERN);
		}
	}

	public void parseStopPointInJourneyPattern(Context context, JourneyPattern journeyPattern) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);
		ServiceJourneyPatternValidator validator = (ServiceJourneyPatternValidator) ValidatorFactory
				.getValidator(context, ServiceJourneyPatternValidator.class);
		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		Long version = (Long) context.get(VERSION);
		String scheduledStopPointId = null;
		String order = xpp.getAttributeValue(null, ORDER);
		Boolean forAlighting = null;
		Boolean forBoarding = null;
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(FOR_ALIGHTING)) {
				forAlighting = Boolean.parseBoolean(xpp.nextText());
			} else if (xpp.getName().equals(FOR_BOARDING)) {
				forBoarding = Boolean.parseBoolean(xpp.nextText());

			} else if (xpp.getName().equals(SCHEDULED_STOP_POINT_REF)) {
				scheduledStopPointId = xpp.getAttributeValue(null, REF);
				String att_version = xpp.getAttributeValue(null, VERSION);
				String content = xpp.nextText();
				// check external reference
				boolean checked = validator.checkNetexRef(context, journeyPattern, SCHEDULED_STOP_POINT_REF,
						scheduledStopPointId, lineNumber, columnNumber);
				if (checked)
					checked = validator.checkInternalRef(context, journeyPattern, SCHEDULED_STOP_POINT_REF,
							scheduledStopPointId, att_version, content, lineNumber, columnNumber);

			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		if (scheduledStopPointId != null) {
			Route route = (Route) context.get(ROUTE_FROM_SERVICE_JOURNEY_PATTERN);
			String objectId = NetexStifUtils.genStopPointId(scheduledStopPointId, order, route);
			StopPoint stopPoint = ObjectFactory.getStopPoint(referential, objectId);
			stopPoint.setObjectVersion(version);
			stopPoint.setRoute(route);
			// log.info("set position : " + order);
			stopPoint.setPosition(Integer.parseInt(order));
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
		}
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
