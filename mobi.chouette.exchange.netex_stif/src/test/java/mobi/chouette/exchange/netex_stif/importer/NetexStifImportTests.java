package mobi.chouette.exchange.netex_stif.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
import javax.transaction.UserTransaction;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
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
import org.testng.annotations.Test;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.FootnoteDAO;
import mobi.chouette.dao.JourneyPatternDAO;
import mobi.chouette.dao.LineDAO;
import mobi.chouette.dao.RouteDAO;
import mobi.chouette.dao.RoutingConstraintDAO;
import mobi.chouette.dao.StopPointDAO;
import mobi.chouette.dao.VehicleJourneyDAO;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.JobDataTest;
import mobi.chouette.exchange.report.ActionReport;
import mobi.chouette.exchange.report.ActionReporter.FILE_STATE;
import mobi.chouette.exchange.report.FileReport;
import mobi.chouette.exchange.report.ReportConstant;
import mobi.chouette.exchange.validation.report.CheckPointErrorReport;
import mobi.chouette.exchange.validation.report.ValidationReport;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.JourneyPattern;
import mobi.chouette.model.Route;
import mobi.chouette.model.RoutingConstraint;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.Referential;
import mobi.chouette.persistence.hibernate.ContextHolder;

@Log4j
public class NetexStifImportTests extends Arquillian implements Constant, ReportConstant {

	@EJB
	LineDAO lineDao;

	@EJB
	RouteDAO routeDao;

	@EJB
	JourneyPatternDAO journeyPatternDao;

	@EJB
	RoutingConstraintDAO routingConstraintDao;

	@EJB
	StopPointDAO stopPointDao;

	@EJB
	FootnoteDAO footnoteDao;

	@EJB
	VehicleJourneyDAO vjDao;

	@PersistenceContext(unitName = "referential")
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
				.addAsWebInfResource("postgres-ds.xml").addClass(NetexStifImportTests.class)
				.addClass(JobDataTest.class);

		result = ShrinkWrap.create(EnterpriseArchive.class, "test.ear").addAsLibraries(jars.toArray(new File[0]))
				.addAsModules(modules.toArray(new JavaArchive[0])).addAsModule(testWar)
				.addAsResource(EmptyAsset.INSTANCE, "beans.xml");
		return result;
	}

	protected static InitialContext initialContext;

	protected static final String path = "src/test/data";

	public static void copyFile(String fileName) throws IOException {
		File srcFile = new File(path, fileName);
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

	protected Context initImportContext() {
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
		List<Long> ids = Arrays.asList(new Long[] { 1L, 2L });
		configuration.setIds(ids);
		JobDataTest jobData = new JobDataTest();
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
		context.put(TESTNG, "true"); // mode test
		context.put(OPTIMIZED, Boolean.FALSE);
		return context;

	}

	@Test(groups = { "ImportLine" }, description = "Import Plugin should import file")
	public void verifyImportLine() throws Exception {
		verifyImportLine("OFFRE_TRANSDEV_20170301122517.zip");
	}

	public void verifyImportLine(String zipFile) throws Exception {
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

		// line should be saved
		utx.begin();
		em.joinTransaction();
		// Line line = lineDao.findByObjectId("");

		List<Route> routes = routeDao.findAll();
		Assert.assertEquals(routes.size(), 4, "Routes");
		buidAndSaveJson();
		verifyFromJson();
		// JSONWriter writer = new JSONWriter(w);
		List<RoutingConstraint> routingConstraints = routingConstraintDao.findAll();
		for (RoutingConstraint routingConstraint : routingConstraints) {
			log.info("routing constraint" + routingConstraint.getName());
		}
		// NeptuneTestsUtils.checkMinimalLine(line);

		utx.rollback();

	}

	@Test(groups = { "ImportBadFiles" }, description = "Import invalid Zip format")
	public void verifyImportBadZip() throws Exception {
		String zipFile = "badformated.zip";
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

		// check error report
		ActionReport report = (ActionReport) context.get(REPORT);
		ValidationReport valReport = (ValidationReport) context.get(VALIDATION_REPORT);
		log.info(report);
		log.info(valReport);
		Assert.assertEquals(report.getResult(), "NOK", "result");
		Assert.assertEquals(report.getZips().size(), 1, "zip reported size ");
		FileReport file = report.getZips().get(0);
		Assert.assertEquals(file.getStatus(),FILE_STATE.ERROR,"zip status reported");
		Assert.assertEquals(file.getCheckPointErrorCount(),1,"zip error reported");
		CheckPointErrorReport error = valReport.getCheckPointErrors().get(file.getCheckPointErrorKeys().get(0).intValue());
		Assert.assertEquals(error.getTestId(),"1-NeTExStif-1","checkpoint code");
		Assert.assertEquals(error.getKey(),"1_netexstif_1_1","message code");
		Assert.assertEquals(error.getSource().getFile().getFilename(),zipFile,"source filename");

	}
	@Test(groups = { "ImportBadFiles" }, description = "Import Zip without calendar")
	public void verifyImportNoCalendarZip() throws Exception {
		String zipFile = "nocalendar.zip";
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

		// check error report
		ActionReport report = (ActionReport) context.get(REPORT);
		ValidationReport valReport = (ValidationReport) context.get(VALIDATION_REPORT);
		log.info(report);
		log.info(valReport);
		Assert.assertEquals(report.getResult(), "NOK", "result");
		Assert.assertEquals(report.getZips().size(), 1, "zip reported size ");
		FileReport file = report.getZips().get(0);
		Assert.assertEquals(file.getStatus(),FILE_STATE.ERROR,"zip status reported");
		Assert.assertEquals(file.getCheckPointErrorCount(),1,"zip error reported");
		CheckPointErrorReport error = valReport.getCheckPointErrors().get(file.getCheckPointErrorKeys().get(0).intValue());
		Assert.assertEquals(error.getTestId(),"1-NeTExStif-1","checkpoint code");
		Assert.assertEquals(error.getKey(),"1_netexstif_1_2","message code");
		Assert.assertEquals(error.getValue(),"calendrier.xml","value");
		Assert.assertEquals(error.getSource().getFile().getFilename(),zipFile,"source filename");
		

	}

	@Test(groups = { "ImportBadFiles" }, description = "Import Zip without offer")
	public void verifyImportNoOfferZip() throws Exception {
		String zipFile = "nooffer.zip";
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

		// check error report
		ActionReport report = (ActionReport) context.get(REPORT);
		ValidationReport valReport = (ValidationReport) context.get(VALIDATION_REPORT);
		log.info(report);
		log.info(valReport);
		Assert.assertEquals(report.getResult(), "NOK", "result");
		Assert.assertEquals(report.getZips().size(), 1, "zip reported size ");
		FileReport file = report.getZips().get(0);
		Assert.assertEquals(file.getStatus(),FILE_STATE.ERROR,"zip status reported");
		Assert.assertEquals(file.getCheckPointErrorCount(),1,"zip error reported");
		CheckPointErrorReport error = valReport.getCheckPointErrors().get(file.getCheckPointErrorKeys().get(0).intValue());
		Assert.assertEquals(error.getTestId(),"1-NeTExStif-1","checkpoint code");
		Assert.assertEquals(error.getKey(),"1_netexstif_1_3","message code");
		Assert.assertEquals(error.getSource().getFile().getFilename(),zipFile,"source filename");
		

	}

	
	private void verifyFromJson() throws JSONException, IOException {
		// InputStream is =
		// this.getClass().getResourceAsStream("/tmp/offre.json");

		InputStream is = new FileInputStream(new File("/tmp/offer0.json"));

		String data = IOUtils.toString(is);
		JSONObject jsonObject = new JSONObject(data);
		verifyRoutes(jsonObject.getJSONArray("routes"));
		verifyJourneyPatterns(jsonObject.getJSONArray("journeyPatterns"));
		verifyRoutingConstraints(jsonObject.getJSONArray("routingConstraints"));
		verifyStopPoints(jsonObject.getJSONArray("stopPoints"));
		verifyFootnotes(jsonObject.getJSONArray("footnotes"));
	}

	private void verifyFootnotes(JSONArray footnotes) throws JSONException {
		for (int i = 0; i < footnotes.length(); ++i) {
			verifyFootnote(footnotes.getJSONObject(i));
		}
	}

	private void verifyFootnote(JSONObject jsonFootnote) throws JSONException {
		log.info("footnote:"+ jsonFootnote);
		Long id = jsonFootnote.getLong("id");
		Footnote footnote = footnoteDao.find(id);
		Assert.assertEquals(footnote.getLabel(), jsonFootnote.getString("label"));
		Assert.assertEquals(footnote.getCode(), jsonFootnote.getString("code"));

	}

	private void verifyRoutes(JSONArray routes) throws JSONException {
		for (int i = 0; i < routes.length(); ++i) {
			verifyRoute(routes.getJSONObject(i));
		}
	}

	private void verifyRoute(JSONObject jsonRoute) throws JSONException {
		String id = jsonRoute.getString("id");
		Route route = routeDao.findByObjectId(id);
		Assert.assertEquals(route.getName(), jsonRoute.getString("name"));
		Assert.assertEquals(route.getDirection().toString(), jsonRoute.getString("direction"));
		Assert.assertEquals(route.getPublishedName(), jsonRoute.getString("publishedName"));
		Assert.assertEquals(route.getOppositeRoute().getObjectId(), jsonRoute.getString("inverse"));
	}

	private void verifyJourneyPatterns(JSONArray jsonJourneyPatterns) throws JSONException {
		for (int i = 0; i < jsonJourneyPatterns.length(); ++i) {
			verifyJourneyPattern(jsonJourneyPatterns.getJSONObject(i));
		}
	}

	private void verifyJourneyPattern(JSONObject jjp) throws JSONException {
		String id = jjp.getString("id");
		JourneyPattern jp = journeyPatternDao.findByObjectId(id);
		Assert.assertEquals(jp.getName(), jjp.getString("name"));
		Assert.assertEquals(jp.getPublishedName(), jjp.getString("publishedName"));
		Assert.assertEquals(jp.getRegistrationNumber(), jjp.getString("registrationNumber"));
		Assert.assertEquals(jp.getRoute().getObjectId(), jjp.getString("routeId"));
	}

	private void verifyRoutingConstraints(JSONArray rcs) throws JSONException {
		for (int i = 0; i < rcs.length(); ++i) {
			verifyRoutingConstraint(rcs.getJSONObject(i));
		}
	}

	private void verifyRoutingConstraint(JSONObject jrc) throws JSONException {
		RoutingConstraint rc = routingConstraintDao.findByObjectId(jrc.getString("id"));
		Assert.assertEquals(rc.getName(), jrc.getString("name"));
		Assert.assertEquals(rc.getRoute().getObjectId(), jrc.getString("routeId"));
	}

	private void verifyStopPoints(JSONArray jsps) throws JSONException {
		for (int i = 0; i < jsps.length(); ++i) {
			verifyStopPoint(jsps.getJSONObject(i));
		}
	}

	private void verifyStopPoint(JSONObject jsp) throws JSONException {
		StopPoint sp = stopPointDao.findByObjectId(jsp.getString("id"));
		Assert.assertEquals(sp.getPosition(), new Integer(jsp.getInt("position")));
		Assert.assertEquals(sp.getStopAreaId(), new Long(jsp.getInt("areaId")));
		Assert.assertEquals(sp.getRoute().getObjectId(), jsp.getString("routeId"));
	}

	static int count = 0;

	private void buidAndSaveJson() throws JSONException, FileNotFoundException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("routes", buildJsonRoute());
		jsonObject.put("routingConstraints", buildRoutingConstraints());
		jsonObject.put("journeyPatterns", buildJsonJourneyPatterns());
		jsonObject.put("stopPoints", buildJsonStopPoints());
		JSONArray tmp = buildJsonFootnotes();
		if (tmp.length() > 0) {
			jsonObject.put("footnotes", tmp);
		}
		File toSave = new File("/tmp/offer" + count++ + ".json");
		PrintWriter pw = new PrintWriter(toSave);
		pw.write(jsonObject.toString());
		pw.close();
	}

	private JSONObject buildJsonJourneyPattern(JourneyPattern journeyPattern) throws JSONException {
		JSONObject jpObject = new JSONObject();
		jpObject.put("id", journeyPattern.getObjectId());
		jpObject.put("name", journeyPattern.getName());
		jpObject.put("publishedName", journeyPattern.getPublishedName());
		jpObject.put("registrationNumber", journeyPattern.getRegistrationNumber());
		jpObject.put("routeId", journeyPattern.getRoute().getObjectId());
		return jpObject;
	}

	private JSONArray buildJsonJourneyPatterns() throws JSONException {
		List<JourneyPattern> journeyPatterns = journeyPatternDao.findAll();
		JSONArray jsonArray = new JSONArray();
		for (JourneyPattern journeyPattern : journeyPatterns) {
			JSONObject jpObject = buildJsonJourneyPattern(journeyPattern);
			jsonArray.put(jpObject);
		}
		return jsonArray;
	}

	private JSONArray buildJsonJourneyPatterns(List<JourneyPattern> journeyPatterns) throws JSONException {
		JSONArray jsonArray = new JSONArray();
		for (JourneyPattern journeyPattern : journeyPatterns) {
			JSONObject jpObject = buildJsonJourneyPattern(journeyPattern);
			jsonArray.put(jpObject);
		}
		return jsonArray;
	}

	private JSONArray buildJsonRoute() throws JSONException {
		JSONArray jsonArray = new JSONArray();
		List<Route> routes = routeDao.findAll();
		for (Route route : routes) {
			JSONObject routeObject = new JSONObject();
			routeObject.put("id", route.getObjectId());
			routeObject.put("name", route.getName());
			routeObject.put("direction", route.getDirection().toString());
			routeObject.put("publishedName", route.getPublishedName());
			// routeObject.put("lineId", route.getLine().getObjectId());
			routeObject.put("inverse", route.getOppositeRoute().getObjectId());
			routeObject.put("journeyPatterns", buildJsonJourneyPatterns(route.getJourneyPatterns()));
			JSONArray tmp = buildRoutingConstraints(route.getRoutingConstraints());
			if (tmp.length() > 0) {
				routeObject.put("routingConstraints", tmp);
			}
			tmp = buildJsonStopPoints(route.getStopPoints());
			if (tmp.length() > 0) {
				routeObject.put("stopPoints", tmp);
			}
			jsonArray.put(routeObject);
		}
		return jsonArray;
	}

	private JSONArray buildJsonFootnotes() throws JSONException {
		List<Footnote> footnotes = footnoteDao.findAll();
		JSONArray jsonArray = new JSONArray();
		for (Footnote footnote : footnotes) {
			JSONObject tmp = buildJsonFootnote(footnote);
			jsonArray.put(tmp);
		}
		return jsonArray;
	}

	private JSONObject buildJsonFootnote(Footnote footnote) throws JSONException {
		JSONObject fnObject = new JSONObject();
		log.info("footnote id : " + footnote.getId());
		fnObject.put("id", footnote.getId());
		fnObject.put("label", footnote.getLabel());
		fnObject.put("code", footnote.getCode());
		return fnObject;
	}

	private JSONObject buildJsonStopPoint(StopPoint stopPoint) throws JSONException {
		JSONObject stObject = new JSONObject();
		stObject.put("id", stopPoint.getObjectId());
		stObject.put("position", stopPoint.getPosition());
		stObject.put("routeId", stopPoint.getRoute().getObjectId());
		stObject.put("areaId", stopPoint.getStopAreaId());
		return stObject;
	}

	private JSONArray buildJsonStopPoints(List<StopPoint> stopPoints) throws JSONException {
		JSONArray jsonArray = new JSONArray();
		for (StopPoint stopPoint : stopPoints) {
			JSONObject stObject = buildJsonStopPoint(stopPoint);
			jsonArray.put(stObject);
		}
		return jsonArray;
	}

	private JSONArray buildJsonStopPoints() throws JSONException {
		List<StopPoint> stopPoints = stopPointDao.findAll();
		JSONArray jsonArray = new JSONArray();
		for (StopPoint stopPoint : stopPoints) {
			JSONObject stObject = buildJsonStopPoint(stopPoint);
			jsonArray.put(stObject);
		}
		return jsonArray;
	}

	private JSONObject buildRoutingConstraint(RoutingConstraint routingConstraint) throws JSONException {
		JSONObject rcObject = new JSONObject();
		rcObject.put("id", routingConstraint.getObjectId());
		rcObject.put("name", routingConstraint.getName());
		rcObject.put("routeId", routingConstraint.getRoute().getObjectId());
		return rcObject;
	}

	private JSONArray buildRoutingConstraints(List<RoutingConstraint> routingConstraints) throws JSONException {
		JSONArray jsonArray = new JSONArray();
		for (RoutingConstraint routingConstraint : routingConstraints) {
			JSONObject rcObject = buildRoutingConstraint(routingConstraint);
			jsonArray.put(rcObject);
		}
		return jsonArray;
	}

	private JSONArray buildRoutingConstraints() throws JSONException {
		JSONArray jsonArray = new JSONArray();
		List<RoutingConstraint> routingConstraints = routingConstraintDao.findAll();
		for (RoutingConstraint routingConstraint : routingConstraints) {
			JSONObject rcObject = buildRoutingConstraint(routingConstraint);
			jsonArray.put(rcObject);
		}
		return jsonArray;
	}
}
