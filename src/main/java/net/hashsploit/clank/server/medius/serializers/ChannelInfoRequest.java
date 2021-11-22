package net.hashsploit.clank.server.medius.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class ChannelInfoRequest extends MediusMessage {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.value];
	private byte[] worldId = new byte[4];
	
	public ChannelInfoRequest(byte[] data) {
		super(MediusMessageType.ChannelInfo, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageId);
		buf.get(sessionKey);
		buf.getShort(); // padding
		buf.get(worldId);
	}
	
	@Override
	public String toString() {
		return "ChannelInfoRequest{" + 
				"'messageId':" + Utils.bytesToHex(messageId) + 
				"'sessionKey':" + Utils.bytesToHex(sessionKey) + 
				"'worldId': " + Utils.bytesToHex(worldId);		
	}
	
	public byte[] getMessageId() {
		return messageId;
	}
	
	public synchronized void setMessageId(byte[] messageId) {
		this.messageId = messageId;
	}
	
	public synchronized byte[] getSessionKey() {
		return sessionKey;
	}
	
	public synchronized void setSessionKey(byte[] sessionKey) {
		this.sessionKey = sessionKey;
	}

	public byte[] getWorldId() {
		return worldId;
	}
	
	public synchronized void setWorldId(byte[] worldId) {
		this.worldId = worldId;
	}

	
}
