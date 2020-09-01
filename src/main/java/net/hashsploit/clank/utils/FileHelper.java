package net.hashsploit.clank.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.hashsploit.clank.Clank;

public class FileHelper {

	// Prevent instantiation
	private FileHelper() {
	}
	
	/**
	 * Read an asset file to a string
	 * @param filename
	 * @return
	 */
	public static final String readAsset(String filename) {
		return readFromInputStream(Clank.getInstance().getClass().getResourceAsStream("/assets/" + filename));
	}

	/**
	 * Read a string from an input string
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
	
}
