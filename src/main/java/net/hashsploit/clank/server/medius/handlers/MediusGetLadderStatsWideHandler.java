package net.hashsploit.clank.server.medius.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.Clank;
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

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] accountOrClanId = new byte[4]; // int
	
	public MediusGetLadderStatsWideHandler() {
		super(MediusMessageType.GetLadderStatsWide,MediusMessageType.GetLadderStatsWideResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		// Process the packet
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageId);
		buf.get(new byte[3]);
		buf.get(accountOrClanId);
		logger.fine("Message ID : " + Utils.bytesToHex(messageId));
		logger.fine("Account or Clan ID: " + Utils.bytesToHex(accountOrClanId));
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {
		byte[] stats = Utils.hexStringToByteArray(Clank.getInstance().getDatabase().getPlayerLadderStatsWide(client.getPlayer().getAccountId()));
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageId);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(Utils.intToBytes(MediusCallbackStatus.SUCCESS.getValue()));
			outputStream.write(accountOrClanId);
			outputStream.write(stats);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(new MediusMessage(responseType, outputStream.toByteArray()));
		return response;
	}

}
