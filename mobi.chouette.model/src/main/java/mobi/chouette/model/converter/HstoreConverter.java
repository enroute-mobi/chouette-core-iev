package mobi.chouette.model.converter;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.postgresql.util.HStoreConverter;

@Converter(autoApply = true)
public class HstoreConverter implements AttributeConverter<Map<String, String>, String> {
    @Override
    public String convertToDatabaseColumn(Map<String, String> attribute) {
        
			return HStoreConverter.toString(attribute);
		
    }

    @SuppressWarnings("unchecked")
	@Override
    public Map<String, String> convertToEntityAttribute(String dbData) {
    	if (dbData == null) return new HashMap<String, String>();
        return HStoreConverter.fromString(dbData);
    }

}
