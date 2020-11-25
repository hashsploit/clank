package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusPacket;

public class GetBuddyInvitationsRequest extends MediusPacket {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	
	public GetBuddyInvitationsRequest(byte[] data) {
		super(MediusPacketType.GetBuddyInvitations, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageId);
	}
	
	public byte[] getMessageId() {
		return messageId;
	}

	public void setMessageId(byte[] messageId) {
		this.messageId = messageId;
	}
	
}

