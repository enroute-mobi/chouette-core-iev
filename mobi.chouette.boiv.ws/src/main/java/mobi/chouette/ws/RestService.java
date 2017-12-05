package mobi.chouette.ws;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.service.JobServiceManager;
import mobi.chouette.service.RequestExceptionCode;
import mobi.chouette.service.RequestServiceException;
import mobi.chouette.service.ServiceException;
import mobi.chouette.service.ServiceExceptionCode;

@Path("/referentials")
@Log4j
@RequestScoped
public class RestService {

	// voir swagger

	private static final String MESSAGE = ", Message = ";
	private static String api_version_key = "X-BOIV_IEV-Media-Type";
	private static String api_version = "iev.v1.0; format=json";

	@Inject
	JobServiceManager jobServiceManager;

	@Context
	UriInfo uriInfo;

	private WebApplicationException toWebApplicationException(ServiceException exception) {
		return new WebApplicationException(exception.getMessage(), toWebApplicationCode(exception.getExceptionCode()));
	}

	private Status toWebApplicationCode(ServiceExceptionCode errorCode) {
		switch (errorCode) {
		case INVALID_REQUEST:
			return Status.BAD_REQUEST;
		case INTERNAL_ERROR:
			return Status.INTERNAL_SERVER_ERROR;
		default:
			return Status.INTERNAL_SERVER_ERROR;
		}
	}

	private WebApplicationException toWebApplicationException(RequestServiceException exception) {
		return new WebApplicationException(exception.getRequestCode(),
				toWebApplicationCode(exception.getRequestExceptionCode()));
	}

	private Status toWebApplicationCode(RequestExceptionCode errorCode) {
		switch (errorCode) {
		case UNKNOWN_ACTION:
		case DUPPLICATE_OR_MISSING_DATA:
		case DUPPLICATE_PARAMETERS:
		case MISSING_PARAMETERS:
		case UNREADABLE_PARAMETERS:
		case INVALID_PARAMETERS:
		case INVALID_FILE_FORMAT:
		case INVALID_FORMAT:
		case ACTION_TYPE_MISMATCH:
			return Status.BAD_REQUEST;
		case UNKNOWN_REFERENTIAL:
		case UNKNOWN_FILE:
		case UNKNOWN_JOB:
			return Status.NOT_FOUND;
		case SCHEDULED_JOB:
			return Status.METHOD_NOT_ALLOWED;
		case REFERENTIAL_BUSY:
			return Status.CONFLICT;
		case TOO_MANY_ACTIVE_JOBS:
			return Status.SERVICE_UNAVAILABLE;
		}
		return Status.BAD_REQUEST;
	}

	// jobs scan
	@GET
	@Path("/{action}/new")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response newJob(@PathParam("action") String action, @QueryParam("id") final Long id) {
		try {
			log.info(Color.CYAN + "Call new on action = " + action + Color.NORMAL);

			// create response
			Response result = null;

			jobServiceManager.create(action, id);

			// no cache control
			ResponseBuilder builder = Response.ok();
			builder.header(api_version_key, api_version);
			result = builder.build();
			return result;
		} catch (RequestServiceException ex) {
			log.info("RequestCode = " + ex.getRequestCode() + MESSAGE + ex.getMessage());
			throw toWebApplicationException(ex);
		} catch (ServiceException e) {
			log.error("Code = " + e.getCode() + MESSAGE + e.getMessage());
			throw toWebApplicationException(e);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new WebApplicationException("INTERNAL_ERROR", Status.INTERNAL_SERVER_ERROR);
		}
	}

	// cancel job
	@DELETE
	@Path("/{action}/{id}")
	public Response cancel(@PathParam("action") String action, @PathParam("id") Long id, String dummy) {
		try {
			// dummy uses when sender call url with content (prevent a
			// NullPointerException)
			log.info(Color.CYAN + "Call cancel action = " + action + ", id = " + id + Color.NORMAL);

			Response result = null;

			jobServiceManager.cancel(action, id);

			ResponseBuilder builder = Response.ok();
			result = builder.build();
			builder.header(api_version_key, api_version);

			return result;
		} catch (RequestServiceException ex) {
			log.info("RequestCode = " + ex.getRequestCode() + MESSAGE + ex.getMessage());
			throw toWebApplicationException(ex);
		} catch (ServiceException ex) {
			log.error("Code = " + ex.getCode() + MESSAGE + ex.getMessage());
			throw toWebApplicationException(ex);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new WebApplicationException("INTERNAL_ERROR", Status.INTERNAL_SERVER_ERROR);
		}
	}

}
