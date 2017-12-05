package mobi.chouette.exchange.importer;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.common.chain.Command;
import mobi.chouette.exchange.validation.ValidationData;
import mobi.chouette.model.util.Referential;

@Log4j
public abstract class AbstractDisposeImportCommand implements Command {

	public boolean execute(Context context) throws Exception {
		boolean result = Constant.ERROR;

		try {
			ValidationData validationData = (ValidationData) context.get(Constant.VALIDATION_DATA);
			if (validationData != null)
				validationData.dispose();
			Referential cache = (Referential) context.get(Constant.CACHE);
			if (cache != null)
				cache.clear(false);
			Referential referential = (Referential) context.get(Constant.REFERENTIAL);
			if (referential != null)
				referential.dispose();
			
			result = Constant.SUCCESS;
			JobData jobData = (JobData) context.get(Constant.JOB_DATA);
			String path = jobData.getPathName();
			Path target = Paths.get(path, Constant.INPUT);
			deleteDirectory(target);

		} catch (Exception e) {
			log.error(e, e);
			throw e;
		}

		return result;
	}

	private void deleteDirectory(Path dirPath) {
		if (dirPath.toFile().exists()) {
			try {
				FileUtils.deleteDirectory(dirPath.toFile());
			} catch (Exception e) {
				log.warn("cannot purge input directory " + e.getMessage());
			}
		}
	}

}
