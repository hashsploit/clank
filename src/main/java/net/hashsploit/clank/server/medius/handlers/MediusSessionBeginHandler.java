package net.hashsploit.clank.server.medius.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusAuthenticationServer;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusSessionBeginHandler extends MediusPacketHandler {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] connectionType = new byte[4];

	public MediusSessionBeginHandler() {
		super(MediusMessageType.SessionBegin, MediusMessageType.SessionBeginResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(3);// buffer
		buf.get(connectionType);

		logger.finest("ChannelInfo data read:");
		logger.finest("Message ID : " + Utils.bytesToHex(messageID) + " | Length: " + Integer.toString(messageID.length));
		logger.finest("Connection Type: " + Utils.bytesToHex(connectionType));
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));

		String generatedSessionKey = ((MediusAuthenticationServer) client.getServer()).generateSessionKey();
		logger.info("Generated session key for new player: " + generatedSessionKey);
		client.getPlayer().setSessionKey(generatedSessionKey);

		byte[] sessionKey = generatedSessionKey.getBytes();

		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
			outputStream.write(callbackStatus);
			outputStream.write(sessionKey);
			outputStream.write(Utils.hexStringToByteArray("000000"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(new MediusMessage(responseType, outputStream.toByteArray()));
		return response;
	}

}
