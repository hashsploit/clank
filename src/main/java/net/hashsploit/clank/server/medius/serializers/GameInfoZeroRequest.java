package net.hashsploit.clank.server.medius.serializers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class GameInfoZeroRequest extends MediusMessage {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.value];
	private int worldId;
	
	public GameInfoZeroRequest(byte[] data) {
		super(MediusMessageType.GameInfo0, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		
		buf.get(messageId);
		buf.get(sessionKey);
		buf.getShort(); // buffer
		worldId = buf.getInt();
	}
	
	public String toString() {
		return "GameInfoZeroRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageId) + '\n' + 
				"sessionKey: " + Utils.bytesToHex(sessionKey) + '\n' + 
				"worldID: " + worldId;		
	}
	
	public byte[] getMessageID() {
		return messageId;
	}
	public byte[] getSessionKey() {
		return sessionKey;
	}
	
	public int getWorldId() {
		return worldId;
	}
	
}
