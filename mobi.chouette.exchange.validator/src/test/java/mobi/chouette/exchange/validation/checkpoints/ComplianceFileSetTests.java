/**
 *  !!! Generated Code (2017-10-09 16:47:20)!!!
 *  Do not edit !
 *  Created by create_ComplianceFileSetTests.py
 */

package mobi.chouette.exchange.validation.checkpoints;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.testng.annotations.Test;

public class ComplianceFileSetTests extends AbstractComplianceFileSetTests {

	@Deployment
	public static EnterpriseArchive createDeployment() {
		return AbstractComplianceFileSetTests.createDeployment(ComplianceFileSetTests.class);
	}

//	@Test(groups = {
//			"2100" }, testName = "2100-04", description = "[Itinéraire] Existence d’une mission passant par tous les arrêts de l'itinéraire", priority = 1)
//	public void verifyCard2100_04() throws Exception {
//		doValidation("testcompliance_2100_04", "OK", "a", "b");
//	}
//
//	@Test(groups = {
//			"2100" }, testName = "2100-05", description = "[Itinéraire] Existence d’une mission passant par tous les arrêts de l'itinéraire", priority = 2)
//	public void verifyCard2100_05() throws Exception {
//		doValidation("testcompliance_2100_05", "NOK", "aaaa", "bb");
//	}
}
