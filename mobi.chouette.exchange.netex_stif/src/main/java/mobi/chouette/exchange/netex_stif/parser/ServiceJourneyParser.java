package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.model.Company;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class ServiceJourneyParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);
		Integer version = (Integer) context.get(VERSION);

		String id = xpp.getAttributeValue(null, ID);
		VehicleJourney vehicleJourney = ObjectFactory.getVehicleJourney(referential, id);
		vehicleJourney.setObjectVersion(version);

		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NAME)) {
				vehicleJourney.setPublishedJourneyName(xpp.nextText());
			} else if (xpp.getName().equals(NOTICE_ASSIGNEMENTS)) {
				parseNoticeAssignements(xpp, context, vehicleJourney);
			} else if (xpp.getName().equals(DAY_TYPE_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				// TODO qu'en fait on
			} else if (xpp.getName().equals(JOURNEY_PATTERN_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				JourneyPattern pattern = ObjectFactory.getJourneyPattern(referential, ref);
				vehicleJourney.setJourneyPattern(pattern);
			} else if (xpp.getName().equals(OPERATOR_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				Company company = ObjectFactory.getCompany(referential, ref);
				vehicleJourney.setCompany(company);
			} else if (xpp.getName().equals(TRAIN_NUMBERS)) {
				parseTrainNumber(xpp, context, vehicleJourney);
			} else if (xpp.getName().equals(PASSING_TIMES)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(TIMETABLED_PASSING_TIME)) {
						Parser parser = ParserFactory.create(TimeTabledPassingTimeParser.class.getName());
						parser.parse(context);
					} else {
						XPPUtil.skipSubTree(log, xpp);
					}
				}
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}

	}

	private void parseTrainNumber(XmlPullParser xpp, Context context, VehicleJourney vehicleJourney) throws Exception {
		// On a 0 ou 1 TRAIN_NUMBER_REF ??
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(TRAIN_NUMBER_REF)) {
				// value of train number in the third field of the ref
				String tmp = xpp.getAttributeValue(null, REF);
				String tab[] = tmp.split(":");
				if (tab.length >= 3) {
					vehicleJourney.setPublishedJourneyIdentifier(tab[2]);
				} else {
					// TODO error
				}
			}
		}
	}

	private void parseNoticeAssignements(XmlPullParser xpp, Context context, VehicleJourney vehicleJourney)
			throws Exception {
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NOTICE_ASSIGNEMENT)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(NOTICE_REF)) {
						String ref = xpp.getAttributeValue(null, REF);
						Footnote footnote = factory.getFootnote(ref);
						vehicleJourney.getFootnotes().add(footnote);

					} else {
						XPPUtil.skipSubTree(log, xpp);
					}
				}
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
	}

	static {
		ParserFactory.register(ServiceJourneyParser.class.getName(), new ParserFactory() {
			private ServiceJourneyParser instance = new ServiceJourneyParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}
}
