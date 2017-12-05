package mobi.chouette.ws;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import lombok.extern.log4j.Log4j;

@Log4j
public class Application extends javax.ws.rs.core.Application implements
		ServletContextListener {

	@Context
	private UriInfo uriInfo;
	
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> result = new HashSet<>();
		result.add(RestService.class);
		return result;
	}

	@Override
	public Set<Object> getSingletons() {
		Set<Object> result = new HashSet<>();
		return result;
	}

	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> result = new HashMap<>();
		return result;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Logger elog = Logger.getLogger("org.jboss.resteasy.core.ExceptionHandler");
		elog.setLevel(Level.ERROR);

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
        log.info("context destroyed");
	}

}
