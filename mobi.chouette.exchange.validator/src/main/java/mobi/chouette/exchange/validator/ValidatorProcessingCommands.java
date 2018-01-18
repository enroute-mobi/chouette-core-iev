package mobi.chouette.exchange.validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.LoadSharedDataCommand;
import mobi.chouette.exchange.ProcessingCommands;
import mobi.chouette.exchange.ProcessingCommandsFactory;

@Log4j
@Data
public class ValidatorProcessingCommands implements ProcessingCommands {

	protected static class DefaultFactory extends ProcessingCommandsFactory {

		@Override
		protected ProcessingCommands create() throws IOException {
			ProcessingCommands result = new ValidatorProcessingCommands();
			return result;
		}
	}

	static {
		ProcessingCommandsFactory.register(ValidatorProcessingCommands.class.getName(),
				new DefaultFactory());
	}

	@Override
	public List<Command> getPreProcessingCommands(Context context, boolean withDao) {
		InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
		List<Command> commands = new ArrayList<>();
		
		try {
			commands.add( CommandFactory.create(initialContext, LoadSharedDataCommand.class.getName()));
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException("unable to call factories");
		}
		return commands;
	}

	@Override
	public List<Command> getLineProcessingCommands(Context context, boolean withDao) {
		InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
		List<Command> commands = new ArrayList<>();
		try {
			if (withDao)
				commands.add(CommandFactory.create(initialContext, DaoLineValidatorCommand.class.getName()));
			else
				throw new IllegalArgumentException("withDao must be false");
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException("unable to call factories");
		}

		return commands;

	}

	@Override
	public List<Command> getPostProcessingCommands(Context context, boolean withDao) {
//		InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
		List<Command> commands = new ArrayList<>();
//		try {
//			// commands.add(CommandFactory.create(initialContext, SharedDataValidatorCommand.class.getName()));
//		} catch (Exception e) {
//			log.error(e, e);
//			throw new RuntimeException("unable to call factories");
//		}
		return commands;
	}

	@Override
	public List<Command> getStopAreaProcessingCommands(Context context, boolean withDao) {
		return new ArrayList<>();
	}
	@Override
	public List<Command> getDisposeCommands(Context context, boolean withDao) {
		List<Command> commands = new ArrayList<>();
		return commands;
	}

}
