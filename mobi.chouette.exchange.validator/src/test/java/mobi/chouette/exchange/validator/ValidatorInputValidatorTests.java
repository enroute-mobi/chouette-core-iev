package mobi.chouette.exchange.validator;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.BasicConfigurator;
import org.testng.Assert;
import org.testng.annotations.Test;

import lombok.extern.log4j.Log4j;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.exchange.validator.checkpoints.CheckpointParameters;
import mobi.chouette.exchange.validator.checkpoints.ControlParameters;
import mobi.chouette.model.Organisation;
import mobi.chouette.model.Referential;
import mobi.chouette.model.ReferentialMetadata;
import mobi.chouette.model.compliance.ComplianceCheck;
import mobi.chouette.model.compliance.ComplianceCheck.CRITICITY;
import mobi.chouette.model.compliance.ComplianceCheckBlock;
import mobi.chouette.model.compliance.ComplianceCheckTask;

@Log4j
public class ValidatorInputValidatorTests {

	// mode/submode
	private static final String[] blockModes = { "bus", "bus/schoolBus", "rail", "rail/suburbanRailway" };
	// origin_code/criticity/(attributes)/(type)
	// attributes : key=value,...
	private static final String[] checksForBlock = { "3-VehicleJourney-1/warning/first_value=5/VehicleJourney",
			"3-VehicleJourney-2/warning/first_value=5,last_value=50/VehicleJourney",
			"3-VehicleJourney-3/warning/first_value=10/VehicleJourney",
			"3-Generique-1/warning/attribute_name=name,first_value=^[a-zA-Z ]+$/Route",
			"3-Generique-2/warning/attribute_name=number,first_value=1,last_value=30000/VehicleJourney",
			"3-Generique-3/warning/attribute_name=number/VehicleJourney" };

	private static final String[] checks = { "3-JourneyPattern-1/warning//JourneyPattern",
			"3-JourneyPattern-2/warning//JourneyPattern", "3-Line-1/warning//Line", "3-Route-1/warning//Route",
			"3-Route-2/error//Route", "3-Route-3/warning//Route", "3-Route-4/warning//Route",
			"3-Route-5/warning//Route", "3-Route-6/warning//Route", "3-Route-8/warning//Route",
			"3-Route-9/warning//Route", "3-Route-10/warning//Route", "3-Route-11/warning//Route",
			"3-ITL-1/warning//RoutingConstraint", "3-ITL-2/warning//RoutingConstraint",
			"3-ITL-3/warning//RoutingConstraint", "3-Shape-1/warning//Shape", "3-Shape-2/warning//Shape",
			"3-Shape-3/warning//Shape", "3-VehicleJourney-4/warning//VehicleJourney",
			"3-VehicleJourney-5/warning//VehicleJourney" };

	public void init() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		Locale.setDefault(Locale.ENGLISH);
	}

	
	private ComplianceCheckTask createTask(Referential referential) {
		ComplianceCheckTask task = new ComplianceCheckTask();

		task.setId(1L);
		task.setReferential(referential);
		addBlocks(task);

		return task;
	}

	private void addBlocks(ComplianceCheckTask task) {
		long id = task.getComplianceCheckBlocks().size() + 1;
		for (String mode : blockModes) {
			String[] modes = mode.split("/");
			ComplianceCheckBlock block = new ComplianceCheckBlock();
			block.setId(id++);
			block.setTask(task);
			block.getConditionAttributes().put(ValidatorInputValidator.TRANSPORT_MODE_KEY, modes[0]);
			if (modes.length > 1)
				block.getConditionAttributes().put(ValidatorInputValidator.TRANSPORT_SUBMODE_KEY, modes[1]);
			task.getComplianceCheckBlocks().add(block);

			addChecksToBlock(task, block);
		}
		addCheckToTask(task);

	}

	private void addCheckToTask(ComplianceCheckTask task) {
		long nextId = task.getComplianceChecks().size() + 1;
		for (String checkData : checks) {
			String[] data = checkData.split("/");
			log.info("checkData = "+checkData+" , size = "+data.length);
			ComplianceCheck check = new ComplianceCheck();
			check.setId(nextId++);
			check.setCode(data[0]);
			check.setCriticity(CRITICITY.valueOf(data[1].toUpperCase()));
			if (data[2].length() > 0) {
				String[] attributes = data[2].split(",");
				for (String attribute : attributes) {
					String[] attr = attribute.split("=");
					check.getControlAttributes().put(attr[0], attr[1]);
				}
			}
			check.setType(data[3]);
			check.setTask(task);
			task.getComplianceChecks().add(check);
		}
		return;
	}

	private void addChecksToBlock(ComplianceCheckTask task, ComplianceCheckBlock block) {
		long nextId = task.getComplianceChecks().size() + 1;
		for (String checkData : checksForBlock) {
			String[] data = checkData.split("/");
			log.info("checkData = "+checkData+" , size = "+data.length);
			ComplianceCheck check = new ComplianceCheck();
			check.setId(nextId++);
			check.setCode(data[0]);
			check.setCriticity(CRITICITY.valueOf(data[1].toUpperCase()));
			if (data[2].length() > 0) {
				String[] attributes = data[2].split(",");
				for (String attribute : attributes) {
					String[] attr = attribute.split("=");
					check.getControlAttributes().put(attr[0], attr[1]);
				}
			}
			check.setType(data[3]);
			check.setBlock(block); // TODO replace by setBlock
			check.setTask(task);
			task.getComplianceChecks().add(check);
		}
		return;
	}

	@Test(groups = { "input" }, description = "toActionParameter", priority = 1)
	public void validateToActionParameter() throws Exception
	{
		init();
		Referential referential = new Referential();
		referential.setId(1L);
		referential.setName("refernetial");
		referential.setLineReferentialId(3L);
		referential.setStopAreaReferentialId(3L);
		referential.setOrganisation(new Organisation());
		referential.getOrganisation().setId(2L);
		referential.getOrganisation().setName("organisation");
		referential.getOrganisation().setCode("code_orga");
		ReferentialMetadata metadata = new ReferentialMetadata();
		referential.getMetadatas().add(metadata);
		metadata.setLineIds(new Long[] {1l,2L});
		ComplianceCheckTask task = createTask(referential);
		ValidatorInputValidator validator = new ValidatorInputValidator();
		AbstractParameter parameters = validator.toActionParameter(task);
		Assert.assertTrue(parameters instanceof ValidateParameters, " instance of ValidateParameters");
        ValidateParameters params = (ValidateParameters) parameters;
        Assert.assertNotNull(params.getControlParameters(),"parameter has controlParameters");
        ControlParameters control = params.getControlParameters();
        Assert.assertEquals(control.getGlobalCheckPoints().size(), 21, "GlobalCheckPoints count");
        Assert.assertEquals(control.getTransportModeCheckpoints().size(), 4, "TransportModeCheckPoints blocks count");
        for (Map<String, Collection<CheckpointParameters>> map : control.getTransportModeCheckpoints().values()) {
        	Assert.assertEquals(map.size(), 6, "TransportModeCheckPoints checks count");
		}
	}
}
