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
import mobi.chouette.dao.ActionDAO;
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

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public JobService createJob(String action, Long id) throws ServiceException {

		// Instancier le modèle du service 'upload'
		if (action.equals(JobData.ACTION.importer.name())) {
			return createImportJob(id);
		} else if (action.equals(JobData.ACTION.validator.name())) {
			return createValidatorJob(id);
		} else {
			throw new RequestServiceException(RequestExceptionCode.UNKNOWN_ACTION, action);
		}

	}

	private JobService createImportJob(Long id) throws ServiceException {
		ImportTask importTask = null;
		try {
			// Instancier le modèle du service 'upload'
			importTask = (ImportTask) actionDAO.find(JobData.ACTION.importer, id);
			if (importTask == null) {
				throw new RequestServiceException(RequestExceptionCode.UNKNOWN_JOB, "unknown import id " + id);
			}
			if (!importTask.getStatus().equals(JobService.STATUS.NEW.name().toLowerCase())) {
				throw new RequestServiceException(RequestExceptionCode.SCHEDULED_JOB, "already managed job " + id);
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
				urlName = guiBaseUrl + "/workbenches/" + importTask.getWorkbenchId() + "/imports/" + importTask.getId()
						+ "/download?token=" + urlName;
			}
			try {
				URL url = new URL(urlName);
				File dest = new File(jobService.getPathName(), jobService.getInputFilename());
				FileUtils.copyURLToFile(url, dest);
			} catch (Exception ex) {
				log.error("fail to get file for import job " + ex.getMessage());
				throw new ServiceException(ServiceExceptionCode.INTERNAL_ERROR, "cannot manage URL " + urlName);
			}

			jobService.setStatus(JobService.STATUS.PENDING);
			importTask.setStatus(jobService.getStatus().name().toLowerCase());
			actionDAO.update(importTask);
			return jobService;

		} catch (RequestServiceException ex) {
			log.info("fail to create import job " + ex.getMessage());
			if (importTask != null) {
				try {
					importTask.setStatus(JobService.STATUS.ABORTED.name().toLowerCase());
					actionDAO.update(importTask);
				} catch (Exception e) {
					log.error(
							"cannot set bad importtask status or task " + importTask.getId() + " : " + ex.getMessage());
				}
			}
			throw ex;
		} catch (ServiceException ex) {
			log.info("invalid import job data" + ex.getMessage());
			if (importTask != null) {
				try {
					importTask.setStatus(JobService.STATUS.ABORTED.name().toLowerCase());
					actionDAO.update(importTask);
				} catch (Exception e) {
					log.error(
							"cannot set bad importtask status or task " + importTask.getId() + " : " + ex.getMessage());
				}
			}
			throw ex;
		} catch (Exception ex) {
			log.info("fail to create import job " + id + " " + ex.getMessage() + " " + ex.getClass().getName(), ex);
			if (importTask != null) {
				try {
					importTask.setStatus(JobService.STATUS.ABORTED.name().toLowerCase());
					actionDAO.update(importTask);
				} catch (Exception e) {
					log.error(
							"cannot set bad importtask status or task " + importTask.getId() + " : " + ex.getMessage());
				}
			}
			throw new ServiceException(ServiceExceptionCode.INTERNAL_ERROR, ex);
		}

	}

	private JobService createValidatorJob(Long id) throws ServiceException {
		ActionTask task = null;
		try {
			// Instancier le modèle du service 'upload'
			task = actionDAO.find(JobData.ACTION.validator, id);
			if (task == null) {
				throw new RequestServiceException(RequestExceptionCode.UNKNOWN_JOB, "unknown validator id " + id);
			}
			if (!task.getStatus().equals(JobService.STATUS.NEW.name().toLowerCase())) {
				throw new RequestServiceException(RequestExceptionCode.SCHEDULED_JOB, "already managed job " + id);
			}
			JobService jobService = new JobService(APPLICATION_NAME, rootDirectory, task);

			log.info("job " + jobService.getId() + " found for validator ");
			// mkdir
			if (Files.exists(jobService.getPath())) {
				// réutilisation anormale d'un id de job (réinitialisation de la
				// séquence à l'extérieur de l'appli?)
				FileUtils.deleteDirectory(jobService.getPath().toFile());
			}
			Files.createDirectories(jobService.getPath());

			jobService.setStatus(JobService.STATUS.PENDING);
			task.setStatus(jobService.getStatus().name().toLowerCase());
			actionDAO.update(task);
			return jobService;

		} catch (RequestServiceException ex) {
			log.info("fail to create validator job " + ex.getMessage());
			if (task != null) {
				try {
					task.setStatus(JobService.STATUS.ABORTED.name().toLowerCase());
					actionDAO.update(task);
				} catch (Exception e) {
					log.error("cannot set bad validator task status or task " + task.getId() + " : " + ex.getMessage());
				}
			}
			throw ex;
		} catch (ServiceException ex) {
			log.info("invalid validator job data" + ex.getMessage());
			if (task != null) {
				try {
					task.setStatus(JobService.STATUS.ABORTED.name().toLowerCase());
					actionDAO.update(task);
				} catch (Exception e) {
					log.error("cannot set bad validator task status or task " + task.getId() + " : " + ex.getMessage());
				}
			}
			throw ex;
		} catch (Exception ex) {
			log.info("fail to create validator job " + id + " " + ex.getMessage() + " " + ex.getClass().getName(), ex);
			if (task != null) {
				try {
					task.setStatus(JobService.STATUS.ABORTED.name().toLowerCase());
					actionDAO.update(task);
				} catch (Exception e) {
					log.error("cannot set bad validator task status or task " + task.getId() + " : " + ex.getMessage());
				}
			}
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

		List<ActionTask> pendingTasks = actionDAO.getTasks(JobService.STATUS.PENDING.name().toLowerCase());
		List<ActionTask> startedTasks = actionDAO.getTasks(JobService.STATUS.RUNNING.name().toLowerCase());

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
		task.setCurrentStepId("Initialization");
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
			// Enregistrer le jobService pour obtenir un id

			log.info("job " + jobService.getId() + " found for " + action);
			if (jobService.getStatus().equals(JobService.STATUS.PENDING)
					|| jobService.getStatus().equals(JobService.STATUS.RUNNING)) {
				jobService.setUpdatedAt(new Date());
				jobService.setStatus(JobService.STATUS.CANCELLED);
				actionTask.setUpdatedAt(new Timestamp(jobService.getUpdatedAt().getTime()));
				actionTask.setStatus(jobService.getStatus().name().toLowerCase());
				actionDAO.update(actionTask);
				// TODO clean directory
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

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void terminate(JobService jobService, JobService.STATUS status) {
		jobService.setStatus(status);
		jobService.setUpdatedAt(new Date());
		jobService.setEndedAt(new Date());
		updateTask(jobService);

		// TODO delete directory

		// TODO send termination to BOIV

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

		// TODO delete directory

		// TODO send termination to BOIV
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JobService> findAll(STATUS status) {
		List<JobService> jobs = new ArrayList<>();
		List<ActionTask> actionTasks = actionDAO.getTasks(status.name().toLowerCase());
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
