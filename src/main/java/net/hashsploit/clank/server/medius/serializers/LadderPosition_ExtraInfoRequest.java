package net.hashsploit.clank.server.medius.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class LadderPosition_ExtraInfoRequest extends MediusMessage {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] accountId = new byte[4];
	private byte[] ladderStatIndex = new byte[4];
    private byte[] sortOrder = new byte[4];
	
	public LadderPosition_ExtraInfoRequest(byte[] data) {
		super(MediusMessageType.LadderPosition_ExtraInfo, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageID);
		buf.get(new byte[3]);
		buf.get(accountId);
		buf.get(ladderStatIndex);
		buf.get(sortOrder);
	}
	
	public String toString() {
		return "LadderPosition_ExtraInfoRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"accountId: " + Utils.bytesToHex(accountId) + '\n' + 
				"ladderStatIndex: " + Utils.bytesToHex(ladderStatIndex) + '\n' + 
				"sortOrder: " + Utils.bytesToHex(sortOrder);
	}

	public byte[] getAccountId() {
		return accountId;
	}

	public byte[] getMessageID() {
		return messageID;
	}

	public byte[] getLadderStatIndex() {
		return ladderStatIndex;
    }
    
    public byte[] getSortOrder() {
        return sortOrder;
    }

	
}