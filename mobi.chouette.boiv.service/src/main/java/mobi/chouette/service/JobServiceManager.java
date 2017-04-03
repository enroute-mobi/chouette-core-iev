package mobi.chouette.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.JobData;
import mobi.chouette.common.PropertyNames;
import mobi.chouette.dao.ImportTaskDAO;
import mobi.chouette.dao.ReferentialDAO;
import mobi.chouette.model.ImportTask;
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
	ImportTaskDAO importTaskDAO;

	@EJB
	JobServiceManager jobServiceManager;

	@EJB
	Scheduler scheduler;

	private String rootDirectory;

	private static Set<String> intializedContexts = new HashSet<>();

	@PostConstruct
	public synchronized void init() {
		if (intializedContexts.contains(APPLICATION_NAME))
			return;
		System.setProperty(APPLICATION_NAME + PropertyNames.MAX_STARTED_JOBS, "5");
		System.setProperty(APPLICATION_NAME + PropertyNames.MAX_COPY_BY_JOB, "5");
		System.setProperty(APPLICATION_NAME + PropertyNames.GUI_URL_BASENAME, "");
		try {
			// set default properties
			System.setProperty(APPLICATION_NAME + PropertyNames.ROOT_DIRECTORY, System.getProperty("user.home"));

			// try to read properties
			File propertyFile = new File("/etc/chouette/" + APPLICATION_NAME + "/" + APPLICATION_NAME + ".properties");
			if (propertyFile.exists() && propertyFile.isFile()) {
				try {
					FileInputStream fileInput = new FileInputStream(propertyFile);
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
					log.error(
							"cannot read properties " + propertyFile.getAbsolutePath() + " , using default properties",
							e);
				}
			} else {
				log.info("no property file found " + propertyFile.getAbsolutePath() + " , using default properties");
			}
		} catch (Exception e) {
			log.error("cannot process properties", e);
		}
		rootDirectory = System.getProperty(APPLICATION_NAME + PropertyNames.ROOT_DIRECTORY);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public JobService create(String action, Long id) throws ServiceException {
		JobService jobService = jobServiceManager.createJob(action, id);
		scheduler.schedule();
		return jobService;
	}

	public JobService createJob(String action, Long id) throws ServiceException {

		// Instancier le modèle du service 'upload'
		if (action.equals(JobData.ACTION.importer.name())) {
			return createImportJob(id);
		} else {
			throw new RequestServiceException(RequestExceptionCode.UNKNOWN_ACTION, action);
		}

	}

	private JobService createImportJob(Long id) throws ServiceException {
		try {
			// Instancier le modèle du service 'upload'
			ImportTask importTask = importTaskDAO.find(id);
			if (importTask == null) {
				throw new RequestServiceException(RequestExceptionCode.UNKNOWN_JOB, "unknown import id " + id);
			}
			JobService jobService = new JobService(APPLICATION_NAME, rootDirectory, importTask);

			log.info("job " + jobService.getId() + " found for import ");
			// mkdir
			if (Files.exists(jobService.getPath())) {
				// réutilisation anormale d'un id de job (réinitialisation de la
				// séquence à l'extérieur de l'appli?)
				FileUtils.deleteDirectory(jobService.getPath().toFile());
			}
			Files.createDirectories(jobService.getPath());
			// copy file from Ruby server
			String urlName = importTask.getUrl();
			String guiBaseUrl = System.getProperty(APPLICATION_NAME + PropertyNames.GUI_URL_BASENAME);
			if (guiBaseUrl != null && !guiBaseUrl.isEmpty()) {
				// build url with token
				urlName = guiBaseUrl + "/workbenches/" + importTask.getWorkbenchId() + "/imports/" + importTask.getId() + "/download?token=" + urlName;
			}
			try {
				URL url = new URL(urlName);
				File dest = new File(jobService.getPathName(), jobService.getInputFilename());
				FileUtils.copyURLToFile(url, dest);
			} catch (Exception ex) {
				log.error("fail to get file for import job " + ex.getMessage());
				throw new ServiceException(ServiceExceptionCode.INTERNAL_ERROR,
						"cannot manage URL " + urlName);
			}

			jobService.setStatus(JobService.STATUS.PENDING);
			importTask.setStatus(jobService.getStatus().name().toLowerCase());
			importTaskDAO.update(importTask);
			return jobService;

		} catch (RequestServiceException ex) {
			log.info("fail to create import job " + ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			log.info("fail to create import job " + id + " " + ex.getMessage() + " " + ex.getClass().getName(), ex);
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
	public JobService getNextJob() {

		List<ImportTask> pendingImportTasks = importTaskDAO.getTasks(JobService.STATUS.PENDING.name().toLowerCase());
		List<ImportTask> startedImportTasks = importTaskDAO.getTasks(JobService.STATUS.RUNNING.name().toLowerCase());

		Set<Long> refIds = new HashSet<>();
		for (ImportTask task : startedImportTasks) {
			refIds.add(task.getReferential().getId());
		}
		for (ImportTask task : pendingImportTasks) {
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
		if (jobService.getAction().equals(JobData.ACTION.importer)) {
			ImportTask importTask = importTaskDAO.find(jobService.getId());
			importTask.setStatus(jobService.getStatus().name().toLowerCase());
			importTask.setUpdatedAt(new Timestamp(jobService.getUpdatedAt().getTime()));
			importTask.setStartedAt(new Timestamp(jobService.getStartedAt().getTime()));
			importTask.setCurrentStepId("Initialization");
			importTask.setCurrentStepProgress(0.);
			importTaskDAO.update(importTask);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JobService cancel(String action, Long id) throws ServiceException {

		// Instancier le modèle du service 'upload'
		if (action.equals(JobData.ACTION.importer.name())) {
			return cancelImport(id);
		} else {
			throw new RequestServiceException(RequestExceptionCode.UNKNOWN_ACTION, action);
		}
	}

	private JobService cancelImport(Long id) throws ServiceException {
		try {
			// Instancier le modèle du service 'upload'
			ImportTask importTask = importTaskDAO.find(id);
			JobService jobService = new JobService(APPLICATION_NAME, rootDirectory, importTask);
			// Enregistrer le jobService pour obtenir un id

			log.info("job " + jobService.getId() + " found for import ");
			if (jobService.getStatus().equals(JobService.STATUS.PENDING)
					|| jobService.getStatus().equals(JobService.STATUS.RUNNING)) {
				jobService.setUpdatedAt(new Date());
				jobService.setStatus(JobService.STATUS.CANCELLED);
				importTask.setUpdatedAt(new Timestamp(jobService.getUpdatedAt().getTime()));
				importTask.setStatus(jobService.getStatus().name().toLowerCase());
				importTaskDAO.update(importTask);
				// TODO clean directory
			}
			return jobService;

		} catch (RequestServiceException ex) {
			log.info("fail to cancel import job " + ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			log.info("fail to cancel import job " + id + " " + ex.getMessage() + " " + ex.getClass().getName());
			throw new ServiceException(ServiceExceptionCode.INTERNAL_ERROR, ex);
		}

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void terminate(JobService jobService, JobService.STATUS status) {
		jobService.setStatus(status);
		jobService.setUpdatedAt(new Date());
		jobService.setEndedAt(new Date());
		if (jobService.getAction().equals(JobData.ACTION.importer)) {
			updateImportTask(jobService);
		}
		// TODO delete directory
	}

	private void updateImportTask(JobService jobService) {
		if (jobService.getId() == null) {
			log.error("null id for jobService");
			throw new NullPointerException("null id for jobService");
		}
		if (importTaskDAO == null) {
			log.error("importTaskDAO is null");
			throw new NullPointerException("null importTaskDAO");
		}

		ImportTask importTask = importTaskDAO.find(jobService.getId());
		if (importTask == null) {
			log.error("null importTask for jobService id = " + jobService.getId());
			throw new NullPointerException("null importTask for jobService");
		}
		importTask.setStatus(jobService.getStatus().name().toLowerCase());
		if (jobService.getStatus() == null) {
			log.error("null status for jobService");
			throw new NullPointerException("null status for jobService");
		}
		importTask.setUpdatedAt(new Timestamp(jobService.getUpdatedAt().getTime()));
		if (jobService.getEndedAt() != null)
			importTask.setEndedAt(new Timestamp(jobService.getEndedAt().getTime()));
		importTaskDAO.update(importTask);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void abort(JobService jobService) {

		jobService.setStatus(JobService.STATUS.ABORTED);
		jobService.setUpdatedAt(new Date());
		if (jobService.getAction().equals(JobData.ACTION.importer)) {
			updateImportTask(jobService);
		}
		// TODO delete directory
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JobService> findAll(STATUS status) {
		List<JobService> jobs = new ArrayList<>();
		List<ImportTask> startedImportTasks = importTaskDAO.getTasks(status.name().toLowerCase());
		for (ImportTask importTask : startedImportTasks) {

			try {
				JobService job = new JobService(APPLICATION_NAME, rootDirectory, importTask);
				jobs.add(job);
			} catch (ServiceException e) {
				log.error("fail to manage import task " + importTask.getId());
			}
		}
		return jobs;
	}

}
