package mobi.chouette.exchange.validator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.JSONUtil;
import mobi.chouette.exchange.AbstractInputValidator;
import mobi.chouette.exchange.InputValidator;
import mobi.chouette.exchange.InputValidatorFactory;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.exchange.validation.parameters.ValidationParameters;
import mobi.chouette.exchange.validator.checkpoints.CheckpointParameters;
import mobi.chouette.exchange.validator.checkpoints.ControlParameters;
import mobi.chouette.exchange.validator.checkpoints.GenericCheckpointParameters;
import mobi.chouette.model.Organisation;
import mobi.chouette.model.Referential;
import mobi.chouette.model.compliance.ComplianceCheck;
import mobi.chouette.model.compliance.ComplianceCheck.CRITICITY;
import mobi.chouette.model.compliance.ComplianceCheckBlock;
import mobi.chouette.model.compliance.ComplianceCheckTask;

@Log4j
public class ValidatorInputValidator extends AbstractInputValidator {

	protected static final String TRANSPORT_MODE_KEY = "transport_mode";
	protected static final String TRANSPORT_SUBMODE_KEY = "transport_sub_mode";
	protected static final String MAXIMUM_VALUE_KEY = "maximum";
	protected static final String MINIMUM_VALUE_KEY = "minimum";
	protected static final String PATTERN_VALUE_KEY = "PATTERN";
	protected static final String ATTRIBUTE_NAME_KEY = "target";
	protected static String[] allowedTypes = { "line", "network", "company", "group_of_line" };

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
	public AbstractParameter toActionParameter(Object task) {
		// TODO : convertir task en ComplianceCheckTask
		// puis s'en servir pour créer et habiller un ValidateParameter
		// le ValidateParameter est à retourner par cette fonction
		// voir NetexStifImporterInputValidator comme exemple au moins sur les
		// éléménts communs (AbstactParameter et
		// ActionTask)

		if (task instanceof ComplianceCheckTask) {
			ComplianceCheckTask checkTask = (ComplianceCheckTask) task;
			Referential referential = checkTask.getReferential();
			Organisation organisation = referential.getOrganisation();
			ValidateParameters parameter = new ValidateParameters();

			// TODO : Didier ===> développement en cours !...Non fonctionnnel
			// !!!!!!!!!!!!!!!!
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

			// populate controlParameter
			ControlParameters controlParameters = parameter.getControlParameters();
			checkTask.getComplianceChecks().stream().forEach(check -> {
				CheckpointParameters cp = buildCheckpoint(check);
				// if check has a block, add check to transportModeCheckpoints
				// map
				if (check.getBlock() != null) {
					// get key for mode/submode
					ComplianceCheckBlock block = check.getBlock();
					String key = block.getConditionAttributes().get(TRANSPORT_MODE_KEY);
					if (block.getConditionAttributes().containsKey(TRANSPORT_SUBMODE_KEY)) {
						key += "/" + block.getConditionAttributes().get(TRANSPORT_SUBMODE_KEY);
					}
					Map<String, Collection<CheckpointParameters>> map = controlParameters.getTransportModeCheckpoints()
							.get(key);
					if (map == null) {
						map = new HashMap<>();
						controlParameters.getTransportModeCheckpoints().put(key, map);
					}
					Collection<CheckpointParameters> list = map.get(cp.getCode());
					if (list == null) {
						list = new ArrayList<>();
						map.put(cp.getCode(), list);
					}
					list.add(cp);
				}
				// else add to globalCheckPoints map
				else {
					Collection<CheckpointParameters> list = controlParameters.getGlobalCheckPoints().get(cp.getCode());
					if (list == null) {
						list = new ArrayList<>();
						controlParameters.getGlobalCheckPoints().put(cp.getCode(), list);
					}
					list.add(cp);
				}
			});

			return parameter;
		}

		return null;
	}

	private CheckpointParameters buildCheckpoint(ComplianceCheck check) {
		CheckpointParameters result = null;
		if (check.getControlAttributes().containsKey(ATTRIBUTE_NAME_KEY)) {
			GenericCheckpointParameters generic = new GenericCheckpointParameters();
			result = generic;
			String key = check.getControlAttributes().get(ATTRIBUTE_NAME_KEY);
			String[] keys = key.split("#");
			generic.setAttributeName(keys[1]);
			// TODO : manage a map between types and classes
			generic.setClassName(toCamelCase(keys[0]));
		} else {
			result = new CheckpointParameters();
		}
		result.setMinimumValue(check.getControlAttributes().get(MINIMUM_VALUE_KEY));
		result.setMaximumValue(check.getControlAttributes().get(MAXIMUM_VALUE_KEY));
		result.setPatternValue(check.getControlAttributes().get(PATTERN_VALUE_KEY));
		result.setCode(check.getCode());
		result.setErrorType(check.getCriticity().equals(CRITICITY.ERROR));
		return result;
	}

	/**
	 * @param underscore
	 * @return
	 */
	protected static String toCamelCase(String underscore) {
		StringBuffer b = new StringBuffer();
		boolean underChar = false;
		boolean first = true;
		for (char c : underscore.toCharArray()) {
			if (first) {
				b.append(Character.toUpperCase(c));
				first = false;
				continue;
			}
			if (c == '_') {
				underChar = true;
				continue;
			}
			if (underChar) {
				b.append(Character.toUpperCase(c));
				underChar = false;
			} else
				b.append(c);
		}
		return b.toString();
	}

}
