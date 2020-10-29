package net.hashsploit.clank.server.medius.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class VersionServerResponse extends MediusPacket {

	private byte[] messageId;
	private String serverVersion;

	public VersionServerResponse(byte[] messageId, String serverVersion) {
		super(MediusPacketType.VersionServerResponse);

		this.messageId = messageId;
		this.serverVersion = serverVersion;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageId);
			outputStream.write(Utils.buildByteArrayFromString(serverVersion, MediusConstants.SERVERVERSION_MAXLEN.getValue()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
}
