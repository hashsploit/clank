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

public class MediusChannelInfo extends MediusPacket {

	private static final Logger logger = Logger.getLogger("");

	
	public MediusChannelInfo() {
		super(MediusPacketType.ChannelInfo);
	}

	@Override
	public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) {

		ByteBuffer buf = ByteBuffer.wrap(packetData);

		byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
		byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
		byte[] buffer = new byte[2];
		byte[] worldId = new byte[4];

		buf.get(messageID);
		buf.get(sessionKey);
		buf.get(buffer);
		buf.get(worldId);

		// RESPONSE

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] callbackStatus = Utils.intToBytesLittle(0);

		try {
			outputStream.write(MediusPacketType.ChannelInfoResponse.getShortByte());
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
			outputStream.write(callbackStatus);

			// Lobby name
			//outputStream.write(Utils.buildByteArrayFromString("Aquatos v2", MediusConstants.LOBBYNAME_MAXLEN.getValue()));
			
			// Active players
			//outputStream.write(Utils.intToBytesLittle(3));
			
			// Max players
			//outputStream.write(Utils.intToBytesLittle(100));
			outputStream.write(Utils.hexStringToByteArray("435930303030303030332D30300000000000000000000000000000000000000020000000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000010100000000010001000000E0000000"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Combine RT id and len
		byte[] data = outputStream.toByteArray();
		logger.fine("LENGTH: " + Integer.toString(data.length));
		DataPacket packet = new DataPacket(RTPacketId.SERVER_APP, data);

		byte[] finalPayload = packet.toData().array();
		logger.fine("Final payload: " + Utils.bytesToHex(finalPayload));
		ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
		ctx.write(msg);
		ctx.flush();

	}

}
