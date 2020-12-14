package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class GetWorldSecurityLevelRequest extends MediusMessage {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] callbackStatus = new byte[4];
	private byte[] worldId = new byte[4];
	private byte[] applicationType = new byte[4];
	
	public GetWorldSecurityLevelRequest(byte[] data) {
		super(MediusMessageType.GetWorldSecurityLevel, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageID);
		buf.get(new byte[3]);
		buf.get(callbackStatus);
		buf.get(worldId);
		buf.get(applicationType);
	}
	
	public String toString() {
		return "GetWorldSecurityLevelRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus) + '\n' + 
				"worldId: " + Utils.bytesToHex(worldId) + '\n' + 
				"applicationType: " + Utils.bytesToHex(applicationType);
	}

	public synchronized byte[] getWorldId() {
		return worldId;
	}

	public synchronized void setWorldId(byte[] worldId) {
		this.worldId = worldId;
	}

	public synchronized byte[] getMessageID() {
		return messageID;
	}

	public synchronized void setMessageID(byte[] messageID) {
		this.messageID = messageID;
	}

	public synchronized byte[] getCallbackStatus() {
		return callbackStatus;
	}

	public synchronized void setCallbackStatus(byte[] callbackStatus) {
		this.callbackStatus = callbackStatus;
	}

	public synchronized byte[] getApplicationType() {
		return applicationType;
	}

	public synchronized void setApplicationType(byte[] applicationType) {
		this.applicationType = applicationType;
	}
	

	
}
