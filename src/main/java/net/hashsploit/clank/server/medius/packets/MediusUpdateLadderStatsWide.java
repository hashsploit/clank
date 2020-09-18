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
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusUpdateLadderStatsWide extends MediusPacket {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] ladderType = new byte[4];
	private byte[] stats = new byte[MediusConstants.LADDERSTATSWIDE_MAXLEN.getValue()];
	
	public MediusUpdateLadderStatsWide() {
		super(MediusPacketType.UpdateLadderStatsWide,MediusPacketType.UpdateLadderStatsWideResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		// Process the packet
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(ladderType);
		buf.get(stats);
	}

	@Override
	public MediusMessage write(Client client) {
		byte[] statsResponse = Utils.buildByteArrayFromString("0000000", 7); // empty 7 byte array
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(statsResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new MediusMessage(responseType, outputStream.toByteArray());
	}

}
