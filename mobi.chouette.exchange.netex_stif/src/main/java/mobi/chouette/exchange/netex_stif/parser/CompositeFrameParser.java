package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;

@Log4j
public class CompositeFrameParser implements Constant, Parser {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		
		
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			if (xpp.getName().equals(TYPE_OF_FRAME_REF)) {
				if (xpp.nextText().equals(NETEX_OFFRE_LIGNE)){
					// TODO dire que c'est valide ou à l'inverse que c'est invalide
				}
			}else if (xpp.getName().equals(FRAMES)) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(GENERAL_FRAME)) {
						Parser netexStructureParser = ParserFactory.create(NetexStructureParser.class.getName());
						netexStructureParser.parse(context);
					}else{
						
					}
				}
			}
		}
	}

}
