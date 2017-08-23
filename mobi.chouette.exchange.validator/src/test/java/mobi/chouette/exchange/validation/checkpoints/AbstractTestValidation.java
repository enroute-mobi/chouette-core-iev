package mobi.chouette.exchange.validation.checkpoints;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.apache.commons.io.FileUtils;
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
import mobi.chouette.common.JobData.ACTION;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.LoadSharedDataCommand;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.report.ObjectCollectionReport;
import mobi.chouette.exchange.report.ObjectReport;
import mobi.chouette.exchange.report.ReportConstant;
import mobi.chouette.exchange.validation.report.CheckPointErrorReport;
import mobi.chouette.exchange.validation.report.CheckPointReport;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.exchange.validator.JobDataTest;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.exchange.validator.checkpoints.CheckPointConstant;
import mobi.chouette.model.ChouetteLocalizedObject;
import mobi.chouette.model.util.Referential;
import mobi.chouette.persistence.hibernate.ChouetteTenantIdentifierGenerator;
import mobi.chouette.persistence.hibernate.ContextHolder;

@Log4j
public abstract class AbstractTestValidation extends Arquillian
		implements Constant, ReportConstant, CheckPointConstant {

	public static final String SCHEMA_NAME = "iev_check_points";

	protected InitialContext initialContext;

	@PersistenceContext(unitName = "referential")
	EntityManager em;

	@Inject
	UserTransaction utx;

	public void init() {
		if (initialContext == null) {
			Locale.setDefault(Locale.ENGLISH);
			try {
				initialContext = new InitialContext();
			} catch (NamingException e) {
				log.error("initialContext", e);
			}

		}
	}

	public static EnterpriseArchive buildDeployment(Class<? extends AbstractTestValidation> clazz) {

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
				.addAsWebInfResource("postgres-ds.xml").addClass(JobDataTest.class)
				.addClass(AbstractTestValidation.class).addClass(clazz);

		result = ShrinkWrap.create(EnterpriseArchive.class, "test.ear").addAsLibraries(jars.toArray(new File[0]))
				.addAsModules(modules.toArray(new JavaArchive[0])).addAsModule(testWar)
				.addAsResource(EmptyAsset.INSTANCE, "beans.xml");
		return result;
	}

	protected void loadSharedData(Context context) throws Exception {
		Command command = CommandFactory.create(initialContext, LoadSharedDataCommand.class.getName());
		command.execute(context);
	}

	protected Context initValidatorContext() {
		init();
		ContextHolder.setContext(SCHEMA_NAME); // set tenant schema

		Context context = new Context();
		context.put(INITIAL_CONTEXT, initialContext);
		context.put(REPORT, new ActionReport());
		context.put(REFERENTIAL, new Referential());
		context.put(VALIDATION_REPORT, new ValidationReport());
		ValidateParameters configuration = new ValidateParameters();
		context.put(CONFIGURATION, configuration);
		configuration.setName("name");
		configuration.setUserName("userName");
		configuration.setOrganisationName("organisation");

		configuration.setReferentialName("checkPoints");
		configuration.setLineReferentialId(1L);
		configuration.setStopAreaReferentialId(1L);
		configuration.setIds(Arrays.asList(new Long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L }));
		JobDataTest test = new JobDataTest();
		context.put(JOB_DATA, test);
		test.setPathName("target/referential/test");
		File f = new File("target/referential/test");
		if (f.exists())
			try {
				FileUtils.deleteDirectory(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		f.mkdirs();
		test.setReferential(SCHEMA_NAME);
		test.setAction(ACTION.validator);
		context.put("testng", "true");
		context.put(SOURCE, SOURCE_DATABASE);
		context.put(OPTIMIZED, Boolean.FALSE);
		return context;

	}

	protected static final String path = "src/test/data/checkpoints";

	public static void copyFile(String fileName) throws IOException {
		File srcFile = new File(path, fileName);
		File destFile = new File("target/referential/test", fileName);
		FileUtils.copyFile(srcFile, destFile);
	}

	public void initSchema() throws Exception {
		init();
		ContextHolder.setContext(SCHEMA_NAME);
		BufferedReader r = new BufferedReader(new FileReader("src/test/data/validation_data.sql"));
		ChouetteTenantIdentifierGenerator.deleteTenant(SCHEMA_NAME);
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

	/**
	 * calculate distance on spheroid
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static double distance(ChouetteLocalizedObject obj1, ChouetteLocalizedObject obj2) {
		double long1rad = Math.toRadians(obj1.getLongitude().doubleValue());
		double lat1rad = Math.toRadians(obj1.getLatitude().doubleValue());
		double long2rad = Math.toRadians(obj2.getLongitude().doubleValue());
		double lat2rad = Math.toRadians(obj2.getLatitude().doubleValue());

		double alpha = Math.cos(lat1rad) * Math.cos(lat2rad) * Math.cos(long2rad - long1rad)
				+ Math.sin(lat1rad) * Math.sin(lat2rad);

		double distance = 6378. * Math.acos(alpha);

		return distance * 1000.;
	}

	public static long diffTime(Time first, Time last) {
		if (first == null || last == null)
			return Long.MIN_VALUE; // TODO
		long diff = last.getTime() / 1000L - first.getTime() / 1000L;
		if (diff < 0)
			diff += 86400L; // step upon midnight : add one day in seconds
		return diff;
	}

	/**
	 * check and return details for checkpoint
	 * 
	 * @param report
	 * @param key
	 * @param detailSize
	 *            (negative for not checked
	 * @return
	 */
	protected List<CheckPointErrorReport> checkReportForTest(ValidationReport report, String key, int detailSize) {
		Assert.assertFalse(report.getCheckPoints().isEmpty(), " report must have items");
		Assert.assertNotNull(report.findCheckPointReportByName(key), " report must have 1 item on key " + key);
		CheckPointReport checkPointReport = report.findCheckPointReportByName(key);
		if (detailSize >= 0)
			Assert.assertEquals(checkPointReport.getCheckPointErrorCount(), detailSize,
					" checkpoint must have " + detailSize + " detail");

		List<CheckPointErrorReport> details = new ArrayList<>();
		for (Integer errorkey : checkPointReport.getCheckPointErrorKeys()) {
			details.add(report.getCheckPointErrors().get(errorkey));
		}
		return details;
	}

	protected void checkReports(Context context, String lineId, String checkPointCode, String messageCode, String value,
			OBJECT_STATE state) {
		checkReports(context, lineId, checkPointCode, messageCode, value, state, 1);

	}

	protected void checkReports(Context context, String lineId, String checkPointCode, String messageCode, String value,
			OBJECT_STATE state, int reportCount) {
		ActionReport report = (ActionReport) context.get(REPORT);

		ValidationReport valReport = (ValidationReport) context.get(VALIDATION_REPORT);
		log.info(report);
		log.info(valReport);
		Assert.assertEquals(report.getResult(), "OK", "result");
		Assert.assertEquals(report.getFiles().size(), 0, "no file reported ");
		CheckPointErrorReport error = null;
		ObjectCollectionReport lineReports = report.getCollections().get(OBJECT_TYPE.LINE);
		Assert.assertNotNull(lineReports, " lines reported");
		Assert.assertEquals(lineReports.getObjectReports().size(), 1, "one line reported");
		ObjectReport lineReport = lineReports.getObjectReports().get(0);

		if (state.equals(OBJECT_STATE.ERROR)) {
			Assert.assertEquals(lineReport.getStatus(), OBJECT_STATE.ERROR, "line status reported");
			Assert.assertEquals(lineReport.getCheckPointErrorCount(), reportCount, "line error reported");
			error = valReport.getCheckPointErrors().get(lineReport.getCheckPointErrorKeys().get(0).intValue());
		} else {
			Assert.assertEquals(lineReport.getStatus(), OBJECT_STATE.OK, "line status reported");
			Assert.assertEquals(lineReport.getCheckPointWarningCount(), reportCount, "line warning reported");
			error = valReport.getCheckPointErrors().get(lineReport.getCheckPointWarningKeys().get(0).intValue());
		}
		Assert.assertEquals(error.getTestId(), checkPointCode, "checkpoint code");
		Assert.assertEquals(error.getKey(), messageCode, "message code");
		if (value == null)
			Assert.assertNull(error.getValue(), "value");
		else
			Assert.assertEquals(error.getValue(), value, "value");
		Assert.assertNotNull(error.getSource().getObjectId(), "source objectId");
		Assert.assertNull(error.getSource().getFile(), "no source filename");

	}

	protected final void checkNoReports(Context context, String lineId) {
		ActionReport report = (ActionReport) context.get(REPORT);

		ValidationReport valReport = (ValidationReport) context.get(VALIDATION_REPORT);
		log.info(report);
		log.info(valReport);
		Assert.assertEquals(report.getResult(), "OK", "result");
		ObjectCollectionReport lineReports = report.getCollections().get(OBJECT_TYPE.LINE);
		Assert.assertNotNull(lineReports, " lines reported");
		Assert.assertEquals(lineReports.getObjectReports().size(), 1, "one line reported");
		ObjectReport lineReport = lineReports.getObjectReports().get(0);
		Assert.assertEquals(lineReport.getStatus(), OBJECT_STATE.OK, "line status reported");
		Assert.assertEquals(lineReport.getCheckPointErrorCount(), 0, "no line error reported");
		Assert.assertEquals(lineReport.getCheckPointWarningCount(), 0, "no line warning reported");

	}

}
