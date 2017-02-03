package mobi.chouette.exchange.netex_stif.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class NetexStifObjectFactory {
	
	@Getter
	@Setter
	private Map<String, Direction> direction= new HashMap<>();
	
	public Direction getDirection (String objectId){
		Direction result = direction.get(objectId);
		if (result == null){
			result = new Direction ();
			result.setObjectId(objectId);
			direction.put(objectId, result);
		}
		return result;
	}
	
	public void clear(){
		direction.clear();
	}
	
	public void dispose()
	{
	     clear();
	}
}
