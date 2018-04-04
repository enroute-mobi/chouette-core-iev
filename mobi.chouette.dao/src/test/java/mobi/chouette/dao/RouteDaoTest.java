package mobi.chouette.dao;

import java.io.File;
import java.sql.SQLException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import lombok.extern.log4j.Log4j;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.Route;
import mobi.chouette.persistence.hibernate.ContextHolder;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.testng.Assert;
import org.testng.annotations.Test;

@Log4j
public class RouteDaoTest extends Arquillian {
	@EJB
	RouteDAO routeDao;

	@PersistenceContext(unitName = "referential")
	EntityManager em;

	@Inject
	UserTransaction utx;
	@Deployment
	public static WebArchive createDeployment() {

		try {
			WebArchive result;
			File[] files = Maven.resolver().loadPomFromFile("pom.xml").resolve("mobi.chouette:mobi.chouette.dao")
					.withTransitivity().asFile();

			result = ShrinkWrap.create(WebArchive.class, "test.war").addAsWebInfResource("postgres-ds.xml")
					.addAsLibraries(files).addAsResource(EmptyAsset.INSTANCE, "beans.xml");
			return result;
		} catch (RuntimeException e) {
			System.out.println(e.getClass().getName());
			throw e;
		}

	}

	@Test (priority=81)
	public void checkStopPoints() {
		Long id = null;
		try {
			ContextHolder.setContext("chouette_gui"); // set tenant schema
			Route route = new Route();
			route.setObjectId("Test:Route:1");
			for (int i = 0; i < 5; i++) {
				StopPoint sp = new StopPoint();
				sp.setObjectId("Test:StopPoint:" + i);
				sp.setPosition(2 * i);
				sp.setRoute(route);
			}
			routeDao.create(route);
			routeDao.flush();
			id = route.getId();

		} catch (RuntimeException ex) {
			Throwable cause = ex.getCause();
			while (cause != null) {
				log.error(cause);
				if (cause instanceof SQLException)
					traceSqlException((SQLException) cause);
				cause = cause.getCause();
			}
			throw ex;
		}
		try {
			utx.begin();
		    em.joinTransaction();
			ContextHolder.setContext("chouette_gui"); // set tenant schema
			Route route = routeDao.find(id);
			Assert.assertNotNull(route, "route should be found");
			Assert.assertEquals(route.getStopPoints().size(), 5, " route should have correct stopPoints count");
			for (int i = 0; i < 5; i++) {
				Assert.assertEquals(route.getStopPoints().get(i).getPosition(), Integer.valueOf(2 * i),
						"position should be preserved");
			}
			// routeDao.delete(route);
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		
		} catch (RuntimeException ex) {
			Throwable cause = ex.getCause();
			while (cause != null) {
				log.error(cause);
				if (cause instanceof SQLException)
					traceSqlException((SQLException) cause);
				cause = cause.getCause();
			}
			throw ex;
		}
	}

	private void traceSqlException(SQLException ex) {
		while (ex.getNextException() != null) {
			ex = ex.getNextException();
			log.error(ex);
		}
	}

}
