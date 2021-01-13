package net.hashsploit.clank.server.medius.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class LadderList_ExtraInfoRequest extends MediusMessage {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] ladderStatIndex = new byte[4];
	private byte[] mediusSortOrder = new byte[4];
	private byte[] startPosition = new byte[4];
	private byte[] pageSize = new byte[4];
	
	public LadderList_ExtraInfoRequest(byte[] data) {
		super(MediusMessageType.LadderList_ExtraInfo, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageID);
		buf.get(new byte[3]);
		buf.get(ladderStatIndex);
		buf.get(mediusSortOrder);
		buf.get(startPosition);
		buf.get(pageSize);
	}
	
	public String toString() {
		return "LadderList_ExtraInfoRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"ladderStatIndex: " + Utils.bytesToHex(ladderStatIndex) + '\n' + 
				"mediusSortOrder: " + Utils.bytesToHex(mediusSortOrder) + '\n' + 
				"startPosition: " + Utils.bytesToHex(startPosition) + '\n' + 
				"pageSize: " + Utils.bytesToHex(pageSize);		
	}

	public byte[] getMessageId() {
		return messageID;
	}

	
}
