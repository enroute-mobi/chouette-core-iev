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
import mobi.chouette.model.util.Referential;

// members level 

@Log4j
public class NetexCalendrierParser implements Parser, Constant {

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
		Referential referential = (Referential) context.get(REFERENTIAL);
		referential.getSharedTimetableTemplates().putAll(referential.getTimetables());
		referential.getTimetables().clear();
	}

	private void parseMember(String tag, XmlPullParser xpp, Context context) throws Exception {
		String elt = members.get(tag);
		if (elt != null) {
			if (xpp.getName().equals(tag)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					log.info("NetexCalendrierParser: tag "+ xpp.getName());
					if (xpp.getName().equals(elt)) {
						String clazz = parsers.get(elt);
						log.info("NetexCalendrierParser: tag "+ xpp.getName() + " use : " + clazz );
						if (clazz != null) {
							log.info("parse with " + clazz);
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
		members.put(DAY_TYPES, DAY_TYPE);
		members.put(DAY_TYPE_ASSIGNMENTS, DAY_TYPE_ASSIGNMENT);
		members.put(OPERATING_PERIODS, OPERATING_PERIOD);

		// init used parsers
		parsers.put(DAY_TYPE, DayTypeParser.class.getName());
		parsers.put(DAY_TYPE_ASSIGNMENT, DayTypeAssignmentParser.class.getName());
		parsers.put(OPERATING_PERIOD, OperatingPeriodParser.class.getName());
		
		ParserFactory.register(NetexCalendrierParser.class.getName(), new ParserFactory() {
			private NetexCalendrierParser instance = new NetexCalendrierParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}
}
