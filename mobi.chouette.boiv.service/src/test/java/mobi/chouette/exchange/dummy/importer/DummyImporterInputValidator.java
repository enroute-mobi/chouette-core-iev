package mobi.chouette.exchange.dummy.importer;

import java.nio.file.Path;
import java.util.Arrays;

import mobi.chouette.common.JSONUtil;
import mobi.chouette.exchange.AbstractInputValidator;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.model.Referential;
import mobi.chouette.model.importer.ImportTask;

public class DummyImporterInputValidator extends AbstractInputValidator {

	@Override
	public AbstractParameter toActionParameter(String abstractParameter) {
		try {
			return JSONUtil.fromJSON(abstractParameter, DummyImportParameters.class);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean checkParameters(String abstractParameter) {
		return true;
	}


	@Override
	public boolean checkFilename(String fileName) {
		return true;
	}
	
	@Override
	public boolean checkFile(String fileName, Path filePath, AbstractParameter abstractParameter) {
		return true;
	}

	// @Override
//	public List<TestDescription> getTestList() {
//		return null;
//	}

	@Override
	public AbstractParameter toActionParameter(Object task) {
		if (task instanceof ImportTask) {
			ImportTask importTask = (ImportTask) task;
			Referential referential = importTask.getReferential();
			DummyImportParameters parameter = new DummyImportParameters();
			parameter.setImportId(importTask.getId());
			parameter.setLineReferentialId(referential.getLineReferentialId());
			parameter.setStopAreaReferentialId(referential.getStopAreaReferentialId());
			parameter.setReferencesType("lines");
			parameter.getIds().addAll(Arrays.asList(referential.getMetadatas().get(0).getLineIds()));
			parameter.setReferentialId(referential.getId());
			parameter.setReferentialName(referential.getName());
			return parameter;
		}
		return null;
	}

	@Override
	public boolean checkParameters(AbstractParameter abstractParameter) {
		return true;
	}

}
