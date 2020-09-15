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

public class MediusSetGameListFilterZero extends MediusPacket {

	private static final Logger logger = Logger.getLogger("");

	public MediusSetGameListFilterZero() {
		super(MediusPacketType.SetGameListFilter0);
	}

	@Override
	public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) {
		// Process the packet

		ByteBuffer buf = ByteBuffer.wrap(packetData);

		byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
		byte[] filterField = new byte[4];
		byte[] mask = new byte[4];
		byte[] comparisonOperator = new byte[4];
		byte[] baselineValue = new byte[4];

		buf.get(messageID);
		buf.get(filterField);
		buf.get(mask);
		buf.get(comparisonOperator);
		buf.get(baselineValue);

		byte[] filterID = Utils.intToBytesLittle(154);
		logger.fine(Utils.bytesToHex(messageID));
		logger.fine(Utils.bytesToHex(Utils.hexStringToByteArray("000000")));
		logger.fine(Utils.bytesToHex(Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue())));
		logger.fine(Utils.bytesToHex(filterID));

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(MediusPacketType.SetGameListFilterResponse0.getShortByte());
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue()));
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
