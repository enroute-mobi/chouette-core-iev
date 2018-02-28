package mobi.chouette.exchange.netex_stif.importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.FileUtil;
import mobi.chouette.common.JobData;
import mobi.chouette.common.chain.Chain;
import mobi.chouette.common.chain.ChainCommand;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.core.CoreExceptionCode;
import mobi.chouette.core.CoreRuntimeException;
import mobi.chouette.exchange.LoadSharedDataCommand;
import mobi.chouette.exchange.ProcessingCommands;
import mobi.chouette.exchange.ProcessingCommandsFactory;
import mobi.chouette.exchange.importer.CleanRepositoryCommand;
import mobi.chouette.exchange.importer.CopyCommand;
import mobi.chouette.exchange.importer.FootnoteRegisterCommand;
import mobi.chouette.exchange.importer.RouteRegisterCommand;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;

@Data
@Log4j
public class NetexStifImporterProcessingCommands implements ProcessingCommands {

	private static final String NO_FACTORIES = "unable to call factories";

	public static class DefaultFactory extends ProcessingCommandsFactory {

		@Override
		protected ProcessingCommands create() throws IOException {
			ProcessingCommands result = new NetexStifImporterProcessingCommands();
			return result;
		}
	}

	static {
		ProcessingCommandsFactory.register(NetexStifImporterProcessingCommands.class.getName(),
				new DefaultFactory());
	}

	@Override
	public List<Command> getPreProcessingCommands(Context context, boolean withDao) {
		InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
		NetexStifImportParameters parameters = (NetexStifImportParameters) context.get(Constant.CONFIGURATION);
		List<Command> commands = new ArrayList<>();
		try {
			if (withDao && parameters.isCleanRepository()) {
				commands.add(CommandFactory.create(initialContext, CleanRepositoryCommand.class.getName()));
			}
			commands.add(CommandFactory.create(initialContext, NetexStifUncompressCommand.class.getName()));
			commands.add(CommandFactory.create(initialContext, NetexStifInitImportCommand.class.getName()));
			commands.add(CommandFactory.create(initialContext, LoadSharedDataCommand.class.getName()));
		} catch (Exception e) {
			log.error(e, e);
			throw new CoreRuntimeException(CoreExceptionCode.NO_FACTORY,NO_FACTORIES);
		}

		return commands;
	}

	private Chain treatOneFile(Context context, String filename, JobData jobData, boolean mandatory)
			throws ClassNotFoundException, IOException {
		InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
		String path = jobData.getPathName();
		Chain chain = (Chain) CommandFactory.create(initialContext, ChainCommand.class.getName());
		File file = new File(path + File.separatorChar + Constant.INPUT + File.separatorChar + filename);
		if (!file.exists()) {
			if (mandatory) {
				// may not occurs : rejected by NetexStifUncompressCommand
				NetexStifImportParameters parameters = (NetexStifImportParameters) context.get(Constant.CONFIGURATION);
				parameters.setNoSave(true); // block save mode to check other
											// files
			}
			return chain;
		}

		String url = file.toURI().toURL().toExternalForm();
		log.info("url : " + url);
		// validation schema
		NetexStifSAXParserCommand schema = (NetexStifSAXParserCommand) CommandFactory.create(initialContext,
				NetexStifSAXParserCommand.class.getName());
		schema.setFileURL(url);
		chain.add(schema);
		NetexStifParserCommand parser = (NetexStifParserCommand) CommandFactory.create(initialContext,
				NetexStifParserCommand.class.getName());
		parser.setFileURL(url);
		chain.add(parser);
		Command validation = CommandFactory.create(initialContext, NetexStifValidationCommand.class.getName());
		chain.add(validation);
		return chain;
	}

	@Override
	public List<Command> getLineProcessingCommands(Context context, boolean withDao) {
		InitialContext initialContext = (InitialContext) context.get(Constant.INITIAL_CONTEXT);
		NetexStifImportParameters parameters = (NetexStifImportParameters) context.get(Constant.CONFIGURATION);
		List<Command> commands = new ArrayList<>();
		JobData jobData = (JobData) context.get(Constant.JOB_DATA);
		try {
			Chain tmp = treatOneFile(context, NetexStifConstant.CALENDRIER_FILE_NAME, jobData, true);
			commands.add(tmp);
			tmp = treatOneFile(context, NetexStifConstant.COMMUN_FILE_NAME, jobData, false);
			commands.add(tmp);

			Path path = Paths.get(jobData.getPathName(), Constant.INPUT);
			List<Path> stream = FileUtil.listFiles(path, NetexStifConstant.OFFRE_FILE_PREFIX+"*.xml", "*metadata*");
			for (Path file : stream) {
				Chain chain = (Chain) CommandFactory.create(initialContext, ChainCommand.class.getName());
				commands.add(chain);
				String url = file.toUri().toURL().toExternalForm();
				// validation schema
				NetexStifSAXParserCommand schema = (NetexStifSAXParserCommand) CommandFactory.create(initialContext,
						NetexStifSAXParserCommand.class.getName());
				schema.setFileURL(url);
				chain.add(schema);

				// parser
				NetexStifParserCommand parser = (NetexStifParserCommand) CommandFactory.create(initialContext,
						NetexStifParserCommand.class.getName());
				parser.setFileURL(url);
				chain.add(parser);

				// validation
				Command validation = CommandFactory.create(initialContext, NetexStifValidationCommand.class.getName());
				chain.add(validation);

				if (withDao && !parameters.isNoSave()) {

					// register
					Command register = CommandFactory.create(initialContext, FootnoteRegisterCommand.class.getName());
					chain.add(register);
					register = CommandFactory.create(initialContext, RouteRegisterCommand.class.getName());
					chain.add(register);

					// save passing times
					Command copy = CommandFactory.create(initialContext, CopyCommand.class.getName());
					chain.add(copy);
				}

			}

		} catch (Exception e) {
			log.error(e, e);
			throw new CoreRuntimeException(CoreExceptionCode.NO_FACTORY,NO_FACTORIES);

		}

		return commands;
	}

	@Override
	public List<Command> getPostProcessingCommands(Context context, boolean withDao) {

		return new ArrayList<>();
	}

	@Override
	public List<Command> getStopAreaProcessingCommands(Context context, boolean withDao) {
		return new ArrayList<>();
	}

	@Override
	public List<Command> getDisposeCommands(Context context, boolean withDao) {
		 InitialContext initialContext = (InitialContext)
		 context.get(Constant.INITIAL_CONTEXT);
		List<Command> commands = new ArrayList<>();
		try {
			commands.add(CommandFactory.create(initialContext, NetexStifDisposeImportCommand.class.getName()));
		} catch (Exception e) {
			log.error(e, e);
			throw new CoreRuntimeException(CoreExceptionCode.NO_FACTORY,NO_FACTORIES);
		}
		return commands;
	}

}
