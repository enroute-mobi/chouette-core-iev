package mobi.chouette.exchange.netex_stif.importer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.ImportMessageDAO;
import mobi.chouette.dao.ImportResourceDAO;
import mobi.chouette.dao.ImportTaskDAO;
import mobi.chouette.dao.ReferentialDAO;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.JobDataTest;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ReportConstant;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.ImportMessage;
import mobi.chouette.model.ImportMessage_;
import mobi.chouette.model.ImportResource;
import mobi.chouette.model.ImportResource_;
import mobi.chouette.model.ImportTask;
import mobi.chouette.model.Referential;
import mobi.chouette.persistence.hibernate.ContextHolder;

@Log4j
public class AbstractNetexStifImportFileSetTests extends Arquillian implements Constant, ReportConstant {

	protected static InitialContext initialContext;

	protected static final String NETEX_TEST_FILES_DIR = "src/test/data/netex-files-set";

	@EJB
	ReferentialDAO referentialDAO;

	@EJB
	ImportTaskDAO importTaskDAO;

	@EJB
	ImportResourceDAO importResourceDAO;

	@EJB
	ImportMessageDAO importMessageDAO;

	@PersistenceContext(unitName = "public")
	EntityManager em;

	@Inject
	UserTransaction utx;

	public static EnterpriseArchive createDeployment(Class testClass) {

		EnterpriseArchive result;
		File[] files = Maven.resolver().loadPomFromFile("pom.xml")
				.resolve("mobi.chouette:mobi.chouette.exchange.netex_stif").withTransitivity().asFile();
		List<File> jars = new ArrayList<>();
		List<JavaArchive> modules = new ArrayList<>();
		for (File file : files) {
			if (file.getName().startsWith("mobi.chouette.exchange")) {
				String name = file.getName().split("\\-")[0] + ".jar";
				JavaArchive archive = ShrinkWrap.create(ZipImporter.class, name).importFrom(file).as(JavaArchive.class);
				modules.add(archive);
			} else {
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

				JavaArchive archive = ShrinkWrap.create(ZipImporter.class, name).importFrom(file).as(JavaArchive.class);
				modules.add(archive);
				if (!modules.contains(archive))
					modules.add(archive);
			} else {
				if (!jars.contains(file))
					jars.add(file);
			}
		}
		final WebArchive testWar = ShrinkWrap.create(WebArchive.class, "test.war")
				.addAsWebInfResource("postgres-ds.xml").addClass(AbstractNetexStifImportFileSetTests.class)
				.addClass(testClass).addClass(YmlMessages.class).addClass(JobDataTest.class);

		result = ShrinkWrap.create(EnterpriseArchive.class, "test.ear").addAsLibraries(jars.toArray(new File[0]))
				.addAsModules(modules.toArray(new JavaArchive[0])).addAsModule(testWar)
				.addAsResource(EmptyAsset.INSTANCE, "beans.xml");
		return result;
	}

	public static void copyFile(String fileName) throws IOException {
		File srcFile = new File(NETEX_TEST_FILES_DIR, fileName);
		File destFile = new File("target/referential/test", fileName);
		FileUtils.copyFile(srcFile, destFile);
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
		// Logger.getInstance(RouteRegisterCommand.class).setLevel(Level.DEBUG)
		// ;
	}

	protected Context initImportContext() throws Exception {
		init();
		ContextHolder.setContext("chouette_gui"); // set tenant schema

		Context context = new Context();
		context.put(INITIAL_CONTEXT, initialContext);
		context.put(REPORT, new ActionReport());
		context.put(VALIDATION_REPORT, new ValidationReport());
		NetexStifImportParameters configuration = new NetexStifImportParameters();
		context.put(CONFIGURATION, configuration);
		configuration.setName("name");
		configuration.setUserName("userName");
		configuration.setNoSave(true);
		configuration.setCleanRepository(true);
		configuration.setOrganisationName("organisation");
		configuration.setReferentialName("test");
		configuration.setLineReferentialId(1L);
		configuration.setStopAreaReferentialId(1L);
		List<Long> ids = Arrays.asList(new Long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L });
		configuration.setIds(ids);
		JobDataTest jobData = new JobDataTest();
		utx.begin();
		em.joinTransaction();
		mobi.chouette.model.Referential ref = referentialDAO.find(Long.valueOf(1L));
		ImportTask task = new ImportTask();
		task.setFormat("netex_stif");
		task.setReferential(ref);
		em.persist(task);
		log.info("task id = " + task.getId());
		jobData.setId(task.getId());
		utx.commit();
		context.put(JOB_DATA, jobData);
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
		context.put(OPTIMIZED, Boolean.FALSE);
		return context;

	}

	protected void doImport(String zipFile, String expectedActionReportResult, String... expectedData)
			throws Exception {

		log.info("########## Import " + zipFile + " ##########");
		Context context = initImportContext();
		context.put(REFERENTIAL, new Referential());
		NetexStifImporterCommand command = (NetexStifImporterCommand) CommandFactory.create(initialContext,
				NetexStifImporterCommand.class.getName());
		copyFile(zipFile);
		JobDataTest jobData = (JobDataTest) context.get(JOB_DATA);
		jobData.setInputFilename(zipFile);
		NetexStifImportParameters configuration = (NetexStifImportParameters) context.get(CONFIGURATION);
		configuration.setNoSave(false);
		configuration.setCleanRepository(true);
		try {
			command.execute(context);
		} catch (Exception ex) {
			log.error("test failed", ex);
			throw ex;
		}


		ActionReport report = (ActionReport) context.get(REPORT);
		log.info(report);

		utx.begin();
		em.joinTransaction();

		ImportTask task = importTaskDAO.find(jobData.getId());

		List<ImportResource> resources = getRessources(task);
		Map<Long, ImportResource> mapImportResources = new HashMap<Long, ImportResource>();
		resources.stream().forEach(x -> mapImportResources.put(x.getId(), x));

		Set<String> actualErrors = new TreeSet<String>();
		List<ImportMessage> messages = getMessages(task);

		messages.stream().forEach(x -> {
			ImportResource ir = mapImportResources.get(x.getResourceId());
			StringBuilder sb = new StringBuilder();
			sb.append(ir.getName());
			sb.append(":");
			sb.append(ir.getStatus());
			sb.append(":");
			sb.append(x.getMessageAttributs().get("test_id"));
			sb.append(":");
			sb.append(x.getMessageKey());

			String message = YmlMessages.populateMessage(x.getMessageKey(), x.getMessageAttributs());
			if (log.isDebugEnabled()) {
				log.debug("message(" + x.getMessageKey() + ")=" + message);
			}
			List<String> missingKeys = YmlMessages.missingKeys(x.getMessageKey(), x.getMessageAttributs());
			Assert.assertEquals(0, missingKeys.size(), "Missing keys { "
					+ missingKeys.stream().collect(Collectors.joining(";")) + " } in message : " + message);

			actualErrors.add(sb.toString());
		});

		Set<String> expectedErrors = new TreeSet<String>();
		Arrays.asList(expectedData).stream().forEach(x -> expectedErrors.add(x.trim()));

		List<String> expectedNotDetected = expectedErrors.stream().filter(x -> !actualErrors.contains(x))
				.collect(Collectors.toList());
		List<String> notExpected = actualErrors.stream().filter(x -> !expectedErrors.contains(x))
				.collect(Collectors.toList());// );
		
		log.error("ALL DETECTED ERRORS ("+actualErrors.size()+"):" + zipFile + ";" + report.getResult() + ";"
				+ actualErrors.stream().collect(Collectors.joining("; ")));
		if (!notExpected.isEmpty()) {
			log.error("NOT EXPECTED ERRORS:" + zipFile + ";" + report.getResult() + ";"
					+ notExpected.stream().collect(Collectors.joining("; ")));
		}
		if (!expectedNotDetected.isEmpty()) {
			log.error("EXPECTED BUT NOT DETECTED:" + expectedNotDetected.stream().collect(Collectors.joining("; ")));
		}
		Assert.assertTrue(expectedNotDetected.isEmpty(),
				expectedNotDetected.size() + " Error(s) not detected (but expected) : "
						+ expectedNotDetected.stream().collect(Collectors.joining("; ")));
		Assert.assertTrue(notExpected.isEmpty(), notExpected.size() + " Error(s) not expected (but detected) : "
				+ notExpected.stream().collect(Collectors.joining("; ")));

		// clean database
		// messages.stream().forEach(m -> em.remove(m));
		// resources.stream().forEach(r -> em.remove(r));
		// em.remove(task);
		utx.commit();

		// ValidationReport valReport = (ValidationReport) context.get(VALIDATION_REPORT);
		// log.info(valReport.getCheckPointErrors());
		// Assert.assertEquals(report.getResult(), expectedActionReportResult);
		// Assert.assertEquals(valReport.getResult().toString(), expectedValidationResult);

	}

	private List<ImportResource> getRessources(ImportTask task) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ImportResource> criteria = builder.createQuery(ImportResource.class);
		Root<ImportResource> root = criteria.from(ImportResource.class);
		Predicate predicate = builder.equal(root.get(ImportResource_.taskId), task.getId());
		criteria.where(predicate);
		TypedQuery<ImportResource> query = em.createQuery(criteria);

		return query.getResultList();

	}

	private List<ImportMessage> getMessages(ImportTask task) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ImportMessage> criteria = builder.createQuery(ImportMessage.class);
		Root<ImportMessage> root = criteria.from(ImportMessage.class);
		Predicate predicate = builder.equal(root.get(ImportMessage_.taskId), task.getId());
		criteria.where(predicate);
		TypedQuery<ImportMessage> query = em.createQuery(criteria);

		return query.getResultList();

	}
}
