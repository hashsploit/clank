package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class GetAllAnnouncementsRequest extends MediusMessage {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	
	public GetAllAnnouncementsRequest(byte[] data) {
		super(MediusMessageType.GetAllAnnouncements, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageID);
		buf.get(sessionKey);
	}
	
	public String toString() {
		return "GetAllAnnouncementsRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"sessionKey: " + Utils.bytesToHex(sessionKey);
	}
	
	public synchronized byte[] getMessageId() {
		return messageID;
	}
	
	public synchronized void setMessageID(byte[] messageID) {
		this.messageID = messageID;
	}
	
	public synchronized byte[] getSessionKey() {
		return sessionKey;
	}
	
	public synchronized void setSessionKey(byte[] sessionKey) {
		this.sessionKey = sessionKey;
	}	
}
