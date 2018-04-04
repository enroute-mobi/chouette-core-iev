package mobi.chouette.dao;

import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
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

import mobi.chouette.common.JobData;
import mobi.chouette.model.ActionTask;
import mobi.chouette.model.Referential;
import mobi.chouette.model.ReferentialMetadata;
import mobi.chouette.model.importer.ImportTask;
import mobi.chouette.model.type.DateRange;
import mobi.chouette.persistence.hibernate.ContextHolder;

public class ActionDaoTest extends Arquillian {
	@EJB
	ReferentialDAO referentialDao;
	
	@EJB 
	ImportTaskDAO importDao;
	
	@EJB 
	ActionDAO actionDao;

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
				.addClass(JobDataImpl.class)
				.addAsLibraries(files).addAsResource(EmptyAsset.INSTANCE, "beans.xml");
		return result;

	}

	@Test (priority=1)
	public void checkSaveReferential() {
		ContextHolder.setContext("chouette_gui"); // set tenant schema
		Long refId = null;
		Long taskId = null;
		{
			try {

				Referential r = createReferential();
				referentialDao.create(r);
				referentialDao.flush();
				Assert.assertNotNull(r.getId(), "referential id");
				refId = r.getId();
				ImportTask task = createImportTask(r);
				importDao.create(task);
				importDao.flush();
				taskId = task.getId();
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
				JobData job = new JobDataImpl(taskId,JobData.ACTION.importer);
				
				ActionTask ac = actionDao.getTask(job);
				Assert.assertNotNull(ac.getId(), "Action id");
				Assert.assertNotNull(ac.getReferential(), "Action referential");
				Assert.assertFalse(ac.getReferential().getMetadatas().isEmpty(), "Action referential metadata");

//				Assert.assertEquals(r.getName(), "toto", "referential name");
//				Assert.assertNotEquals(r.getMetadatas().size(), 0, "metadata not empty");
//				ReferentialMetadata md = r.getMetadatas().get(0);
//				Assert.assertEquals(md.getLineIds().length, 4, "lines size");
//				Assert.assertEquals(md.getPeriods().length, 2, "periods size");
				importDao.delete((ImportTask) ac);
				referentialDao.delete(referentialDao.find(refId));
				utx.commit();
			} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException
					| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				e.printStackTrace();
			}
		}

	}

	private ImportTask createImportTask(Referential r) {
		ImportTask t = new ImportTask();
		t.setReferential(r);
		t.setFile("toto");
		t.setUrl("http://toto.com");
		t.setFormat("netex_stif");
		t.setCreatedAt(new Timestamp((new java.util.Date()).getTime()));
 		return t;
	}

	private Referential createReferential() {
		Referential r = new Referential();
		r.setName("toto");
		r.setLineReferentialId(1L);
		r.setStopAreaReferentialId(1L);
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
		r.getMetadatas().clear();
		r.getMetadatas().add(md);
		return r;
	}
	
	

}
