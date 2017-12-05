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
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.exchange.validator.checkpoints.JourneyPatternValidator;
import mobi.chouette.exchange.validator.checkpoints.LineValidator;
import mobi.chouette.exchange.validator.checkpoints.RouteValidator;
import mobi.chouette.exchange.validator.checkpoints.RoutingConstraintValidator;
import mobi.chouette.exchange.validator.checkpoints.VehicleJourneyValidator;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.util.Referential;

/**
 *
 */
@Log4j
public class LineValidatorCommand implements Command {
	public static final String COMMAND = "LineValidatorCommand";

	private LineValidator lineCheckPoints = new LineValidator();
	private RouteValidator routeCheckPoints = new RouteValidator();
	private RoutingConstraintValidator routingConstraintCheckPoints = new RoutingConstraintValidator();
	private JourneyPatternValidator journeyPatternCheckPoints = new JourneyPatternValidator();
	private VehicleJourneyValidator vehicleJourneyCheckPoints = new VehicleJourneyValidator();

	@Override
	public boolean execute(Context context) throws Exception {
		boolean result = Constant.ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);

		ValidationReport report = (ValidationReport) context.get(Constant.VALIDATION_REPORT);
		Referential r = (Referential) context.get(Constant.REFERENTIAL);
		ValidateParameters parameters = (ValidateParameters) context.get(Constant.CONFIGURATION);
		if (report == null) {
			context.put(Constant.VALIDATION_REPORT, new ValidationReport());
		}
		try {
			LineLite line = r.getCurrentLine();
			String transportMode = line.getTransportModeName();
			lineCheckPoints.validate(context, line, parameters, transportMode);
			r.getRoutes().values().stream().forEach(
					route -> routeCheckPoints.validate(context, route, parameters, transportMode));
			r.getRoutingConstraints().values().stream().forEach(
					routingConstraint -> routingConstraintCheckPoints.validate(context, routingConstraint, parameters, transportMode));
			r.getJourneyPatterns().values().stream().forEach(
					journeyPattern-> journeyPatternCheckPoints.validate(context, journeyPattern,parameters,transportMode));
			r.getVehicleJourneys().values().stream().forEach(
					vehicleJourney-> vehicleJourneyCheckPoints.validate(context, vehicleJourney,parameters,transportMode));

			result = Constant.SUCCESS;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			context.remove(Constant.ATTRIBUTE_CONTEXT);
			context.remove(Constant.DISTANCE_CONTEXT);
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}

		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = new LineValidatorCommand();
			return result;
		}
	}

	static {
		CommandFactory.register(LineValidatorCommand.class.getName(), new DefaultCommandFactory());
	}

}
