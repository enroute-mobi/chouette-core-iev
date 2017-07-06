package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.Direction;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.validator.DirectionValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;

@Log4j
public class DirectionParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);

		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		Long version = (Long) context.get(VERSION);
		String id = xpp.getAttributeValue(null, ID);
		Direction direction = factory.getDirection(id);

		DirectionValidator validator = (DirectionValidator) ValidatorFactory.getValidator(context,
				DirectionValidator.class);
		validator.checkNetexId(context, DIRECTION, id, lineNumber, columnNumber);

		direction.setOppositeDirectionRef(null); 
		direction.setDirectionType(null); 

		direction.setObjectVersion(version);
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(NAME)) {
				direction.setName(xpp.nextText());
			} else if (xpp.getName().equals(DIRECTION_TYPE)) {// TODO Michel : is it ok ?
				direction.setDirectionType(xpp.nextText());
			} else if (xpp.getName().equals(OPPOSITE_DIRECTION_REF)) {// TODO  Michel : is it ok ?
				direction.setOppositeDirectionRef(xpp.nextText());
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		direction.setFilled(true);
		validator.validate(context, direction, lineNumber, columnNumber);
	}

	static {
		ParserFactory.register(DirectionParser.class.getName(), new ParserFactory() {
			private DirectionParser instance = new DirectionParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
