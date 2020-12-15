package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class AccountGetIDRequest extends MediusMessage {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] accountName = new byte[MediusConstants.ACCOUNTNAME_MAXLEN.getValue()];
	
	public AccountGetIDRequest(byte[] data) {
		super(MediusMessageType.AccountGetID, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageID);
		buf.get(sessionKey);
		buf.get(accountName);
	}
	
	public String toString() {
		return "AccountGetIDRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"sessionKey: " + Utils.bytesToHex(sessionKey) + '\n' + 
				"usernameBytes: " + Utils.bytesToHex(accountName);
	}

	public byte[] getMessageID() {
		return messageID;
	}
	


	
}
