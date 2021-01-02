package net.hashsploit.clank.server.common.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class CreateClanResponse extends MediusMessage {

	private byte[] messageId;
	private MediusCallbackStatus callbackStatus;
	private int clanId;

	public CreateClanResponse(byte[] messageId, MediusCallbackStatus callbackStatus, int clanId) {
		super(MediusMessageType.CreateClanResponse);

		this.messageId = messageId;
		this.callbackStatus = callbackStatus;
		this.clanId = clanId;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageId);
			outputStream.write(new byte[3]); // padding
			outputStream.write(Utils.intToBytesLittle(callbackStatus.getValue()));
			outputStream.write(Utils.intToBytesLittle(clanId));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}

}
