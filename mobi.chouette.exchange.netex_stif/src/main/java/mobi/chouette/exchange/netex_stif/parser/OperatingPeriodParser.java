package mobi.chouette.exchange.netex_stif.parser;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.model.OperatingPeriod;
import mobi.chouette.exchange.netex_stif.validator.OperatingPeriodValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;

public class OperatingPeriodParser implements Parser {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(Constant.PARSER);
		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		String id = xpp.getAttributeValue(null, NetexStifConstant.ID);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NetexStifConstant.NETEX_STIF_OBJECT_FACTORY);
		OperatingPeriodValidator validator =  (OperatingPeriodValidator) ValidatorFactory.getValidator(context, OperatingPeriodValidator.class);
		OperatingPeriod period = factory.getOperatingPeriod(id);
		validator.checkNetexId(context, NetexStifConstant.OPERATING_PERIOD, id, lineNumber, columnNumber);
		String modification = xpp.getAttributeValue(null, NetexStifConstant.MODIFICATION);
		validator.addModification(context, id, modification);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			// log.info("OperatingPeriodParser tag : " + xpp.getName());
			if (xpp.getName().equals(NetexStifConstant.FROM_DATE)) {
				Date date = sdf.parse(xpp.nextText());
				period.getPeriod().setStartDate(new java.sql.Date(date.getTime()));
			} else if (xpp.getName().equals(NetexStifConstant.TO_DATE)) {
				Date date = sdf.parse(xpp.nextText());
				period.getPeriod().setEndDate(new java.sql.Date(date.getTime()));
			}
		}
		
		validator.validate(context, period, lineNumber, columnNumber);

	}
	
	static {

			ParserFactory.register(OperatingPeriodParser.class.getName(), new ParserFactory() {
			private OperatingPeriodParser instance = new OperatingPeriodParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
