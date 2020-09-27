package net.hashsploit.clank.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;

public class Utils {
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	
	// Prevent instantiation
	private Utils() {}
	
	public static String bytesToStringClean(byte[] data) {
		String result = "";
		for (byte b: data) {
			if (b == 0x00)
				break;
			result += (char) b;
		}
		return result;
	}

	public static List<DataPacket> decodeRTMessageFrames(byte[] data) {
		final List<DataPacket> packets = new ArrayList<DataPacket>();

		int index = 0;

		try {
			while (index < data.length) {
				final byte id = data[index + 0];

				ByteBuffer bb = ByteBuffer.allocate(2);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				bb.put(data[index + 1]);
				bb.put(data[index + 2]);
				short length = bb.getShort(0);

				//logger.fine("Length: " + Integer.toString(length));
				byte[] finalData = new byte[length];
				int offset = 0;

				if (length > 0) {
					// ID(1) + Length(2)
					offset += 1 + 2;
				}

				// logger.warning("PLAIN DATA PACKET");
				System.arraycopy(data, index + offset, finalData, 0, finalData.length);

				RTPacketId rtid = null;

				for (RTPacketId p : RTPacketId.values()) {
					if (p.getByte() == id) {
						rtid = p;
						break;
					}
				}

				packets.add(new DataPacket(rtid, finalData));

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
			dst[len-1] = 0x00;
		}
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
	}
	
	
}
