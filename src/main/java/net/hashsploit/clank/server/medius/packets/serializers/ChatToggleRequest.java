package net.hashsploit.clank.server.medius.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class ChatToggleRequest extends MediusPacket {
	
	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];

	
	public ChatToggleRequest(byte[] data) {
		super(MediusPacketType.ChatToggle, data);
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
