package mobi.chouette.ws;

import java.io.File;
import java.io.InputStream;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
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

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHeaders;
import org.codehaus.jettison.json.JSONObject;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.service.JobServiceManager;

@Path("/")
@Log4j
@RequestScoped
public class RestStatus {

	private static String api_version_key = "X-BOIV_IEV-Media-Type";
	private static String api_version = "iev_status.v1.0; format=json";

	@Inject
	JobServiceManager jobServiceManager;

	@Context
	UriInfo uriInfo;

	/**
	 * heartbeat
	 * 
	 * @param format
	 * @return
	 */
	@GET
	@Path("/status{format:(\\.[^\\.]+?)?}")
	@Produces({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON })
	public Response status(@PathParam("format") String format) {
		if (format == null || format.isEmpty())
			format = ".json";
		try {
			log.info(Color.BLUE + "Call status " + Color.NORMAL);

			ResponseBuilder builder = null;
			if (format.equals(".json")) {
				JSONObject resjson = new JSONObject();
				resjson.put("status", "ok");
				builder = Response.ok(resjson.toString(2)).type(MediaType.APPLICATION_JSON_TYPE);
			} else {
				StringBuilder result = new StringBuilder();
				result.append("status=ok");
				builder = Response.ok(result).type(MediaType.TEXT_PLAIN);
			}
			builder.header(api_version_key, api_version);
			return builder.build();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new WebApplicationException("INTERNAL_ERROR", Status.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * notification loopBack for local tests
	 * 
	 * @param format
	 * @param id
	 * @param auth
	 * @return
	 */
	@GET
	@Path("/api/v1/internals/{format}/{id}/notify_parent")
	@Produces({ MediaType.TEXT_PLAIN })
	public Response apiNotifyParent(@PathParam("format") String format, @PathParam("id") String id,
			@HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
		try {
			log.info(Color.BLUE + "Call /api/v1/internals/" + format + "/" + id + "/notify_parent " + Color.NORMAL);
			log.info(Color.BLUE + "     AUTHORIZATION = " + auth + Color.NORMAL);

			ResponseBuilder builder = null;
			StringBuilder result = new StringBuilder();
			result.append("notification=ok");
			builder = Response.ok(result).type(MediaType.TEXT_PLAIN);
			builder.header(api_version_key, api_version);
			return builder.build();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new WebApplicationException("INTERNAL_ERROR", Status.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * notification loopBack for local tests
	 * 
	 * @param format
	 * @param id
	 * @param fileName
	 * @param auth
	 * @return
	 */
	@PUT
	@Path("/api/v1/internals/{format}/{id}/upload")
	@Produces({ MediaType.TEXT_PLAIN })
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	public Response apiUpload(InputStream fileInputStream, @PathParam("format") String format,
			@PathParam("id") String id, @QueryParam("file") final String fileName,
			@HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
		try {
			log.info(Color.BLUE + "Call /api/v1/internals/" + format + "/" + id + "/upload?file=" + fileName
					+ Color.NORMAL);
			log.info(Color.BLUE + "     AUTHORIZATION = " + auth + Color.NORMAL);

			File outPut = new File("/tmp", fileName);
			FileUtils.copyInputStreamToFile(fileInputStream, outPut);

			ResponseBuilder builder = null;
			StringBuilder result = new StringBuilder();
			result.append("upload=ok");
			builder = Response.ok(result).type(MediaType.TEXT_PLAIN);
			builder.header(api_version_key, api_version);
			return builder.build();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new WebApplicationException("INTERNAL_ERROR", Status.INTERNAL_SERVER_ERROR);
		}
	}

}
