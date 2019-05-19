package mobi.chouette.model.type;

import java.util.Map;

public class DayMaskPair implements Map.Entry <Integer, DayTypeEnum> {
	private final Integer key;
    private DayTypeEnum value;

    public DayMaskPair(Integer key, DayTypeEnum value) {
        this.key = key;
        this.value = value;
    }
    
	@Override
	public Integer getKey() {
		return this.key;
	}

	@Override
	public DayTypeEnum getValue() {
		return this.value;
	}

	@Override
	public DayTypeEnum setValue(DayTypeEnum value) {
		DayTypeEnum old = this.value;
		this.value = value;
		return old;
	}
}

