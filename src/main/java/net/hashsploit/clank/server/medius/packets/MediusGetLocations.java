package net.hashsploit.clank.server.medius.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.Client;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.utils.Utils;

public class MediusGetLocations extends MediusPacket {

	// WORKING!!!!

	private static final Logger logger = Logger.getLogger("");

	public MediusGetLocations() {
		super(MediusPacketType.GetLocations);
	}

	@Override
	public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) {
		// Process the packet
		logger.fine("Get my clans: " + Utils.bytesToHex(packetData));

		ByteBuffer buf = ByteBuffer.wrap(packetData);

		byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
		byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];

		buf.get(messageID);
		buf.get(sessionKey);

		byte[] locationID = Utils.intToBytesLittle(40);// random location
		byte[] locationName = Utils.buildByteArrayFromString("Aquatos", MediusConstants.LOCATIONNAME_MAXLEN.getValue());
		byte[] statusCode = Utils.intToBytesLittle(MediusCallbackStatus.MediusSuccess.getValue());
		byte[] endOfList = Utils.hexStringToByteArray("01000000");

		logger.fine("Message ID: " + Utils.bytesToHex(messageID));
		logger.fine("Padding: 000000");
		logger.fine("locationID: " + Utils.bytesToHex(locationID));
		logger.fine("locationName: " + Utils.bytesToHex(locationName));
		logger.fine("statusCode: " + Utils.bytesToHex(statusCode));
		logger.fine("endOfList: " + Utils.bytesToHex(endOfList));

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(MediusPacketType.GetLocationsResponse.getShortByte());
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

		// Combine RT id and len
		byte[] data = outputStream.toByteArray();
		logger.fine("Total length: " + Integer.toString(data.length));

		DataPacket packet = new DataPacket(RTPacketId.SERVER_APP, data);

		byte[] finalPayload = packet.toData().array();
		logger.fine("Final payload: " + Utils.bytesToHex(finalPayload));
		ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
		ctx.write(msg); // (1)
		ctx.flush(); // (2)
	}

}
