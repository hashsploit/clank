package net.hashsploit.clank.server.common.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class MediusGetLocationsHandler extends MediusPacketHandler {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
		
	public MediusGetLocationsHandler() {
		super(MediusMessageType.GetLocations,MediusMessageType.GetLocationsResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		// Process the packet
		logger.fine("Get locations: " + Utils.bytesToHex(mm.getPayload()));

		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(sessionKey);
	}

	@Override
	public MediusPacket write(MediusClient client) {

		byte[] locationID = Utils.intToBytesLittle(40);// random location
		byte[] locationName = Utils.buildByteArrayFromString("Aquatos", MediusConstants.LOCATIONNAME_MAXLEN.getValue());
		byte[] statusCode = Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue());
		byte[] endOfList = Utils.hexStringToByteArray("01000000");

		logger.fine("Message ID: " + Utils.bytesToHex(messageID));
		logger.fine("Padding: 000000");
		logger.fine("locationID: " + Utils.bytesToHex(locationID));
		logger.fine("locationName: " + Utils.bytesToHex(locationName));
		logger.fine("statusCode: " + Utils.bytesToHex(statusCode));
		logger.fine("endOfList: " + Utils.bytesToHex(endOfList));

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(locationID);
			outputStream.write(locationName);
			outputStream.write(statusCode);
			outputStream.write(endOfList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new MediusPacket(responseType, outputStream.toByteArray());
	}

}
