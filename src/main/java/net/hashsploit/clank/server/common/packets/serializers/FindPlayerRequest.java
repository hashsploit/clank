package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class FindPlayerRequest extends MediusMessage {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] searchType = new byte[4];
	private byte[] accountId = new byte[4];
	private byte[] accountName = new byte[MediusConstants.PLAYERNAME_MAXLEN.getValue()];
	
	public FindPlayerRequest(byte[] data) {
		super(MediusMessageType.FindPlayer, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageID);
		buf.get(sessionKey);
		buf.get(new byte[2]);
		buf.get(searchType);
		buf.get(accountId);
		buf.get(accountName);
	}
	
	public String toString() {
		return "FindPlayerRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"sessionKey: " + Utils.bytesToHex(sessionKey) + '\n' + 
				"searchType: " + Utils.bytesToHex(searchType) + '\n' + 
				"accountId: " + Utils.bytesToHex(accountId) + '\n' + 
				"accountName: " + Utils.bytesToHex(accountName);
	}

	public byte[] getAccountId() {
		return accountId;
	}

	public byte[] getMessageID() {
		return messageID;
	}

	
}
