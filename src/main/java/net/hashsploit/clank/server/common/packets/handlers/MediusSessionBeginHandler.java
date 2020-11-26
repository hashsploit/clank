package net.hashsploit.clank.server.common.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class MediusSessionBeginHandler extends MediusPacketHandler {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] connectionType = new byte[4];
	
	public MediusSessionBeginHandler() {
		super(MediusMessageType.SessionBegin, MediusMessageType.SessionBeginResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(3);// buffer
		buf.get(connectionType);
		
		logger.finest("ChannelInfo data read:");
		logger.finest("Message ID : " + Utils.bytesToHex(messageID) + " | Length: " + Integer.toString(messageID.length));
		logger.finest("Connection Type: " + Utils.bytesToHex(connectionType));
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));
    	byte[] sessionKey = Utils.buildByteArrayFromString("123456789", MediusConstants.SESSIONKEY_MAXLEN.getValue());
    	
				
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
	
		return new MediusPacket(responseType, outputStream.toByteArray());
	}


}
