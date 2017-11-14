package mobi.chouette.dao;

import java.io.File;
import java.util.stream.Collectors;

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

import lombok.extern.log4j.Log4j;
import mobi.chouette.model.compliance.ComplianceCheck;
import mobi.chouette.model.compliance.ComplianceCheck.CRITICITY;
import mobi.chouette.model.compliance.ComplianceCheckBlock;

@Log4j
public class ComplianceCheckDaoTest extends Arquillian {
	@EJB
	ComplianceCheckDAO complianceCheckDAO;

	@PersistenceContext(unitName = "public")
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

	@Test
	public void checkReadComplianceCheckTask() {
		try {
			utx.begin();
			em.joinTransaction();
			ComplianceCheck cc = complianceCheckDAO.find(1L);
			Assert.assertEquals( cc.getCode(), "3-NETEX-8");
			Assert.assertEquals( cc.getCriticity(), CRITICITY.error);
			Assert.assertEquals( cc.getComment(), "my comment");
			
			Assert.assertEquals("rien_ne_sert => de_courir", cc.getControlAttributes().keySet().stream()
					.map(x -> x + " => " + cc.getControlAttributes().get(x)).collect(Collectors.joining(", ")));
			
			
			ComplianceCheckBlock ccb  = cc.getBlock();
			Assert.assertEquals( ccb.getName(), "my_checkblock_00");
			
			Assert.assertEquals("la_vie => est_belle", ccb.getConditionAttributes().keySet().stream()
					.map(x -> x + " => " + ccb.getConditionAttributes().get(x)).collect(Collectors.joining(", ")));

			log.info("ComplianceCheck=" + cc);
			utx.commit();
		} catch (NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SystemException e) {
			e.printStackTrace();
		} 
	}

}
