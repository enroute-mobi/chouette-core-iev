/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */

package mobi.chouette.exchange.validator;

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
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.ERROR_CODE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.validation.ValidationData;
import mobi.chouette.exchange.validator.checkpoints.TimetableValidator;

/**
 *
 */
@Log4j
public class SharedDataValidatorCommand implements Command {
	public static final String COMMAND = "SharedDataValidatorCommand";

	private TimetableValidator timetableCheckPoints = new TimetableValidator();

	@Override
	public boolean execute(Context context) throws Exception {
		boolean result = Constant.ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);
		ActionReporter reporter = ActionReporter.Factory.getInstance();

		ValidationData data = (ValidationData) context.computeIfAbsent(Constant.VALIDATION_DATA,
				v -> new ValidationData());
		ValidateParameters parameters = (ValidateParameters) context.get(Constant.CONFIGURATION);

		try {
			reporter.addObjectReport(context, "", OBJECT_TYPE.TIME_TABLE, "", OBJECT_STATE.OK, null);
			data.getTimetables()
					.forEach(timetable -> timetableCheckPoints.validate(context, timetable, parameters, null));

			if (reporter.hasObjectValidationErrors(context, OBJECT_TYPE.TIME_TABLE)) {
				reporter.setActionError(context, ERROR_CODE.INVALID_DATA, "Error for timetables ");
			}
			result = Constant.SUCCESS;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}

		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = new SharedDataValidatorCommand();
			return result;
		}
	}

	static {
		CommandFactory.register(SharedDataValidatorCommand.class.getName(), new DefaultCommandFactory());
	}

}
