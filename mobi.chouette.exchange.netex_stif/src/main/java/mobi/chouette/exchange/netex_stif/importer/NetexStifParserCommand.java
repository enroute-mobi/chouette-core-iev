package mobi.chouette.exchange.netex_stif.importer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.naming.InitialContext;

import org.apache.commons.io.input.BOMInputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.parser.PublicationDeliveryParser;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.FILE_ERROR_CODE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.util.Referential;

@Log4j
public class NetexStifParserCommand implements Command, Constant {

	public static final String COMMAND = "NetexStifParserCommand";

	@Getter
	@Setter
	private String fileURL;

	@Override
	public boolean execute(Context context) throws Exception {
		boolean result = ERROR;

		Monitor monitor = MonitorFactory.start(COMMAND);
		context.put(FILE_URL, fileURL);

		// report service
		ActionReporter reporter = ActionReporter.Factory.getInstance();
		String fileName = new File(new URL(fileURL).toURI()).getName();
		reporter.addFileReport(context, fileName, IO_TYPE.INPUT);
		context.put(FILE_NAME, fileName);

		try {

			URL url = new URL(fileURL);
			log.info("parsing file : " + url);

			Referential referential = (Referential) context.get(REFERENTIAL);
			if (referential != null) {
				referential.clear(true);
			}
			if (fileName.startsWith("offre_")) {
				// create line report entry
				String id = fileName.split("_")[1];
				id = "STIF:CODIFLIGNE:Line:" + id;
				LineLite line = referential.getSharedReadOnlyLines().get(id);
				if (line != null) {
					reporter.addObjectReport(context, id, OBJECT_TYPE.LINE, line.getName(), OBJECT_STATE.OK,
							IO_TYPE.INPUT);
				}
			}

			InputStream input = new BOMInputStream(url.openStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(input), 8192 * 10);
			XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
			xmlPullParserFactory.setNamespaceAware(true);
			XmlPullParser xpp = xmlPullParserFactory.newPullParser();
			xpp.setInput(in);
			context.put(PARSER, xpp);

			NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);
			if (factory == null) {
				factory = new NetexStifObjectFactory();
				context.put(NETEX_STIF_OBJECT_FACTORY, factory);
			} else {
				factory.clear();
			}

			Parser parser = ParserFactory.create(PublicationDeliveryParser.class.getName());
			parser.parse(context);

			result = SUCCESS;
			// log.info("Referential.routes : " + referential.getRoutes());
		} catch (Exception e) {
			// report service
			reporter.addFileErrorInReport(context, fileName, FILE_ERROR_CODE.INTERNAL_ERROR, e.toString());
			log.error("parsing failed ", e);
		} finally {
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}

		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = new NetexStifParserCommand();
			return result;
		}
	}

	static {
		CommandFactory.factories.put(NetexStifParserCommand.class.getName(), new DefaultCommandFactory());
	}
}
