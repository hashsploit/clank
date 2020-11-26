package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class ClearGameListFilterZeroRequest extends MediusMessage {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	
	public ClearGameListFilterZeroRequest(byte[] data) {
		super(MediusMessageType.ClearGameListFilter0, data);
		
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
