package net.hashsploit.clank.server.medius.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class GetMyIPResponse extends MediusMessage {

	private byte[] messageId;
	private String ipAddress;
	private MediusCallbackStatus callbackStatus;

	public GetMyIPResponse(byte[] messageId, String ipAddress, int port, MediusCallbackStatus callbackStatus) {
		super(MediusMessageType.GetMyIPResponse);

		this.messageId = messageId;
		this.ipAddress = ipAddress;
		this.callbackStatus = callbackStatus;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageId);
			outputStream.write(Utils.buildByteArrayFromString(ipAddress, MediusConstants.IP_MAXLEN.value));
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
			outputStream.write(Utils.intToBytesLittle(callbackStatus.getValue()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
}
