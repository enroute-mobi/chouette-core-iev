package mobi.chouette.exchange.netex_stif.parser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.importer.ParserUtils;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.model.CalendarDay;
import mobi.chouette.model.CompanyLite;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.util.ChouetteModelUtil;
import mobi.chouette.model.util.CopyUtil;
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
		LineLite line = (LineLite) context.get(LINE);
		if (line != null)
			NetexStifUtils.uniqueObjectIdOnLine(vehicleJourney, line);
		JourneyPattern pattern = null;
		Set<String> timetableRefs = new HashSet<>();
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			log.info("ServiceJourneyParser tag : " + xpp.getName());
			if (xpp.getName().equals(NAME)) {
				vehicleJourney.setPublishedJourneyName(xpp.nextText());
			} else if (xpp.getName().equals(NOTICE_ASSIGNMENTS)) {
				parseNoticeAssignements(xpp, context, vehicleJourney);
			} else if (xpp.getName().equals(DAY_TYPE_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				timetableRefs.add(ref);
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(JOURNEY_PATTERN_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				pattern = ObjectFactory.getJourneyPattern(referential, ref);
				vehicleJourney.setJourneyPattern(pattern);
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(OPERATOR_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				CompanyLite company = referential.getSharedReadOnlyCompanies().get(ref);
				if (company != null) {
					vehicleJourney.setCompanyId(company.getId());
					// don't save if company is same as line main one
					if (line != null && company.getId().equals(line.getCompanyId()))
						vehicleJourney.setCompanyId(null);
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
				if (route != null) {
					footnote.setLineId(route.getLineId());
				}
			}
		}

		// affect timetables
		affectTimetables(context, referential, vehicleJourney, timetableRefs);

	}

	private Timetable getTimetable(Referential referential, String ref) {
		Timetable tm = referential.getTimetables().get(ref);
		if (tm == null) {
			tm = referential.getSharedTimetables().get(ref);
			if (tm == null) {
				tm = referential.getSharedTimetableTemplates().get(ref);
				if (tm == null) {
					log.info("tm not found " + ref);
					return null;
				}
				tm = CopyUtil.copy(tm);
			}
		}
		return tm;
	}

	private void affectTimetables(Context context, Referential referential, VehicleJourney vehicleJourney,
			Set<String> timetableRefs) {
		if (referential.getSharedTimetableTemplates().isEmpty()) {
			log.info("no tm loaded ");
			return;
		}
		Set<String> negative = getNegative(referential, timetableRefs);
		if (negative.isEmpty()) {
			log.info("no negative timetable");
			for (String ref : timetableRefs) {
				Timetable tm = getTimetable(referential, ref);
				if (tm != null) {
					referential.getTimetables().put(ref, tm);
					log.info("affect tm " + ref + " on vj " + vehicleJourney.getObjectId());
					tm.addVehicleJourney(vehicleJourney);
				}
			}
		} else {
			// combine excluded tm with others
			log.info("negative timetable");
			for (String ref : timetableRefs) {
				if (negative.contains(ref)) continue; // skip negative timetables
				Timetable tm = getTimetable(referential, ref);
				if (tm != null) {
					String key = ref;
					for (String refNeg : negative) {
						Timetable tmNeg = referential.getSharedTimetableTemplates().get(refNeg);
						//
						List<CalendarDay> list = tmNeg.getCalendarDays();
						boolean hasActiveDay = false;
						for (CalendarDay calendarDay : list) {
							if (tm.isActiveOn(calendarDay.getDate())) {
								hasActiveDay = true;
								break;
							}
						}
						if (hasActiveDay) {
							log.info("timetable will be combined");
							key = combineTMId(key, refNeg);
							tm = CopyUtil.copy(tm);
							for (CalendarDay calendarDay : list) {
								if (tm.isActiveOn(calendarDay.getDate())) {
									tm.addCalendarDay(new CalendarDay(calendarDay));
								}
							}
						}
					}
					tm.setObjectId(key);
					referential.getTimetables().put(key, tm);
					log.info("affect tm " + key + " on vj " + vehicleJourney.getObjectId());
					tm.addVehicleJourney(vehicleJourney);
				}
			}
		}

	}

	private String combineTMId(String first, String second) {
		String firstSuffix = first.split(":")[2];
		String secondSuffix = second;
		if (secondSuffix.contains(":"))
			secondSuffix = second.split(":")[2];
		if (firstSuffix.contains("-without-")) 
		    return ChouetteModelUtil.changeSuffix(first, firstSuffix + "-and-" + secondSuffix);
		else
			return ChouetteModelUtil.changeSuffix(first, firstSuffix + "-without-" + secondSuffix);

	}

	private Set<String> getNegative(Referential referential, Set<String> timetableRefs) {
		Set<String> negative = new HashSet<>();
		for (String ref : timetableRefs) {
			Timetable tm = referential.getSharedTimetableTemplates().get(ref);
			if (tm != null) {
				if (!tm.getPeriods().isEmpty())
					continue;
				if (tm.getEffectiveDates().isEmpty()) {
					log.info("negative tm found " + ref);
					negative.add(ref);
				}
			}
		}
		return negative;
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
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NOTICE_ASSIGNMENT)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(NOTICE_REF)) {
						String ref = xpp.getAttributeValue(null, REF);
						Referential referential = (Referential) context.get(REFERENTIAL);
						Footnote footnote = referential.getFootnotes().get(ref);
						if (footnote == null) {
							footnote = referential.getSharedFootnotes().get(ref);
							if (footnote != null) {
								footnote = CopyUtil.copy(footnote);
								referential.getFootnotes().put(ref, footnote);
							}
						}
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
