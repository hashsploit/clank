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
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.utils.Utils;

public class MediusGetBuddyList_ExtraInfo extends MediusPacket {

	private static final Logger logger = Logger.getLogger("");

	public MediusGetBuddyList_ExtraInfo() {
		super(MediusPacketType.GetBuddyList_ExtraInfo);
	}

	@Override
	public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) {
		// Process the packet

		ByteBuffer buf = ByteBuffer.wrap(packetData);

		byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];

		buf.get(messageID);

		byte[] announcementID = Utils.intToBytes(10);
		byte[] announcement = Utils.buildByteArrayFromString("Announcment TEST", MediusConstants.ANNOUNCEMENT_MAXLEN.getValue());
		byte endOfList = 0x49;

		byte[] testResp = Utils.hexStringToByteArray("000000000000000100000053696D4163636F756E7420310000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049");

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(MediusPacketType.GetBuddyList_ExtraInfoResponse.getShortByte());
			outputStream.write(messageID);
			outputStream.write(testResp);
			// outputStream.write(Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue()));
			// outputStream.write(accountID);
			// outputStream.write(accountNameBuddy);
			// outputStream.write(accountNameBuddy);
			// outputStream.write(endOfList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//    	byte[] locationID = Utils.intToBytes(10);// random location
//    	byte[] locationName = Utils.buildByteArrayFromString("Chicago", MediusConstants.LOCATIONNAME_MAXLEN.getValue());
//    	byte[] statusCode = Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue());
//    	byte[] endOfList = Utils.hexStringToByteArray("00");
//
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
//		try {
//			outputStream.write(MediusPacketType.GetLocationsResponse.getShortByte());
//			outputStream.write(messageID);
//			outputStream.write(locationID);			
//			outputStream.write(locationName);			
//			outputStream.write(statusCode);			
//			outputStream.write(endOfList);			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// Combine RT id and len
		byte[] data = outputStream.toByteArray();
		DataPacket packet = new DataPacket(RTPacketId.SERVER_APP, data);

		byte[] finalPayload = packet.toData().array();
		logger.fine("Final payload: " + Utils.bytesToHex(finalPayload));
		ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
		ctx.write(msg); // (1)
		ctx.flush(); // (2)
	}

}
