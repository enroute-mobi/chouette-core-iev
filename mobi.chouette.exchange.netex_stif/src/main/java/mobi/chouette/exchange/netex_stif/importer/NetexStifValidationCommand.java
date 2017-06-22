package mobi.chouette.exchange.netex_stif.importer;

import java.io.IOException;

import javax.naming.InitialContext;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.util.NamingUtil;
import mobi.chouette.model.util.Referential;

@Log4j
public class NetexStifValidationCommand implements Command, Constant {

	public static final String COMMAND = "NetexStifValidationCommand";

	@Override
	public boolean execute(Context context) throws Exception {

		boolean result = ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);

		ActionReporter reporter = ActionReporter.Factory.getInstance();
		String fileName = (String) context.get(FILE_NAME);
		try {
			Referential referential = (Referential) context.get(REFERENTIAL);

			// Tests are done during parsing just check file status

			if (!reporter.hasFileValidationErrors(context, fileName))
				result = SUCCESS;
			
			if (result) {
				addStats(context, reporter, referential);
			}

		} catch (Exception e) {
			log.error("Netex Stif validation failed ", e);
			throw e;
		} finally {
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}
		if (result == ERROR) {
			reporter.addFileErrorInReport(context, fileName, ActionReporter.FILE_ERROR_CODE.INVALID_FORMAT,
					"Netex Stif compliance failed");
		}
		return result;
	}

	private void addStats(Context context, ActionReporter reporter, Referential referential) {

		LineLite line = referential.getCurrentLine();
		if (line != null) {
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, NamingUtil.getName(line),
					OBJECT_STATE.OK, IO_TYPE.INPUT);
			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.LINE, 1);

			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.ROUTE,
					referential.getRoutes().size());

			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.TIMETABLE,
					referential.getTimetables().size());

			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.VEHICLE_JOURNEY,
					referential.getVehicleJourneys().size());

			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.JOURNEY_PATTERN,
					referential.getJourneyPatterns().size());
		}
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = new NetexStifValidationCommand();
			return result;
		}
	}

	static {
		CommandFactory.factories.put(NetexStifValidationCommand.class.getName(), new DefaultCommandFactory());
	}
}
