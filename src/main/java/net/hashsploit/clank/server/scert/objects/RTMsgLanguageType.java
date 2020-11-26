package net.hashsploit.clank.server.scert.objects;

/**
 * This is the RT_MSG_LANGUAGE_TYPE variant used in the RT_MSG_SERVER_SYSTEM_MESSAGE (0x1c) packet.
 * 
 * @author hashsploit
 *
 */
public enum RTMsgLanguageType {

	RT_MSG_LANGUAGE_NONE(0x00),
	
	RT_MSG_LANGUAGE_US_ENGLISH(0x01),
	
	RT_MSG_LANGUAGE_UK_ENGLISH(0x02),
	
	RT_MSG_LANGUAGE_JAPANESE(0x03),
	
	RT_MSG_LANGUAGE_KOREAN(0x04),
	
	RT_MSG_LANGUAGE_ITALIAN(0x05),
	
	RT_MSG_LANGUAGE_SPANISH(0x06),
	
	RT_MSG_LANGUAGE_GERMAN(0x07),
	
	RT_MSG_LANGUAGE_FRENCH(0x08),
	
	RT_MSG_LANGUAGE_DUTCH(0x09),
	
	RT_MSG_LANGUAGE_PORTUGUESE(0x0a),
	
	RT_MSG_LANGUAGE_CHINESE(0x0b),
	
	RT_MSG_LANGUAGE_TAIWANESE(0x0c);
	
	private final byte value;

	private RTMsgLanguageType(int value) {
		this.value = (byte) value;
	}

	public final byte getValue() {
		return value;
	}
	
}
