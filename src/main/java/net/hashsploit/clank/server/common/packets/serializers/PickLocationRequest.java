package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusPacket;

public class PickLocationRequest extends MediusPacket {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private int locationId;
	
	public PickLocationRequest(byte[] data) {
		super(MediusMessageType.PickLocation, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageId);
		buf.get(sessionKey);
		buf.get(new byte[2]); // padding
		buf.get(locationId);
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
	
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	
	public int getLocationId() {
		return locationId;
	}
	
}

