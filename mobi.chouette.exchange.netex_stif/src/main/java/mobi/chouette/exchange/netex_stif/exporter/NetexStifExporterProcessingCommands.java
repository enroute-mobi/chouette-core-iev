package mobi.chouette.exchange.netex_stif.exporter;

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
import mobi.chouette.core.CoreExceptionCode;
import mobi.chouette.core.CoreRuntimeException;
import mobi.chouette.exchange.LoadSharedDataCommand;
import mobi.chouette.exchange.ProcessingCommands;
import mobi.chouette.exchange.ProcessingCommandsFactory;
import mobi.chouette.exchange.exporter.CompressCommand;

@Data
@Log4j
public class NetexStifExporterProcessingCommands implements ProcessingCommands {

	
	private static final String NO_FACTORIES = "unable to call factories";

	public static class DefaultFactory extends ProcessingCommandsFactory {

		@Override
		protected ProcessingCommands create() throws IOException {
			ProcessingCommands result = new NetexStifExporterProcessingCommands();
			return result;
		}
	}

	static {
		ProcessingCommandsFactory.register(NetexStifExporterProcessingCommands.class.getName(),
				new DefaultFactory());
	}

	@Override
	public List<Command> getPreProcessingCommands(Context context,boolean withDao) {
		InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
		List<Command> commands = new ArrayList<>();
		try {
			commands.add(CommandFactory.create(initialContext, NetexStifInitExportCommand.class.getName()));
			commands.add(CommandFactory.create(initialContext, LoadSharedDataCommand.class.getName()));
		} catch (Exception e) {
			log.error(e, e);
			throw new CoreRuntimeException(CoreExceptionCode.NO_FACTORY,NO_FACTORIES);
		}
		
		return commands;
	}

	@Override
	public List<Command> getLineProcessingCommands(Context context,boolean withDao) {
		InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
		List<Command> commands = new ArrayList<>();
		try {
			if (withDao)
				commands.add(CommandFactory.create(initialContext, DaoNetexStifLineProducerCommand.class.getName()));
			else
				commands.add(CommandFactory.create(initialContext, NetexStifLineProducerCommand.class.getName()));
		} catch (Exception e) {
			log.error(e, e);
			throw new CoreRuntimeException(CoreExceptionCode.NO_FACTORY,NO_FACTORIES);
		}
		
		return commands;
		
	}

	public List<Command> getPostLineProcessingCommands(Context context,boolean withDao) {
		InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
		List<Command> commands = new ArrayList<>();
		try {
			commands.add(CommandFactory.create(initialContext, NetexStifSharedOperatorDataProducerCommand.class.getName()));
		} catch (Exception e) {
			log.error(e, e);
			throw new CoreRuntimeException(CoreExceptionCode.NO_FACTORY,NO_FACTORIES);
		}
		return commands;
	}

	@Override
	public List<Command> getPostProcessingCommands(Context context,boolean withDao) {
		InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
		List<Command> commands = new ArrayList<>();
		try {
			// add ICAR and ILIGO export commands if necessary
			commands.add(CommandFactory.create(initialContext, DaoNetexStifSharedDataProducerCommand.class.getName()));
			commands.add(CommandFactory.create(initialContext, CompressCommand.class.getName()));
		} catch (Exception e) {
			log.error(e, e);
			throw new CoreRuntimeException(CoreExceptionCode.NO_FACTORY,NO_FACTORIES);
		}
		return commands;
	}

	@Override
	public List<Command> getStopAreaProcessingCommands(Context context, boolean withDao) {
		return new ArrayList<>();
	}
	
	@Override
	public List<Command> getDisposeCommands(Context context, boolean withDao) {
		return new ArrayList<>();
	}

}
