package mobi.chouette.exchange.netex_stif.parser;

import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.importer.ParserUtils;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.model.CompanyLite;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class ServiceJourneyParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		Referential referential = (Referential) context.get(REFERENTIAL);
		Long version = (Long) context.get(VERSION);

		String id = xpp.getAttributeValue(null, ID);
		VehicleJourney vehicleJourney = ObjectFactory.getVehicleJourney(referential, id);
		vehicleJourney.setObjectVersion(version);
		JourneyPattern pattern = null;
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			log.info("ServiceJourneyParser tag : " + xpp.getName());
			if (xpp.getName().equals(NAME)) {
				vehicleJourney.setPublishedJourneyName(xpp.nextText());
			} else if (xpp.getName().equals(NOTICE_ASSIGNMENTS)) {
				parseNoticeAssignements(xpp, context, vehicleJourney);
			} else if (xpp.getName().equals(DAY_TYPE_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				Timetable timetable = ObjectFactory.getTimetable(referential, ref);
				vehicleJourney.getTimetables().add(timetable);
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(JOURNEY_PATTERN_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				pattern = ObjectFactory.getJourneyPattern(referential, ref);
				vehicleJourney.setJourneyPattern(pattern);
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(OPERATOR_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				// Company company = ObjectFactory.getCompany(referential, ref);
				// vehicleJourney.setCompany(company);
				CompanyLite company = referential.getSharedReadOnlyCompanies().get(ref);
				if (company != null) {
					vehicleJourney.setCompanyId(company.getId());
				}
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(TRAIN_NUMBERS)) {
				parseTrainNumber(xpp, context, vehicleJourney);
			} else if (xpp.getName().equals(PASSING_TIMES)) {
				parsePassingTimes(xpp, context, vehicleJourney);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		if (pattern != null) {
			List<Footnote> list = vehicleJourney.getFootnotes();
			for (Footnote footnote : list) {
				Route route = pattern.getRoute();
				log.info("route : " + route);
				if (route != null) {
					log.info("lineid : " + route.getLineId());
					footnote.setLineId(route.getLineId());
				}
			}
		}

	}

	private void parsePassingTimes(XmlPullParser xpp, Context context, VehicleJourney vehicleJourney) throws Exception {
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(TIMETABLED_PASSING_TIME)) {
				VehicleJourneyAtStop vjas = new VehicleJourneyAtStop();
				int rank = vehicleJourney.getVehicleJourneyAtStops().size();
				vjas.setVehicleJourney(vehicleJourney);
				if (rank < vehicleJourney.getJourneyPattern().getStopPoints().size()) {
					StopPoint sp = vehicleJourney.getJourneyPattern().getStopPoints().get(rank);
					vjas.setStopPoint(sp);
				}
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(ARRIVAL_TIME)) {
						vjas.setArrivalTime(ParserUtils.getSQLTime(xpp.nextText()));
					} else if (xpp.getName().equals(ARRIVAL_DAY_OFFSET)) {
						vjas.setArrivalDayOffset(Integer.parseInt(xpp.nextText()));
					} else if (xpp.getName().equals(DEPARTURE_TIME)) {
						vjas.setDepartureTime(ParserUtils.getSQLTime(xpp.nextText()));
					} else if (xpp.getName().equals(DEPARTURE_DAY_OFFSET)) {
						vjas.setDepartureDayOffset(Integer.parseInt(xpp.nextText()));
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
				XPPUtil.skipSubTree(log, xpp);
			}
		}
	}

	private void parseNoticeAssignements(XmlPullParser xpp, Context context, VehicleJourney vehicleJourney)
			throws Exception {
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NOTICE_ASSIGNMENT)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(NOTICE_REF)) {
						String ref = xpp.getAttributeValue(null, REF);
						// Footnote footnote = factory.getFootnote(ref);
						Referential referential = (Referential) context.get(REFERENTIAL);
						Footnote footnote = referential.getFootnotes().get(ref);
						log.info("Footnote with  id : " + ref + " footnotes : " + referential.getFootnotes());
						log.info("Footnote : " + footnote);
						if (footnote != null) {
							vehicleJourney.getFootnotes().add(footnote);
						}
						XPPUtil.skipSubTree(log, xpp);
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
