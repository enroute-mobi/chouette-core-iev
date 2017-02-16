package mobi.chouette.exchange.netex_stif.parser;

import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.DestinationDisplay;

@Log4j
public class NetexStructureParser implements Parser, Constant {

	// Here we are at the members level

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			String name = xpp.getName();
			// check if it is one of the member we treat
			if (members.containsKey(name)) {
				parseMember(name, xpp, context);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
	}

	private void parseMember(String tag, XmlPullParser xpp, Context context) throws Exception {
		String elt = members.get(tag);
		if (elt != null) {
			if (xpp.getName().equals(tag)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(elt)) {
						String clazz = parsers.get(elt);
						if (clazz != null) {
							Parser parser = ParserFactory.create(clazz);
							parser.parse(context);
						} else {
							XPPUtil.skipSubTree(log, xpp);
						}
					}
				}
			}
		} else {
			XPPUtil.skipSubTree(log, xpp);
		}
	}

	static Map<String, String> members = new HashMap<>();
	static Map<String, String> parsers = new HashMap<>();

	static {
		// init used members
		members.put(ROUTES, ROUTE);
		members.put(DIRECTIONS, DIRECTION);
		members.put(SERVICE_JOURNEY_PATTERNS, SERVICE_JOURNEY_PATTERN);
		members.put(DESTINATION_DISPLAYS, DESTINATION_DISPLAY);
		members.put(SCHEDULED_STOP_POINTS, SCHEDULED_STOP_POINT);
		members.put(PASSENGER_STOP_ASSIGNEMENTS, PASSENGER_STOP_ASSIGNEMENT);
		members.put(ROUTING_CONSTRAINT_ZONES, ROUTING_CONSTRAINT_ZONE);

		// init used parsers
		parsers.put(ROUTE, RouteParser.class.getName());
		parsers.put(DIRECTION, DirectionParser.class.getName());
		parsers.put(SERVICE_JOURNEY_PATTERN, ServiceJourneyPatternParser.class.getName());
		parsers.put(DESTINATION_DISPLAY, DestinationDisplay.class.getName());
		parsers.put(SCHEDULED_STOP_POINT, ScheduledStopPointParser.class.getName());
		parsers.put(PASSENGER_STOP_ASSIGNEMENT, PassengerStopAssignementParser.class.getName());
		parsers.put(ROUTING_CONSTRAINT_ZONE, RoutingConstraintZoneParser.class.getName());

		ParserFactory.register(NetexStructureParser.class.getName(), new ParserFactory() {
			private NetexStructureParser instance = new NetexStructureParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
