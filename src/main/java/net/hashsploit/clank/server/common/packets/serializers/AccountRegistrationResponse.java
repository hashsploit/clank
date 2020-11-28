package net.hashsploit.clank.server.common.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class AccountRegistrationResponse extends MediusMessage {
	
	private final byte[] messageId;

	public AccountRegistrationResponse(byte[] messageId) {
		super(MediusMessageType.AccountRegistrationResponse);
		
		this.messageId = messageId;
	}
	
	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			outputStream.write(messageId);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue()));
			outputStream.write(Utils.intToBytesLittle(1));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
	
}
