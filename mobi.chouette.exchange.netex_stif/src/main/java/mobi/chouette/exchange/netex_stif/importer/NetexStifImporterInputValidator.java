package mobi.chouette.exchange.netex_stif.importer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.JSONUtil;
import mobi.chouette.exchange.AbstractInputValidator;
import mobi.chouette.exchange.InputValidator;
import mobi.chouette.exchange.InputValidatorFactory;
import mobi.chouette.exchange.TestDescription;
import mobi.chouette.exchange.importer.updater.DatabaseTestUtils;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.exchange.validation.parameters.ValidationParameters;
import mobi.chouette.model.ImportTask;
import mobi.chouette.model.Referential;

@Log4j
public class NetexStifImporterInputValidator extends AbstractInputValidator {

	@Override
	public AbstractParameter toActionParameter(String abstractParameter) {
		try {
			return JSONUtil.fromJSON(abstractParameter, NetexStifImportParameters.class);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean checkParameters(String abstractParameterString, String validationParametersString) {

		try {
			NetexStifImportParameters parameters = JSONUtil.fromJSON(abstractParameterString,
					NetexStifImportParameters.class);

			ValidationParameters validationParameters = JSONUtil.fromJSON(validationParametersString,
					ValidationParameters.class);

			return checkParameters(parameters, validationParameters);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return false;
		}
	}

	@Override
	public boolean checkParameters(AbstractParameter abstractParameter, ValidationParameters validationParameters) {
		if (!(abstractParameter instanceof NetexStifImportParameters)) {
			log.error("invalid parameters for Netex import " + abstractParameter.getClass().getName());
			return false;
		}
		return true;
	}

	@Override
	public boolean checkFilename(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			log.error("input data expected");
			return false;
		}

		if (!fileName.endsWith(".zip") && !fileName.endsWith(".xml")) {
			log.error("xml or Zip archive input data expected");
			return false;
		}

		return true;
	}

	@Override
	public boolean checkFile(String fileName, Path filePath, AbstractParameter abstractParameter) {
		return checkFileExistenceInZip(fileName, filePath, "xml");
	}

	public static class DefaultFactory extends InputValidatorFactory {

		@Override
		protected InputValidator create() throws IOException {
			InputValidator result = new NetexStifImporterInputValidator();
			return result;
		}
	}

	static {
		InputValidatorFactory.factories.put(NetexStifImporterInputValidator.class.getName(), new DefaultFactory());
	}

	@Override
	public List<TestDescription> getTestList() {
		DatabaseTestUtils dbTestUtils = DatabaseTestUtils.getInstance();
		List<TestDescription> lstTestWithDatabase = new ArrayList<TestDescription>();
		lstTestWithDatabase.addAll(dbTestUtils.getTestUtilsList());
		//lstTestWithDatabase.addAll(AbstractValidation.getTestLevel3FileList());

		return lstTestWithDatabase;
	}

	@Override
	public AbstractParameter toActionParameter( Object task) {
		if (task instanceof ImportTask) {
			ImportTask importTask = (ImportTask) task;
			if (!importTask.getFormat().equals("netex_stif"))
				return null;
			Referential referential = importTask.getReferential();
			NetexStifImportParameters parameter = new NetexStifImportParameters();
			parameter.setImportId(importTask.getId());
			parameter.setLineReferentialId(referential.getLineReferentialId());
			parameter.setStopAreaReferentialId(referential.getStopAreaReferentialId());
			parameter.setReferencesType("lines");
			if (referential.getId() == null) throw new RuntimeException("referential id is null");
			if (referential.getMetadatas().isEmpty()) throw new RuntimeException("referential id "+referential.getId() +" metadata is null");
			if (referential.getMetadatas().get(0).getLineIds() == null) throw new RuntimeException("referential's metadata line ids  null");
			parameter.setIds(Arrays.asList(referential.getMetadatas().get(0).getLineIds()));
			parameter.setReferentialId(referential.getId());
			parameter.setReferentialName(referential.getName());
			return parameter;
		}
		return null;
	}

}
