package mobi.chouette.model.util;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ChouetteModelUtilTest {
	@Test(groups = { "chouetteModel" }, description = "change prefix")
	public void testChangePrefix() throws Exception 
	{
		String changed = ChouetteModelUtil.changePrefix("CITYWAY:PassengerStopAssignment:1-2:LOC", "TOTO");
		Assert.assertEquals(changed,"TOTO:PassengerStopAssignment:1-2:LOC","prefix should be changed");
	}
	@Test(groups = { "chouetteModel" }, description = "change suffix")
	public void testChangeSuffix() throws Exception 
	{
		String changed = ChouetteModelUtil.changeSuffix("CITYWAY:PassengerStopAssignment:1-2:LOC", "2-1");
		Assert.assertEquals(changed,"CITYWAY:PassengerStopAssignment:2-1:LOC","suffix should be changed");
		changed = ChouetteModelUtil.changeSuffix("CITYWAY:PassengerStopAssignment:1-2", "2-1");
		Assert.assertEquals(changed,"CITYWAY:PassengerStopAssignment:2-1","suffix should be changed");
	}

}
