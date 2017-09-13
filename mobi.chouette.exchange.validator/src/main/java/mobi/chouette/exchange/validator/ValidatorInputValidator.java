package mobi.chouette.exchange.validator;

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
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.exchange.validation.parameters.ValidationParameters;
import mobi.chouette.model.Organisation;
import mobi.chouette.model.Referential;
import mobi.chouette.model.compliance.ComplianceCheckTask;

@Log4j
public class ValidatorInputValidator extends AbstractInputValidator {

	private static String[] allowedTypes = { "line", "network", "company", "group_of_line" };

	@Override
	public AbstractParameter toActionParameter(String abstractParameter) {
		try {
			return JSONUtil.fromJSON(abstractParameter, ValidateParameters.class);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean checkParameters(String abstractParameterString, String validationParametersString) {

		try {
			ValidateParameters parameters = JSONUtil.fromJSON(abstractParameterString, ValidateParameters.class);

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
		if (!(abstractParameter instanceof ValidateParameters)) {
			log.error("invalid parameters for validator " + abstractParameter.getClass().getName());
			return false;
		}

		ValidateParameters parameters = (ValidateParameters) abstractParameter;

		String type = parameters.getReferencesType();
		if (type != null && !type.isEmpty()) {
			if (!Arrays.asList(allowedTypes).contains(type.toLowerCase())) {
				log.error("invalid type " + type);
				return false;
			}
		}

		if (validationParameters == null) {
			log.error("validation parameters expected");
			return false;
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
	public boolean checkFile(String fileName, Path pathFile, AbstractParameter abstractParameter) {
		if (fileName != null) {
			log.error("input data not expected");
			return false;
		}
		return true;
	}

	public static class DefaultFactory extends InputValidatorFactory {

		@Override
		protected InputValidator create() throws IOException {
			InputValidator result = new ValidatorInputValidator();
			return result;
		}
	}

	static {
		InputValidatorFactory.factories.put(ValidatorInputValidator.class.getName(), new DefaultFactory());
	}

	 @Override
	 public List<TestDescription> getTestList() {
	 List<TestDescription> lstResults = new ArrayList<TestDescription>();
	
	 //lstResults.addAll(AbstractValidation.getTestLevel3DatabaseList());
	
	 return lstResults;
	 }

	@Override
	public AbstractParameter toActionParameter(Object task) {
		// TODO : convertir task en ComplianceeCheckTask
		// puis s'en servir pour créer et habiller un ValidateParameter
		// le ValidateParameter est à retourner par cette fonction
		// voir NetexStifImporterInputValidator comme exemple au moins sur les éléménts communs (AbstactParameter et
		// ActionTask)

		if (task instanceof ComplianceCheckTask) {
			ComplianceCheckTask checkTask = (ComplianceCheckTask) task;
			Referential referential = checkTask.getReferential();
			Organisation organisation = referential.getOrganisation();
			ValidateParameters parameter = new ValidateParameters();

			// TODO : Didier ===> développement en cours !...Non fonctionnnel !!!!!!!!!!!!!!!!
			parameter.setLineReferentialId(referential.getLineReferentialId());
			parameter.setStopAreaReferentialId(referential.getStopAreaReferentialId());
			parameter.setReferencesType("lines");
			if (referential.getId() == null)
				throw new RuntimeException("referential id is null");
			if (referential.getMetadatas().isEmpty())
				throw new RuntimeException("referential id " + referential.getId() + " metadata is null");
			if (referential.getMetadatas().get(0).getLineIds() == null)
				throw new RuntimeException("referential's metadata line ids  null");
			parameter.setIds(Arrays.asList(referential.getMetadatas().get(0).getLineIds()));
			parameter.setReferentialId(referential.getId());
			parameter.setReferentialName(referential.getName());
			parameter.setOrganisationName(organisation.getName());
			parameter.setOrganisationCode(organisation.getCode());

			return parameter;
		}

		return null;
	}

}
