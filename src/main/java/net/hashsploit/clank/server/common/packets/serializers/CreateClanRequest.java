package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class CreateClanRequest extends MediusMessage {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private int applicationId;
	private String clanName;

	public CreateClanRequest(byte[] data) {
		super(MediusMessageType.CreateClan, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.get(messageId);
		buf.get(sessionKey);
		applicationId = buf.getInt();
		
		final byte[] clanNameBytes = new byte[MediusConstants.CLANNAME_MAXLEN.getValue()];
		buf.get(clanNameBytes);
		
		clanName = Utils.bytesToStringClean(clanNameBytes);
	}

	public byte[] getMessageId() {
		return messageId;
	}

	public byte[] getSessionKey() {
		return sessionKey;
	}
	
	public int getApplicationId() {
		return applicationId;
	}
	
	public String getClanName() {
		return clanName;
	}

}
