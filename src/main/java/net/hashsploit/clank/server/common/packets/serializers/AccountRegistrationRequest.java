package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusAccountType;
import net.hashsploit.clank.server.common.objects.MediusPacket;

public class AccountRegistrationRequest extends MediusPacket {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private MediusAccountType accountType;
	private String username;
	private String password;
	
	public AccountRegistrationRequest(byte[] data) {
		super(MediusMessageType.AccountRegistration, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageID);
		buf.get(sessionKey);
		
		// FIXME: ADD REGISTRATION SHIT
		// accountType
		// accountName (username)
		// password
		
		// TODO: Username most likely is 32-bytes (14+18).
		//buf.get(usernameBytes);
		//buf.get(new byte[18]);
		// TODO: Password is also most likely 32-bytes, probably has 18-bytes trailing.
		//buf.get(passwordBytes);
	}
	
	public byte[] getMessageID() {
		return messageID;
	}

	public synchronized void setMessageID(byte[] messageID) {
		this.messageID = messageID;
	}

	public byte[] getSessionKey() {
		return sessionKey;
	}

	public synchronized void setSessionKey(byte[] sessionKey) {
		this.sessionKey = sessionKey;
	}

}
