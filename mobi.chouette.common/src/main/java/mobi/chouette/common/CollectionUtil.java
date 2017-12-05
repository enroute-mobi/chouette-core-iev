package mobi.chouette.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class CollectionUtil {

	private CollectionUtil() {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Serializable> Collection<Pair<T, T>> intersection(Collection<T> oldList, Collection<T> newList,
			java.util.Comparator comparator) {
		Collection<Pair<T, T>> result = new ArrayList<>();
		for (T oldValue : oldList) {
			for (T newValue : newList) {
				if (comparator.compare(oldValue, newValue) == 0) {
					result.add(Pair.of(oldValue, newValue));
				}
			}
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Collection<T> substract(Collection<T> oldList, Collection<T> newList,
			java.util.Comparator comparator) {
		Collection<T> result = new ArrayList<>(oldList);
		for (T oldValue : oldList) {
			for (T newValue : newList) {
				if (comparator.compare(oldValue, newValue) == 0) {
					result.remove(oldValue);
				}
			}
		}
		return result;
	}

}
