package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class ChatToggleRequest extends MediusMessage {
	
	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.value];

	
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
