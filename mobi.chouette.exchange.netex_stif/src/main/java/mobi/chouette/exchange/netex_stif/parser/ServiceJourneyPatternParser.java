package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Route;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class ServiceJourneyPatternParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);

		xpp.require(XmlPullParser.START_TAG, null, SERVICE_JOURNEY_PATTERN);
		String id = xpp.getAttributeValue(null, ID);
		JourneyPattern journeyPattern = ObjectFactory.getJourneyPattern(referential, id);
		Integer version = Integer.valueOf(xpp.getAttributeValue(null, VERSION));
		journeyPattern.setObjectVersion(version != null ? version : 0);

		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NAME)) {
				journeyPattern.setName(xpp.nextText());
			} else if (xpp.getName().equals(DESTINATION_DISPLAY_REF)) {
				String tmp = xpp.getAttributeValue(null, ID);

				// TODO
			} else if (xpp.getName().equals(DESTINATION_DISPLAY_REF)) {

			} else if (xpp.getName().equals(POINTS_IN_SEQUENCE)) {

			} else if (xpp.getName().equals(SERVICE_JOURNEY_PATTERN_TYPE)) {

			} else if (xpp.getName().equals(ROUTE_REF)) {
				String routeRef = xpp.getAttributeValue(null, ID);
				Route route = ObjectFactory.getRoute(referential, routeRef);
				journeyPattern.setRoute(route);

			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
	}

}
