package mobi.chouette.exchange.netex_stif.importer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import javax.naming.InitialContext;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.FILE_ERROR_CODE;
import mobi.chouette.exchange.report.IO_TYPE;

@Log4j
public class NetexStifSAXParserCommand implements Command, Constant {

	public static final String COMMAND = "NetexStifSAXParserCommand";

	public static final String SCHEMA_FILE = "/xsd/NeTEx_publication.xsd";

	@Getter
	@Setter
	private String fileURL;

	@Override
	public boolean execute(Context context) throws Exception {

		boolean result = ERROR;

		Monitor monitor = MonitorFactory.start(COMMAND);

		ActionReporter reporter = ActionReporter.Factory.getInstance();

		String fileName = new File(new URL(fileURL).toURI()).getName();
		reporter.addFileReport(context, fileName, IO_TYPE.INPUT);

		Schema schema = (Schema) context.get(SCHEMA);
		if (schema == null) {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			schema = factory.newSchema(getClass().getResource(SCHEMA_FILE));
			context.put(SCHEMA, schema);
		}

		URL url = new URL(fileURL);

		NetexStifSAXErrorHandler handler = new NetexStifSAXErrorHandler(context, fileURL);
		Reader reader = null;
		try {
			reader = new BufferedReader(CharSetChecker.getEncodedInputStreamReader(url.toString(), url.openStream()), 8192 * 10);
			StreamSource file = new StreamSource(reader);

			Validator validator = schema.newValidator();
			validator.setErrorHandler(handler);
			validator.validate(file);
			result = SUCCESS;
		} catch (IOException | SAXException e) {
			log.error(e.getMessage(), e);
			reporter.addFileErrorInReport(context, fileName, FILE_ERROR_CODE.INVALID_FORMAT,e.getMessage());
		} finally {
			if (reader != null ) reader.close();
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}

		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = new NetexStifSAXParserCommand();
			return result;
		}
	}

	static {
		CommandFactory.factories.put(NetexStifSAXParserCommand.class.getName(), new DefaultCommandFactory());
	}
}
