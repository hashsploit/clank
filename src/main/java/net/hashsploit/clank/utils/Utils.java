package net.hashsploit.clank.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;

public class Utils {

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	private static final int PUBLIC_IP_UPDATE_TRESHOLD = 60;

	private static String lastPublicIp = null;
	private static long lastPublicIpUpdate = 0L;

	// Prevent instantiation
	private Utils() {
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
	
	public static RtMessageId getRtMessageId(byte id) {
		for (final RtMessageId p : RtMessageId.values()) {
			if (p.getValue() == id) {
				return p;
			}
		}
		return null;
	}
	
	public static ByteBuffer toNioBuffer(final ByteBuf buffer) {
		if (buffer.isDirect()) {
			return buffer.nioBuffer();
		}
		final byte[] bytes = new byte[buffer.readableBytes()];
		buffer.getBytes(buffer.readerIndex(), bytes);
		return ByteBuffer.wrap(bytes);
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

	/**
	 * Decode an array of bytes into RT message frames.
	 * 
	 * @param data
	 * @return
	 */
	public static List<RTMessage> decodeRTMessageFrames(byte[] data) {
		final List<RTMessage> packets = new ArrayList<RTMessage>();

		int index = 0;

		try {
			while (index < data.length) {
				final byte id = data[index + 0];

				ByteBuffer bb = ByteBuffer.allocate(2);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				bb.put(data[index + 1]);
				bb.put(data[index + 2]);
				short length = bb.getShort(0);

				// logger.fine("Length: " + Integer.toString(length));
				byte[] finalData = new byte[length];
				int offset = 0;

				if (length > 0) {
					// ID(1) + Length(2)
					offset += 1 + 2;
				}

				// logger.warning("PLAIN DATA PACKET");
				System.arraycopy(data, index + offset, finalData, 0, finalData.length);

				RtMessageId rtid = null;

				for (RtMessageId p : RtMessageId.values()) {
					if (p.getValue() == id) {
						rtid = p;
						break;
					}
				}

				packets.add(new RTMessage(rtid, finalData));

				index += length + 3;
			}
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}

		return packets;
	}

	public static short bytesToShortLittle(final byte byte1, final byte byte2) {
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(byte1);
		bb.put(byte2);
		short shortVal = bb.getShort(0);
		return shortVal;
	}

	public static int bytesToIntLittle(final byte[] data) {

		ByteBuffer b = ByteBuffer.wrap(data); // big-endian by default
		b.order(ByteOrder.LITTLE_ENDIAN); // optional, the initial order of a byte buffer is always BIG_ENDIAN.
		int num = b.getInt();
		return num;
	}

	public static byte[] shortToBytesLittle(final short data) {
		ByteBuffer b = ByteBuffer.allocate(2);
		b.order(ByteOrder.LITTLE_ENDIAN); // optional, the initial order of a byte buffer is always BIG_ENDIAN.
		b.putShort(data);

		byte[] result = b.array();
		return result;
	}

	public static byte[] intToBytesLittle(final int data) {
		ByteBuffer b = ByteBuffer.allocate(4);
		b.order(ByteOrder.LITTLE_ENDIAN); // optional, the initial order of a byte buffer is always BIG_ENDIAN.
		b.putInt(data);

		byte[] result = b.array();
		return result;
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
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.put(input);
		buffer.flip();
		return buffer.array();
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
		ByteBuffer b = ByteBuffer.allocate(4);

		b.order(ByteOrder.BIG_ENDIAN); // optional, the initial order of a byte buffer is always BIG_ENDIAN.
		b.putInt(data);

		return b.array();
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

		// Cache the IP for a little while.
		if (lastPublicIpUpdate + (1000 * PUBLIC_IP_UPDATE_TRESHOLD) > new Date().getTime()) {
			return lastPublicIp;
		}

		try {
			Clank.getInstance().getTerminal().print(Level.FINE, "Getting public IP Address from api.ipify.org ...");
			URL url = new URL("https://api.ipify.org");
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

			if (connection.getResponseCode() == 200 || connection.getResponseCode() == 304) {
				final String ipAddr = new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining()).trim();
				Clank.getInstance().getTerminal().print(Level.FINEST, String.format("Public IP address: %s", ipAddr));

				// Set cache.
				lastPublicIp = ipAddr;
				lastPublicIpUpdate = new Date().getTime();

				return ipAddr;
			}

			Clank.getInstance().getTerminal().print(Level.SEVERE, "Got non-HTTP 200 status from ipify endpoint!");
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

	public static ByteBuffer byteBufToNioBuffer(final ByteBuf buffer) {
		if (buffer.isDirect()) {
			return buffer.nioBuffer();
		}
		final byte[] bytes = new byte[buffer.readableBytes()];
		buffer.getBytes(buffer.readerIndex(), bytes);
		return ByteBuffer.wrap(bytes);
	}

	public static byte[] flipByteArray(byte[] array) {
	      byte[] result = new byte[array.length];
	      for (int i = 0; i < array.length; i++) {
	    	  result[array.length-1-i] = array[i];
	      }
	      return result;
	}
	
}
