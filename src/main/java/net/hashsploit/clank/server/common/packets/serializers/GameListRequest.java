package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusPacket;

public class GameListRequest extends MediusPacket {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private int applicationId;
	private short pageId;
	private short pageSize;
	
	public GameListRequest(byte[] data) {
		super(MediusMessageType.GameList, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageId);
		buf.get(sessionKey);
		buf.get(new byte[2]);
		buf.get(applicationId);
		buf.get(pageId);
		buf.get(pageSize);
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
	
	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}
	
	public int getApplicationId() {
		return applicationId;
	}
	
	public void setPageId(short pageId) {
		this.pageId = pageId;
	}
	
	public int getPageId() {
		return pageId;
	}
	
}
