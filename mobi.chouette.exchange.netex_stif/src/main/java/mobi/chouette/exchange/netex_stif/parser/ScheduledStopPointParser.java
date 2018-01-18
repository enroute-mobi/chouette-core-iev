package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.model.ScheduledStopPoint;
import mobi.chouette.exchange.netex_stif.validator.ScheduledStopPointValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;

@Log4j
public class ScheduledStopPointParser implements Parser {

	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(Constant.PARSER);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NetexStifConstant.NETEX_STIF_OBJECT_FACTORY);

		ScheduledStopPointValidator validator = (ScheduledStopPointValidator) ValidatorFactory.getValidator(context, ScheduledStopPointValidator.class);

		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();

		String id = xpp.getAttributeValue(null, NetexStifConstant.ID);
		String modification = xpp.getAttributeValue(null, NetexStifConstant.MODIFICATION);
		// save ScheduledStopPoint to use it latter, call get to create the
		// point if not exists
		ScheduledStopPoint stopPoint = factory.getScheduledStopPoint(id);
		validator.addModification(context, stopPoint.getObjectId(), modification);

		// scheduledStopPoint.setOrder(order);
		XPPUtil.skipSubTree(log, xpp);
		
		validator.validate(context, stopPoint, lineNumber, columnNumber);
	}

	static {
		ParserFactory.register(ScheduledStopPointParser.class.getName(), new ParserFactory() {
			private ScheduledStopPointParser instance = new ScheduledStopPointParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}
}
