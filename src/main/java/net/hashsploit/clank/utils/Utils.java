package net.hashsploit.clank.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.medius.crypto.CipherContext;
import net.hashsploit.medius.crypto.rc.PS2_RC4;

public class Utils {

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	private static final int PUBLIC_IP_UPDATE_TRESHOLD = 86400;

	private static String lastPublicIp = null;
	private static long lastPublicIpUpdate = 0L;

	// Prevent instantiation
	private Utils() {
	}
	
	/**
	 * Generate an RC4 key using a context.
	 * @param context
	 */
	public static PS2_RC4 generateRC4Key(CipherContext context) {
		Random rng = new Random();
		byte[] randomBytes = new byte[64];
		rng.nextBytes(randomBytes);
		return new PS2_RC4(randomBytes, context);
	}

	/**
	 * Convert an array of bytes into a string (without null-bytes) and convert line-endings to readable chars.
	 * 
	 * @param data
	 * @return
	 */
	public static String bytesToStringClean(byte[] data) {
		final StringBuilder sb = new StringBuilder();

		for (byte b : data) {
			if (b == (byte) 0x00) {
				break;
			}
			if (b == '\r') {
				sb.append("\\r");
				continue;
			}
			if (b == '\n') {
				sb.append("\\n");
				continue;
			}
			sb.append((char) b);
		}

		return sb.toString();
	}
	
	public static byte[] buildByteArray(int length, Object... args) {
        ByteArrayOutputStream bOutput = new ByteArrayOutputStream(length);
        try {
	        for (Object b: args) {
	        	bOutput.write((byte[]) b);
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return bOutput.toByteArray();
	}
	
	public static RtMessageId getRtMessageId(byte id) {
		for (final RtMessageId p : RtMessageId.values()) {
			if (p.getValue() == id) {
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Reads a byte array up until a null-terminator and convert it to a string.
	 * 
	 * @param data
	 * @return
	 */
	public static String parseMediusString(byte[] data) {
		final StringBuilder sb = new StringBuilder();
		
		for (byte b : data) {
			if (b == 0x00) {
				break;
			}
			sb.append((char) b);
		}
		
		return sb.toString();
	}

	public static short bytesToShortLittle(final byte[] bytes) {
		return bytesToShortLittle(bytes[0], bytes[1]);
	}
	
	public static short bytesToShortLittle(final byte byte1, final byte byte2) {
		return (short)(((byte2 & 0xFF) << 8) | (byte1 & 0xFF));
	}

	public static int bytesToIntLittle(final byte[] data) {
        return ((data[3] & 0xFF) << 24) |
                ((data[2] & 0xFF) << 16) |
                ((data[1] & 0xFF) << 8) |
                ((data[0] & 0xFF) << 0);
	}

	public static byte[] shortToBytesLittle(final short data) {
		byte[] result = new byte[2];
		result[0] = (byte)(data & 0xff);
		result[1] = (byte)((data >> 8) & 0xff);
		return result;
	}

	public static byte[] intToBytesLittle(final int data) {
        return new byte[] {
                (byte)data,
                (byte)(data >> 8),
                (byte)(data >> 16),
                (byte)(data >> 24)};
	}

	public static byte[] buildByteArrayFromString(String s, int len) {
		byte[] dst = new byte[len];
		byte[] src = s.getBytes();
		System.arraycopy(src, 0, dst, 0, src.length);
		if (len != 1) {
			dst[len - 1] = 0x00;
		}
		return dst;
	}

	public static byte[] padByteArray(byte[] input, int length) {
		byte[] newArray = new byte[length];
		System.arraycopy(input, 0, newArray, 0, input.length);
		return newArray;
	}

	/**
	 * Validate if a sequence of two byte arrays equal each other.
	 * 
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
	 * Convert a byte to a hex string.
	 * 
	 * @param data
	 * @return
	 */
	public static final String byteToHex(byte data) {
		return String.format("%02X", data);
	}

	/**
	 * Convert an integer to a hex string.
	 * 
	 * @param value
	 * @return
	 */
	public static final String intToHex(int value) {
		return String.format("%1$02X", value);
	}

	/**
	 * Convert an array of bytes to a hex string.
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];

		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}

		return new String(hexChars).toLowerCase();
	}

	/**
	 * Convert a hex string to a byte array. Must be full bytes! AE13 ... etc.
	 * 
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToByteArray(final String hexString) {
		final int len = hexString.length();
		final byte[] data = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
		}

		return data;
	}

	/**
	 * Convert an int to a byte array.
	 * 
	 * @param data
	 * @return byteArray
	 */
	public static byte[] intToBytes(final int data) {
        return new byte[] {
                (byte)(data >> 24),
                (byte)(data >> 16),
                (byte)(data >> 8),
                (byte) data};
	}

	/**
	 * This is a safe function which takes in a Netty ByteBuf object
	 * and returns a byte[] from it, regardless if it has a backing array or not.
	 * 
	 * @param buf
	 * @return
	 */
	public static byte[] nettyByteBufToByteArray(ByteBuf buf) {
		byte[] bytes;
		int length = buf.readableBytes();
		if (buf.hasArray()) {
		    bytes = buf.array();
		} else {
		    bytes = new byte[length];
		    buf.getBytes(buf.readerIndex(), bytes);
		}
		return bytes;
	}
	
	/**
	 * Check if a TCP port is already in use.
	 * 
	 * @param port
	 * @return boolean
	 */
	public static boolean tcpPortIsAvailable(String address, int port) {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket();
			ss.bind(new InetSocketAddress(address, port));
			ss.setReuseAddress(true);
			return true;
		} catch (IOException e) {
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
				}
			}
		}
		return false;
	}

	/**
	 * Check if a UDP port is already in use.
	 * 
	 * @param port
	 * @return boolean
	 */
	public static boolean udpPortIsAvailable(String address, int port) {
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket();
			ds.bind(new InetSocketAddress(address, port));
			ds.setReuseAddress(true);
			return true;
		} catch (IOException e) {
		} finally {
			if (ds != null) {
				ds.close();
			}
		}
		return false;
	}

	/**
	 * Get the server's current public IP Address. This will return null if it
	 * cannot contact the remote service.
	 * 
	 * @return publicIpAddress
	 */
	public static String getPublicIpAddress() {
		
		// If the IP Address is still cached, use it
		if (lastPublicIpUpdate + (1000 * PUBLIC_IP_UPDATE_TRESHOLD) > new Date().getTime()) {
			return lastPublicIp;
		}
		
		String address = null;
		
		address = getPublicIpAddressFromService("https://ip.seeip.org/");
		
		if (address == null) {
			address = getPublicIpAddressFromService("https://api.ipify.org/");
		}
		
		if (address != null) {
			// Cache the IP Address for a while
			lastPublicIp = address;
			lastPublicIpUpdate = new Date().getTime();
		}
		
		return address;
	}
	
	private static String getPublicIpAddressFromService(String url) {

		try {
			Clank.getInstance().getTerminal().print(Level.FINE, String.format("Getting public IP Address from %s ...", url));
			URL urlObj = new URL(url);
			HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();

			if (connection.getResponseCode() == 200 || connection.getResponseCode() == 304) {
				final String ipAddr = new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining()).trim();
				Clank.getInstance().getTerminal().print(Level.FINEST, String.format("Public IP address: %s", ipAddr));
				return ipAddr;
			}

			Clank.getInstance().getTerminal().print(Level.SEVERE, String.format("Got non-HTTP 200 status from %s endpoint!", url));
		} catch (IOException e) {
			Clank.getInstance().getTerminal().print(Level.SEVERE, "Failed to get the public IP Address!");
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Add needle flag(s) to the haystack bitmask.
	 * 
	 * @param needle
	 * @param haystack
	 * @return
	 */
	public static int addToBitmask(int needle, int haystack) {
		return needle | haystack;
	}

	/**
	 * Remove needle flag(s) from the haystack bitmask.
	 * 
	 * @param needle
	 * @param haystack
	 * @return
	 */
	public static int removeFromBitmask(int needle, int haystack) {
		return haystack &= ~needle;
	}

	/**
	 * Check if the needle flag(s) are in the haystack mask. This is useful for
	 * checking if a value is in a bitmask. For example
	 * (MEDIUS_AUTHENTICATION_SERVER | MEDIUS_LOBBY_SERVER).
	 * 
	 * @param needle
	 * @param haystack
	 * @return
	 */
	public static boolean isInBitmask(int needle, int haystack) {
		return (needle & haystack) != 0;
	}

	/**
	 * Generate a pretty Key-Value pair table to display packet disassembly.
	 * 
	 * @param title
	 * @param keys
	 * @param values
	 * @return
	 */
	public static String generateDebugPacketString(String title, String[] keys, String[] values) {
		final StringBuilder sb = new StringBuilder();
		final String cornerSeparator = "+";
		final String horizSeparator = "-";
		final String vertSeparator = "|";
		int maxValuePrintLength = 64;

		if (Clank.getInstance() != null && Clank.getInstance().getTerminal() != null && Clank.getInstance().getTerminal().getConsoleReader().getTerminal().isSupported()) {
			maxValuePrintLength = Math.max(8, Clank.getInstance().getTerminal().getConsoleReader().getTerminal().getWidth() - 24);
		}

		int maxKeyLength = 0;
		int maxValueLength = 0;

		for (final String k : keys) {
			if (k.length() > maxKeyLength) {
				maxKeyLength = k.length() + 1;
			}
		}

		for (final String v : values) {
			if (v.length() > maxValueLength) {
				maxValueLength = v.length() + 1;
			}
		}

		if (maxValueLength > maxValuePrintLength) {
			maxValueLength = maxValuePrintLength + 1;
		}

		sb.append("PACKET DISASSEMBLY: ").append(title).append("\n");

		sb.append(cornerSeparator);
		for (int i = 0; i < maxKeyLength + 1; i++) {
			sb.append(horizSeparator);
		}
		sb.append(cornerSeparator);
		for (int i = 0; i < maxValueLength + 1; i++) {
			sb.append(horizSeparator);
		}
		sb.append(cornerSeparator).append("\n");

		sb.append(vertSeparator).append(" ").append("Key");
		for (int i = 0; i < maxKeyLength - 3; i++) {
			sb.append(" ");
		}
		sb.append(vertSeparator).append(" ").append("Value");
		for (int i = 0; i < maxValueLength - 5; i++) {
			sb.append(" ");
		}
		sb.append(vertSeparator).append("\n");

		sb.append(cornerSeparator);
		for (int i = 0; i < maxKeyLength + 1; i++) {
			sb.append(horizSeparator);
		}
		sb.append(cornerSeparator);
		for (int i = 0; i < maxValueLength + 1; i++) {
			sb.append(horizSeparator);
		}
		sb.append(cornerSeparator).append("\n");

		for (int i = 0; i < keys.length; i++) {
			final String key = Utils.bytesToStringClean(keys[i].getBytes());
			final String value = Utils.bytesToStringClean(values[i].getBytes());
			
			sb.append(vertSeparator).append(" ").append(key);

			for (int j = 0; j < maxKeyLength - key.length(); j++) {
				sb.append(" ");
			}

			if (value.length() > maxValuePrintLength) {
				for (int j = 0; j < value.length(); j += maxValuePrintLength) {
					int remaining = j + maxValuePrintLength;
					if (remaining > value.length()) {
						remaining = value.length();
					}

					final String segment = value.substring(j, remaining);

					// Add key spacing
					if (j > 0) {
						sb.append(vertSeparator).append(" ");
						for (int k = 0; k < maxKeyLength; k++) {
							sb.append(" ");
						}
					}

					sb.append(vertSeparator).append(" ").append(segment);
					for (int k = 0; k < maxValueLength - segment.length(); k++) {
						sb.append(" ");
					}
					sb.append(vertSeparator).append("\n");
				}

			} else {
				sb.append(vertSeparator).append(" ").append(value);
				for (int j = 0; j < maxValueLength - value.length(); j++) {
					sb.append(" ");
				}
				sb.append(vertSeparator).append("\n");
			}

		}

		sb.append(cornerSeparator);
		for (int i = 0; i < maxKeyLength + 1; i++) {
			sb.append(horizSeparator);
		}
		sb.append(cornerSeparator);
		for (int i = 0; i < maxValueLength + 1; i++) {
			sb.append(horizSeparator);
		}
		sb.append(cornerSeparator).append("\n");

		return sb.toString();
	}

	public static byte[] flipByteArray(byte[] array) {
	      byte[] result = new byte[array.length];
	      for (int i = 0; i < array.length; i++) {
	    	  result[array.length-1-i] = array[i];
	      }
	      return result;
	}
	
}