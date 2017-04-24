package mobi.chouette.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import lombok.extern.log4j.Log4j;
import mobi.chouette.model.RoutingConstraint;
import mobi.chouette.model.StopPoint;

@Log4j
public class NetexInterceptor extends EmptyInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2139978840612539482L;

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		log.info("Class :" + entity.getClass().getName());
		if (entity instanceof RoutingConstraint) {
			log.info("onSave for RoutingConstraint");
			RoutingConstraint routingConstraint = (RoutingConstraint) entity;
			List<StopPoint> stopPoints = routingConstraint.getStopPoints();
			Long ids[] = new Long[stopPoints.size()];
			int c = 0;
			for (StopPoint stopPoint : stopPoints) {
				ids[c++] = stopPoint.getId();
			}
			routingConstraint.setStopPointIds(ids);
			return true;
		} else {
			log.info("onSave other");
		}

		return false;
	}
}
