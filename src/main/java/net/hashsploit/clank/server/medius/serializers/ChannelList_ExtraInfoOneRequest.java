package net.hashsploit.clank.server.medius.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class ChannelList_ExtraInfoOneRequest extends MediusMessage {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] pageID = new byte[2];
	private byte[] pageSize = new byte[2];
	
	public ChannelList_ExtraInfoOneRequest(byte[] data) {
		super(MediusMessageType.ChannelList_ExtraInfo1, data);
		
    	ByteBuffer buf = ByteBuffer.wrap(data);
    	buf.get(messageID);
    	buf.get(pageID);
    	buf.get(pageSize);
	}
	
	public String toString() {
		return "ChannelList_ExtraInfoOneRequest: \n" + 
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
