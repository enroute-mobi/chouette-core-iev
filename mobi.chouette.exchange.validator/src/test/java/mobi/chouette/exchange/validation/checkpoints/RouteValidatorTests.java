package mobi.chouette.exchange.validation.checkpoints;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.BasicConfigurator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.dao.RouteDAO;
import mobi.chouette.exchange.validator.JobDataTest;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.exchange.validator.checkpoints.CheckpointParameters;
import mobi.chouette.exchange.validator.checkpoints.RouteValidator;
import mobi.chouette.model.Route;

@Log4j
public class RouteValidatorTests extends AbstractTestValidation {
	
	@EJB 
	RouteDAO dao;
	
	@Deployment
	public static EnterpriseArchive createDeployment() {

		EnterpriseArchive result;
		File[] files = Maven.resolver().loadPomFromFile("pom.xml")
				.resolve("mobi.chouette:mobi.chouette.exchange.validator").withTransitivity().asFile();
		List<File> jars = new ArrayList<>();
		List<JavaArchive> modules = new ArrayList<>();
		for (File file : files) {
			if (file.getName().startsWith("mobi.chouette.exchange"))
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
		File[] filesDao = Maven.resolver().loadPomFromFile("pom.xml")
				.resolve("mobi.chouette:mobi.chouette.dao").withTransitivity().asFile();
		if (filesDao.length == 0) 
		{
			throw new NullPointerException("no dao");
		}
		for (File file : filesDao) {
			if (file.getName().startsWith("mobi.chouette.dao"))
			{
				String name = file.getName().split("\\-")[0]+".jar";
				
				JavaArchive archive = ShrinkWrap
						  .create(ZipImporter.class, name)
						  .importFrom(file)
						  .as(JavaArchive.class);
				modules.add(archive);
				if (!modules.contains(archive))
				   modules.add(archive);
			}
			else
			{
				if (!jars.contains(file))
				   jars.add(file);
			}
		}
		final WebArchive testWar = ShrinkWrap.create(WebArchive.class, "test.war").addAsWebInfResource("postgres-ds.xml")
				.addClass(JobDataTest.class)
				.addClass(AbstractTestValidation.class)
				.addClass(RouteValidatorTests.class);
		
		result = ShrinkWrap.create(EnterpriseArchive.class, "test.ear")
				.addAsLibraries(jars.toArray(new File[0]))
				.addAsModules(modules.toArray(new JavaArchive[0]))
				.addAsModule(testWar)
				.addAsResource(EmptyAsset.INSTANCE, "beans.xml");
		return result;
	}
	
	@BeforeTest
	public void initTest()
	{
		if (em == null) return; // workaround arquillian invoke Before.. twice : first in client mode
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		try {
			initSchema();
		} catch (Exception e) {
			log.error("cannot init schema",e);
		}
	}
	
	
	/**
	 * @throws Exception
	 */
	@Test(groups = { "route" }, description = "3_Route_1", priority = 1)
	public void verifyTest_3_Route_1() throws Exception {
		
		Context context = initValidatorContext();
		loadSharedData(context);
		Route route = dao.find(Long.valueOf(2));
		Assert.assertNotNull(route,"route id 2 not found");
		RouteValidator validator = new RouteValidator();
		ValidateParameters parameters = (ValidateParameters) context.get(CONFIGURATION);
		Collection<CheckpointParameters> checkPoints = new ArrayList<>();
		CheckpointParameters checkPoint = new CheckpointParameters(L3_Route_1,false,null,null);		
		checkPoints.add(checkPoint);
		parameters.getControlParameters().getGlobalCheckPoints().put(L3_Route_1, checkPoints);
		String transportMode = "Bus";
		validator.validate(context, route, parameters, transportMode);
		
		// TODO : contrôler les retours
		
	}	
	

}
