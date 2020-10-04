package net.hashsploit.clank.server.medius.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class AccountUpdateStatsRequest extends MediusMessage {
	
	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] stats = new byte[MediusConstants.ACCOUNTSTATS_MAXLEN.getValue()];
	
	public AccountUpdateStatsRequest(byte[] data) {
		super(MediusPacketType.AccountUpdateStats, data);
		// Process the packet
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageID);
		buf.get(sessionKey);
		buf.get(stats);
	}
	
	public String toString() {
		return "AccountUpdateStatsRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"sessionKey: " + Utils.bytesToHex(sessionKey) + '\n' + 
				"stats: " + Utils.bytesToHex(stats);
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

	public synchronized byte[] getStats() {
		return stats;
	}

	public synchronized void setStats(byte[] stats) {
		this.stats = stats;
	}

	
}
