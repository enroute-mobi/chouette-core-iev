package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
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

		Integer version = (Integer) context.get(VERSION);

		String id = xpp.getAttributeValue(null, ID);
		String order = xpp.getAttributeValue(null, ORDER);
		String objectId = NetexStifUtils.genStopPointId(id, order);
		StopPoint stopPoint = ObjectFactory.getStopPoint(referential, objectId);
		stopPoint.setObjectVersion(version);
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(FOR_ALIGHTING)) {
				Boolean tmp = Boolean.parseBoolean(xpp.nextText());
				stopPoint.setForAlighting(tmp ? AlightingPossibilityEnum.normal : AlightingPossibilityEnum.forbidden);
			} else if (xpp.getName().equals(FOR_BOARDING)) {
				Boolean tmp = Boolean.parseBoolean(xpp.nextText());
				stopPoint.setForBoarding(tmp ? BoardingPossibilityEnum.normal : BoardingPossibilityEnum.forbidden);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		XPPUtil.skipSubTree(log, xpp);

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
