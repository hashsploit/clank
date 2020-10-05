package net.hashsploit.clank.server.medius.packets.handlers;

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
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class MediusUpdateLadderStatsWideHandler extends MediusPacketHandler {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] ladderType = new byte[4];
	private byte[] stats = new byte[MediusConstants.LADDERSTATSWIDE_MAXLEN.getValue()];
	
	public MediusUpdateLadderStatsWideHandler() {
		super(MediusPacketType.UpdateLadderStatsWide,MediusPacketType.UpdateLadderStatsWideResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		// Process the packet
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(ladderType);
		buf.get(stats);
	}

	@Override
	public MediusPacket write(MediusClient client) {
		byte[] statsResponse = Utils.buildByteArrayFromString("0000000", 7); // empty 7 byte array
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(statsResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new MediusPacket(responseType, outputStream.toByteArray());
	}

}
