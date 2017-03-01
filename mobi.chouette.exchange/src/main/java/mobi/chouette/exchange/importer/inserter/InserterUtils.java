package mobi.chouette.exchange.importer.inserter;

import java.util.ArrayList;
import java.util.Collection;

import mobi.chouette.model.ChouetteIdentifiedObject;

public class InserterUtils {

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
