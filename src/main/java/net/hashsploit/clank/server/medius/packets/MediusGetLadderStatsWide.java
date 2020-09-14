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

public class MediusGetLadderStatsWide extends MediusPacket {

	private static final Logger logger = Logger.getLogger("");

	public MediusGetLadderStatsWide() {
		super(MediusPacketType.GetLadderStatsWide);
	}

	@Override
	public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) {
		// Process the packet
		ByteBuffer buf = ByteBuffer.wrap(packetData);

		// byte[] finalPayload =
		// Utils.hexStringToByteArray("0a1e00019731000000000000000000000000000000000000000000000000000000");
		byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
		byte[] accountOrClanID = new byte[4]; // int

		buf.get(messageID);
		buf.get(accountOrClanID);

		logger.fine("Message ID : " + Utils.bytesToHex(messageID));
		logger.fine("Account or Clan ID: " + Utils.bytesToHex(accountOrClanID));

		byte[] stats = Utils.buildByteArrayFromString("0", MediusConstants.LADDERSTATSWIDE_MAXLEN.getValue());

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(MediusPacketType.GetLadderStatsWideResponse.getShortByte());
			outputStream.write(messageID);
			outputStream.write(Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue()));
			outputStream.write(accountOrClanID);
			outputStream.write(stats);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Combine RT id and len
		byte[] data = outputStream.toByteArray();
		logger.fine("Data: " + Utils.bytesToHex(data));
		DataPacket packet = new DataPacket(RTPacketId.SERVER_APP, data);

		byte[] finalPayload = packet.toData().array();
		logger.fine("Final payload: " + Utils.bytesToHex(finalPayload));
		ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
		ctx.write(msg); // (1)
		ctx.flush(); // (2)
	}

}
