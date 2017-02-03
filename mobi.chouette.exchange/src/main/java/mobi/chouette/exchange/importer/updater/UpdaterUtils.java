package mobi.chouette.exchange.importer.updater;

import java.util.ArrayList;
import java.util.Collection;

import mobi.chouette.model.ChouetteIdentifiedObject;

public class UpdaterUtils {

	public static Collection<String> getObjectIds(Collection<?> list) {
		final Collection<String> result = new ArrayList<String>();
		for (Object o : list) {
			if (o instanceof ChouetteIdentifiedObject) {
				result.add(((ChouetteIdentifiedObject) o).getObjectId());
			}
		}
		return result;
	}
}
