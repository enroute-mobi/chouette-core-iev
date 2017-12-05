package mobi.chouette.common.chain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;

public class ChainCommand implements Chain {

	public static final String COMMAND = "ChainCommand";

	private List<Command> commands = new ArrayList<>();

	@Getter
	@Setter
	private boolean ignored = false;

	@Override
	public void add(Command command) {
		commands.add(command);
	}
	
	@Getter
	private int executedCommands = 0;
	
	public int getCommandCount()
	{
		return commands.size();
	}

	@Override
	public boolean execute(Context context) throws Exception {

		if (context == null) {
			throw new IllegalArgumentException();
		}

		boolean result = Constant.SUCCESS;
		for (Command command : commands) {
			try {
				result = command.execute(context);
				if (result == Constant.ERROR && !ignored) {
					break;
				}
				executedCommands ++;
			} catch (Exception e) {
					throw e;
			}
		}
		return result;
	}

	@Override
	public void clear() {
		for (Command command : commands) {
			if (command instanceof Chain) {
				Chain chain = (Chain) command;
				chain.clear();
			}
		}
		commands.clear();
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			return new ChainCommand();
		}
	}

	static {
		CommandFactory.register(ChainCommand.class.getName(),
				new DefaultCommandFactory());
	}

}
