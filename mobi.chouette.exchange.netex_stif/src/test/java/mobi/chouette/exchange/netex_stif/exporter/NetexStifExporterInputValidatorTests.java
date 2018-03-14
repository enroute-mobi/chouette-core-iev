package mobi.chouette.exchange.netex_stif.exporter;

import java.sql.Date;
import java.util.Calendar;
import java.util.Locale;

import org.apache.log4j.BasicConfigurator;
import org.testng.Assert;
import org.testng.annotations.Test;

import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.model.Organisation;
import mobi.chouette.model.Referential;
import mobi.chouette.model.ReferentialMetadata;
import mobi.chouette.model.exporter.ExportTask;
import mobi.chouette.model.type.DateRange;

public class NetexStifExporterInputValidatorTests {

	private void init() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		Locale.setDefault(Locale.ENGLISH);
	}

	private ExportTask createTask(Referential referential, boolean full) {
		ExportTask task = new ExportTask();

		task.setId(1L);
		task.setReferential(referential);
		if (full) {
			task.getOptions().put("export_type", "full");
			task.getOptions().put("duration", "28");
		} else {
			task.getOptions().put("export_type", "line");
			task.getOptions().put("duration", "365");
			task.getOptions().put("line_id", "2");
		}
		return task;
	}

	@Test(groups = { "input" }, description = "toActionParameter", priority = 3010)
	public void validateToActionParameter() throws Exception {
		init();
		Referential referential = new Referential();
		referential.setId(1L);
		referential.setName("referential");
		referential.setLineReferentialId(3L);
		referential.setStopAreaReferentialId(3L);
		referential.setOrganisation(new Organisation());
		referential.getOrganisation().setId(2L);
		referential.getOrganisation().setName("organisation");
		referential.getOrganisation().setCode("code_orga");
		ReferentialMetadata metadata = new ReferentialMetadata();
		referential.getMetadatas().add(metadata);
		metadata.setLineIds(new Long[] { 1L, 2L });
		metadata.setPeriods(new DateRange[1]);
		Calendar c = Calendar.getInstance();
		Date start = new Date(c.getTime().getTime());
		c.add(Calendar.DATE, 180);
		Date end = new Date(c.getTime().getTime());
		metadata.getPeriods()[0] = new DateRange(start,end);
		ExportTask task = createTask(referential, true);
		NetexStifExporterInputValidator validator = new NetexStifExporterInputValidator();
		Assert.assertNotNull(validator,"validator should be created");
		AbstractParameter parameters  = null;
		try
		{
		 parameters = validator.toActionParameter(task);
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			throw e;
		}
		
		Assert.assertTrue(parameters instanceof NetexStifExportParameters, " instance of NetexStifExportParameters");
		NetexStifExportParameters params = (NetexStifExportParameters) parameters;

		// check data in parameter
        Assert.assertNotNull(params.getExportId(),"ExportId should be set");
        Assert.assertNotNull(params.getLineReferentialId(),"LineReferentialId should be set");
        Assert.assertNotNull(params.getStopAreaReferentialId(),"StopAreaReferentialId should be set");
        Assert.assertNotNull(params.getReferentialId(),"ReferentialId should be set");
        Assert.assertNotNull(params.getOrganisationCode(),"OrganisationCode should be set");
        Assert.assertNotNull(params.getOrganisationName(),"OrganisationName should be set");
        Assert.assertNotNull(params.getReferencesType(),"ReferencesType should be set");
        Assert.assertEquals(params.getReferencesType(),"line","ReferencesType should be set to line");
		Assert.assertNull(params.getIds(), "Ids should be null for full");
		Assert.assertNotNull(params.getStartDate(), "StartDate should be set");
		Assert.assertNotNull(params.getEndDate(), "EndDate should be set");
		Assert.assertTrue(params.getEndDate().after(params.getStartDate()), "dates should be ordered");

		// one line
		task = createTask(referential, false);
		try
		{
		 parameters = validator.toActionParameter(task);
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			throw e;
		}
		Assert.assertTrue(parameters instanceof NetexStifExportParameters, " instance of NetexStifExportParameters");
		params = (NetexStifExportParameters) parameters;
        Assert.assertNotNull(params.getExportId(),"ExportId should be set");
        Assert.assertNotNull(params.getLineReferentialId(),"LineReferentialId should be set");
        Assert.assertNotNull(params.getStopAreaReferentialId(),"StopAreaReferentialId should be set");
        Assert.assertNotNull(params.getReferentialId(),"ReferentialId should be set");
        Assert.assertNotNull(params.getOrganisationCode(),"OrganisationCode should be set");
        Assert.assertNotNull(params.getOrganisationName(),"OrganisationName should be set");
        Assert.assertNotNull(params.getReferencesType(),"ReferencesType should be set");
        Assert.assertEquals(params.getReferencesType(),"line","ReferencesType should be set to line");
		Assert.assertNotNull(params.getIds(), "Ids should be not null for line");
		Assert.assertEquals(params.getIds().size(),1, "Ids should contains 1 item");
		Assert.assertNotNull(params.getStartDate(), "StartDate should be set");
		Assert.assertNotNull(params.getEndDate(), "EndDate should be set");
		Assert.assertTrue(params.getEndDate().after(params.getStartDate()), "dates should be ordered");
	}

}
