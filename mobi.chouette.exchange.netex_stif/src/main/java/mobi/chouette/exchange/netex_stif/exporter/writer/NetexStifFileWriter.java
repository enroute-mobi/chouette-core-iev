package mobi.chouette.exchange.netex_stif.exporter.writer;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.io.output.FileWriterWithEncoding;

import lombok.extern.log4j.Log4j;
import mobi.chouette.exchange.netex_stif.exporter.ExportableData;

@Log4j
public class NetexStifFileWriter {

	public NetexStifFileWriter() {
	}


	public File writeOffreFile(ExportableData collection, File file) throws IOException, DatatypeConfigurationException {
		
		Writer output = new FileWriterWithEncoding(file, "UTF-8");
		
		NetexOffreLigneWriter.write(output, collection);
		
		output.close();

		log.info("File : " + file.getName() + " created");

		return file;
	}

	public File writeCalendriersFile(ExportableData collection, File file) throws IOException, DatatypeConfigurationException {
		
		Writer output = new FileWriterWithEncoding(file, "UTF-8");
		
		NetexCalendrierWriter.write(output, collection);
		
		output.close();

		log.info("File : " + file.getName() + " created");

		return file;
	}

	public File writeCommunFile(ExportableData collection, File file) throws IOException, DatatypeConfigurationException {
		
		Writer output = new FileWriterWithEncoding(file, "UTF-8");
		
		NetexCommunWriter.write(output, collection);
		
		output.close();

		log.info("File : " + file.getName() + " created");

		return file;
	}


	public File writeLignesFile(ExportableData collection, File file) throws IOException, DatatypeConfigurationException {
		Writer output = new FileWriterWithEncoding(file, "UTF-8");
		
		NetexLignesWriter.write(output, collection);
		
		output.close();

		log.info("File : " + file.getName() + " created");

		return file;
		
	}

	public File writeArretsFile(ExportableData collection, File file) throws IOException, DatatypeConfigurationException {
		Writer output = new FileWriterWithEncoding(file, "UTF-8");
		
		NetexArretsWriter.write(output, collection);
		
		output.close();

		log.info("File : " + file.getName() + " created");

		return file;
		
	}


}
