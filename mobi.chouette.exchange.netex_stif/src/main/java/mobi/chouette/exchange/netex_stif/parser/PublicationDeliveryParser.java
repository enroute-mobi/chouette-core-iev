package mobi.chouette.exchange.netex_stif.parser;

import java.util.ArrayList;
import java.util.Collection;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.validator.FrameValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;

@Log4j
public class PublicationDeliveryParser implements Parser{

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(Constant.PARSER);
		
		XPPUtil.nextStartTag(xpp, NetexStifConstant.PUBLICATION_DELIVERY);

		xpp.require(XmlPullParser.START_TAG, null, NetexStifConstant.PUBLICATION_DELIVERY);
		// Referential referential = (Referential) context.get(Constant.REFERENTIAL);
		// String version = xpp.getAttributeValue(null, VERSION);
		// TODO vérifier que la version soit celle du stif : test non spécifié
		Collection<String> compositeFrameNames =  new ArrayList<String>();
		Collection<String> generalFrameNames =  new ArrayList<String>();
		context.put(NetexStifConstant.COMPOSITE_FRAMES, compositeFrameNames);
		context.put(NetexStifConstant.GENERAL_FRAMES, generalFrameNames);

		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NetexStifConstant.DATA_OBJECTS)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					// log.info("PublicationDeliveryParser tag : "  + xpp.getName());
					if (xpp.getName().equals(NetexStifConstant.COMPOSITE_FRAME)) {
						Parser compositeFrameParser = ParserFactory.create(CompositeFrameParser.class.getName());
						compositeFrameParser.parse(context);
					}
					else if (xpp.getName().equals(NetexStifConstant.GENERAL_FRAME)) {
						Parser parser = ParserFactory.create(GeneralFrameParser.class.getName());
						parser.parse(context);
					} else {
						XPPUtil.skipSubTree(log, xpp);
					}
				}
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		// check frames completion
		FrameValidator validator = (FrameValidator) ValidatorFactory.getValidator(context, FrameValidator.class); 
		validator.checkMandatoryCompositeFrames(context, compositeFrameNames, lineNumber, columnNumber);
		validator.checkMandatoryGeneralFrames(context, generalFrameNames, lineNumber, columnNumber);
		
		// Referential ref = (Referential)context.get(Constant.REFERENTIAL);
		// log.info("Referential.routes : " + ref.getRoutes());
	}

	static {
		ParserFactory.register(PublicationDeliveryParser.class.getName(), new ParserFactory() {
			private PublicationDeliveryParser instance = new PublicationDeliveryParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
