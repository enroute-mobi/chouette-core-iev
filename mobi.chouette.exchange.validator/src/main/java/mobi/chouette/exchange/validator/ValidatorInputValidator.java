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
import mobi.chouette.exchange.validator.checkpoints.CheckPointConstant;
import mobi.chouette.exchange.validator.checkpoints.CheckpointParameters;
import mobi.chouette.exchange.validator.checkpoints.ControlParameters;
import mobi.chouette.exchange.validator.checkpoints.GenericCheckpointParameters;
import mobi.chouette.model.Organisation;
import mobi.chouette.model.Referential;
import mobi.chouette.model.compliance.ComplianceCheck;
import mobi.chouette.model.compliance.ComplianceCheck.CRITICITY;
import mobi.chouette.model.compliance.ComplianceCheckBlock;
import mobi.chouette.model.compliance.ComplianceCheckTask;
import mobi.chouette.model.util.ChouetteModelUtil;

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

		try {
			if (task instanceof ComplianceCheckTask) {
				ComplianceCheckTask checkTask = (ComplianceCheckTask) task;
				Referential referential = checkTask.getReferential();
				if (referential == null)
					throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential id is null");
				Organisation organisation = referential.getOrganisation();
				if (organisation == null)
					throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA,
							"referential organisation_id is null");
				ValidateParameters parameter = new ValidateParameters();
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
					throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "referential metadata is null");
				// loop to collect all lines (#6136)
				referential.getMetadatas().forEach(m -> {
					if (m.getLineIds() != null) {
						if (m.getLineIds().length > 0) {
							parameter.getIds().addAll(Arrays.asList(m.getLineIds()));
						}
					}
				});
				if (parameter.getIds().isEmpty())
					throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA,
							"referential's metadata line ids empty");

				parameter.setReferentialId(referential.getId());
				parameter.setReferentialName(referential.getName());
				parameter.setOrganisationName(organisation.getName());
				parameter.setOrganisationCode(organisation.getCode());
				parameter.getLinesScope().addAll(organisation.getLines());

				// populate controlParameter
				ControlParameters controlParameters = parameter.getControlParameters();
				// Java IEV is told not to validate controls it doesn't implement
				checkTask.getComplianceChecks().stream().filter(cc -> cc.getIevEnabledCheck()).forEach(check -> {
					CheckpointParameters cp;
					try {
						cp = buildCheckpoint(check);
					} catch (Exception e) {
						log.error("problem with compliance_check " + check.getSpecificCode() + " " + e.getMessage(), e);
						throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, e,
								"cannot parse ComplianceCheck Attributes for " + check.getSpecificCode());
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

		} catch (CoreRuntimeException ex) {
			// log problem before throw it
			log.error(ex.getMessage(), ex);
			throw ex;
		}
		return null;
	}

	private void addToGlobalCheckPoints(ControlParameters controlParameters, CheckpointParameters cp) {
		Collection<CheckpointParameters> list = controlParameters.getGlobalCheckPoints()
				.computeIfAbsent(cp.getOriginCode(), l -> new ArrayList<>());
		list.add(cp);
	}

	private void addToTransportModeCheckPoints(ControlParameters controlParameters, ComplianceCheckBlock block,
			CheckpointParameters cp) {
		// get key for mode/submode
		String key = block.getConditionAttributes().get(TRANSPORT_MODE_KEY);
		if (block.getConditionAttributes().containsKey(TRANSPORT_SUBMODE_KEY)) {
			key += "/" + block.getConditionAttributes().get(TRANSPORT_SUBMODE_KEY);
		}
		Map<String, Collection<CheckpointParameters>> map = controlParameters.getTransportModeCheckpoints()
				.computeIfAbsent(key, l -> new HashMap<>());
		Collection<CheckpointParameters> list = map.computeIfAbsent(cp.getOriginCode(), l -> new ArrayList<>());
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
			generic.setClassName(ChouetteModelUtil.classNameforRubyName(keys[0]));

			// check valid class and attribute
			try {
				Class<?> clazz = Class.forName("mobi.chouette.model." + generic.getClassName());
				String methodName = "get" + ChouetteModelUtil.toCamelCase(generic.getAttributeName());
				if (!Arrays.stream(clazz.getMethods()).anyMatch(m -> m.getName().equalsIgnoreCase(methodName)))
					throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA, "unknown check point attribute "
							+ generic.getClassName() + " for class " + generic.getClassName());
			} catch (ClassNotFoundException e) {
				throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA,
						"unknown check point class " + generic.getClassName());
			}
		} else {
			result = new CheckpointParameters();
		}
		result.setCheckId(check.getId());
		result.setMinimumValue(check.getControlAttributes().optString(MINIMUM_VALUE_KEY, null));
		result.setMaximumValue(check.getControlAttributes().optString(MAXIMUM_VALUE_KEY, null));
		result.setPatternValue(check.getControlAttributes().optString(PATTERN_VALUE_KEY, null));
		// check existing code
		if (!CheckPointConstant.exists(check.getOriginCode()))
			throw new CoreRuntimeException(CoreExceptionCode.UNVALID_DATA,
					"unknown check point code " + check.getOriginCode());
		result.setOriginCode(check.getOriginCode());
		result.setSpecificCode(check.getSpecificCode());
		result.setName(check.getName());
		result.setErrorType(check.getCriticity().equals(CRITICITY.error));
		return result;
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
