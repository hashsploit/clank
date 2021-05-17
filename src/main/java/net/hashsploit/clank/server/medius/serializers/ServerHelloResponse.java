package net.hashsploit.clank.server.medius.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;


public class ServerHelloResponse extends MediusMessage {

	private byte[] messageId;
	private String serverHello;

	public ServerHelloResponse(byte[] messageId, String serverHello) {
		super(MediusMessageType.MediusServerHelloResponse);

		this.messageId = messageId;
		this.serverHello = serverHello;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageId);
			outputStream.write(Utils.buildByteArrayFromString(serverHello, 56));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
}