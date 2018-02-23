package mobi.chouette.exchange.netex_stif.exporter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.JobData;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.model.LineLite;

public class NetexStifLineProducer {

	public void produce(Context context) throws Exception {

		ActionReporter reporter = ActionReporter.Factory.getInstance();
		ExportableData collection = (ExportableData) context.get(Constant.EXPORTABLE_DATA);
		JobData jobData = (JobData) context.get(Constant.JOB_DATA);
		String rootDirectory = jobData.getPathName();

		Path dir = Paths.get(rootDirectory, Constant.OUTPUT);
		LineLite line = collection.getLineLite();
		String fileName = "offre_"+line.objectIdSuffix() + "_"+ line.getNumber() + ".xml";
		File file = new File(dir.toFile(), fileName);

		NetexStifFileWriter writer = new NetexStifFileWriter();
		writer.writeXmlFile(collection, file);

		reporter.addFileReport(context, fileName, IO_TYPE.OUTPUT);

	}

}
