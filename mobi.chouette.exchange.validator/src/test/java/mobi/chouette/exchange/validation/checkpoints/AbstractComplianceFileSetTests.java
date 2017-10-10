package mobi.chouette.exchange.validation.checkpoints;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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

import org.hibernate.Session;
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
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.ComplianceCheckMessageDAO;
import mobi.chouette.dao.ComplianceCheckResourceDAO;
import mobi.chouette.dao.ComplianceCheckTaskDAO;
import mobi.chouette.dao.ReferentialDAO;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ReportConstant;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.exchange.validator.JobDataTest;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.exchange.validator.ValidatorCommand;
import mobi.chouette.model.ImportMessage;
import mobi.chouette.model.ImportMessage_;
import mobi.chouette.model.ImportResource;
import mobi.chouette.model.ImportResource_;
import mobi.chouette.model.ImportTask;
import mobi.chouette.model.Referential;
import mobi.chouette.model.compliance.ComplianceCheckTask;
import mobi.chouette.persistence.hibernate.ChouetteTenantIdentifierGenerator;
import mobi.chouette.persistence.hibernate.ContextHolder;

@Log4j
public class AbstractComplianceFileSetTests extends Arquillian implements Constant, ReportConstant {

	protected static InitialContext initialContext;

	protected static final String TEST_FILES_DIR = "src/test/data/db-dumps/generated";

	@EJB
	ReferentialDAO referentialDAO;

	@EJB
	ComplianceCheckTaskDAO ccTaskDAO;

	@EJB
	ComplianceCheckResourceDAO ccResourceDAO;

	@EJB
	ComplianceCheckMessageDAO ccMessageDAO;

	@PersistenceContext(unitName = "public")
	EntityManager em;

	@Inject
	UserTransaction utx;

	public static EnterpriseArchive createDeployment(Class<? extends Arquillian> testClass) {

		EnterpriseArchive result;
		File[] files = Maven.resolver().loadPomFromFile("pom.xml")
				.resolve("mobi.chouette:mobi.chouette.exchange.validator").withTransitivity().asFile();
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
				.addAsWebInfResource("postgres-ds.xml").addClass(AbstractComplianceFileSetTests.class)
				.addClass(testClass).addClass(JobDataTest.class);

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
		// Logger.getInstance(RouteRegisterCommand.class).setLevel(Level.DEBUG)
		// ;
	}

	protected Context initImportContext(String schema) throws Exception {
		init();
		ContextHolder.setContext(schema); // set tenant schema

		Context context = new Context();
		context.put(INITIAL_CONTEXT, initialContext);
		context.put(REPORT, new ActionReport());
		context.put(VALIDATION_REPORT, new ValidationReport());

		ValidateParameters configuration = new ValidateParameters();

		context.put(CONFIGURATION, configuration);
		configuration.setName("name");
		configuration.setUserName("userName");
		// configuration.setNoSave(true);
		// configuration.setCleanRepository(true);
		configuration.setOrganisationName("organisation");
		configuration.setReferentialName("test");
		configuration.setReferentialId(1L);
		configuration.setLineReferentialId(1L);
		configuration.setStopAreaReferentialId(1L);
		List<Long> ids = Arrays.asList(new Long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L });
		configuration.setIds(ids);
		utx.begin();
		em.joinTransaction();
		mobi.chouette.model.Referential ref = referentialDAO.find(Long.valueOf(1L));
		ComplianceCheckTask task = new ComplianceCheckTask();
		task.setFormat("netex_stif");
		task.setReferential(ref);
		em.persist(task);
		log.info("task id = " + task.getId());
		// JobDataTest jobData = new JobDataTest();
		// jobData.setId(task.getId());
		// utx.commit();
		// context.put(JOB_DATA, jobData);
		// jobData.setPathName("target/referential/test");
		// File f = new File("target/referential/test");
		// if (f.exists())
		// try {
		// FileUtils.deleteDirectory(f);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// f.mkdirs();
		// jobData.setReferential("chouette_gui");
		// jobData.setAction(JobData.ACTION.validator);
		// jobData.setType("netex_stif");
		// // context.put(TESTNG, "true"); // mode test
		context.put(OPTIMIZED, Boolean.FALSE);
		return context;

	}

	private String nextSqlCommand(BufferedReader r) throws Exception {

		StringBuilder b = new StringBuilder();
		String line = null;

		do {
			line = r.readLine();
			if (line == null)
				break;
			line = line.trim();
			if (line.isEmpty())
				continue;
			if (line.startsWith("--"))
				continue;
			b.append(' ').append(line);
			if (line.endsWith(";"))
				break;
		} while (line != null);
		if (b.length() == 0)
			return null;

		return b.toString();
	}

//	public void loadSqlSchemaWithPsqlCmd(String schema) {
//		try {
//			String line;
//			String pattern = "psql -U {0} -d {1} -h {2} -f {3}";
//			String username = "chouette";
//			String host = "localhost";
//			String database = "boiv_test";
//			String sqlfilename = TEST_FILES_DIR + "/" + schema + ".sql";
//			
//			File sqlFile = new File(sqlfilename);
//			if (! sqlFile.exists()){
//				throw new FileNotFoundException(sqlfilename);
//			}
//			
//			String cmd = MessageFormat.format(pattern, username, database, host, sqlFile.getAbsolutePath());
//			Process p = Runtime.getRuntime().exec(cmd);
//			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			while ((line = input.readLine()) != null) {
//				System.out.println(line);
//			}
//			input.close();
//		} catch (Exception err) {
//			err.printStackTrace();
//		}
//	}

	private void loadSqlSchema(String schema) throws Exception {
		init();
		ContextHolder.setContext(schema);
		BufferedReader r = new BufferedReader(new FileReader(TEST_FILES_DIR + "/" + schema + ".sql"));
		ChouetteTenantIdentifierGenerator.deleteTenant(schema);
		utx.begin();
		em.joinTransaction();
		Session hibernateSession = em.unwrap(Session.class);
		//
		hibernateSession.doWork(connection -> {
			Statement statement = null;
			try {
				statement = connection.createStatement();
				String command = null;
				int i = 0;
				boolean batch = false;
				do {
					i++;
					command = nextSqlCommand(r);
					if (command != null) {
						if (command.startsWith(" SELECT")) {
							if (batch)
								statement.executeBatch();
							batch = false;
							statement.executeQuery(command);
						} else {
							batch = true;
							statement.addBatch(command);
						}
					}
					if (i % 20 == 0) {
						if (batch)
							statement.executeBatch();
						batch = false;
					}

				} while (command != null);
				if (batch)
					statement.executeBatch();
			} catch (Exception ex) {
				ex.printStackTrace();
				if (ex instanceof SQLException) {
					SQLException sqlex = (SQLException) ex;
					if (sqlex.getNextException() != null)
						sqlex.getNextException().printStackTrace();
				}
			} finally {
				try {
					r.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (statement != null)
					statement.close();
			}
			//
		});
		utx.commit();

	}

	protected void doValidation(String schema, String expectedActionReportResult, String... expectedData)
			throws Exception {

		log.info("########## Import " + schema + ".sql ##########");
		loadSqlSchema(schema);

		
		
		Context context = initImportContext(schema);
		context.put(REFERENTIAL, new Referential());
		ValidatorCommand command = (ValidatorCommand) CommandFactory.create(initialContext,
				ValidatorCommand.class.getName());


		// JobDataTest jobData = (JobDataTest) context.get(JOB_DATA);
		// // jobData.setInputFilename(zipFile);
		// ValidateParameters configuration = (ValidateParameters) context.get(CONFIGURATION);
		// // configuration.setNoSave(false);
		// // configuration.setCleanRepository(true);
		try {
			command.execute(context);
		} catch (Exception ex) {
			log.error("test failed", ex);
			throw ex;
		}

		ActionReport actionReport = (ActionReport) context.get(REPORT);

		// utx.begin();
		// em.joinTransaction();
		//
		// ComplianceCheckTask task = ccTaskDAO.find(jobData.getId());
		//
		// List<ImportResource> resources = getRessources(task);
		// Map<Long, ImportResource> mapImportResources = new HashMap<Long, ImportResource>();
		// resources.stream().forEach(x -> {
		// mapImportResources.put(x.getId(), x);
		// log.info(x.toString());
		// });
		//
		// Set<String> actualErrors = new TreeSet<String>();
		// List<ImportMessage> messages = getMessages(task);
		//
		// messages.stream().forEach(x -> {
		// ImportResource ir = mapImportResources.get(x.getResourceId());
		// StringBuilder sb = new StringBuilder();
		// sb.append(ir.getName());
		// sb.append(":");
		// sb.append(ir.getStatus());
		// sb.append(":");
		// sb.append(x.getMessageAttributs().get("test_id"));
		// sb.append(":");
		// sb.append(x.getMessageKey());
		//
		// String message = YmlMessages.populateMessage(x.getMessageKey(), x.getMessageAttributs());
		// log.info("message(" + x.getMessageKey() + ")=" + message);
		// List<String> missingKeys = YmlMessages.missingKeys(x.getMessageKey(), x.getMessageAttributs());
		// Assert.assertEquals(0, missingKeys.size(), "Missing keys { "
		// + missingKeys.stream().collect(Collectors.joining(";")) + " } in message : " + message);
		//
		// log.info("POUR ANALYSE : " + zipFile + ";" + sb.toString() + ";MSG=" + message);
		//
		// actualErrors.add(sb.toString());
		// });
		//
		// resources.stream().forEach(x -> {
		// List<ImportMessage> msgs = getMessages(task, x);
		// if (x.getType().equalsIgnoreCase("file") && x.getStatus().equalsIgnoreCase("ERROR")) {
		// Assert.assertTrue(((msgs != null) ? msgs.size() : 0) > 0,
		// "ImportResource " + x.getName() + " contains ERROR, but no message !!!");
		// }
		// });
		//
		// Set<String> expectedErrors = new TreeSet<String>();
		// Arrays.asList(expectedData).stream().forEach(x -> expectedErrors.add(x.trim()));
		//
		// List<String> expectedNotDetected = expectedErrors.stream().filter(x -> !actualErrors.contains(x))
		// .collect(Collectors.toList());
		// List<String> notExpected = actualErrors.stream().filter(x -> !expectedErrors.contains(x))
		// .collect(Collectors.toList());// );
		//
		// log.info("ALL DETECTED ERRORS (" + actualErrors.size() + "):" + zipFile + ";" + actionReport.getResult() +
		// ";"
		// + actualErrors.stream().collect(Collectors.joining("; ")));
		// if (!notExpected.isEmpty()) {
		// log.error("NOT EXPECTED ERRORS:" + zipFile + ";" + actionReport.getResult() + ";"
		// + notExpected.stream().collect(Collectors.joining("; ")));
		// }
		// if (!expectedNotDetected.isEmpty()) {
		// log.error("EXPECTED BUT NOT DETECTED:" + expectedNotDetected.stream().collect(Collectors.joining("; ")));
		// }

		log.info(actionReport);
		Assert.assertEquals(actionReport.getResult(), expectedActionReportResult);

		// Assert.assertTrue(expectedNotDetected.isEmpty(),
		// expectedNotDetected.size() + " Error(s) not detected (but expected) : "
		// + expectedNotDetected.stream().collect(Collectors.joining("; ")));
		// Assert.assertTrue(notExpected.isEmpty(), notExpected.size() + " Error(s) not expected (but detected) : "
		// + notExpected.stream().collect(Collectors.joining("; ")));
		//
		// // clean database
		// messages.stream().forEach(m -> em.remove(m));
		// resources.stream().forEach(r -> em.remove(r));
		//
		//
		// em.remove(task);
		// utx.commit();

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
		return getMessages(task, null);
	}

	private List<ImportMessage> getMessages(ImportTask task, ImportResource ressource) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ImportMessage> criteria = builder.createQuery(ImportMessage.class);
		Root<ImportMessage> root = criteria.from(ImportMessage.class);
		Predicate predicateTaskId = builder.equal(root.get(ImportMessage_.taskId), task.getId());
		if (ressource != null) {
			Predicate predicateResourceId = builder.equal(root.get(ImportMessage_.resourceId), ressource.getId());
			criteria.where(predicateTaskId, predicateResourceId);
		} else {
			criteria.where(predicateTaskId);
		}

		TypedQuery<ImportMessage> query = em.createQuery(criteria);

		return query.getResultList();

	}
}
