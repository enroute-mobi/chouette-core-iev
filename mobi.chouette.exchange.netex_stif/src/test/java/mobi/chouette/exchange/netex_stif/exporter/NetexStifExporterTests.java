package mobi.chouette.exchange.netex_stif.exporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import org.hibernate.Session;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.testng.annotations.Test;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData.ACTION;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.ImportTaskDAO;
import mobi.chouette.dao.ReferentialDAO;
import mobi.chouette.exchange.netex_stif.JobDataImpl;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.type.DateRange;
import mobi.chouette.model.util.Referential;
import mobi.chouette.persistence.hibernate.ChouetteTenantIdentifierGenerator;
import mobi.chouette.persistence.hibernate.ContextHolder;

@Log4j
public class NetexStifExporterTests extends Arquillian {

	protected static InitialContext initialContext;

	public static final String SCHEMA_NAME = "iev_export";

	@EJB
	ReferentialDAO referentialDAO;

	@EJB
	ImportTaskDAO importTaskDAO;

	@PersistenceContext(unitName = "public")
	EntityManager em;

	@Inject
	UserTransaction utx;

	@Deployment
	public static EnterpriseArchive createDeployment() {

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
				.addAsWebInfResource("postgres-ds.xml").addClass(NetexStifExporterTests.class)
				.addClass(JobDataImpl.class);

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

	protected Context initExporterContext(boolean all) {
		init();
		ContextHolder.setContext(SCHEMA_NAME); // set tenant schema

		Context context = new Context();
		context.put(Constant.INITIAL_CONTEXT, initialContext);
		context.put(Constant.REPORT, new ActionReport());
		context.put(Constant.REFERENTIAL, new Referential());
		context.put(Constant.VALIDATION_REPORT, new ValidationReport());
		NetexStifExportParameters configuration = new NetexStifExportParameters();
		context.put(Constant.CONFIGURATION, configuration);
		configuration.setName("name");
		configuration.setUserName("userName");
		configuration.setOrganisationName("organisation");

		configuration.setReferentialName("export");
		configuration.setLineReferentialId(1L);
		configuration.setStopAreaReferentialId(1L);
		if (all) {
			configuration.setIds(Arrays.asList(new Long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L }));
		} else {
			configuration.setReferencesType("line");
			configuration.setIds(Arrays.asList(new Long[] { 3L }));
		}

		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2017);
		c.set(Calendar.MONTH, Calendar.JUNE);
		c.set(Calendar.DAY_OF_MONTH, 25);
		c.set(Calendar.HOUR_OF_DAY, 12);
		DateRange dr = new DateRange();
		dr.setFirst(new Date(c.getTime().getTime()));
		configuration.setStartDate(new Date(c.getTime().getTime()));
		c.add(Calendar.DATE, 28);
		dr.setLast(new Date(c.getTime().getTime()));
		configuration.getValidityPeriods().add(dr);
		configuration.setEndDate(new Date(c.getTime().getTime()));

		JobDataImpl test = new JobDataImpl();
		context.put(Constant.JOB_DATA, test);
		File f = null;
		if (all) {
			test.setPathName("target/referential/test_all");
			f = new File("target/referential/test_all");
		} else {
			test.setPathName("target/referential/test_1");
			f = new File("target/referential/test_1");
		}
		if (f.exists())
			try {
				FileUtils.deleteDirectory(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		f.mkdirs();
		test.setReferential(SCHEMA_NAME);
		test.setAction(ACTION.exporter);
		context.put("testng", "true"); // no Progression reporting
		context.put(Constant.SOURCE, Constant.SOURCE_DATABASE);
		return context;

	}

	protected void initSchema() throws Exception {
		init();
		ContextHolder.setContext(SCHEMA_NAME);
		BufferedReader r = new BufferedReader(new FileReader("src/test/data/export_data.sql"));
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

	@Test(groups = { "export" }, description = "export all data", priority = 3001)
	public void verifyExportAll() throws Exception {
		log.info(Color.CYAN + " export all data " + Color.NORMAL);
		initSchema();
		Context context = initExporterContext(true);
		context.put(Constant.REFERENTIAL, new Referential());
		Command command = CommandFactory.create(initialContext, NetexStifExporterCommand.class.getName());
		try {
			command.execute(context);
		} catch (Exception ex) {
			log.error("test failed", ex);
			throw ex;
		}

	}

	@Test(groups = { "export" }, description = "export line 3", priority = 3002)
	public void verifyExportLine() throws Exception {
		log.info(Color.CYAN + " export line" + Color.NORMAL);
		initSchema();
		Context context = initExporterContext(false);
		context.put(Constant.REFERENTIAL, new Referential());
		Command command = CommandFactory.create(initialContext, NetexStifExporterCommand.class.getName());
		try {
			command.execute(context);
		} catch (Exception ex) {
			log.error("test failed", ex);
			throw ex;
		}

	}

}
