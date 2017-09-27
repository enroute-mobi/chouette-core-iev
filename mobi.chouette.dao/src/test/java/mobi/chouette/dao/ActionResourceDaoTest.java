package mobi.chouette.dao;

import java.io.File;

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

import org.apache.log4j.BasicConfigurator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.testng.Assert;
import org.testng.annotations.Test;

import mobi.chouette.model.ImportResource;
import mobi.chouette.persistence.hibernate.ContextHolder;

public class ActionResourceDaoTest extends Arquillian {
	
	@EJB 
	ImportResourceDAO importResourceDao;
	
	@EJB 
	ActionResourceDAO actionResourceDao;

	@PersistenceContext(unitName = "public")
	EntityManager em;

	@Inject
	UserTransaction utx;

	@Deployment
	public static WebArchive createDeployment() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		WebArchive result;
		File[] files = Maven.resolver().loadPomFromFile("pom.xml").resolve("mobi.chouette:mobi.chouette.dao")
				.withTransitivity().asFile();

		result = ShrinkWrap.create(WebArchive.class, "test.war").addAsWebInfResource("postgres-ds.xml")
				.addClass(TestJobData.class)
				.addAsLibraries(files).addAsResource(EmptyAsset.INSTANCE, "beans.xml");
		return result;

	}

	@Test
	public void checkSaveImportResource() {
		ContextHolder.setContext("chouette_gui"); // set tenant schema
		Long resId = null;
		{
			try {

				ImportResource r = new ImportResource(12L);
				r.setStatus("status");
				r.getMetrics().put("key", "value");
				importResourceDao.create(r);
				importResourceDao.flush();
				Assert.assertNotNull(r.getId(), "resource id");
				resId = r.getId();
			} catch (RuntimeException ex) {
				System.err.println("Exception " + ex.getClass().getName() + " " + ex.getMessage());
				ex.printStackTrace();
				Throwable e2 = ex.getCause();
				while (e2 != null) {
					System.err.println("caused by " + e2.getClass().getName() + " " + e2.getMessage());
					e2.printStackTrace();
					if (e2 instanceof java.sql.SQLException) {
						java.sql.SQLException es = (java.sql.SQLException) (e2);
						e2 = es.getNextException();
					} else {
						e2 = e2.getCause();
					}
				}
				throw ex;
			}

		}
		{
			try {
				utx.begin();
				em.joinTransaction();
				
				ImportResource r = importResourceDao.find(resId);
				Assert.assertNotNull(r.getId(), "Action id");
				Assert.assertEquals(r.getMetrics().size(), 1, "metrics not empty");

//				Assert.assertEquals(r.getName(), "toto", "referential name");
//				Assert.assertNotEquals(r.getMetadatas().size(), 0, "metadata not empty");
//				ReferentialMetadata md = r.getMetadatas().get(0);
//				Assert.assertEquals(md.getLineIds().length, 4, "lines size");
//				Assert.assertEquals(md.getPeriods().length, 2, "periods size");
				importResourceDao.delete(r);
				utx.commit();
			} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException
					| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	

}
