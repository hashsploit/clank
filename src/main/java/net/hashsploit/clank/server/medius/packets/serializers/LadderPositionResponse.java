package net.hashsploit.clank.server.medius.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class LadderPositionResponse extends MediusMessage {
	
	private final byte[] messageId;

	public LadderPositionResponse(byte[] messageId) {
		super(MediusMessageType.LadderPositionResponse);
		
		this.messageId = messageId;
	}
	
	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			outputStream.write(messageId);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(Utils.intToBytesLittle(1));
			outputStream.write(Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
	
}
