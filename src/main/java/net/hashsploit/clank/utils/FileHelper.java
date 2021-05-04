package net.hashsploit.clank.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.hashsploit.clank.Clank;

public class FileHelper {

	// Prevent instantiation
	private FileHelper() {
	}

	/**
	 * Read an asset file to a string
	 * 
	 * @param filename
	 * @return
	 */
	public static final String readAsset(String filename) {
		return readFromInputStream(Clank.getInstance().getClass().getResourceAsStream(filename));
	}

	/**
	 * Read a string from an input string
	 * 
	 * @param inputStream
	 * @return
	 */
	public static final String readFromInputStream(InputStream inputStream) {
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		} catch (IOException e) {
			Clank.getInstance().getTerminal().handleException(e);
		}
		return resultStringBuilder.toString();
	}

	/**
	 * Unzips a .zip archive.
	 * @param source
	 * @param target
	 * @throws IOException
	 */
	public static void unzipFolder(Path source, Path target) throws IOException {

		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {

			// list files in zip
			ZipEntry zipEntry = zis.getNextEntry();

			while (zipEntry != null) {

				boolean isDirectory = false;
				// example 1.1
				// some zip stored files and folders separately
				// e.g data/
				// data/folder/
				// data/folder/file.txt
				if (zipEntry.getName().endsWith(File.separator)) {
					isDirectory = true;
				}

				Path newPath = zipSlipProtect(zipEntry, target);

				if (isDirectory) {
					Files.createDirectories(newPath);
				} else {

					// example 1.2
					// some zip stored file path only, need create parent directories
					// e.g data/folder/file.txt
					if (newPath.getParent() != null) {
						if (Files.notExists(newPath.getParent())) {
							Files.createDirectories(newPath.getParent());
						}
					}

					// copy files, nio
					Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);

					// copy files, classic
					/*
					 * try (FileOutputStream fos = new FileOutputStream(newPath.toFile())) { byte[]
					 * buffer = new byte[1024]; int len; while ((len = zis.read(buffer)) > 0) {
					 * fos.write(buffer, 0, len); } }
					 */
				}

				zipEntry = zis.getNextEntry();

			}
			zis.closeEntry();

		}

	}

	// protect zip slip attack
	public static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir) throws IOException {
		// test zip slip vulnerability
		// Path targetDirResolved = targetDir.resolve("../../" + zipEntry.getName());

		Path targetDirResolved = targetDir.resolve(zipEntry.getName());

		// make sure normalized file still has targetDir as its prefix
		// else throws exception
		Path normalizePath = targetDirResolved.normalize();
		if (!normalizePath.startsWith(targetDir)) {
			throw new IOException("Bad zip entry: " + zipEntry.getName());
		}

		return normalizePath;
	}

}
