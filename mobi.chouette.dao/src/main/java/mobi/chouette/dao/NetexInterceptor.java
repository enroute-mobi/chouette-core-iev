package mobi.chouette.dao;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.jamonapi.utils.Logger;

import lombok.extern.java.Log;

@Log
public class NetexInterceptor extends EmptyInterceptor {

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		Logger.logInfo("onSave");
		return false;
	}
}
