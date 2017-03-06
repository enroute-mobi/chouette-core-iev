package mobi.chouette.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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

	@PersistenceContext(unitName = "public")
	EntityManager em;

	@Inject
	UserTransaction utx;

	@Deployment
	public static EnterpriseArchive createDeployment() {

		EnterpriseArchive result;
		File[] files = Maven.resolver().loadPomFromFile("pom.xml")
				.resolve("mobi.chouette:mobi.chouette.dao").withTransitivity().asFile();
		List<File> jars = new ArrayList<>();
		List<JavaArchive> modules = new ArrayList<>();
		for (File file : files) {
			if (file.getName().startsWith("mobi.chouette.dao"))
			{
				String name = file.getName().split("\\-")[0]+".jar";
				JavaArchive archive = ShrinkWrap
						  .create(ZipImporter.class, name)
						  .importFrom(file)
						  .as(JavaArchive.class);
				modules.add(archive);
			}
			else
			{
				jars.add(file);
			}
		}
		final WebArchive testWar = ShrinkWrap.create(WebArchive.class, "test.war").addAsWebInfResource("postgres-ds.xml")
				.addClass(OrganisationDaoTest.class);
		
		result = ShrinkWrap.create(EnterpriseArchive.class, "test.ear")
				.addAsLibraries(jars.toArray(new File[0]))
				.addAsModules(modules.toArray(new JavaArchive[0]))
				.addAsModule(testWar)
				.addAsResource(EmptyAsset.INSTANCE, "beans.xml");
		return result;
	}

 	// @Deployment
	public static WebArchive createDeploymentOld() {

		try
		{
		WebArchive result;
		File[] files = Maven.resolver().loadPomFromFile("pom.xml")
				.resolve("mobi.chouette:mobi.chouette.dao").withTransitivity().asFile();

		for (File file : files) {
			System.out.println(file.getName());
		}
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
