package net.hashsploit.clank.server.medius.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusPolicyType;

public class PolicyRequest extends MediusMessage {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.value];
	private MediusPolicyType policyType;
	
	public PolicyRequest(byte[] data) {
		super(MediusMessageType.Policy, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageId);
		buf.get(sessionKey);
		buf.getShort(); // padding
		policyType = MediusPolicyType.getTypeFromValue(buf.getInt());
	}
	
	public byte[] getMessageId() {
		return messageId;
	}
	
	public synchronized void setMessageId(byte[] messageId) {
		this.messageId = messageId;
	}
	
	public byte[] getSessionKey() {
		return sessionKey;
	}
	
	public synchronized void setSessionKey(byte[] sessionKey) {
		this.sessionKey = sessionKey;
	}
	
	public MediusPolicyType getPolicyType() {
		return policyType;
	}
	
	public synchronized void setPolicyType(MediusPolicyType policyType) {
		this.policyType = policyType;
	}
	
}
