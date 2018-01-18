package mobi.chouette.exchange.netex_stif.exporter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.JSONUtil;
import mobi.chouette.exchange.AbstractInputValidator;
import mobi.chouette.exchange.InputValidator;
import mobi.chouette.exchange.InputValidatorFactory;
import mobi.chouette.exchange.parameters.AbstractParameter;

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
		// TODO Auto-generated method stub
		return null;
	}

}
