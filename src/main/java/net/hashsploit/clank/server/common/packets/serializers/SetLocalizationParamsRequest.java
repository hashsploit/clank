package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class SetLocalizationParamsRequest extends MediusMessage {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.value];
	private int characterEncoding;
	private int languageType;
	
	public SetLocalizationParamsRequest(byte[] data) {
		super(MediusMessageType.SetLocalizationParams, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageId);
		buf.get(sessionKey);
		buf.getShort(); // 2 byte padding
		buf.get(characterEncoding); 
		buf.get(languageType);
	}
	
	// @Override
	// public String toString() {
	// 	return "SetLocalizationParamsRequest: \n" + 
	// 		"messageId: " + Utils.bytesToHex(messageId) + '\n' + 
    //         "sessionKey: " + Utils.bytesToHex(sessionKey) + '\n' +
    //         "characterEncoding: " + Utils.bytesToHex(characterEncoding) + '\n' +
	// 		"languageType: " + Utils.bytesToHex(languageType);
	// }

	public byte[] getMessageId() {
		return messageId;
	}

	public byte[] getSessionKey() {
		return sessionKey;
	}

	public int getLanguageType() {
		return languageType;
    }
    
    public int getCharacterEncoding() {
		return characterEncoding;
    }
	
}
