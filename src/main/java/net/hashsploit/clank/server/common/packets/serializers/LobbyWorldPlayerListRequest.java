package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusPacket;

public class LobbyWorldPlayerListRequest extends MediusPacket {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private int worldId;

	public LobbyWorldPlayerListRequest(byte[] data) {
		super(MediusMessageType.LobbyWorldPlayerList, data);

		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageId);
		buf.get(sessionKey);
		buf.get(new byte[2]);
		buf.get(worldId);
	}

	public byte[] getMessageId() {
		return messageId;
	}

	public void setMessageId(byte[] messageId) {
		this.messageId = messageId;
	}

	public byte[] getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(byte[] sessionKey) {
		this.sessionKey = sessionKey;
	}

	public void setWorldId(int worldId) {
		this.worldId = worldId;
	}

	public int getWorldId() {
		return worldId;
	}

}
