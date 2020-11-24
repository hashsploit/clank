package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class ClearGameListFilterZeroRequest extends MediusPacket {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	
	public ClearGameListFilterZeroRequest(byte[] data) {
		super(MediusPacketType.ClearGameListFilter0, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);

		buf.get(messageID);
	}
	
	public String toString() {
		return "ClearGameListFilterZeroRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageID);		
	}

	public synchronized byte[] getMessageID() {
		return messageID;
	}

	public synchronized void setMessageID(byte[] messageID) {
		this.messageID = messageID;
	}
	
	
}
