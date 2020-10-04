package net.hashsploit.clank.server.medius.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusSessionBeginHandler extends MediusPacket {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] connectionType = new byte[4];
	
	public MediusSessionBeginHandler() {
		super(MediusPacketType.SessionBegin, MediusPacketType.SessionBeginResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(3);// buffer
		buf.get(connectionType);
		
		logger.finest("ChannelInfo data read:");
		logger.finest("Message ID : " + Utils.bytesToHex(messageID) + " | Length: " + Integer.toString(messageID.length));
		logger.finest("Connection Type: " + Utils.bytesToHex(connectionType));
	}
	
	@Override
	public MediusMessage write(MediusClient client) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.MediusSuccess.getValue()));
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
	
		return new MediusMessage(responseType, outputStream.toByteArray());
	}


}
