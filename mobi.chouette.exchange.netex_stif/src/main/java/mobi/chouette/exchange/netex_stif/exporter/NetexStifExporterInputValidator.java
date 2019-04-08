package mobi.chouette.exchange.netex_stif.exporter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import mobi.chouette.core.CoreExceptionCode;
import mobi.chouette.core.CoreRuntimeException;
import mobi.chouette.exchange.AbstractInputValidator;
import mobi.chouette.exchange.InputValidator;
import mobi.chouette.exchange.InputValidatorFactory;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.model.Organisation;
import mobi.chouette.model.Referential;
import mobi.chouette.model.exporter.ExportTask;
import mobi.chouette.model.type.DateRange;

public class NetexStifExporterInputValidator extends AbstractInputValidator {

	public static final String LINE = "line";
	public static final String FULL = "full";
	public static final String DURATION = "duration";
	public static final String LINE_CODE = "line_code";
	public static final String EXPORT_TYPE = "export_type";

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
			int duration = 0;
			
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
			if (!exportTask.getOptions().containsKey(EXPORT_TYPE))
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "export_type option missing");
			if (!exportTask.getOptions().containsKey(DURATION)) {
				if (exportTask.getOptions().get(EXPORT_TYPE).equals(FULL)) {
					throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "duration option missing");
				}
			} else {
				 duration = Integer.parseInt(exportTask.getOptions().get(DURATION)) - 1;
			}

			Calendar c = Calendar.getInstance();
			parameter.setStartDate(c.getTime());
			c.add(Calendar.DATE, duration);
			parameter.setEndDate(c.getTime());
			DateRange limits = new DateRange(new java.sql.Date(parameter.getStartDate().getTime()),
					new java.sql.Date(parameter.getEndDate().getTime()));

			parameter.setMode(exportTask.getOptions().get(EXPORT_TYPE));
			if (exportTask.getOptions().get(EXPORT_TYPE).equals(LINE)) {
				// manage one line export
				if (!exportTask.getOptions().containsKey(LINE_CODE))
					throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "line_code option missing");
				Long lineId = Long.valueOf(exportTask.getOptions().get(LINE_CODE));
				parameter.getIds().add(lineId);
				referential.getMetadatas().forEach(m -> {
					Set<Long> ids = new HashSet<>(Arrays.asList(m.getLineIds()));
					if (ids.contains(lineId) && m.getPeriods() != null && m.getPeriods().length > 0) {
						for (DateRange range : m.getPeriods()) {
							if (range.intersects(limits))
								parameter.getValidityPeriods().add(range.clone());
						}
					}
				});

			} else if (exportTask.getOptions().get(EXPORT_TYPE).equals(FULL)) {
				// manage full export : add all line ids
				parameter.getValidityPeriods().add(limits);
				referential.getMetadatas().forEach(m -> {
					if (m.getLineIds() != null && m.getLineIds().length > 0)
						parameter.getIds().addAll(Arrays.asList(m.getLineIds()));
				});

			} else {
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA,
						"export_type : unknown option" + exportTask.getOptions().get(EXPORT_TYPE));
			}
			parameter.setReferentialId(referential.getId());
			parameter.setReferentialName(referential.getName());
			parameter.setOrganisationName(organisation.getName());
			parameter.setOrganisationCode(organisation.getCode());
			parameter.setReferencesType(LINE);
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
