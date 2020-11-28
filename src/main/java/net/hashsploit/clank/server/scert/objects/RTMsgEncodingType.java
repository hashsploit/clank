package net.hashsploit.clank.server.scert.objects;

/**
 * This is the RT_MSG_ENCODING_TYPE variant used in the RT_MSG_SERVER_SYSTEM_MESSAGE (0x1c) packet.
 * 
 * @author hashsploit
 *
 */
public enum RTMsgEncodingType {
	
	RT_MSG_ENCODING_NONE(0x00),
	
	RT_MSG_ENCODING_ISO_8859_1(0x01),
	
	RT_MSG_ENCODING_UTF8(0x02),
	
	RT_MSG_ENCODING_BINARY(0x03);
	
	private final byte value;

	private RTMsgEncodingType(int value) {
		this.value = (byte) value;
	}

	public final byte getValue() {
		return value;
	}
	
}
