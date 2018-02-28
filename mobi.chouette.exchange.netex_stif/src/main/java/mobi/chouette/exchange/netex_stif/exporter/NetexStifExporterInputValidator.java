package mobi.chouette.exchange.netex_stif.exporter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.JSONUtil;
import mobi.chouette.core.CoreExceptionCode;
import mobi.chouette.core.CoreRuntimeException;
import mobi.chouette.exchange.AbstractInputValidator;
import mobi.chouette.exchange.InputValidator;
import mobi.chouette.exchange.InputValidatorFactory;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.model.Organisation;
import mobi.chouette.model.Referential;
import mobi.chouette.model.exporter.ExportTask;

@Log4j
public class NetexStifExporterInputValidator extends AbstractInputValidator {

	private static String[] allowedTypes = { "line", "network", "company", "group_of_line" };

	@Override
	public AbstractParameter toActionParameter(String abstractParameter) {
		try {
			return JSONUtil.fromJSON(abstractParameter, NetexStifExportParameters.class);
		} catch (Exception e) {
			log.error("Cannot parse parameter "+e.getMessage());
			return null;
		}
	}
	@Override
	public boolean checkParameters(String abstractParameterString) {

		try {
			NetexStifExportParameters parameters = JSONUtil.fromJSON(abstractParameterString, NetexStifExportParameters.class);

			return checkParameters(parameters);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return false;
		}
	}
	@Override
	public boolean checkParameters(AbstractParameter abstractParameter) {
		if (!(abstractParameter instanceof NetexStifExportParameters)) {
			log.error("invalid parameters for netex export " + abstractParameter.getClass().getName());
			return false;
		}

		NetexStifExportParameters parameters = (NetexStifExportParameters) abstractParameter;
		if (parameters.getStartDate() != null && parameters.getEndDate() != null) {
			if (parameters.getStartDate().after(parameters.getEndDate())) {
				log.error("end date before start date ");
				return false;
			}
		}

		String type = parameters.getReferencesType();
		if (type != null && !type.isEmpty()) {
			if (!Arrays.asList(allowedTypes).contains(type.toLowerCase())) {
				log.error("invalid type " + type);
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean checkFilename(String fileName) {
		if (fileName != null) {
			log.error("input data not expected");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean checkFile(String fileName, Path filePath, AbstractParameter abstractParameter) {
		if (fileName != null) {
			log.error("input data not expected");
			return false;
		}
		return true;
	}

	public static class DefaultFactory extends InputValidatorFactory {

		@Override
		protected InputValidator create() throws IOException {
			return new NetexStifExporterInputValidator();
		}
	}

	static {
		InputValidatorFactory.factories.put(NetexStifExporterInputValidator.class.getName(), new DefaultFactory());
	}

	@Override
	public AbstractParameter toActionParameter(Object task) {
		if (task instanceof ExportTask) {
			ExportTask exportTask = (ExportTask) task;
			if (!"netex_stif".equals(exportTask.getFormat()))
				return null;
			Referential referential = exportTask.getReferential();
			if (referential == null)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential id is null");
			Organisation organisation = referential.getOrganisation();
			if (organisation == null)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential organisation_id is null");
			NetexStifExportParameters parameter = new NetexStifExportParameters();
			parameter.setExportId(exportTask.getId());
			if (referential.getLineReferentialId() == null)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential line_referential_id is null");
			parameter.setLineReferentialId(referential.getLineReferentialId());
			if (referential.getStopAreaReferentialId() == null)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential stop_area_referential_id is null");
			parameter.setStopAreaReferentialId(referential.getStopAreaReferentialId());
			parameter.setReferencesType("lines");
			if (referential.getId() == null)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential id is null");
			if (referential.getMetadatas().isEmpty())
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA,"referential metadata is null");
			if (referential.getMetadatas().get(0).getLineIds() == null)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential's metadata line ids  null");
			if (referential.getMetadatas().get(0).getLineIds().length == 0)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential's metadata line ids empty");
			// TODO : fix where export tells which line is to be exported (one line or all)
			parameter.setIds(Arrays.asList(referential.getMetadatas().get(0).getLineIds()));
			parameter.setValidityPeriods(Arrays.asList(referential.getMetadatas().get(0).getPeriods()));
			parameter.setReferentialId(referential.getId());
			parameter.setReferentialName(referential.getName());
			parameter.setOrganisationName(organisation.getName());
			parameter.setOrganisationCode(organisation.getCode());

			return parameter;
		}
		return null;
	}

}
