package net.hashsploit.clank.server.medius.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusGetLadderStatsWideHandler extends MediusPacketHandler {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] accountOrClanID = new byte[4]; // int
	
	public MediusGetLadderStatsWideHandler() {
		super(MediusMessageType.GetLadderStatsWide,MediusMessageType.GetLadderStatsWideResponse);
	}

	@Override
	public void read(MediusMessage mm) {
		// Process the packet
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(new byte[3]);
		buf.get(accountOrClanID);
		logger.fine("Message ID : " + Utils.bytesToHex(messageID));
		logger.fine("Account or Clan ID: " + Utils.bytesToHex(accountOrClanID));
	}
	
	@Override
	public void write(MediusClient client) {

		byte[] stats = Utils.buildByteArrayFromString("", MediusConstants.LADDERSTATSWIDE_MAXLEN.getValue());

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(Utils.intToBytes(MediusCallbackStatus.SUCCESS.getValue()));
			outputStream.write(accountOrClanID);
			outputStream.write(stats);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		client.sendMediusMessage(new MediusMessage(responseType, outputStream.toByteArray()));
	}

}
