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
import mobi.chouette.model.compliance.ComplianceCheckTask;

@Log4j
public class ComplianceTaskDaoTest extends Arquillian {
	@EJB
	ComplianceCheckTaskDAO complianceCheckTaskDAO;

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

	@Test (priority=41)
	public void checkReadComplianceCheckTask() {
		try {
			utx.begin();
			em.joinTransaction();
			ComplianceCheckTask cct = complianceCheckTaskDAO.find(1L);

			Assert.assertEquals(new Long(1L), cct.getId());
			Assert.assertEquals("OKAY", cct.getStatus());
			Assert.assertEquals("name0", cct.getName());
			Assert.assertEquals("8", cct.getCurrentStepId());
			Assert.assertEquals(52d, cct.getCurrentStepProgress());
			Assert.assertEquals(new Long(1L), cct.getWorkbenchId());

			ComplianceCheckBlock ccb = cct.getComplianceCheckBlocks().get(0);

			Assert.assertEquals("la_vie => est_belle", ccb.getConditionAttributes().keySet().stream()
					.map(x -> x + " => " + ccb.getConditionAttributes().get(x)).collect(Collectors.joining(", ")));

			ComplianceCheck cc = cct.getComplianceChecks().get(0);

			Assert.assertEquals("3-NETEX-8", cc.getOriginCode());

			Assert.assertEquals(CRITICITY.error, cc.getCriticity());

			Assert.assertEquals("my comment", cc.getComment());

			log.info("ComplianceTask=" + cct);
			utx.commit();
		} catch (NotSupportedException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SystemException e) {
			e.printStackTrace();
		} 
	}

}
