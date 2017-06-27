package mobi.chouette.exchange.netex_stif.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.validator.FrameValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;

@Log4j
public class GeneralFrameParser implements Constant, Parser {

	private static Map<String, String> frameTypes = new HashMap<>();

	static {
		frameTypes.put(NETEX_STRUCTURE, NetexStructureParser.class.getName());
		frameTypes.put(NETEX_HORAIRE, NetexHoraireParser.class.getName());
		frameTypes.put(NETEX_COMMUN, NetexCommunParser.class.getName());
		frameTypes.put(NETEX_CALENDRIER, NetexCalendrierParser.class.getName());

		ParserFactory.register(GeneralFrameParser.class.getName(), new ParserFactory() {
			private GeneralFrameParser instance = new GeneralFrameParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class);
		String type = null;
		boolean valid = false;
		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		Collection<String> frames = (Collection<String>) context.get(GENERAL_FRAMES);

		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(TYPE_OF_FRAME_REF)) {
				type = xpp.getAttributeValue(null, REF);
				valid = validator.checkForbiddenGeneralFrames(context, type, lineNumber, columnNumber);
				frames.add(type);  // to check mandatory frames
				XPPUtil.skipSubTree(log, xpp);
			} else if (xpp.getName().equals(MEMBERS)) {
				if (valid && type != null) {
					String clazz = frameTypes.get(type);
					if (clazz != null) {
						// log.info("Parse with "+ clazz);
						Parser subParser = ParserFactory.create(clazz);
						subParser.parse(context);
					} else {
						XPPUtil.skipSubTree(log, xpp);
					}
				} else {
					XPPUtil.skipSubTree(log, xpp);
				}
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		// Referential ref = (Referential)context.get(REFERENTIAL);
		// log.info("Referential.routes : " + ref.getRoutes());
	}

}
