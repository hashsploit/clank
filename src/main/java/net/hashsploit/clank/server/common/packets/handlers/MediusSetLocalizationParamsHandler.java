package net.hashsploit.clank.server.common.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class MediusSetLocalizationParamsHandler extends MediusPacketHandler {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] characterEncoding = new byte[4];
	private byte[] language = new byte[4];
	
	public MediusSetLocalizationParamsHandler() {
		super(MediusPacketType.SetLocalizationParams, MediusPacketType.SetLocalizationParamsResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(sessionKey);// buffer
		buf.get(new byte[2]);
		buf.get(characterEncoding);
		buf.get(language);

		logger.finest("Message ID : " + Utils.bytesToHex(messageID) + " | Length: " + Integer.toString(messageID.length));
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			outputStream.write(Utils.buildByteArrayFromString("", 28));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return new MediusPacket(responseType, outputStream.toByteArray());
	}


}
