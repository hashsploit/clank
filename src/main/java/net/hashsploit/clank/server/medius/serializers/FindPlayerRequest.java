package net.hashsploit.clank.server.medius.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class FindPlayerRequest extends MediusMessage {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.value];
	private byte[] searchType = new byte[4];
	private byte[] accountId = new byte[4];
	private byte[] accountName = new byte[MediusConstants.PLAYERNAME_MAXLEN.value];
	
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

	public byte[] getAccountName() {
		return accountName;
	}

	
}
