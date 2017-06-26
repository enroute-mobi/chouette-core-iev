package mobi.chouette.model.util;

import java.security.MessageDigest;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.model.CalendarDay;
import mobi.chouette.model.CompanyLite;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Period;
import mobi.chouette.model.Route;
import mobi.chouette.model.RoutingConstraint;
import mobi.chouette.model.SignedChouetteObject;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;

/**
 * Chaque objet dès qu'il est créé ou souvegardé va contenir en base :
 *  <ul>
 *  <li>un attribut string checksum codé en sha256</li>
 *  <li>un attribut text checksum_source qui est la chaîne avant encodage</li>
 *  </ul>
 * Si des objets utilise d'autres objets dans le calcul de leur checksum on intègre leur checksum dans la chaîne de caractère source.
 * le séparateur de donnée sera le '|'
 * <ul>
 * <li>Un champ non renseigné sera toujours renseigné à '-'</li>
 * <li>Les attributs booléens seront pris en compte avec la valeur true/false</li>
 * <li>Les attributs énumérés seront pris en compte avec la valeur en base qui se trouve ếtre la clé</li>
 * </ul>
 * 
 * <b>Note</b> : checksum des objets REFLEX et CODIFLIGNE = partie technique des identifiants 
 * <p>
 * carte https://projects.af83.io/issues/3845
 * 
 * @author michel
 *
 */
@Log4j
public class ChecksumUtil {

	private static final String STRING_ENCODING = "UTF-8";
	private static final String ALGORITHM = "SHA-256";
	private static final char SEP = '|';
	private static final String EMPTY_VALUE = "-";

	/**
	 * Checksum d'une Route :
	 * <ul>
	 *     <li>name/li>
	 *     <li>published_name (direction NeTEx)/li>
	 *     <li>wayback/li>
	 *     <li>pour la liste des StopArea ordonnés : checksum StopArea + route.for_boarding + route.for_alighting/li>
	 *     <li>pour la liste des RoutingConstraintZone : checksum des RoutingConstraintZone ordonnés alphabétiquement/li>
	 * </ul>
	 * @param context context de travail
	 * @param route Route à mettre à jour
	 */
	public static void checksum(Context context, Route route) {
		Referential r = (Referential) context.get(Constant.REFERENTIAL);
		StringBuilder cs = new StringBuilder(getValue(route.getName()));
		cs.append(SEP);
		cs.append(getValue(route.getPublishedName()));
		cs.append(SEP);
		cs.append(getValue(route.getWayBack()));
		cs.append(SEP);

		for (StopPoint stop : route.getStopPoints()) {
			StopAreaLite area = r.findStopArea(stop.getStopAreaId());
			cs.append(SEP);
			cs.append(area.objectIdSuffix());
			cs.append(SEP);
			cs.append(getValue(stop.getForBoarding()));
			cs.append(SEP);
			cs.append(getValue(stop.getForAlighting()));
		}
		List<String> rcs = collectChecksums(route.getRoutingConstraints(), true);
		for (String rc : rcs) {
			cs.append(SEP);
			cs.append(rc);
		}
		route.setChecksumSource(cs.toString());
		route.setChecksum(hashString(route.getChecksumSource()));
	}

	/**
	 * Checksum d'un JourneyPattern :
	 * <ul>
	 *     <li>name</li>
	 *     <li>published_name</li>
	 *     <li>registration_number</li>
	 *     <li>pour la liste des StopArea ordonnés : checksum StopArea</li>
	 * </ul>
	 * @param context context de travail
	 * @param journeyPattern JourneyPattern à mettre à jour
	 */
	public static void checksum(Context context, JourneyPattern journeyPattern) {
		Referential r = (Referential) context.get(Constant.REFERENTIAL);
		StringBuilder cs = new StringBuilder(getValue(journeyPattern.getName()));
		cs.append(SEP);
		cs.append(getValue(journeyPattern.getPublishedName()));
		cs.append(SEP);
		cs.append(getValue(journeyPattern.getRegistrationNumber()));
		for (StopPoint stop : journeyPattern.getStopPoints()) {
			StopAreaLite area = r.findStopArea(stop.getStopAreaId());
			cs.append(SEP);
			cs.append(area.objectIdSuffix());
		}
		journeyPattern.setChecksumSource(cs.toString());
		journeyPattern.setChecksum(hashString(journeyPattern.getChecksumSource()));
	}

	/**
	 * Checksum d'un Footnote :
	 * <ul>
	 *     <li>code</li>
	 *     <li>texte</li>
	 * </ul>
	 * @param context context de travail
	 * @param note Footnote à mettre à jour
	 */
	public static void checksum(Context context, Footnote note) {
		StringBuilder cs = new StringBuilder(getValue(note.getCode()));
		cs.append(SEP);
		cs.append(getValue(note.getLabel()));
		note.setChecksumSource(cs.toString());
		note.setChecksum(hashString(note.getChecksumSource()));
	}

	/**
	 * Checksum d'un RoutingConstraintZone :
	 * <ul>
	 *     <li>liste des checksum des StopArea dans l'ordre de l'itinéraire</li>
	 * </ul>
	 * @param context context de travail
	 * @param routing RoutingConstraint à mettre à jour
	 */
	public static void checksum(Context context, RoutingConstraint routing) {
		Referential r = (Referential) context.get(Constant.REFERENTIAL);
		StringBuilder cs = new StringBuilder();
		for (StopPoint stop : routing.getStopPoints()) {
			StopAreaLite area = r.findStopArea(stop.getStopAreaId());
			cs.append(SEP);
			cs.append(area.objectIdSuffix());
		}
		cs.replace(0,1,""); // remove first SEP
		routing.setChecksumSource(cs.toString());
		routing.setChecksum(hashString(routing.getChecksumSource()));
	}

	/**
     *Checksum d'un VehicleJourney :
	 * <ul>
	 *     <li>published_journey_name</li>
	 *     <li>published_journey_identifier</li>
	 *     <li>operator (technicalID)</li>
	 *     <li>pour la liste des footnotes : checksum des footnotes ordonnés alphabétiquement</li>
	 *     <li>pour la liste des VehicleJourneyAtStop : checksum des VehicleJourneyAtStop dans l'ordre des arrêts de l'itinéraire</li>
	 * </ul>
	 * @param context context de travail
	 * @param journey VehicleJourney à mettre à jour
	 */
	public static void checksum(Context context, VehicleJourney journey) {
		Referential r = (Referential) context.get(Constant.REFERENTIAL);
		StringBuilder cs = new StringBuilder(getValue(journey.getPublishedJourneyName()));
		cs.append(SEP);
		cs.append(getValue(journey.getPublishedJourneyIdentifier()));
		cs.append(SEP);
		if (journey.getCompanyId() != null) {
			CompanyLite c = r.findCompany(journey.getCompanyId());
			cs.append(c.objectIdSuffix());
		} else {
			cs.append(EMPTY_VALUE);
		}

		List<String> ftcs = collectChecksums(journey.getFootnotes(), true);
		for (String value : ftcs) {
			cs.append(SEP);
			cs.append(value);
		}
		List<String> vjascs = collectChecksums(journey.getVehicleJourneyAtStops(), false);
		for (String value : vjascs) {
			cs.append(SEP);
			cs.append(value);
		}
		journey.setChecksumSource(cs.toString());
		journey.setChecksum(hashString(journey.getChecksumSource()));
	}

	/**
	 * Checksum d'un VehicleJourneyAtStop :
	 * <ul>
	 *     <li>departure_time (format HH:mm:ss)</li>
	 *     <li>arrival_time (format HH:mm:ss)</li>
	 *     <li>departure_day_offset</li>
	 *     <li>arrival_day_offset</li>
	 * </ul>
	 * 
	 * @param context context de travail
	 * @param passingTime VehicleJourneyAtStop à mettre à jour
	 */
	public static void checksum(Context context, VehicleJourneyAtStop passingTime) {
		StringBuilder cs = new StringBuilder();
		cs.append(toString(passingTime.getDepartureTime()));
		cs.append(SEP);
		cs.append(toString(passingTime.getArrivalTime()));
		cs.append(SEP);
		cs.append(getValue(passingTime.getDepartureDayOffset()));
		cs.append(SEP);
		cs.append(getValue(passingTime.getArrivalDayOffset()));
		passingTime.setChecksumSource(cs.toString());
		passingTime.setChecksum(hashString(passingTime.getChecksumSource()));
	}
	
	/**
	 * Checksum d'un CalendarDay :
	 * <ul>
	 *     <li>date (format date YYYY-MM-dd)</li>
	 *     <li>in_out</li>
	 * </ul>
	 * @param context context de travail
	 * @param date CalendarDay à mettre à jour
	 */
	public static void checksum(Context context, CalendarDay date) {
		StringBuilder cs = new StringBuilder();
		cs.append(toString(date.getDate()));
		cs.append(SEP);
		cs.append(date.getIncluded().toString().toLowerCase());
		date.setChecksumSource(cs.toString());
		date.setChecksum(hashString(date.getChecksumSource()));
	}

	/**
	 * Checksum d'un Period :
	 * <ul>
	 *     <li>period_start (format date YYYY-MM-dd)</li>
	 *     <li>period_end (format date YYYY-MM-dd)</li>
	 * </ul>
	 * @param context context de travail
	 * @param period Period à mettre à jour
	 */
	public static void checksum(Context context, Period period) {
		StringBuilder cs = new StringBuilder();
		cs.append(toString(period.getStartDate()));
		cs.append(SEP);
		cs.append(toString(period.getEndDate()));
		period.setChecksumSource(cs.toString());
		period.setChecksum(hashString(period.getChecksumSource()));
	}

	/**
	 * Checksum d'un Timetable :
	 * <ul>
	 *     <li>int_day_types</li>
	 *     <li>liste des checksum des TimeTableDate classés dans l'ordre alphabétique</li>
	 *     <li>liste des checksum des TimeTablePeriod classés dans l'ordre alphabétique</li>
	 * </ul>
	 * @param context context de travail
	 * @param timetable Timetable à mettre à jour
	 */
	public static void checksum(Context context, Timetable timetable) {
		StringBuilder cs = new StringBuilder();
		cs.append(getValue(timetable.getIntDayTypes()));
		List<String> calcs = collectChecksums(timetable.getCalendarDays(), true);
		for (String value : calcs) {
			cs.append(SEP);
			cs.append(value);
		}
		List<String> periodcs = collectChecksums(timetable.getPeriods(), true);
		for (String value : periodcs) {
			cs.append(SEP);
			cs.append(value);
		}
		timetable.setChecksumSource(cs.toString());
		timetable.setChecksum(hashString(timetable.getChecksumSource()));
	}

	/**
	 * convert time to string
	 * @param time time to format
	 * @return formated time for checksum
	 */
	private static String toString(Time time) {
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(time);
	}
	
	/**
	 * convert date o string
	 * @param date date to format
	 * @return formated date for checksum
	 */
	private static String toString(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	/**
	 * compute string as checksum
	 * 
	 * @param message string to compute
	 * @return checksum as hex string
	 */
	private static String hashString(String message) {

		try {
			MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
			byte[] hashedBytes = digest.digest(message.getBytes(STRING_ENCODING));

			return convertByteArrayToHexString(hashedBytes);
		} catch (Exception e) {
			log.error("cannot calculate checksum", e);
			return null;
		}

	}

	/**
	 * convert byte array to hex string
	 * @param arrayBytes data to convert
	 * @return string
	 */
	private static String convertByteArrayToHexString(byte[] arrayBytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < arrayBytes.length; i++) {
			stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return stringBuffer.toString();
	}

	/**
	 * convert data as string using default value if empty or null
	 * 
	 * @param o object to convert
	 * @return string form 
	 */
	private static String getValue(Object o) {
		if (o == null)
			return EMPTY_VALUE;
		String value = o.toString();
		if (value.isEmpty())
			return EMPTY_VALUE;
		return value;
	}

	/**
	 * collect every checksums from list 
	 * 
	 * @param objects collection input
	 * @param sorted if true : sort checksum alphabetically, else maintains input order
	 * @return collected checksums
	 */
	private static List<String> collectChecksums(Collection<? extends SignedChouetteObject> objects, boolean sorted) {
		List<String> result = new ArrayList<>();
		for (SignedChouetteObject object : objects) {
			result.add(object.getChecksum());
		}
		if (sorted)
			Collections.sort(result);
		return result;
	}
}
