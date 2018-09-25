package mobi.chouette.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.JobData;
import mobi.chouette.common.PropertyNames;
import mobi.chouette.dao.ActionDAO;
import mobi.chouette.dao.DaoException;
import mobi.chouette.dao.ReferentialDAO;
import mobi.chouette.model.ActionTask;
import mobi.chouette.model.importer.ImportTask;
import mobi.chouette.scheduler.Scheduler;
import mobi.chouette.service.JobService.STATUS;

@Stateless(name = JobServiceManager.BEAN_NAME)
@Startup
@Log4j
public class JobServiceManager {

	public static final String BEAN_NAME = "JobServiceManager";

	public static final String APPLICATION_NAME = "boiv";

	@EJB
	ReferentialDAO referentialDAO;

	@EJB
	ActionDAO actionDAO;

	@EJB
	JobServiceManager jobServiceManager;

	@EJB
	Scheduler scheduler;

	private static String rootDirectory;

	private static Set<String> intializedContexts = new HashSet<>();

	@PostConstruct
	public synchronized void init() {
		if (intializedContexts.contains(APPLICATION_NAME))
			return;
		System.setProperty(APPLICATION_NAME + PropertyNames.MAX_STARTED_JOBS, "5");
		System.setProperty(APPLICATION_NAME + PropertyNames.MAX_COPY_BY_JOB, "5");
		System.setProperty(APPLICATION_NAME + PropertyNames.GUI_URL_BASENAME, "");
		System.setProperty(APPLICATION_NAME + PropertyNames.GUI_URL_TOKEN, "");
		try {
			// set default properties
			System.setProperty(APPLICATION_NAME + PropertyNames.ROOT_DIRECTORY, System.getProperty("user.home"));

			// try to read properties
			File propertyFile = new File(
					"/etc/chouette/" + APPLICATION_NAME + File.separatorChar + APPLICATION_NAME + ".properties");
			if (propertyFile.exists() && propertyFile.isFile()) {
				loadProperties(propertyFile);
			} else {
				log.info("no property file found " + propertyFile.getAbsolutePath() + " , using default properties");
			}
		} catch (Exception e) {
			log.error("cannot process properties", e);
		}
		rootDirectory = System.getProperty(APPLICATION_NAME + PropertyNames.ROOT_DIRECTORY);
		intializedContexts.add(APPLICATION_NAME);
	}

	private void loadProperties(File propertyFile) {
		try (FileInputStream fileInput = new FileInputStream(propertyFile);) {
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();
			log.info("reading properties from " + propertyFile.getAbsolutePath());
			for (String key : properties.stringPropertyNames()) {
				if (key.startsWith(APPLICATION_NAME))
					System.setProperty(key, properties.getProperty(key));
				else
					System.setProperty(APPLICATION_NAME + "." + key, properties.getProperty(key));
			}
		} catch (IOException e) {
			log.error("cannot read properties " + propertyFile.getAbsolutePath() + " , using default properties", e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public JobService create(String action, Long id) throws ServiceException {
		JobService jobService = jobServiceManager.createJob(action, id);
		scheduler.schedule();
		return jobService;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JobService createJob(String action, Long id) throws ServiceException {

		// Instancier le modèle du service 'upload'
		if (action.equals(JobData.ACTION.importer.name())) {
			return createImportJob(id);
		} else if (action.equals(JobData.ACTION.exporter.name())) {
			return createExportJob(id);
		} else if (action.equals(JobData.ACTION.validator.name())) {
			return createValidatorJob(id);
		} else {
			throw new RequestServiceException(RequestExceptionCode.UNKNOWN_ACTION, action);
		}

	}

	private JobService createExportJob(Long id) throws ServiceException {
		if (id == 0L)
			throw new RequestServiceException(RequestExceptionCode.UNKNOWN_JOB, "invalid export id " + id);
		ActionTask task = null;
		try {
			// Instancier le modèle du service 'upload'
			task = actionDAO.find(JobData.ACTION.exporter, id);
			if (task == null) {
				throw new RequestServiceException(RequestExceptionCode.UNKNOWN_JOB, "unknown export id " + id);
			}
			if (!task.getStatus().equalsIgnoreCase(JobService.STATUS.NEW.name())) {
				throw new RequestServiceException(RequestExceptionCode.SCHEDULED_JOB, "already managed job " + id);
			}
			JobService jobService = new JobService(APPLICATION_NAME, rootDirectory, task);

			log.info("job " + jobService.getId() + " found for export ");
			// mkdir
			deleteDirectory(jobService);
			Files.createDirectories(jobService.getPath());

			jobService.setStatus(JobService.STATUS.PENDING);
			task.setStatus(jobService.getStatus().name().toLowerCase());
			actionDAO.update(task);
			return jobService;

		} catch (RequestServiceException ex) {
			log.info("fail to create export job " + ex.getMessage());
			abortTask(task);
			throw ex;
		} catch (ServiceException ex) {
			log.info("invalid export job data" + ex.getMessage());
			abortTask(task);
			throw ex;
		} catch (Exception ex) {
			log.info("fail to create export job " + id + " " + ex.getMessage() + " " + ex.getClass().getName(), ex);
			abortTask(task);
			throw new ServiceException(ServiceExceptionCode.INTERNAL_ERROR, ex);
		}
	}

	private JobService createImportJob(Long id) throws ServiceException {
		if (id == 0L)
			throw new RequestServiceException(RequestExceptionCode.UNKNOWN_JOB, "invalid import id " + id);

		ImportTask importTask = null;
		try {
			// Instancier le modèle du service 'upload'
			importTask = (ImportTask) actionDAO.find(JobData.ACTION.importer, id);
			
			if (importTask == null) {
				throw new RequestServiceException(RequestExceptionCode.UNKNOWN_JOB, "unknown import id " + id);
			}
			else if (!importTask.getStatus().equalsIgnoreCase(JobService.STATUS.NEW.name())) {
				throw new RequestServiceException(RequestExceptionCode.SCHEDULED_JOB, "already managed job " + id);
			}
			
			log.info("Log importTask : "+importTask.toString() );
			
			JobService jobService = new JobService(APPLICATION_NAME, rootDirectory, importTask);
			
			log.info("job " + jobService.getId() + " found for import ");
			// mkdir
			deleteDirectory(jobService);
			Files.createDirectories(jobService.getPath());
			// copy file from Ruby server

			String guiBaseUrl = System.getProperty(APPLICATION_NAME + PropertyNames.GUI_URL_BASENAME);
			String guiToken =  System.getProperty(APPLICATION_NAME + PropertyNames.GUI_URL_TOKEN);

			if (guiBaseUrl == null || guiBaseUrl.isEmpty()) {
				throw new Exception("Missing gui.url.base param in conf file."); 
			}
			if (guiToken == null || guiToken.isEmpty()) {
				throw new Exception("Missing gui.url.token param in conf file."); 
			}
		
			String	urlPath = guiBaseUrl + "/api/v1/internals/netex_imports/" + importTask.getId()+ "/download";
		
			log.info("createImportJob 4 - pre uploadImportFile : "+urlPath);

			uploadImportFileFromHttp(jobService, urlPath);

			jobService.setStatus(JobService.STATUS.PENDING);
			importTask.setStatus(jobService.getStatus().name().toLowerCase());
			actionDAO.update(importTask);
			return jobService;

		} catch (RequestServiceException ex) {
			log.info("fail to create import job " + ex.getMessage());
			abortTask(importTask);
			throw ex;
		} catch (ServiceException ex) {
			log.info("invalid import job data" + ex.getMessage());
			abortTask(importTask);
			throw ex;
		} catch (Exception ex) {
			log.info("fail to create import job " + id + " " + ex.getMessage() + " " + ex.getClass().getName(), ex);
			abortTask(importTask);
			throw new ServiceException(ServiceExceptionCode.INTERNAL_ERROR, ex);
		}

	}

	private void abortTask(ActionTask actionTask) {
		if (actionTask != null) {
			try {
				actionTask.setStatus(JobService.STATUS.ABORTED.name().toLowerCase());
				actionDAO.update(actionTask);
			} catch (Exception e) {
				log.error("cannot set bad " + actionTask.getAction() + " task status or task " + actionTask.getId()
						+ " : " + e.getMessage());
			}
		}
	}

	private void uploadImportFile(JobService jobService, String urlName) throws ServiceException {
		log.info("testlog uploadImportFile");

		try {
			URL url = new URL(urlName);
			log.info("Import URL : " + urlName);
			log.info("Full URL : "+url.toString());
			File dest = new File(jobService.getPathName(), jobService.getInputFilename());
			log.info("Import destination file  : "+jobService.getPathName()+" : "+jobService.getInputFilename());
			FileUtils.copyURLToFile(url, dest);
		} catch (Exception ex) {
			log.error("test 3 : fail to get file for import job " + ex.getMessage());
			throw new ServiceException(ServiceExceptionCode.INTERNAL_ERROR, "cannot manage URL " + urlName);
		}
	}
	
	
	private void uploadImportFileFromHttp(JobService jobService, String urlName) throws ServiceException {
		log.info("UploadImportFileFromHttp : " + urlName);

		String guiToken = System.getProperty(APPLICATION_NAME + PropertyNames.GUI_URL_TOKEN);
		File dest = new File(jobService.getPathName(), jobService.getInputFilename());
		
		log.info("UploadImportFileFromHttp destination file  : "+jobService.getPathName()+" : "+jobService.getInputFilename());

		try (CloseableHttpClient httpclient = HttpClients.createDefault();
				BufferedOutputStream baos = new BufferedOutputStream(new FileOutputStream(dest))) {
			Thread.sleep(1000L);
			HttpGet httpget = new HttpGet(urlName);
			httpget.setHeader(HttpHeaders.AUTHORIZATION, "Token token=" + guiToken);

			HttpResponse response = httpclient.execute(httpget);
			// check response headers.
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode >= 200 && statusCode < 300) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				IOUtils.copy(content, baos);
			} else {
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			}

		} catch (Exception ex) {
			log.error("test 2 :  fail to get file for import job " + ex.getMessage());
			throw new ServiceException(ServiceExceptionCode.INTERNAL_ERROR, "cannot manage URL " + urlName);
		}

	}

	private JobService createValidatorJob(Long id) throws ServiceException {
		if (id == 0L)
			throw new RequestServiceException(RequestExceptionCode.UNKNOWN_JOB, "invalid validator id " + id);
		ActionTask task = null;
		try {
			task = actionDAO.find(JobData.ACTION.validator, id);
			if (task == null) {
				throw new RequestServiceException(RequestExceptionCode.UNKNOWN_JOB, "unknown validator id " + id);
			}
			if (!task.getStatus().equalsIgnoreCase(JobService.STATUS.NEW.name())) {
				throw new RequestServiceException(RequestExceptionCode.SCHEDULED_JOB, "already managed job " + id);
			}
			JobService jobService = new JobService(APPLICATION_NAME, rootDirectory, task);

			log.info("job " + jobService.getId() + " found for validator ");
			// mkdir
			deleteDirectory(jobService);
			Files.createDirectories(jobService.getPath());

			jobService.setStatus(JobService.STATUS.PENDING);
			task.setStatus(jobService.getStatus().name().toLowerCase());
			actionDAO.update(task);
			return jobService;

		} catch (RequestServiceException ex) {
			log.info("fail to create validator job " + ex.getMessage());
			abortTask(task);
			throw ex;
		} catch (ServiceException ex) {
			log.info("invalid validator job data" + ex.getMessage());
			abortTask(task);
			throw ex;
		} catch (Exception ex) {
			log.info("fail to create validator job " + id + " " + ex.getMessage() + " " + ex.getClass().getName(), ex);
			abortTask(task);
			throw new ServiceException(ServiceExceptionCode.INTERNAL_ERROR, ex);
		}

	}

	/**
	 * find next waiting job on referential <br/>
	 * return null if a job is STARTED or if no job is SCHEDULED
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public JobService getNextJob() throws ServiceException {
		List<ActionTask> pendingTasks = null;
		List<ActionTask> startedTasks = null;
		try {
			pendingTasks = actionDAO.getTasks(JobService.STATUS.PENDING.name().toLowerCase());
			startedTasks = actionDAO.getTasks(JobService.STATUS.RUNNING.name().toLowerCase());
		} catch (DaoException ex) {
			log.fatal("unable to get jobs " + ex.getMessage());
			throw new ServiceException(ServiceExceptionCode.DATABASE_CORRUPTED, ex.getMessage());
		}

		Set<Long> refIds = new HashSet<>();
		for (ActionTask task : startedTasks) {
			refIds.add(task.getReferential().getId());
		}
		for (ActionTask task : pendingTasks) {
			if (refIds.contains(task.getReferential().getId()))
				continue;
			JobService js = null;
			try {
				js = new JobService(APPLICATION_NAME, rootDirectory, task);
			} catch (ServiceException e) {
				log.error("abnormal problem when getting job info " + e.getMessage(), e);
				// TODO aborting task ?
			}
			return js;
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void start(JobService jobService) {
		jobService.setStatus(JobService.STATUS.RUNNING);
		jobService.setUpdatedAt(new Date());
		jobService.setStartedAt(new Date());
		ActionTask task = actionDAO.find(jobService.getAction(), jobService.getId());
		task.setStatus(jobService.getStatus().name().toLowerCase());
		task.setUpdatedAt(new Timestamp(jobService.getUpdatedAt().getTime()));
		task.setStartedAt(new Timestamp(jobService.getStartedAt().getTime()));
		task.setCurrentStepId("INITIALISATION");
		task.setCurrentStepProgress(0.);
		actionDAO.update(task);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JobService cancel(String action, Long id) throws ServiceException {

		JobData.ACTION jobAction = null;
		try {
			jobAction = JobData.ACTION.valueOf(action);
		} catch (Exception ex) {
			throw new RequestServiceException(RequestExceptionCode.UNKNOWN_ACTION, action);
		}
		try {
			ActionTask actionTask = actionDAO.find(jobAction, id);
			JobService jobService = new JobService(APPLICATION_NAME, rootDirectory, actionTask);

			log.info("job " + jobService.getId() + " found for " + action);
			if (jobService.getStatus().equals(JobService.STATUS.PENDING)
					|| jobService.getStatus().equals(JobService.STATUS.RUNNING)) {
				jobService.setUpdatedAt(new Date());
				jobService.setStatus(JobService.STATUS.CANCELLED);
				actionTask.setUpdatedAt(new Timestamp(jobService.getUpdatedAt().getTime()));
				actionTask.setStatus(jobService.getStatus().name().toLowerCase());
				actionDAO.update(actionTask);
				// delete directory
				deleteDirectory(jobService);
			}
			return jobService;

		} catch (RequestServiceException ex) {
			log.info("fail to cancel " + action + " job " + ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			log.info("fail to cancel " + action + " job " + id + " " + ex.getMessage() + " " + ex.getClass().getName());
			throw new ServiceException(ServiceExceptionCode.INTERNAL_ERROR, ex);
		}

	}

	private void deleteDirectory(JobService jobService) {
		if (jobService.getPath().toFile().exists()) {
			try {
				FileUtils.deleteDirectory(jobService.getPath().toFile());
			} catch (IOException e) {
				log.warn("unable to delete directory " + jobService.getPath().toString());
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void terminate(JobService jobService, JobService.STATUS status) {
		jobService.setStatus(status);
		jobService.setUpdatedAt(new Date());
		jobService.setEndedAt(new Date());
		updateTask(jobService);

		// send termination to BOIV
		notifyGui(jobService);

	}

	private void notifyGui(final JobService jobService) {
		log.info("JobServiceManager notifyGIU ");

		String guiBaseUrl = System.getProperty(APPLICATION_NAME + PropertyNames.GUI_URL_BASENAME);
		String guiToken = System.getProperty(APPLICATION_NAME + PropertyNames.GUI_URL_TOKEN);

		if (guiBaseUrl != null && !guiBaseUrl.isEmpty() && guiToken != null && !guiToken.isEmpty()) {
		final String token = guiToken;
		final String urlName;
		final String method;
		final File putFile;
		switch (jobService.getAction()) {
		case importer:
			urlName = guiBaseUrl + "/api/v1/internals/netex_imports/" + jobService.getId() + "/notify_parent";
			method = "GET";
			putFile = null;
			break;
		case validator:
			urlName = guiBaseUrl + "/api/v1/internals/compliance_check_sets/" + jobService.getId() + "/notify_parent";
			method = "GET";
			putFile = null;
			break;
		case exporter:
			log.info("JobServiceManager : export output file : "+jobService.getPath().toFile() + jobService.getOutputFilename());
			putFile = new File(jobService.getPath().toFile(), "" + jobService.getOutputFilename());
			if (jobService.notFailed() && putFile.isFile()) {
				urlName = guiBaseUrl + "/api/v1/internals/netex_exports/" + jobService.getId() + "/upload";
				method = "POST";
			} else {
				urlName = guiBaseUrl + "/api/v1/internals/netex_exports/" + jobService.getId() + "/notify_parent";
				method = "GET";
			}
			break;
		default:
			log.warn("no notify URL for job type " + jobService.getAction());
			urlName = "";
			method = "";
			putFile = null;
		}

		if (!urlName.isEmpty()) {
			// send message with some delay to let transaction terminate
			Runnable r = () -> {

				try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
					Thread.sleep(1000L);
					log.info("Notify End of Task : " + method + " " + urlName);
					HttpRequestBase httpRequest = null;
					switch (method) {
					case "GET":
						httpRequest = new HttpGet(urlName);
						break;
					case "POST":
						HttpPost postRequest = new HttpPost(urlName);
						MultipartEntityBuilder builder = MultipartEntityBuilder.create();
						builder.addBinaryBody(
							    "exportFile",
							    new FileInputStream(putFile),
							    ContentType.APPLICATION_OCTET_STREAM,
							    putFile.getName()
							);
						HttpEntity multipart = builder.build();
						postRequest.setEntity(multipart);
						httpRequest = postRequest;
						break;
					}
					log.info("JobServiceManager notifyGIU : step 1) httpRequest");

					httpRequest.setHeader(HttpHeaders.AUTHORIZATION, "Token token=" + token);
					ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
						@Override
						public String handleResponse(final HttpResponse response)
								throws ClientProtocolException, IOException {
							int status = response.getStatusLine().getStatusCode();
							if (status >= 200 && status < 300) {
								HttpEntity entity = response.getEntity();
								return entity != null ? EntityUtils.toString(entity) : null;
							} else {
								throw new ClientProtocolException("Unexpected response status: " + status);
							}
						}
					};
					log.info("JobServiceManager notifyGIU : step 2) before httpclient.execute");

					String responseBody = httpclient.execute(httpRequest, responseHandler);
					
					log.info("End of task notified : response = " + responseBody);

				} catch (Exception e) {
					log.error("End of task not notified, cannot invoke url " + urlName + ", cause : " + e.getMessage());
					if (method.equals("POST")) {
						// abort task if file not sent
						jobServiceManager.abort(jobService);
					}
				} finally {
					deleteDirectory(jobService);
				}
			};
			new Thread(r).start();
		}
		} else {
			log.warn("No URL or no Token to notify end of job.");
			deleteDirectory(jobService);
		}
	}

	private void updateTask(JobService jobService) {
		if (jobService.getId() == null) {
			log.error("null id for jobService");
			throw new NullPointerException("null id for jobService");
		}
		if (actionDAO == null) {
			log.error("actionDAO is null");
			throw new NullPointerException("null actionDAO");
		}
		ActionTask actionTask = actionDAO.find(jobService.getAction(), jobService.getId());
		if (actionTask == null) {
			log.error("null " + jobService.getAction() + " task for jobService id = " + jobService.getId());
			throw new NullPointerException("null " + jobService.getAction() + " Task for jobService");
		}
		actionTask.setStatus(jobService.getStatus().name().toLowerCase());
		if (jobService.getStatus() == null) {
			log.error("null status for jobService");
			throw new NullPointerException("null status for jobService");
		}
		actionTask.setUpdatedAt(new Timestamp(jobService.getUpdatedAt().getTime()));
		if (jobService.getEndedAt() != null)
			actionTask.setEndedAt(new Timestamp(jobService.getEndedAt().getTime()));
		actionDAO.update(actionTask);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void abort(JobService jobService) {

		jobService.setStatus(JobService.STATUS.ABORTED);
		jobService.setUpdatedAt(new Date());
		updateTask(jobService);

		// send termination to BOIV
		notifyGui(jobService);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JobService> findAll(STATUS status) throws ServiceException {
		// catch exception and detect if a PersistenceException is found ;
		// if such case, return empty list and log FATAL error database
		// corrupted !

		/* debuglog */
		log.info("jobServiceManager : findAll start");
		log.info("status name : "+status.name());
		/* debuglog end */

		List<JobService> jobs = new ArrayList<>();
		List<ActionTask> actionTasks = new ArrayList<>();

		try {
			actionTasks = actionDAO.getTasks(status.name().toLowerCase());
		} catch (DaoException ex) {
			log.fatal("Cannot process jobs : " + ex.getCode() + " : " + ex.getMessage());
			log.fatal("contact DBA to correct problem");
			/* debuglog */
			log.info("problems  actionDAO");
			/* debuglog end*/
			throw new ServiceException(ServiceExceptionCode.DATABASE_CORRUPTED, ex.getMessage());
		}
		/* debuglog */
		log.info("actionTasks : "+actionTasks.size());
		for (ActionTask at : actionTasks) {
			log.info("at : "+at.toString());
		}
		/* debuglog end*/


		for (ActionTask actionTask : actionTasks) {

			try {
				JobService job = new JobService(APPLICATION_NAME, rootDirectory, actionTask);
				jobs.add(job);
			} catch (ServiceException e) {
				log.error("fail to manage task " + actionTask);
			} catch (Exception e) {
				log.error("fail to manage task " + actionTask, e);
			}
		}
		return jobs;
	}

}
