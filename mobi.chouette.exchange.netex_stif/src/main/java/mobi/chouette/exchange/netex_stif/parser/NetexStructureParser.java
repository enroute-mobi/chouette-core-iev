package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;

@Log4j
public class NetexStructureParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(TYPE_OF_FRAME_REF)) {
				if (xpp.nextText().equals(NETEX_STRUCTURE)) {
					// TODO dire que c'est valide ou à l'inverse que c'est
					// invalide
				}
			} else if (xpp.getName().equals(MEMBERS)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(ROUTES)) {
						while (xpp.nextTag() == XmlPullParser.START_TAG) {
							if (xpp.getName().equals(ROUTE)) {
								Parser routeParser = ParserFactory.create(RouteParser.class.getName());
								routeParser.parse(context);
							}
						}

					} else if (xpp.getName().equals(DIRECTIONS)) {
						while (xpp.nextTag() == XmlPullParser.START_TAG) {
							if (xpp.getName().equals(DIRECTION)) {
								Parser directionParser = ParserFactory.create(DirectionParser.class.getName());
								directionParser.parse(context);
							}
						}
					} else if (xpp.getName().equals(SERVICE_JOURNEY_PATTERNS)) {
						while (xpp.nextTag() == XmlPullParser.START_TAG) {
							if (xpp.getName().equals(SERVICE_JOURNEY_PATTERN)) {
								Parser serviceJourneyPatternParser = ParserFactory
										.create(ServiceJourneyPatternParser.class.getName());
								serviceJourneyPatternParser.parse(context);
							}
						}
					} else if (xpp.getName().equals(DESTINATION_DISPLAYS)) {
						while (xpp.nextTag() == XmlPullParser.START_TAG) {
							if (xpp.getName().equals(DESTINATION_DISPLAY)) {
								Parser destinationDisplayParser = ParserFactory
										.create(DestinationDisplayParser.class.getName());
								destinationDisplayParser.parse(context);
							}
						}
					} else if (xpp.getName().equals(SCHEDULED_STOP_POINTS)) {
						while (xpp.nextTag() == XmlPullParser.START_TAG) {
							if (xpp.getName().equals(SCHEDULED_STOP_POINT)) {
								Parser scheduledStopPointParser = ParserFactory
										.create(ScheduledStopPointParser.class.getName());
								scheduledStopPointParser.parse(context);
							}
						}
					} else if (xpp.getName().equals(PASSENGER_STOP_ASSIGNEMENTS)) {
						while (xpp.nextTag() == XmlPullParser.START_TAG) {
							if (xpp.getName().equals(PASSENGER_STOP_ASSIGNEMENT)) {
								Parser passengerStopAssignementParser = ParserFactory
										.create(PassengerStopAssignementParser.class.getName());
								passengerStopAssignementParser.parse(context);
							}
						}
					} else {
						XPPUtil.skipSubTree(log, xpp);
					}
				}
			}
		}
	}

}
