package net.hashsploit.clank.server.medius.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class GameList_ExtraInfoZeroRequest extends MediusPacket {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] pageID = new byte[2];
	private byte[] pageSize = new byte[2];
	
	public GameList_ExtraInfoZeroRequest(byte[] data) {
		super(MediusPacketType.GameList_ExtraInfo0, data);
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageID);
		buf.get(pageID);
		buf.get(pageSize);
	}
	
	public String toString() {
		return "GameList_ExtraInfoZeroRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"pageID: " + Utils.bytesToHex(pageID) + '\n' + 
				"pageSize: " + Utils.bytesToHex(pageSize);		
	}

	public synchronized byte[] getMessageID() {
		return messageID;
	}

	public synchronized void setMessageID(byte[] messageID) {
		this.messageID = messageID;
	}

	public synchronized byte[] getPageID() {
		return pageID;
	}

	public synchronized void setPageID(byte[] pageID) {
		this.pageID = pageID;
	}

	public synchronized byte[] getPageSize() {
		return pageSize;
	}

	public synchronized void setPageSize(byte[] pageSize) {
		this.pageSize = pageSize;
	}
	

	
}
