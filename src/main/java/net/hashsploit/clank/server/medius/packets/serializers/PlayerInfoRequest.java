package net.hashsploit.clank.server.medius.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class PlayerInfoRequest extends MediusMessage {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] accountID = new byte[4];
	
	public PlayerInfoRequest(byte[] data) {
		super(MediusPacketType.PlayerInfo, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageID);
		buf.get(sessionKey);
		buf.getShort(); // buffer
		buf.get(accountID);
	}
	
	public String toString() {
		return "PlayerInfoRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"sessionKey: " + Utils.bytesToHex(sessionKey) + '\n' + 
				"accountID: " + Utils.bytesToHex(accountID);		
	}
	
	public synchronized byte[] getMessageID() {
		return messageID;
	}

	public synchronized void setMessageID(byte[] messageID) {
		this.messageID = messageID;
	}

	public synchronized byte[] getSessionKey() {
		return sessionKey;
	}

	public synchronized void setSessionKey(byte[] sessionKey) {
		this.sessionKey = sessionKey;
	}

	public synchronized byte[] getAccountID() {
		return accountID;
	}

	public synchronized void setAccountID(byte[] accountID) {
		this.accountID = accountID;
	}
	
}
