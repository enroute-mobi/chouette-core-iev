package mobi.chouette.exchange.netex_stif.importer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import mobi.chouette.core.CoreExceptionCode;
import mobi.chouette.core.CoreRuntimeException;
import mobi.chouette.exchange.AbstractInputValidator;
import mobi.chouette.exchange.InputValidator;
import mobi.chouette.exchange.InputValidatorFactory;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.model.Organisation;
import mobi.chouette.model.Referential;
import mobi.chouette.model.importer.ImportTask;

public class NetexStifImporterInputValidator extends AbstractInputValidator {

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
		if (task instanceof ImportTask) {
			ImportTask importTask = (ImportTask) task;
			if (!"netex_stif".equals(importTask.getFormat()))
				return null;
			Referential referential = importTask.getReferential();
			if (referential == null)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential id is null");
			Organisation organisation = referential.getOrganisation();
			if (organisation == null)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential organisation_id is null");
			NetexStifImportParameters parameter = new NetexStifImportParameters();
			parameter.setImportId(importTask.getId());
			if (referential.getLineReferentialId() == null)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA,
						"referential line_referential_id is null");
			parameter.setLineReferentialId(referential.getLineReferentialId());
			if (referential.getStopAreaReferentialId() == null)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA,
						"referential stop_area_referential_id is null");
			parameter.setStopAreaReferentialId(referential.getStopAreaReferentialId());
			parameter.setReferencesType("lines");
			if (referential.getId() == null)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential id is null");
			if (referential.getMetadatas().isEmpty())

			throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA,"referential metadata is null");
         // loop to collect all lines (#6136)
         referential.getMetadatas().forEach(m -> {
            if (m.getLineIds() != null) {
               if (m.getLineIds().length > 0) {
                  parameter.getIds().addAll(Arrays.asList(m.getLineIds()));
               }
            }
         });
         if (parameter.getIds().isEmpty())
            throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential's metadata line ids empty");
			parameter.setReferentialId(referential.getId());
			parameter.setReferentialName(referential.getName());
			parameter.setOrganisationName(organisation.getName());
			parameter.setOrganisationCode(organisation.getCode());

			// for debug only
			parameter
					.setCleanRepository("true".equalsIgnoreCase(System.getProperty("boiv.clean.repository.on.import")));

			return parameter;
		}
		return null;
	}

	public static class DefaultFactory extends InputValidatorFactory {

		@Override
		protected InputValidator create() throws IOException {
			return new NetexStifImporterInputValidator();
		}
	}

	static {
		InputValidatorFactory.factories.put(NetexStifImporterInputValidator.class.getName(), new DefaultFactory());
	}

}
