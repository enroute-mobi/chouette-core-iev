package mobi.chouette.dao;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.jamonapi.utils.Logger;

import lombok.extern.log4j.Log4j;

@Log4j
public class NetexInterceptor extends EmptyInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2139978840612539482L;

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		log.info("onSave");
		return false;
	}
}
