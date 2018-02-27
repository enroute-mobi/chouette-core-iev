package mobi.chouette.exchange.netex_stif.importer;

import java.io.IOException;

import javax.naming.InitialContext;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.parser.NetexStifUtils;
import mobi.chouette.exchange.netex_stif.validator.DayTypeValidator;
import mobi.chouette.exchange.netex_stif.validator.RouteValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.util.NamingUtil;
import mobi.chouette.model.util.Referential;

@Log4j
public class NetexStifValidationCommand implements Command {

	public static final String COMMAND = "NetexStifValidationCommand";

	@Override
	public boolean execute(Context context) throws Exception {

		boolean result = Constant.ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);

		ActionReporter reporter = ActionReporter.Factory.getInstance();
		String fileName = (String) context.get(Constant.FILE_NAME);
		try {
			Referential referential = (Referential) context.get(Constant.REFERENTIAL);

			if (fileName.equals(NetexStifConstant.CALENDRIER_FILE_NAME)) {
				boolean res = validateCalendrier(context) && !reporter.hasFileValidationErrors(context, fileName);
				if (!res) {
					// block save mode to check other files
					NetexStifImportParameters parameters = (NetexStifImportParameters) context.get(Constant.CONFIGURATION);
					parameters.setNoSave(true);
				} else {
					result = Constant.SUCCESS;
				}
			} else if (fileName.equals(NetexStifConstant.COMMUN_FILE_NAME)) {
				boolean res = !reporter.hasFileValidationErrors(context, fileName);
				if (!res) {
					// block save mode to check other files
					NetexStifImportParameters parameters = (NetexStifImportParameters) context.get(Constant.CONFIGURATION);
					parameters.setNoSave(true);
				} else {
					result = Constant.SUCCESS;
				}
			} else {
				RouteValidator validator = (RouteValidator) ValidatorFactory.getValidator(context,
						RouteValidator.class);
				validator.validateAll(context);

				if (!reporter.hasFileValidationErrors(context, fileName)) {
					addStats(context, reporter, referential);
					updateObjectIds(context);
					result = Constant.SUCCESS;
				}

			}

		} catch (Exception e) {
			log.error("Netex Stif validation failed ", e);
			throw e;
		} finally {
			// clear validation context
			Context validation = (Context) context.get(Constant.VALIDATION_CONTEXT);
			if (validation != null)
			{
				validation.values().stream().filter(o -> o instanceof Context).forEach(o -> ((Context) o).clear());
				validation.clear();
			}
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}
		if (result == Constant.ERROR) {
			reporter.addFileErrorInReport(context, fileName, ActionReporter.FILE_ERROR_CODE.INVALID_FORMAT,
					"Netex Stif compliance failed");
		}
		return result;
	}
	
	private void updateObjectIds(Context context) {
		Referential referential = (Referential) context.get(Constant.REFERENTIAL);
		LineLite line = (LineLite) context.get(Constant.LINE);
		if (line == null) return;
		referential.getRoutes().values().stream().forEach(obj -> NetexStifUtils.uniqueObjectIdOnLine(context,obj, line));
		referential.getStopPoints().values().stream().forEach(obj -> NetexStifUtils.uniqueObjectIdOnLine(context,obj, line));
		referential.getRoutingConstraints().values().stream().forEach(obj -> NetexStifUtils.uniqueObjectIdOnLine(context,obj, line));
		referential.getJourneyPatterns().values().stream().forEach(obj -> NetexStifUtils.uniqueObjectIdOnLine(context,obj, line));
		referential.getVehicleJourneys().values().stream().forEach(obj -> NetexStifUtils.uniqueObjectIdOnLine(context,obj, line));
		referential.getTimetables().values().stream().forEach(obj -> NetexStifUtils.uniqueObjectId(context, obj));
	
}


	private boolean validateCalendrier(Context context) {
		DayTypeValidator validator = (DayTypeValidator) ValidatorFactory.getValidator(context, DayTypeValidator.class);
		return validator.validateAll(context);
	}

	private void addStats(Context context, ActionReporter reporter, Referential referential) {

		LineLite line = referential.getCurrentLine();
		if (line != null) {
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, NamingUtil.getName(line),
					OBJECT_STATE.OK, IO_TYPE.INPUT);
			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.LINE, 1);

			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.ROUTE,
					referential.getRoutes().size());

			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.TIME_TABLE,
					referential.getTimetables().size());

			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.VEHICLE_JOURNEY,
					referential.getVehicleJourneys().size());

			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.JOURNEY_PATTERN,
					referential.getJourneyPatterns().size());

			NetexStifImportParameters parameters = (NetexStifImportParameters) context.get(Constant.CONFIGURATION);
			if (parameters.isNoSave()) {
				reporter.setObjectStatus(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_STATE.IGNORED);
			}

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
		CommandFactory.register(NetexStifValidationCommand.class.getName(), new DefaultCommandFactory());
	}
}
