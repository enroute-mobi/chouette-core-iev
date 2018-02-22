package mobi.chouette.scheduler;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ReportConstant;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.service.JobService;
import mobi.chouette.service.JobServiceManager;

@Log4j
@Stateless(name = MainCommand.COMMAND)
public class MainCommand implements Command {

	public static final String COMMAND = "MainCommand";

	@EJB
	JobServiceManager jobManager;

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean execute(Context context) throws Exception {
		boolean result = false;

		JobService jobService = (JobService) context.get(Constant.JOB_DATA);
		try {
			context.put(Constant.CONFIGURATION, jobService.getActionParameter());
			context.put(Constant.REPORT, new ActionReport());
			context.put(Constant.VALIDATION_REPORT, new ValidationReport());

			String name = jobService.getCommandName();

			InitialContext ctx = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
			Command command = CommandFactory.create(ctx, name);
			result = command.execute(context);

			ActionReport report = (ActionReport) context.get(Constant.REPORT);
			log.info(report);
			if (report.getResult().equals(ReportConstant.STATUS_ERROR)
					&& report.getFailure().getCode().equals(ActionReporter.ERROR_CODE.INTERNAL_ERROR))
			{
				jobManager.abort(jobService);
			}
			else if (report.getResult().equals(ReportConstant.STATUS_ERROR))
			{
				jobManager.terminate(jobService,JobService.STATUS.FAILED);
			}
			else if (report.isWarning())
			{ 
				jobManager.terminate(jobService,JobService.STATUS.WARNING);
			}
			else
			{
				jobManager.terminate(jobService,JobService.STATUS.SUCCESSFUL);
			}

		} catch (javax.ejb.EJBTransactionRolledbackException ex) {
			log.warn("exception bypassed " + ex);
			// just ignore this exception
			ActionReport report = (ActionReport) context.get(Constant.REPORT);
			if (report.getResult().equals(ReportConstant.STATUS_ERROR)
					&& report.getFailure().getCode().equals(ActionReporter.ERROR_CODE.INTERNAL_ERROR))
				jobManager.abort(jobService);
			else if (report.getResult().equals(ReportConstant.STATUS_ERROR))
			{
				jobManager.terminate(jobService,JobService.STATUS.FAILED);
			}
			else if (report.isWarning())
			{ 
				jobManager.terminate(jobService,JobService.STATUS.WARNING);
			}
			else
			{
				jobManager.terminate(jobService,JobService.STATUS.SUCCESSFUL);
			}

		} catch (Exception ex) {
			if (!Constant.COMMAND_CANCELLED.equals(ex.getMessage())) {
				log.error(ex,ex);
				jobManager.abort(jobService);
			}

		}
		ActionReport report = (ActionReport) context.get(Constant.REPORT);
		if (report != null) report.clear();
		ValidationReport vreport = (ValidationReport) context.get(Constant.VALIDATION_REPORT);
		if (vreport != null) vreport.clear();
		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = null;
			try {
				String name = "java:app/mobi.chouette.service/" + COMMAND;
				result = (Command) context.lookup(name);
			} catch (NamingException e) {
				// try another way on test context
				String name = "java:module/" + COMMAND;
				try {
					result = (Command) context.lookup(name);
				} catch (NamingException e1) {
					log.error(e);
				}
			} catch (Exception e) {
				log.error(e);
			}
			return result;
		}
	}

	static {
		CommandFactory.register(MainCommand.class.getName(), new DefaultCommandFactory());
	}
}
