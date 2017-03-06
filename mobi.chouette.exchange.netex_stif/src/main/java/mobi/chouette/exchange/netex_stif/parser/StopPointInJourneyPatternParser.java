package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.type.AlightingPossibilityEnum;
import mobi.chouette.model.type.BoardingPossibilityEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class StopPointInJourneyPatternParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);
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
				XPPUtil.skipSubTree(log, xpp);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		if (scheduledStopPointId != null) {
			String objectId = NetexStifUtils.genStopPointId(scheduledStopPointId, order);
			StopPoint stopPoint = ObjectFactory.getStopPoint(referential, objectId);
			Route route = (Route) context.get(ROUTE_FROM_SERVICE_JOURNEY_PATTERN);
			stopPoint.setObjectVersion(version);
			stopPoint.setRoute(route);
			log.info("set position : "+ order);
			stopPoint.setPosition(Integer.parseInt(order));
			if (forAlighting != null) {
				stopPoint.setForAlighting(
						forAlighting ? AlightingPossibilityEnum.normal : AlightingPossibilityEnum.forbidden);
			}
			if (forBoarding != null) {
				stopPoint.setForBoarding(
						forBoarding ? BoardingPossibilityEnum.normal : BoardingPossibilityEnum.forbidden);
			}
			factory.addStopPoint(scheduledStopPointId, stopPoint);
		}
	}

	static {
		ParserFactory.register(StopPointInJourneyPatternParser.class.getName(), new ParserFactory() {
			private StopPointInJourneyPatternParser instance = new StopPointInJourneyPatternParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
