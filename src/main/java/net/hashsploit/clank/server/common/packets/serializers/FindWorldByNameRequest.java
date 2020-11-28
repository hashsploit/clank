package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;

public class FindWorldByNameRequest extends MediusMessage {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] name = new byte[MediusConstants.WORLDNAME_MAXLEN.getValue()];
	private int worldType;
	
	public FindWorldByNameRequest(byte[] data) {
		super(MediusMessageType.FindWorldByName, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageId);
		buf.get(sessionKey);
		buf.get(name);
		buf.get(new byte[2]);
		
		// TODO: change to FindWorldType object
		buf.get(worldType);
	}
	
	public byte[] getMessageId() {
		return messageId;
	}

	public void setMessageId(byte[] messageId) {
		this.messageId = messageId;
	}

	public byte[] getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(byte[] sessionKey) {
		this.sessionKey = sessionKey;
	}
	
	public void setName(byte[] name) {
		this.name = name;
	}
	
	public byte[] getName() {
		return name;
	}
	
	public void setWorldType(int worldType) {
		this.worldType = worldType;
	}
	
	public int getWorldType() {
		return worldType;
	}
	
}
