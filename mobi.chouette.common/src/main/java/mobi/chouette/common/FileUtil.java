package mobi.chouette.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import lombok.extern.log4j.Log4j;

@Log4j
public class FileUtil {
	private static final String GLOB = "glob:";

	private FileUtil() {
	}

	public static List<Path> listFiles(Path path, String glob) throws IOException {
		final PathMatcher matcher = path.getFileSystem().getPathMatcher(GLOB + glob);

		final DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {

			@Override
			public boolean accept(Path entry) throws IOException {
				return entry.toFile().isDirectory() || matcher.matches(entry.getFileName());
			}
		};
		List<Path> result = new ArrayList<>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, filter)) {
			for (Path entry : stream) {
				if (entry.toFile().isDirectory()) {
					result.addAll(listFiles(entry, glob));
					return result;
				}
				result.add(entry);
			}
		}
		return result;
	}

	public static List<Path> listFiles(Path path, String glob, String exclusionGlob) throws IOException {
		final PathMatcher matcher = path.getFileSystem().getPathMatcher(GLOB + glob);

		final PathMatcher excludeMatcher = path.getFileSystem().getPathMatcher(GLOB + exclusionGlob);

		final DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {

			@Override
			public boolean accept(Path entry) throws IOException {
				return entry.toFile().isDirectory()
						|| (matcher.matches(entry.getFileName()) && !excludeMatcher.matches(entry.getFileName()));
			}
		};
		List<Path> result = new ArrayList<>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, filter)) {
			for (Path entry : stream) {
				if (entry.toFile().isDirectory()) {
					result.addAll(listFiles(entry, glob, exclusionGlob));
					return result;
				}
				result.add(entry);
			}
		}
		return result;
	}

	public static void uncompress(String filename, String path) throws IOException, ArchiveException {
		try (ArchiveInputStream in = new ArchiveStreamFactory()
				.createArchiveInputStream(new BufferedInputStream(new FileInputStream(new File(filename))));) {
			ArchiveEntry entry = null;
			while ((entry = in.getNextEntry()) != null) {

				String name = FilenameUtils.getName(entry.getName());
				File file = new File(path, name);
				if (!entry.isDirectory()) {
					if (file.exists()) {
						file.delete();
					}
					file.createNewFile();
					try (OutputStream out = new FileOutputStream(file);) {
						IOUtils.copy(in, out);
					}
				}
			}
		}
	}

	public static void compress(String path, String filename)  {

		File directoryToZip = new File(path);
		List<File> fileList = new ArrayList<>();
		getAllFiles(directoryToZip, fileList);
		writeZipFile(directoryToZip, filename, fileList);

	}

	private static void getAllFiles(File dir, List<File> fileList) {

		File[] files = dir.listFiles();
		for (File file : files) {
			fileList.add(file);
			if (file.isDirectory()) {
				getAllFiles(file, fileList);
			}
		}

	}

	private static void writeZipFile(File path, String zipName, List<File> fileList) {

		try (FileOutputStream fos = new FileOutputStream(zipName); ZipOutputStream zos = new ZipOutputStream(fos);) {

			for (File file : fileList) {
				if (!file.isDirectory()) { // we only zip files, not directories
					addToZip(path, file, zos);
				}
			}

		} catch (IOException e) {
			log.error("fail to write in zip "+zipName,e);
		
		} 
	}

	private static void addToZip(File directoryToZip, File file, ZipOutputStream zos)
			throws IOException {

		// we want the zipEntry's path to be a relative path that is relative
		// to the directory being zipped, so chop off the rest of the path
		String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
				file.getCanonicalPath().length());
		try (FileInputStream fis = new FileInputStream(file);) {
			ZipEntry zipEntry = new ZipEntry(zipFilePath);
			zos.putNextEntry(zipEntry);

			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zos.write(bytes, 0, length);
			}

			zos.closeEntry();
		}
	}

}
