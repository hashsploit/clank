package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;

public class GetUniverseInformationRequest extends MediusMessage {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] infoType = new byte[4];
	private byte[] mediusCharacterEncodingType = new byte[4];
	private byte[] mediusLanguageType = new byte[4];

	public GetUniverseInformationRequest(byte[] data) {
		super(MediusMessageType.GetUniverseInformation, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageId);
		buf.get(new byte[3]);
		buf.get(infoType);
		buf.get(mediusCharacterEncodingType);
		buf.get(mediusLanguageType);
	}
	
	public byte[] getMessageId() {
		return messageId;
	}
	
	public byte[] getInfoType() {
		return infoType;
	}
	
	public byte[] getMediusCharacterEncodingType() {
		return mediusCharacterEncodingType;
	}
	
	public byte[] getMediusLanguageType() {
		return mediusLanguageType;
	}

}
