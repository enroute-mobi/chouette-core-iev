package mobi.chouette.exchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public abstract class InputValidatorFactory {

	   public static final Map<String, InputValidatorFactory> factories = new HashMap<>();

	   protected abstract InputValidator create() throws IOException;

	   public static final InputValidator create(String name)
	         throws ClassNotFoundException, IOException

	   {
	      if (!factories.containsKey(name))
	      {
	         Class.forName(name);
	         if (!factories.containsKey(name))
	            throw new ClassNotFoundException(name);
	      }
	      return factories.get(name).create();
	   }
}
