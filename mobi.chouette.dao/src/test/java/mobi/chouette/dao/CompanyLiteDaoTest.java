package mobi.chouette.dao;

import java.io.File;
import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.testng.Assert;
import org.testng.annotations.Test;

import mobi.chouette.model.CompanyLite;


public class CompanyLiteDaoTest extends Arquillian
{
	@EJB 
	CompanyLiteDAO companyLiteDao;


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
	
	@Test (priority=21)
	public void checkReadCompanies()
	{
		List<CompanyLite> companies = companyLiteDao.findAll(1L);
		Assert.assertNotEquals(companies.size(), 0,"company list");
		for (CompanyLite companyLite : companies) {
			Assert.assertNotNull(companyLite.getId(),"company id");
		}
	}
	

}
