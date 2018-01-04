package mobi.chouette.exchange.validator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.JSONUtil;
import mobi.chouette.core.CoreExceptionCode;
import mobi.chouette.core.CoreRuntimeException;
import mobi.chouette.exchange.AbstractInputValidator;
import mobi.chouette.exchange.InputValidator;
import mobi.chouette.exchange.InputValidatorFactory;
import mobi.chouette.exchange.parameters.AbstractParameter;
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
	protected static final String PATTERN_VALUE_KEY = "pattern";
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
	public boolean checkParameters(String abstractParameterString) {

		try {
			ValidateParameters parameters = JSONUtil.fromJSON(abstractParameterString, ValidateParameters.class);

			return checkParameters(parameters);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return false;
		}
	}

	@Override
	public boolean checkParameters(AbstractParameter abstractParameter) {
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

	@Override
	public AbstractParameter toActionParameter(Object task) {

		if (task instanceof ComplianceCheckTask) {
			ComplianceCheckTask checkTask = (ComplianceCheckTask) task;
			Referential referential = checkTask.getReferential();
			Organisation organisation = referential.getOrganisation();
			ValidateParameters parameter = new ValidateParameters();

			parameter.setLineReferentialId(referential.getLineReferentialId());
			parameter.setStopAreaReferentialId(referential.getStopAreaReferentialId());
			parameter.setReferencesType("lines");
			if (referential.getId() == null)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential id is null");
			if (referential.getMetadatas().isEmpty())
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA,
						"referential id " + referential.getId() + " metadata is null");
			if (referential.getMetadatas().get(0).getLineIds() == null)
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential's metadata line ids  null");
			parameter.setIds(Arrays.asList(referential.getMetadatas().get(0).getLineIds()));
			parameter.setReferentialId(referential.getId());
			parameter.setReferentialName(referential.getName());
			parameter.setOrganisationName(organisation.getName());
			parameter.setOrganisationCode(organisation.getCode());

			// populate controlParameter
			ControlParameters controlParameters = parameter.getControlParameters();
			checkTask.getComplianceChecks().stream().forEach(check -> {
				CheckpointParameters cp;
				try {
					cp = buildCheckpoint(check);
				} catch (Exception e) {
					throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, e, "cannot parse ComplianceCheck Attributes for "+check.getCode());
				}
				ComplianceCheckBlock block = check.getBlock();
				if (block != null) {
					// if check has a block, add check to
					// transportModeCheckpoints map
					addToTransportModeCheckPoints(controlParameters, block, cp);
				} else {
					// else add to globalCheckPoints map
					addToGlobalCheckPoints(controlParameters, cp);
				}
			});

			return parameter;
		}

		return null;
	}

	private void addToGlobalCheckPoints(ControlParameters controlParameters, CheckpointParameters cp) {
		Collection<CheckpointParameters> list = controlParameters.getGlobalCheckPoints().get(cp.getCode());
		if (list == null) {
			list = new ArrayList<>();
			controlParameters.getGlobalCheckPoints().put(cp.getCode(), list);
		}
		list.add(cp);
	}

	private void addToTransportModeCheckPoints(ControlParameters controlParameters, ComplianceCheckBlock block,
			CheckpointParameters cp) {
		// get key for mode/submode
		String key = block.getConditionAttributes().get(TRANSPORT_MODE_KEY);
		if (block.getConditionAttributes().containsKey(TRANSPORT_SUBMODE_KEY)) {
			key += "/" + block.getConditionAttributes().get(TRANSPORT_SUBMODE_KEY);
		}
		Map<String, Collection<CheckpointParameters>> map = controlParameters.getTransportModeCheckpoints().get(key);
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

	private CheckpointParameters buildCheckpoint(ComplianceCheck check) throws JSONException {
		CheckpointParameters result = null;
		if (check.getControlAttributes().has(ATTRIBUTE_NAME_KEY)) {
			GenericCheckpointParameters generic = new GenericCheckpointParameters();
			result = generic;
			String key = check.getControlAttributes().getString(ATTRIBUTE_NAME_KEY);
			String[] keys = key.split("#");
			generic.setAttributeName(keys[1]);
			generic.setClassName(toCamelCase(keys[0]));
		} else {
			result = new CheckpointParameters();
		}
		result.setCheckId(check.getId());
		result.setMinimumValue(check.getControlAttributes().optString(MINIMUM_VALUE_KEY,null));
		result.setMaximumValue(check.getControlAttributes().optString(MAXIMUM_VALUE_KEY,null));
		result.setPatternValue(check.getControlAttributes().optString(PATTERN_VALUE_KEY,null));
		result.setCode(check.getCode());
		result.setErrorType(check.getCriticity().equals(CRITICITY.error));
		return result;
	}

	/**
	 * @param underscore
	 * @return
	 */
	protected static String toCamelCase(String underscore) {
		StringBuilder b = new StringBuilder();
		boolean underChar = false;
		boolean first = true;
		for (char c : underscore.toCharArray()) {
			if (first) {
				b.append(Character.toUpperCase(c));
				first = false;
			} else if (c == '_') {
				underChar = true;
			} else {
				if (underChar) {
					b.append(Character.toUpperCase(c));
					underChar = false;
				} else
					b.append(c);
			}
		}
		return b.toString();
	}

	public static class DefaultFactory extends InputValidatorFactory {

		@Override
		protected InputValidator create() throws IOException {
			return new ValidatorInputValidator();
		}
	}

	static {
		InputValidatorFactory.factories.put(ValidatorInputValidator.class.getName(), new DefaultFactory());
	}

}
