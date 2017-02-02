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
import mobi.chouette.exchange.ProcessingCommands;
import mobi.chouette.exchange.ProcessingCommandsFactory;
import mobi.chouette.exchange.exporter.CompressCommand;
import mobi.chouette.exchange.exporter.SaveMetadataCommand;

@Data
@Log4j
public class NetexStifExporterProcessingCommands implements ProcessingCommands, Constant {

	
	public static class DefaultFactory extends ProcessingCommandsFactory {

		@Override
		protected ProcessingCommands create() throws IOException {
			ProcessingCommands result = new NetexStifExporterProcessingCommands();
			return result;
		}
	}

	static {
		ProcessingCommandsFactory.factories.put(NetexStifExporterProcessingCommands.class.getName(),
				new DefaultFactory());
	}

	@Override
	public List<? extends Command> getPreProcessingCommands(Context context,boolean withDao) {
		InitialContext initCtx = (InitialContext) context.get(INITIAL_CONTEXT);
		List<Command> commands = new ArrayList<>();
		try {
			commands.add(CommandFactory.create(initCtx, NetexStifInitExportCommand.class.getName()));
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException("unable to call factories");
		}
		
		return commands;
	}

	@Override
	public List<? extends Command> getLineProcessingCommands(Context context,boolean withDao) {
		InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
		List<Command> commands = new ArrayList<>();
		try {
			if (withDao)
				commands.add(CommandFactory.create(initialContext, DaoNetexStifLineProducerCommand.class.getName()));
			else
				commands.add(CommandFactory.create(initialContext, NetexStifLineProducerCommand.class.getName()));
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException("unable to call factories");
		}
		
		return commands;
		
	}

	@Override
	public List<? extends Command> getPostProcessingCommands(Context context,boolean withDao) {
		InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
		NetexStifExportParameters parameters = (NetexStifExportParameters) context.get(CONFIGURATION);
		List<Command> commands = new ArrayList<>();
		try {
			if (parameters.isAddMetadata())
				commands.add(CommandFactory.create(initialContext, SaveMetadataCommand.class.getName()));
			commands.add(CommandFactory.create(initialContext, CompressCommand.class.getName()));
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException("unable to call factories");
		}
		return commands;
	}

	@Override
	public List<? extends Command> getStopAreaProcessingCommands(Context context, boolean withDao) {
		return new ArrayList<>();
	}
	
	@Override
	public List<? extends Command> getDisposeCommands(Context context, boolean withDao) {
		List<Command> commands = new ArrayList<>();
		return commands;
	}

}
