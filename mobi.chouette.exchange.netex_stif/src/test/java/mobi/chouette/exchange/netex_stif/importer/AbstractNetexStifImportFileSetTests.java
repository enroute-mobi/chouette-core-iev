package mobi.chouette.exchange.netex_stif.importer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.dao.ImportMessageDAO;
import mobi.chouette.dao.ImportResourceDAO;
import mobi.chouette.dao.ImportTaskDAO;
import mobi.chouette.dao.ReferentialDAO;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.JobDataTest;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ReportConstant;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.ImportTask;
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

	private ExpectedData data;

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
				.addClass(testClass).addClass(JobDataTest.class);

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

	private String next(Iterator<String> iter) {
		if (iter.hasNext()) {
			return iter.next();
		} else {
			return null;
		}
	}

	public ExpectedData parse(String line) {
		ExpectedData data = null;

		String[] tmp = line.split(":");

		List<String> list = Arrays.asList(tmp);
		Iterator<String> iter = list.iterator();

		ExpectedDataBuilder builder = new ExpectedDataBuilder();
		data = builder.withFile(next(iter)).withStatus(next(iter)).withValidationCode(next(iter))
				.withMessageCode(next(iter)).build();

		return data;
	}

	@ToString
	class ExpectedData {
		@Getter
		@Setter
		String file;
		@Getter
		@Setter
		String status;
		@Getter
		@Setter
		String validationCode;
		@Getter
		@Setter
		String messageCode;

	}

	class ExpectedDataBuilder {
		String file;
		String status;
		String validationCode;
		String messageCode;

		public ExpectedDataBuilder withFile(String file) {
			this.file = file;
			return this;
		}

		public ExpectedDataBuilder withStatus(String status) {
			this.status = status;
			return this;
		}

		public ExpectedDataBuilder withValidationCode(String vCode) {
			this.validationCode = vCode;
			return this;
		}

		public ExpectedDataBuilder withMessageCode(String messageCode) {
			this.messageCode = messageCode;
			return this;
		}

		public ExpectedData build() {
			ExpectedData data = new ExpectedData();
			data.setFile(file);
			data.setMessageCode(messageCode);
			data.setStatus(status);
			data.setValidationCode(validationCode);

			return data;
		}
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

	protected void doImport(String zipFile, String expectedActionReportResult, String... expectedData) throws Exception {

		StringBuilder b = new StringBuilder();
		for (String arg : expectedData) {
			data = parse(arg);
			b.append(data.toString());
			b.append(", ");
		}
		System.err.println("expectedActionReportResult="+expectedActionReportResult+", expectedData:"+b.toString());

		// Context context = initImportContext();
		// context.put(REFERENTIAL, new Referential());
		// NetexStifImporterCommand command = (NetexStifImporterCommand) CommandFactory.create(initialContext,
		// NetexStifImporterCommand.class.getName());
		// copyFile(zipFile);
		// JobDataTest jobData = (JobDataTest) context.get(JOB_DATA);
		// jobData.setInputFilename(zipFile);
		// NetexStifImportParameters configuration = (NetexStifImportParameters) context.get(CONFIGURATION);
		// configuration.setNoSave(false);
		// configuration.setCleanRepository(true);
		// try {
		// command.execute(context);
		// } catch (Exception ex) {
		// log.error("test failed", ex);
		// throw ex;
		// }
		//
		// Arrays.asList(args).stream().forEach(System.out::println);
		//
		// ActionReport report = (ActionReport) context.get(REPORT);
		// log.info(report);
		// // à lire dans importResources par fichier
		//
		// utx.begin();
		// em.joinTransaction();
		//
		// // TODO:
		// // récupérer id import dans jobData
		// //
		// ImportTask task = importTaskDAO.find(jobData.getId());
		//
		// // importresoucedao.getResources(task).
		// // importresoucedao.getResources(task).
		//
		// utx.commit();
		//
		// ValidationReport valReport = (ValidationReport) context.get(VALIDATION_REPORT);
		// log.info(valReport.getCheckPointErrors());
		// // Assert.assertEquals(report.getResult(), expectedActionReportResult);
		// // Assert.assertEquals(valReport.getResult().toString(), expectedValidationResult);

	}

}
