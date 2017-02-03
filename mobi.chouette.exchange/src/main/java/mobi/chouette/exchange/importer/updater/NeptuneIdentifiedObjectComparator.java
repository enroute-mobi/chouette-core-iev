package mobi.chouette.exchange.importer.updater;

import java.util.Comparator;

import mobi.chouette.model.ChouetteIdentifiedObject;

public class NeptuneIdentifiedObjectComparator implements
		Comparator<ChouetteIdentifiedObject> {
	public static final Comparator<ChouetteIdentifiedObject> INSTANCE = new NeptuneIdentifiedObjectComparator();

	@Override
	public int compare(ChouetteIdentifiedObject left,
			ChouetteIdentifiedObject right) {
		return left.getObjectId().compareTo(right.getObjectId());
	}
}
