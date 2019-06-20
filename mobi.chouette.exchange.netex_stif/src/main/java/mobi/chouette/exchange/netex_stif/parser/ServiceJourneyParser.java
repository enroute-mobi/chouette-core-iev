package mobi.chouette.exchange.netex_stif.parser;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.importer.ParserUtils;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.validator.PassingTimeValidator;
import mobi.chouette.exchange.netex_stif.validator.ServiceJourneyValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;
import mobi.chouette.model.CalendarDay;
import mobi.chouette.model.CompanyLite;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.LineNotice;
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
public class ServiceJourneyParser implements Parser {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(Constant.PARSER);
		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		ServiceJourneyValidator validator = (ServiceJourneyValidator) ValidatorFactory.getValidator(context, ServiceJourneyValidator.class);
		Referential referential = (Referential) context.get(Constant.REFERENTIAL);
		Long version = (Long) context.get(NetexStifConstant.VERSION);

		String id = xpp.getAttributeValue(null, NetexStifConstant.ID);
		VehicleJourney vehicleJourney = ObjectFactory.getVehicleJourney(referential, id);
		String changed = xpp.getAttributeValue(null, NetexStifConstant.CHANGED);
		if (changed != null) {
			vehicleJourney.setCreationTime(NetexStifUtils.getDate(changed));
		}
		String modification = xpp.getAttributeValue(null, NetexStifConstant.MODIFICATION);
		vehicleJourney.setObjectVersion(version);
		String dataSourceRef = (String) context.get(NetexStifConstant.IMPORT_DATA_SOURCE_REF);
		vehicleJourney.setDataSourceRef(dataSourceRef);
		LineLite line = (LineLite) context.get(Constant.LINE);
		// if (line != null)
		// NetexStifUtils.uniqueObjectIdOnLine(context,vehicleJourney, line);
		JourneyPattern pattern = null;
		Set<String> timetableRefs = new HashSet<>();
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NetexStifConstant.NAME)) {
				vehicleJourney.setPublishedJourneyName(xpp.nextText());
			} else if (xpp.getName().equals(NetexStifConstant.NOTICE_ASSIGNMENTS)) {
				parseNoticeAssignements(xpp, context, vehicleJourney, validator);
			} else if (xpp.getName().equals(NetexStifConstant.DAY_TYPES)) {
				timetableRefs.addAll(parseDayTypes(xpp, context, vehicleJourney, validator));
			} else if (xpp.getName().equals(NetexStifConstant.JOURNEY_PATTERN_REF)) {
				String ref = xpp.getAttributeValue(null, NetexStifConstant.REF);
				String attrVersion = xpp.getAttributeValue(null, NetexStifConstant.VERSION);
				String content = xpp.nextText();
				// check internal reference
				boolean checked = validator.checkNetexRef(context, vehicleJourney,
						NetexStifConstant.JOURNEY_PATTERN_REF, ref, lineNumber, columnNumber);
				if (checked)
					validator.checkInternalRef(context, vehicleJourney, NetexStifConstant.JOURNEY_PATTERN_REF, ref,
							attrVersion, content, lineNumber, columnNumber);
				pattern = ObjectFactory.getJourneyPattern(referential, ref);
				vehicleJourney.setJourneyPattern(pattern);
				vehicleJourney.setRoute(pattern.getRoute());
			} else if (xpp.getName().equals(NetexStifConstant.OPERATOR_REF)) {
				String ref = xpp.getAttributeValue(null, NetexStifConstant.REF);
				String attrVersion = xpp.getAttributeValue(null, NetexStifConstant.VERSION);
				String content = xpp.nextText();
				// check external reference
				boolean checked = validator.checkNetexRef(context, vehicleJourney, NetexStifConstant.OPERATOR_REF, ref,
						lineNumber, columnNumber);
				if (checked)
					checked = validator.checkExternalRef(context, vehicleJourney, NetexStifConstant.OPERATOR_REF, ref,
							attrVersion, content, lineNumber, columnNumber);
				if (checked)
					validator.checkExistsRef(context, vehicleJourney, NetexStifConstant.OPERATOR_REF, ref, attrVersion,
							content, lineNumber, columnNumber);
				CompanyLite company = referential.getSharedReadOnlyCompanies().get(ref);
				if (company != null) {
					vehicleJourney.setCompanyId(company.getId());
					// don't save if company is same as line main one
					if (line != null && company.getId().equals(line.getCompanyId()))
						vehicleJourney.setCompanyId(null);
				}
			} else if (xpp.getName().equals(NetexStifConstant.TRAIN_NUMBERS)) {
				parseTrainNumber(xpp, context, vehicleJourney, validator);
			} else if (xpp.getName().equals(NetexStifConstant.PASSING_TIMES)) {
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

		validator.addModification(context, vehicleJourney.getObjectId(), modification);
		if (validator.validate(context, vehicleJourney, lineNumber, columnNumber)) {
			// affect timetables
			affectTimetables(referential, vehicleJourney, timetableRefs);
		}

	}

	private Collection<String> parseDayTypes(XmlPullParser xpp, Context context, VehicleJourney vehicleJourney,
			ServiceJourneyValidator validator) throws XmlPullParserException, IOException {
		Set<String> result = new HashSet<>();
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			int lineNumber = xpp.getLineNumber();
			int columnNumber = xpp.getColumnNumber();
			if (xpp.getName().equals(NetexStifConstant.DAY_TYPE_REF)) {
				String ref = xpp.getAttributeValue(null, NetexStifConstant.REF);
				String attrVersion = xpp.getAttributeValue(null, NetexStifConstant.VERSION);
				String content = xpp.nextText();
				// check external reference
				boolean checked = validator.checkNetexRef(context, vehicleJourney, NetexStifConstant.DAY_TYPE_REF, ref,
						lineNumber, columnNumber);
				if (checked) {
					checked = validator.checkExternalRef(context, vehicleJourney, NetexStifConstant.DAY_TYPE_REF, ref, attrVersion, content, lineNumber, columnNumber);
				}
				if (checked) {
					validator.checkExistsRef(context, vehicleJourney, NetexStifConstant.DAY_TYPE_REF, ref, attrVersion, content, lineNumber, columnNumber);
				}
				result.add(ref);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		return result;
	}

	private Timetable getTimetable(Referential referential, String ref) {
		Timetable tm = referential.getTimetables().get(ref);
		if (tm == null) {
			tm = referential.getSharedTimetableTemplates().get(ref);
			if (tm == null) { return null; }
			tm = CopyUtil.copy(tm);
		}
		return tm;
	}

	private void affectTimetables(Referential referential, VehicleJourney vehicleJourney, Set<String> timetableRefs) {
		if (referential.getSharedTimetableTemplates().isEmpty()) {
			log.warn("no tm loaded ");
			return;
		}
		Set<String> negative = getNegative(referential, timetableRefs);
		if (negative.isEmpty()) {
			for (String ref : timetableRefs) {
				Timetable tm = getTimetable(referential, ref);
				if (tm != null) {
					referential.getTimetables().put(ref, tm);
					tm.addVehicleJourney(vehicleJourney);
				}
			}
		} else {
			// combine excluded tm with others
			for (String ref : timetableRefs) {
				if (negative.contains(ref))
					continue; // skip negative timetables
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
					negative.add(ref);
				}
			}
		}
		return negative;
	}

	private void parsePassingTimes(XmlPullParser xpp, Context context, VehicleJourney vehicleJourney) throws Exception {
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NetexStifConstant.TIMETABLED_PASSING_TIME)) {
				int columnNumber = xpp.getColumnNumber();
				int lineNumber = xpp.getLineNumber();
				PassingTimeValidator validator = (PassingTimeValidator) ValidatorFactory.getValidator(context,
						PassingTimeValidator.class);
				VehicleJourneyAtStop vjas = new VehicleJourneyAtStop();
				int rank = vehicleJourney.getVehicleJourneyAtStops().size();
				vjas.setVehicleJourney(vehicleJourney);
				if (vehicleJourney.getJourneyPattern() != null
						&& rank < vehicleJourney.getJourneyPattern().getStopPoints().size()) {
					StopPoint sp = vehicleJourney.getJourneyPattern().getStopPoints().get(rank);
					vjas.setStopPoint(sp);
				}

				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					switch (xpp.getName()) {
					case NetexStifConstant.ARRIVAL_TIME:
						vjas.setArrivalTime(ParserUtils.getSQLTime(xpp.nextText()));
						break;
					case NetexStifConstant.ARRIVAL_DAY_OFFSET:
						vjas.setArrivalDayOffset(Integer.parseInt(xpp.nextText()));
						break;
					case NetexStifConstant.DEPARTURE_TIME:
						vjas.setDepartureTime(ParserUtils.getSQLTime(xpp.nextText()));
						break;
					case NetexStifConstant.DEPARTURE_DAY_OFFSET:
						vjas.setDepartureDayOffset(Integer.parseInt(xpp.nextText()));
						break;
					default:
						XPPUtil.skipSubTree(log, xpp);
					}
				}
				if (vjas.getDepartureTime() != null) {
					if (vjas.getArrivalTime() == null) {
						vjas.setArrivalTime(new Time(vjas.getDepartureTime().getTime()));
						vjas.setArrivalDayOffset(vjas.getDepartureDayOffset());
					} else if (vjas.getDepartureDayOffset() > 0 && vjas.getArrivalDayOffset() == null
							&& vjas.getArrivalTime().after(vjas.getDepartureTime())) {
						vjas.setArrivalDayOffset(vjas.getDepartureDayOffset() - 1);
					}
				}
				validator.validate(context, vjas, lineNumber, columnNumber, rank);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
	}

	private void parseTrainNumber(XmlPullParser xpp, Context context, VehicleJourney vehicleJourney,
			ServiceJourneyValidator validator) throws Exception {
		// On a 0 ou 1 TRAIN_NUMBER_REF ??
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NetexStifConstant.TRAIN_NUMBER_REF)) {
				int lineNumber = xpp.getLineNumber();
				int columnNumber = xpp.getColumnNumber();
				// value of train number in the third field of the ref
				String ref = xpp.getAttributeValue(null, NetexStifConstant.REF);
				validator.addTrainNumberRef(context, vehicleJourney.getObjectId(), ref);
				String version = xpp.getAttributeValue(null, NetexStifConstant.VERSION);
				String content = xpp.nextText();
				// check external reference
				boolean checked = validator.checkNetexRef(context, vehicleJourney, NetexStifConstant.TRAIN_NUMBER_REF,
						ref, lineNumber, columnNumber);
				if (checked)
					checked = validator.checkExternalRef(context, vehicleJourney, NetexStifConstant.TRAIN_NUMBER_REF,
							ref, version, content, lineNumber, columnNumber);
				if (checked) {
					String[] tab = ref.split(":");
					if (tab.length >= 3) {
						vehicleJourney.setPublishedJourneyIdentifier(tab[2]);
					} else {
						// cannot happen :checked by validator.checkNetexRef
					}
				}
			}
		}
	}

	private void parseNoticeAssignements(XmlPullParser xpp, Context context, VehicleJourney vehicleJourney, ServiceJourneyValidator validator) throws Exception {
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NetexStifConstant.NOTICE_ASSIGNMENT)) {
				int lineNumber = xpp.getLineNumber();
				int columnNumber = xpp.getColumnNumber();
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(NetexStifConstant.NOTICE_REF)) {
						String ref = xpp.getAttributeValue(null, NetexStifConstant.REF);
						String attrVersion = xpp.getAttributeValue(null, NetexStifConstant.VERSION);
						String content = xpp.nextText();
						
						// check external reference
						boolean checked = (validator.checkNetexRef(context, vehicleJourney, NetexStifConstant.NOTICE_REF, ref, lineNumber, columnNumber) 
								&& validator.checkExternalRef(context, vehicleJourney, NetexStifConstant.NOTICE_REF, ref, attrVersion, content, lineNumber, columnNumber)
								&& validator.checkExistsRef(context, vehicleJourney, NetexStifConstant.NOTICE_REF, ref, attrVersion, content, lineNumber, columnNumber));
						
						Referential referential = (Referential) context.get(Constant.REFERENTIAL);
						
						if (referential.getSharedFootnotes().containsKey(ref) || referential.getFootnotes().containsKey(ref)) {
							Footnote footnote = referential.getFootnotes().get(ref);
							if (footnote == null && referential.getSharedFootnotes().get(ref)!=null) {
								footnote = CopyUtil.copy(referential.getSharedFootnotes().get(ref));
								footnote.setCode(referential.getSharedFootnotes().get(ref).getCode());
								referential.getFootnotes().put(ref, footnote);
							}
							if (footnote != null) {
								vehicleJourney.getFootnotes().add(footnote);
							}
						}
						else if (referential.getSharedLineNotices().containsKey(ref) || referential.getLineNotices().containsKey(ref)) {
							LineNotice lineNotice = referential.getLineNotices().get(ref);
							if (lineNotice == null && referential.getSharedLineNotices().get(ref) != null) {
								lineNotice = CopyUtil.copy(referential.getSharedLineNotices().get(ref));
								referential.getLineNotices().put(ref, lineNotice);
							}
							if (lineNotice != null) {
								vehicleJourney.getLineNotices().add(lineNotice);
								Long[] lineNoticeIds = vehicleJourney.getLineNoticeIds();
								ArrayList<Long> lineNoticeIdsList = new ArrayList<Long>(Arrays.asList(lineNoticeIds));
								lineNoticeIdsList.add(lineNotice.getId());
								lineNoticeIds = lineNoticeIdsList.toArray(lineNoticeIds);
							    vehicleJourney.setLineNoticeIds(lineNoticeIds);
							}
						}
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
