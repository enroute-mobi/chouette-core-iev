package mobi.chouette.exchange.netex_stif.exporter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Calendar;

import mobi.chouette.core.CoreExceptionCode;
import mobi.chouette.core.CoreRuntimeException;
import mobi.chouette.exchange.AbstractInputValidator;
import mobi.chouette.exchange.InputValidator;
import mobi.chouette.exchange.InputValidatorFactory;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.model.Organisation;
import mobi.chouette.model.Referential;
import mobi.chouette.model.exporter.ExportTask;

public class NetexStifExporterInputValidator extends AbstractInputValidator {

	@Override
	public AbstractParameter toActionParameter(String abstractParameter) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public boolean checkParameters(String abstractParameterString) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public boolean checkParameters(AbstractParameter abstractParameter) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public boolean checkFilename(String fileName) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public boolean checkFile(String fileName, Path filePath, AbstractParameter abstractParameter) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
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
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA,
						"referential line_referential_id is null");
			parameter.setLineReferentialId(referential.getLineReferentialId());
			if (referential.getStopAreaReferentialId() == null)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA,
						"referential stop_area_referential_id is null");
			parameter.setStopAreaReferentialId(referential.getStopAreaReferentialId());
			if (referential.getId() == null)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential id is null");
			if (referential.getMetadatas().isEmpty())
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential metadata is null");
			if (referential.getMetadatas().get(0).getLineIds() == null)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential's metadata line ids  null");
			if (referential.getMetadatas().get(0).getLineIds().length == 0)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential's metadata line ids empty");
			if (referential.getMetadatas().get(0).getPeriods().length == 0)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA,
						"referential's metadata periods ids empty");
			if (!exportTask.getOptions().containsKey("export_type"))
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "export_type option missing");
			if (!exportTask.getOptions().containsKey("duration"))
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "duration option missing");

			parameter.setMode(exportTask.getOptions().get("export_type"));
			if (exportTask.getOptions().get("export_type").equals("line")) {
				// manage one line export
				if (!exportTask.getOptions().containsKey("line_id"))
					throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "line_id option missing");
				parameter.getIds().add(Long.valueOf(exportTask.getOptions().get("line_id")));

			} else if (exportTask.getOptions().get("export_type").equals("full")) {
				// manage full export : add all line ids
				referential.getMetadatas().forEach(m -> {
					if (m.getLineIds() != null && m.getLineIds().length > 0)
						parameter.getIds().addAll(Arrays.asList(m.getLineIds()));
				});

			} else {
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA,
						"export_type : unknown option" + exportTask.getOptions().get("export_type"));
			}
			referential.getMetadatas().forEach(m -> {
				if (m.getPeriods() != null && m.getPeriods().length > 0)
					parameter.getValidityPeriods().addAll(Arrays.asList(m.getPeriods()));
			});
			parameter.setReferentialId(referential.getId());
			parameter.setReferentialName(referential.getName());
			parameter.setOrganisationName(organisation.getName());
			parameter.setOrganisationCode(organisation.getCode());
			parameter.setReferencesType("line");
			int duration = Integer.parseInt(exportTask.getOptions().get("duration")) - 1;
			Calendar c = Calendar.getInstance();
			parameter.setStartDate(c.getTime());
			c.add(Calendar.DATE, duration);
			parameter.setEndDate(c.getTime());
			return parameter;
		}
		return null;
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

}
