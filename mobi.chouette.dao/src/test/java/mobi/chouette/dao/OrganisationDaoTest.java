package mobi.chouette.dao;

import java.io.File;
import java.util.Locale;
import java.util.Map;

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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.testng.Assert;
import org.testng.annotations.Test;

import mobi.chouette.model.Organisation;
import mobi.chouette.persistence.hibernate.ContextHolder;


public class OrganisationDaoTest extends Arquillian
{
	@EJB 
	OrganisationDAO organisationDao;

	@PersistenceContext(unitName = "referential")
	EntityManager em;

	@Inject
	UserTransaction utx;

	@Deployment
	public static WebArchive createDeployment() {

		try
		{
		WebArchive result;
		File[] files = Maven.resolver().loadPomFromFile("pom.xml")
				.resolve("mobi.chouette:mobi.chouette.dao").withTransitivity().asFile();

		result = ShrinkWrap.create(WebArchive.class, "test.war").addAsWebInfResource("postgres-ds.xml")
				.addAsLibraries(files).addAsResource(EmptyAsset.INSTANCE, "beans.xml");
		return result;
		}
		catch (RuntimeException e)
		{
			System.out.println(e.getClass().getName());
			throw e;
		}

	}
	
	@Test
	public void checkSaveOrganisation()
	{
		ContextHolder.setContext("chouette_gui"); // set tenant schema
		Long id = null;
		{
			Organisation r = createOrganisation();
			try
			{
			organisationDao.create(r);
			}
			catch (RuntimeException ex)
			{
				System.err.println("Exception "+ex.getClass().getName()+" "+ex.getMessage()) ;
				ex.printStackTrace();
				Throwable e2 = ex.getCause();
				while (e2 != null)
				{
					System.err.println("caused by "+e2.getClass().getName()+" "+e2.getMessage()) ;
					e2.printStackTrace();
					if (e2 instanceof java.sql.SQLException)
					{
						java.sql.SQLException es = (java.sql.SQLException)(e2);
						e2 = es.getNextException();
					}
					else
					{
					e2=e2.getCause();
					}
				}
				throw ex;
			}
			organisationDao.flush();
			Assert.assertNotNull(r.getId(),"organisation id");
			id = r.getId();
		}
		{
			try {
				utx.begin();
		    em.joinTransaction();
			Organisation r = organisationDao.find(id);
			Assert.assertNotNull(r.getId(),"organisation id");
			Assert.assertEquals(r.getName(), "toto","organisation name");
			Assert.assertNotNull(r.getSsoAttributes(),"ssoAttributes");
			Map<String,String> md = r.getSsoAttributes();
			Assert.assertEquals(md.size(),2, "map size");
			organisationDao.delete(r);
			utx.commit();
			} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private Organisation createOrganisation()
	{
		Organisation r = new Organisation();
		r.setName("toto");
		r.getSsoAttributes().put("K1","V1");
		r.getSsoAttributes().put("K2","V2");
		return r;
	}
	

}
