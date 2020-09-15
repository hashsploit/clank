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

public class MediusClearGameListFilterZero extends MediusPacket {

	private static final Logger logger = Logger.getLogger("");
	
	private byte[] responsePacketType = MediusPacketType.ClearGameListFilterResponse.getShortByte();

	private byte[] messageID;
	private byte[] filterID;
	
	public MediusClearGameListFilterZero() {
		super(MediusPacketType.ClearGameListFilter0);
	}
	
	@Override
	public void read(byte[] packetData) {
		ByteBuffer buf = ByteBuffer.wrap(packetData);

		messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
		byte[] buffer = new byte[3];
		filterID = new byte[4];

		buf.get(messageID);
		buf.get(buffer);
		buf.get(filterID);
		
		logger.finest("ClearGameListFilter0 data read:");
		logger.finest("Message ID  : " + Utils.bytesToHex(messageID) + " | Length: " + Integer.toString(messageID.length));
		logger.finest("filterID : " + Utils.bytesToHex(filterID) + " | Length: " + Integer.toString(filterID.length));
	}
	
	@Override
	public void write(Client client, ChannelHandlerContext ctx) {
		byte[] callbackStatus = Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue());

		logger.finest("Writing ClearGameListFilter0 OUT:");
		logger.finest("CallbackStatus : " + Utils.bytesToHex(callbackStatus) + " | Length: " + Integer.toString(callbackStatus.length));
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(responsePacketType);
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
			outputStream.write(callbackStatus);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Combine RT id and len
		byte[] data = outputStream.toByteArray();
		logger.finest("Final Payload Length: " + Integer.toString(data.length));
		DataPacket packet = new DataPacket(RTPacketId.SERVER_APP, data);

		byte[] finalPayload = packet.toData().array();
		logger.finest("Final payload: " + Utils.bytesToHex(finalPayload));
		ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
		ctx.write(msg); // (1)
		ctx.flush(); // (2)
	}

	@Override
	public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) {
		read(packetData);
		write(client, ctx);
	}

}
