package net.hashsploit.clank.utils;

public class Utils {
	
	// Prevent instantiation
	private Utils() {}
	
	/**
	 * Validate if a sequence of two byte arrays equal each other.
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	public static boolean sequenceEquals(byte[] arr1, byte[] arr2) {
		if (arr1.length != arr2.length) {
			return false;
		}
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] != arr2[i]) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Represent a byte as a string.
	 * @param data
	 * @return
	 */
	public static final String byteToString(byte data) {
		return String.format("%02X", data);
	}
	
	/**
	 * Represent an array of bytes as a string.
	 * @param buffer
	 * @return
	 */
	public static final String bytesToString(byte[] buffer) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < buffer.length; ++i) {
			sb.append(byteToString(buffer[i]));
		}
		return sb.toString();
	}
	
	
}
