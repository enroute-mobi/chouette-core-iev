package mobi.chouette.exchange.netex_stif.importer;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

@Log4j
public class NetexStifSAXErrorHandler implements ErrorHandler {

	public NetexStifSAXErrorHandler(Context context, String fileURL) {
	}

	@Override
	public void warning(SAXParseException e) throws SAXException {

		log.warn(e);
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		log.error(e);

	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		log.fatal(e);
	}

}
