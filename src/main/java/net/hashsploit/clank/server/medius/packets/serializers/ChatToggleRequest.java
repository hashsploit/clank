package net.hashsploit.clank.server.medius.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class ChatToggleRequest extends MediusMessage {
	
	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];

	
	public ChatToggleRequest(byte[] data) {
		super(MediusMessageType.ChatToggle, data);
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageID);
	}
	
	public String toString() {
		return "ChatToggleRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"rawData: " + Utils.bytesToHex(getPayload());		
	}

	public synchronized byte[] getMessageID() {
		return messageID;
	}

	public synchronized void setMessageID(byte[] messageID) {
		this.messageID = messageID;
	}
	
}
