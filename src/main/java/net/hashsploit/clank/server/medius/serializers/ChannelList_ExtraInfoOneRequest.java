package net.hashsploit.clank.server.medius.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class ChannelList_ExtraInfoOneRequest extends MediusMessage {

<<<<<<< HEAD:src/main/java/net/hashsploit/clank/server/medius/serializers/ChannelList_ExtraInfoOneRequest.java
	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] pageID = new byte[2];
=======
	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] pageId = new byte[2];
>>>>>>> d6454359959fa8c3a5b8310f8d01ffbcf1d9a367:src/main/java/net/hashsploit/clank/server/common/packets/serializers/ChannelList_ExtraInfoOneRequest.java
	private byte[] pageSize = new byte[2];
	
	public ChannelList_ExtraInfoOneRequest(byte[] data) {
		super(MediusMessageType.ChannelList_ExtraInfo1, data);
		
    	ByteBuffer buf = ByteBuffer.wrap(data);
    	buf.get(messageId);
    	buf.get(pageId);
    	buf.get(pageSize);
	}
	
	public String toString() {
		return "ChannelList_ExtraInfoOneRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageId) + '\n' + 
				"pageID: " + Utils.bytesToHex(pageId) + '\n' + 
				"pageSize: " + Utils.bytesToHex(pageSize);		
	}

	public byte[] getMessageId() {
		return messageId;
	}

	public byte[] getPageId() {
		return pageId;
	}

	public byte[] getPageSize() {
		return pageSize;
	}

	
}
