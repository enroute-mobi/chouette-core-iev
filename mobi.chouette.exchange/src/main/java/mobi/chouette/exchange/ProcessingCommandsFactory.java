package mobi.chouette.exchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class ProcessingCommandsFactory {

	private static final Map<String, ProcessingCommandsFactory> factories = new HashMap<>();

	protected abstract ProcessingCommands create() throws IOException;

	public static final ProcessingCommands create(String name) throws ClassNotFoundException, IOException

	{
		if (!factories.containsKey(name)) {
			Class.forName(name);
			// log.info("[DSU] create : " + name);
			if (!factories.containsKey(name))
				throw new ClassNotFoundException(name);
		}
		return factories.get(name).create();
	}

	public static final void register(String name, ProcessingCommandsFactory factory) {
		factories.put(name, factory);
	}
}
