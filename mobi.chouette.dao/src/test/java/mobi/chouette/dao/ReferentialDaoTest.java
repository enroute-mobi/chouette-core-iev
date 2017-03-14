package mobi.chouette.dao;

import java.io.File;
import java.sql.Date;
import java.util.Calendar;

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

import mobi.chouette.model.Referential;
import mobi.chouette.model.ReferentialMetadata;
import mobi.chouette.model.type.DateRange;
import mobi.chouette.persistence.hibernate.ContextHolder;

public class ReferentialDaoTest extends Arquillian {
	@EJB
	ReferentialDAO referentialDao;

	@PersistenceContext(unitName = "referential")
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
					.addAsLibraries(files).addAsResource(EmptyAsset.INSTANCE, "beans.xml");
			return result;

	}

	@Test
	public void checkSaveReferential() {
		ContextHolder.setContext("chouette_gui"); // set tenant schema
		Long id = null;
		{
			Referential r = createReferential();
			referentialDao.create(r);
			referentialDao.flush();
			Assert.assertNotNull(r.getId(), "referential id");
			id = r.getId();
		}
		{
			try {
				utx.begin();
				em.joinTransaction();
				Referential r = referentialDao.find(id);
				Assert.assertNotNull(r.getId(), "referential id");
				Assert.assertEquals(r.getName(), "toto", "referential name");
				Assert.assertNotNull(r.getMetadata(), "metadata");
				ReferentialMetadata md = r.getMetadata();
				Assert.assertEquals(md.getLineIds().length, 4, "lines size");
				Assert.assertEquals(md.getPeriods().length, 2, "periods size");
				referentialDao.delete(r);
				utx.commit();
			} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException
					| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			catch (RuntimeException ex) {
//				System.err.println("Exception " + ex.getClass().getName() + " " + ex.getMessage());
//				ex.printStackTrace();
//				Throwable e2 = ex.getCause();
//				while (e2 != null) {
//					System.err.println("caused by " + e2.getClass().getName() + " " + e2.getMessage());
//					e2.printStackTrace();
//					if (e2 instanceof java.sql.SQLException) {
//						java.sql.SQLException es = (java.sql.SQLException) (e2);
//						e2 = es.getNextException();
//					} else {
//						e2 = e2.getCause();
//					}
//				}
//				throw ex;
//			}
		}

	}

	private Referential createReferential() {
		Referential r = new Referential();
		r.setName("toto");
		ReferentialMetadata md = new ReferentialMetadata();
		Long[] lids = new Long[] { 1L, 2L, 3L, 4L };
		md.setLineIds(lids);
		DateRange[] periods = new DateRange[2];
		Calendar c = Calendar.getInstance();
		Date first1 = new Date(c.getTimeInMillis());
		c.add(Calendar.DATE, 5);
		Date last1 = new Date(c.getTimeInMillis());
		periods[0] = new DateRange(first1, last1);
		c.add(Calendar.DATE, 2);
		Date first2 = new Date(c.getTimeInMillis());
		c.add(Calendar.DATE, 5);
		Date last2 = new Date(c.getTimeInMillis());
		periods[1] = new DateRange(first2, last2);
		md.setPeriods(periods);
		r.setMetadata(md);
		return r;
	}

}
