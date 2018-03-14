package mobi.chouette.exchange;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import lombok.extern.log4j.Log4j;

@Log4j
public abstract class AbstractInputValidator implements InputValidator {
	
	public static final String NOT_IMPLEMENTED = "Not yet implemented";

	protected boolean checkFileExistenceInZip(String fileName, Path filePath, String format) {
		boolean isZipFileValid = true;

		if (fileName.endsWith(".zip")) {
			isZipFileValid = false;
			File file = new File(filePath.toString());
			try (ZipFile zipFile = new ZipFile(file);) {
				for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements();) {
					ZipEntry ze = e.nextElement();
					String name = ze.getName();
					if (name.endsWith("." + format) && !name.contains("metadata")) {
						isZipFileValid = true;
						break;
					}
				}
			} catch (IOException e) {
				log.error("Erreur ouverture fichier zip " + fileName);
			}
		}

		return isZipFileValid;
	}
	
	
}
