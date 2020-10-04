package net.hashsploit.clank.server.medius.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class PlayerReportRequest extends MediusMessage {

	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] mediusWorldID = new byte[4];
	private byte[] stats = new byte[MediusConstants.ACCOUNTSTATS_MAXLEN.getValue()];
	

	public PlayerReportRequest(byte[] data) {
		super(MediusPacketType.PlayerReport, data);
    	// Process the packet
    	ByteBuffer buf = ByteBuffer.wrap(data);
    	
    	buf.get(sessionKey);
    	buf.get(new byte[3]); // buffer
    	buf.get(mediusWorldID);
    	buf.get(stats);

	}
	
	public String toString() {
		return "PlayerReportRequest: \n" + 
				"sessionKey: " + Utils.bytesToHex(sessionKey) + '\n' + 
				"mediusWorldID: " + Utils.bytesToHex(mediusWorldID) + '\n' + 
				"stats: " + Utils.bytesToHex(stats);		
	}

	public synchronized byte[] getSessionKey() {
		return sessionKey;
	}

	public synchronized void setSessionKey(byte[] sessionKey) {
		this.sessionKey = sessionKey;
	}

	public synchronized byte[] getMediusWorldID() {
		return mediusWorldID;
	}

	public synchronized void setMediusWorldID(byte[] mediusWorldID) {
		this.mediusWorldID = mediusWorldID;
	}

	public synchronized byte[] getStats() {
		return stats;
	}

	public synchronized void setStats(byte[] stats) {
		this.stats = stats;
	}

	
}
