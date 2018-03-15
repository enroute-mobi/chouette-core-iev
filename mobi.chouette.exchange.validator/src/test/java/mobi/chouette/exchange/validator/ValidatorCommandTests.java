package mobi.chouette.exchange.validator;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import org.apache.commons.io.FileUtils;
import org.codehaus.jettison.json.JSONException;
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

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.ComplianceCheckTaskDAO;
import mobi.chouette.dao.ImportTaskDAO;
import mobi.chouette.dao.ReferentialDAO;
import mobi.chouette.exchange.netex_stif.importer.NetexStifImportParameters;
import mobi.chouette.exchange.netex_stif.importer.NetexStifImporterCommand;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.Referential;
import mobi.chouette.model.compliance.ComplianceCheck;
import mobi.chouette.model.compliance.ComplianceCheck.CRITICITY;
import mobi.chouette.model.compliance.ComplianceCheckBlock;
import mobi.chouette.model.compliance.ComplianceCheckMessage;
import mobi.chouette.model.compliance.ComplianceCheckMessage_;
import mobi.chouette.model.compliance.ComplianceCheckResource;
import mobi.chouette.model.compliance.ComplianceCheckResource_;
import mobi.chouette.model.compliance.ComplianceCheckTask;
import mobi.chouette.model.importer.ImportTask;
import mobi.chouette.persistence.hibernate.ContextHolder;

@Log4j
public class ValidatorCommandTests extends Arquillian {

	protected static final String NETEX_TEST_FILES_DIR = "src/test/data/netex-files-set";

	protected InitialContext initialContext;

	@EJB
	ReferentialDAO referentialDAO;

	@EJB
	ImportTaskDAO importTaskDAO;

	@EJB
	ComplianceCheckTaskDAO complianceCheckTaskDAO;

	@PersistenceContext(unitName = "public")
	EntityManager em;

	@Inject
	UserTransaction utx;

	@Deployment
	public static EnterpriseArchive createDeployment() {
		EnterpriseArchive result;
		File[] files = Maven.resolver().loadPomFromFile("pom.xml")
				.resolve("mobi.chouette:mobi.chouette.exchange.validator").withTransitivity().asFile();
		List<File> jars = new ArrayList<>();
		List<JavaArchive> modules = new ArrayList<>();
		List<String> moduleNames = new ArrayList<>();
		for (File file : files) {
			if (file.getName().startsWith("mobi.chouette.exchange")) {
				String name = file.getName().split("\\-")[0] + ".jar";

				JavaArchive archive = ShrinkWrap.create(ZipImporter.class, name).importFrom(file).as(JavaArchive.class);
				modules.add(archive);
				moduleNames.add(name);
			} else {
				jars.add(file);
			}
		}
		File[] filesNeptune = Maven.resolver().loadPomFromFile("pom.xml")
				.resolve("mobi.chouette:mobi.chouette.exchange.netex_stif").withTransitivity().asFile();
		if (filesNeptune.length == 0) {
			throw new NullPointerException("no netex_stif");
		}
		for (File file : filesNeptune) {
			if (file.getName().startsWith("mobi.chouette.exchange")) {
				String name = file.getName().split("\\-")[0] + ".jar";
				if (!moduleNames.contains(name)) {

					JavaArchive archive = ShrinkWrap.create(ZipImporter.class, name).importFrom(file)
							.as(JavaArchive.class);
					modules.add(archive);
					moduleNames.add(name);
				}
			} else {
				if (!jars.contains(file))
					jars.add(file);
			}
		}
		File[] filesDao = Maven.resolver().loadPomFromFile("pom.xml").resolve("mobi.chouette:mobi.chouette.dao")
				.withTransitivity().asFile();
		if (filesDao.length == 0) {
			throw new NullPointerException("no dao");
		}
		for (File file : filesDao) {
			if (file.getName().startsWith("mobi.chouette.dao")) {
				String name = file.getName().split("\\-")[0] + ".jar";
				if (!moduleNames.contains(name)) {

					JavaArchive archive = ShrinkWrap.create(ZipImporter.class, name).importFrom(file)
							.as(JavaArchive.class);
					modules.add(archive);
					moduleNames.add(name);
				}
			} else {
				if (!jars.contains(file))
					jars.add(file);
			}
		}
		final WebArchive testWar = ShrinkWrap.create(WebArchive.class, "test.war")
				.addAsWebInfResource("postgres-ds.xml").addClass(JobDataImpl.class).addClass(YmlMessages.class)
				.addClass(ValidatorCommandTests.class);

		result = ShrinkWrap.create(EnterpriseArchive.class, "test.ear").addAsLibraries(jars.toArray(new File[0]))
				.addAsModules(modules.toArray(new JavaArchive[0])).addAsModule(testWar)
				.addAsResource(EmptyAsset.INSTANCE, "beans.xml");
		return result;
	}

	protected void init() {
		Locale.setDefault(Locale.ENGLISH);
		if (initialContext == null) {
			try {
				initialContext = new InitialContext();
			} catch (NamingException e) {
				e.printStackTrace();
			}

		}

	}

	protected Context initImportContext() throws Exception {
		init();
		ContextHolder.setContext("chouette_gui"); // set tenant schema

		Context context = new Context();
		context.put(Constant.INITIAL_CONTEXT, initialContext);
		context.put(Constant.REPORT, new ActionReport());
		context.put(Constant.VALIDATION_REPORT, new ValidationReport());
		NetexStifImportParameters configuration = new NetexStifImportParameters();
		context.put(Constant.CONFIGURATION, configuration);
		configuration.setName("name");
		configuration.setUserName("userName");
		configuration.setNoSave(false);
		configuration.setCleanRepository(true);
		configuration.setOrganisationName("organisation");
		configuration.setReferentialName("test");
		configuration.setReferentialId(1L);
		configuration.setLineReferentialId(1L);
		configuration.setStopAreaReferentialId(1L);
		List<Long> ids = Arrays.asList(new Long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L });
		configuration.getIds().addAll(ids);
		JobDataImpl jobData = new JobDataImpl();
		utx.begin();
		em.joinTransaction();
		Referential ref = referentialDAO.find(Long.valueOf(1L));
		ImportTask task = new ImportTask();
		task.setFormat("netex_stif");
		task.setReferential(ref);
		em.persist(task);
		log.info("task id = " + task.getId());
		jobData.setId(task.getId());
		utx.commit();
		context.put(Constant.JOB_DATA, jobData);
		jobData.setPathName("target/referential/test");
		File f = new File("target/referential/test");
		if (f.exists())
			try {
				FileUtils.deleteDirectory(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		f.mkdirs();
		jobData.setReferential("chouette_gui");
		jobData.setAction(JobData.ACTION.importer);
		jobData.setType("netex_stif");
		// context.put(TESTNG, "true"); // mode test
		// context.put(Constant.OPTIMIZED, Boolean.FALSE); // ne pas activer le thread copy
		return context;

	}

	protected Context initValidatorContext() throws Exception {
		init();
		ContextHolder.setContext("chouette_gui"); // set tenant schema

		Context context = new Context();
		context.put(Constant.INITIAL_CONTEXT, initialContext);
		context.put(Constant.REPORT, new ActionReport());
		context.put(Constant.VALIDATION_REPORT, new ValidationReport());
		JobDataImpl jobData = new JobDataImpl();
		utx.begin();
		em.joinTransaction();
		Referential ref = referentialDAO.find(Long.valueOf(1L));
		ComplianceCheckTask task = createTask(ref);
		em.persist(task);
		log.info("task id = " + task.getId());
		jobData.setId(task.getId());
		utx.commit();
		ValidatorInputValidator validator = new ValidatorInputValidator();
		context.put(Constant.CONFIGURATION, validator.toActionParameter(task));
		context.put(Constant.JOB_DATA, jobData);
		jobData.setPathName("target/referential/test");
		File f = new File("target/referential/test");
		if (f.exists())
			try {
				FileUtils.deleteDirectory(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		f.mkdirs();
		jobData.setReferential("chouette_gui");
		jobData.setAction(JobData.ACTION.validator);
		context.put(Constant.OPTIMIZED, Boolean.FALSE);
		return context;

	}

	private static final String[] blockModes = { "bus", "metro" };

	private static final String[] checksForBus = { "3-VehicleJourney-1/warning/maximum=5/VehicleJourney",
			"3-VehicleJourney-2/error/minimum=5,maximum=50/VehicleJourney",
			"3-VehicleJourney-3/warning/maximum=10/VehicleJourney",
			"3-Generic-1/warning/target=route#name,pattern=^[a-zA-Z ]+$/Route",
			"3-Generic-2/error/target=vehicle_journey#number,minimum=1,maximum=30000/VehicleJourney",
			"3-Generic-3/warning/target=vehicle_journey#number/VehicleJourney" };

	private static final String[] checksForMetro = { "3-VehicleJourney-1/warning/maximum=5/VehicleJourney",
			"3-VehicleJourney-2/error/minimum=5,maximum=50/VehicleJourney",
			"3-VehicleJourney-3/warning/maximum=10/VehicleJourney",
			"3-Generic-1/warning/target=route#name,pattern=^[a-zA-Z ]+$/Route",
			"3-Generic-2/warning/target=vehicle_journey#number,minimum=1,maximum=30000/VehicleJourney",
			"3-Generic-3/warning/target=vehicle_journey#number/VehicleJourney" };

	private static final String[] checksForAll = { "3-JourneyPattern-1/warning//JourneyPattern",
			"3-JourneyPattern-2/warning//JourneyPattern", "3-Line-1/warning//Line", "3-Route-1/warning//Route",
			"3-Route-2/error//Route", "3-Route-3/warning//Route", "3-Route-4/warning//Route",
			"3-Route-5/warning//Route", "3-Route-6/warning//Route", "3-Route-8/warning//Route",
			"3-Route-9/warning//Route", "3-Route-10/warning//Route", "3-Route-11/warning//Route",
			"3-RoutingConstraint-1/warning//RoutingConstraint", "3-RoutingConstraint-2/warning//RoutingConstraint",
			"3-RoutingConstraint-3/warning//RoutingConstraint", "3-Shape-1/warning//Shape", "3-Shape-2/warning//Shape",
			"3-Shape-3/warning//Shape", "3-VehicleJourney-1/warning/maximum=5/VehicleJourney",
			"3-VehicleJourney-2/error/minimum=5,maximum=50/VehicleJourney",
			"3-VehicleJourney-3/warning/maximum=10/VehicleJourney", "3-VehicleJourney-4/warning//VehicleJourney",
			"3-VehicleJourney-5/warning//VehicleJourney" };

	private ComplianceCheckTask createTask(Referential referential) {
		ComplianceCheckTask task = new ComplianceCheckTask();

		task.setReferential(referential);
		task.setCreatedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		task.setUpdatedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		addBlocks(task);

		return task;
	}

	private void addBlocks(ComplianceCheckTask task) {
		for (String mode : blockModes) {
			String[] modes = mode.split("/");
			ComplianceCheckBlock block = new ComplianceCheckBlock();
			block.setTask(task);
			block.setName(mode);
			block.getConditionAttributes().put(ValidatorInputValidator.TRANSPORT_MODE_KEY, modes[0]);
			if (modes.length > 1)
				block.getConditionAttributes().put(ValidatorInputValidator.TRANSPORT_SUBMODE_KEY, modes[1]);
			task.getComplianceCheckBlocks().add(block);

			addChecksToBlock(task, block, mode.equals("bus") ? checksForBus : checksForMetro);
		}
		addCheckToTask(task);

	}

	private void addCheckToTask(ComplianceCheckTask task) {
		for (String checkData : checksForAll) {
			String[] data = checkData.split("/");
			// log.info("checkData = " + checkData + " , size = " + data.length);
			ComplianceCheck check = new ComplianceCheck();
			check.setOriginCode(data[0]);
			check.setSpecificCode(data[0]);
			check.setComment(checkData);
			check.setCriticity(CRITICITY.valueOf(data[1]));
			if (data[2].length() > 0) {
				String[] attributes = data[2].split(",");
				for (String attribute : attributes) {
					String[] attr = attribute.split("=");
					try {
						check.getControlAttributes().put(attr[0], attr[1]);
					} catch (JSONException e) {
						log.error(e.getMessage());
					}
				}
			}
			check.setType(data[3]);
			check.setTask(task);
			task.getComplianceChecks().add(check);
		}
		return;
	}

	private void addChecksToBlock(ComplianceCheckTask task, ComplianceCheckBlock block, String[] checksForBlock) {
		for (String checkData : checksForBlock) {
			String[] data = checkData.split("/");
			// log.info("checkData = " + checkData + " , size = " + data.length);
			ComplianceCheck check = new ComplianceCheck();
			check.setOriginCode(data[0]);
			check.setSpecificCode(data[0]);
			check.setComment(checkData);
			check.setCriticity(CRITICITY.valueOf(data[1]));
			if (data[2].length() > 0) {
				String[] attributes = data[2].split(",");
				for (String attribute : attributes) {
					String[] attr = attribute.split("=");
					try {
						check.getControlAttributes().put(attr[0], attr[1]);
					} catch (JSONException e) {
						log.error(e.getMessage());
					}
				}
			}
			check.setType(data[3]);
			check.setBlock(block);
			check.setTask(task);
			task.getComplianceChecks().add(check);
		}
		return;
	}

	public static void copyFile(String fileName) throws IOException {
		File srcFile = new File(NETEX_TEST_FILES_DIR, fileName);
		File destFile = new File("target/referential/test", fileName);
		FileUtils.copyFile(srcFile, destFile);
	}

	protected void doImport(String zipFile) throws Exception {
		log.info("########## Import " + zipFile + " ##########");
		Context context = initImportContext();
		context.put(Constant.REFERENTIAL, new Referential());
		NetexStifImporterCommand command = (NetexStifImporterCommand) CommandFactory.create(initialContext,
				NetexStifImporterCommand.class.getName());
		copyFile(zipFile);
		JobDataImpl jobData = (JobDataImpl) context.get(Constant.JOB_DATA);
		jobData.setInputFilename(zipFile);
		NetexStifImportParameters configuration = (NetexStifImportParameters) context.get(Constant.CONFIGURATION);
		configuration.setNoSave(false);
		configuration.setCleanRepository(true);
		try {
			command.execute(context);
		} catch (Exception ex) {
			log.error("test failed", ex);
			throw ex;
		}

	}

	private List<ComplianceCheckResource> getRessources(ComplianceCheckTask task) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ComplianceCheckResource> criteria = builder.createQuery(ComplianceCheckResource.class);
		Root<ComplianceCheckResource> root = criteria.from(ComplianceCheckResource.class);
		Predicate predicate = builder.equal(root.get(ComplianceCheckResource_.taskId), task.getId());
		criteria.where(predicate);
		TypedQuery<ComplianceCheckResource> query = em.createQuery(criteria);

		return query.getResultList();

	}

	private List<ComplianceCheckMessage> getMessages(ComplianceCheckTask task) {
		return getMessages(task, null);
	}

	private List<ComplianceCheckMessage> getMessages(ComplianceCheckTask task, ComplianceCheckResource ressource) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ComplianceCheckMessage> criteria = builder.createQuery(ComplianceCheckMessage.class);
		Root<ComplianceCheckMessage> root = criteria.from(ComplianceCheckMessage.class);
		Predicate predicateTaskId = builder.equal(root.get(ComplianceCheckMessage_.taskId), task.getId());
		if (ressource != null) {
			Predicate predicateResourceId = builder.equal(root.get(ComplianceCheckMessage_.resourceId),
					ressource.getId());
			criteria.where(predicateTaskId, predicateResourceId);
		} else {
			criteria.where(predicateTaskId);
		}

		TypedQuery<ComplianceCheckMessage> query = em.createQuery(criteria);

		return query.getResultList();

	}

	protected void doValidate(String zipFile, String expectedActionReportResult, int lineCount, String... expectedData)
			throws Exception {

		doImport(zipFile);

		log.info("########## Validate " + zipFile + " ##########");
		Context context = initValidatorContext();
		// context.put(Constant.REFERENTIAL, new Referential());
		Command command = CommandFactory.create(initialContext, ValidatorCommand.class.getName());
		JobDataImpl jobData = (JobDataImpl) context.get(Constant.JOB_DATA);
		// jobData.setInputFilename(zipFile);
		try {
			command.execute(context);
		} catch (Exception ex) {
			log.error("test failed", ex);
			throw ex;
		}

		ActionReport actionReport = (ActionReport) context.get(Constant.REPORT);

		utx.begin();
		em.joinTransaction();

		ComplianceCheckTask task = complianceCheckTaskDAO.find(jobData.getId());

		List<ComplianceCheckResource> resources = getRessources(task);
		List<ComplianceCheckMessage> messages = getMessages(task);
		try {
			log.info("task = " + task.toString());
			log.info("report = " + actionReport.toString());

			Assert.assertTrue(actionReport.isWarning(), "report should return warning status");
			Map<Long, ComplianceCheckResource> mapResources = new HashMap<Long, ComplianceCheckResource>();
			int lines = 0;
			for (ComplianceCheckResource x : resources) {
				mapResources.put(x.getId(), x);
				if (x.getType().equals("line"))
					lines++;
			}
			Assert.assertEquals(lines, lineCount, "unexpected line count");

			Set<String> actualErrors = new TreeSet<String>();

			messages.stream().forEach(x -> {
				ComplianceCheckResource ir = mapResources.get(x.getResourceId());
				StringBuilder sb = new StringBuilder();
				sb.append(ir.getName());
				sb.append(":");
				sb.append(ir.getStatus());
				sb.append(":");
				sb.append(x.getMessageAttributs().get("test_id"));
				sb.append(":");
				sb.append(x.getMessageKey());

				String message = YmlMessages.populateMessage(x.getMessageKey(), x.getMessageAttributs());
				List<String> missingKeys = YmlMessages.missingKeys(x.getMessageKey(), x.getMessageAttributs());
				Assert.assertEquals(0, missingKeys.size(), "Missing keys { "
						+ missingKeys.stream().collect(Collectors.joining(";")) + " } in message : " + message);

				Assert.assertTrue(x.getResourceAttributs().containsKey("object_path"),
						"resource should contains object_path");

				log.info("POUR ANALYSE : " + zipFile + ";" + sb.toString() + ";PATH="
						+ x.getResourceAttributs().get("object_path") + ";MSG=" + message);

				actualErrors.add(sb.toString());
			});

			resources.stream().forEach(x -> {
				log.info("resource = "+x);
				List<ComplianceCheckMessage> msgs = getMessages(task, x);
				if (x.getType().equalsIgnoreCase("line")) {
					if (x.getStatus().equalsIgnoreCase("ERROR") || x.getStatus().equalsIgnoreCase("WARNING")) {
						Assert.assertTrue(((msgs != null) ? msgs.size() : 0) > 0, "ComplianceCheckResource "
								+ x.getName() + " contains " + x.getStatus() + ", but no message !!!");
					} else {
						Assert.assertTrue(((msgs != null) ? msgs.size() : 0) == 0,
								"ComplianceCheckResource " + x.getName() + " contains OK, but with messages !!!");
					}
				}
			});

			Set<String> expectedErrors = new TreeSet<String>();
			Arrays.asList(expectedData).stream().forEach(x -> expectedErrors.add(x.trim()));

			List<String> expectedNotDetected = expectedErrors.stream().filter(x -> !actualErrors.contains(x))
					.collect(Collectors.toList());
			List<String> notExpected = actualErrors.stream().filter(x -> !expectedErrors.contains(x))
					.collect(Collectors.toList());// );

			log.info("ALL DETECTED ERRORS (" + actualErrors.size() + "):" + zipFile + ";" + actionReport.getResult()
					+ ";" + actualErrors.stream().collect(Collectors.joining("; ")));
			if (!notExpected.isEmpty()) {
				log.error("NOT EXPECTED ERRORS:" + zipFile + ";" + actionReport.getResult() + ";"
						+ notExpected.stream().collect(Collectors.joining("; ")));
			}
			if (!expectedNotDetected.isEmpty()) {
				log.error(
						"EXPECTED BUT NOT DETECTED:" + expectedNotDetected.stream().collect(Collectors.joining("; ")));
			}

			Assert.assertEquals(actionReport.getResult(), expectedActionReportResult);

			Assert.assertTrue(expectedNotDetected.isEmpty(),
					expectedNotDetected.size() + " Error(s) not detected (but expected) : "
							+ expectedNotDetected.stream().collect(Collectors.joining("; ")));
			Assert.assertTrue(notExpected.isEmpty(), notExpected.size() + " Error(s) not expected (but detected) : "
					+ notExpected.stream().collect(Collectors.joining("; ")));

		} finally {
			// clean database
			messages.forEach(m -> em.remove(m));
			resources.forEach(r -> em.remove(r));
			task.getComplianceCheckBlocks().forEach(b -> em.remove(b));
			task.getComplianceChecks().forEach(c -> em.remove(c));
			em.remove(task);
			utx.commit();
		}

	}

	//@Test(groups = { "Level3" }, testName = "nominal", description = "no error", priority = 1)
	public void verifyNominal() throws Exception {
		doValidate("OFFRE_SNTYO_Nominal.zip", "NOK", 8, "01:ERROR:3-Generic-1:3_generic_1",
"01:ERROR:3-VehicleJourney-2:3_vehiclejourney_2_1", 
"01:ERROR:3-VehicleJourney-2:3_vehiclejourney_2_2", 
"03:ERROR:3-Generic-1:3_generic_1", 
"03:ERROR:3-VehicleJourney-2:3_vehiclejourney_2_1", 
"03:ERROR:3-VehicleJourney-2:3_vehiclejourney_2_2", 
"04:ERROR:3-Generic-1:3_generic_1", 
"04:ERROR:3-VehicleJourney-2:3_vehiclejourney_2_1", 
"05:ERROR:3-VehicleJourney-2:3_vehiclejourney_2_1", 
"05:ERROR:3-VehicleJourney-2:3_vehiclejourney_2_2", 
"08:ERROR:3-Generic-1:3_generic_1", 
"08:ERROR:3-VehicleJourney-2:3_vehiclejourney_2_1", 
"08:ERROR:3-VehicleJourney-2:3_vehiclejourney_2_2", 
"11:ERROR:3-Generic-1:3_generic_1", 
"11:ERROR:3-Route-9:3_route_9", 
"11:ERROR:3-VehicleJourney-2:3_vehiclejourney_2_1", 
"11:ERROR:3-VehicleJourney-2:3_vehiclejourney_2_2");
	}

}
