package net.hashsploit.clank.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.hashsploit.clank.server.medius.MediusConstants;

public class Utils {
	
	// Prevent instantiation
	private Utils() {}
	
	
	
	
	
	
	
	
	public static byte[] buildByteArrayFromString(String s, int len) {
		byte[] dst = new byte[len];
		byte[] src = s.getBytes();
		System.arraycopy(src, 0, dst, 0, src.length);
		dst[len-1] = 0x00;
		return dst;
	}
	
	
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
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars).toLowerCase();
	}
	
	/* s must be an even-length string. */
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	public static byte[] intToBytes(final int data) {
		ByteBuffer b = ByteBuffer.allocate(4);
		b.order(ByteOrder.BIG_ENDIAN); // optional, the initial order of a byte buffer is always BIG_ENDIAN.
		b.putInt(data);

		byte[] result = b.array();
		return result;
//	    return new byte[] {
//	        (byte)((data >> 24) & 0xff),
//	        (byte)((data >> 16) & 0xff),
//	        (byte)((data >> 8) & 0xff),
//	        (byte)((data >> 0) & 0xff),
//	    };
	}
	
	
}
