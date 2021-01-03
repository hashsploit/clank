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
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.AccountUpdateStatsRequest;
import net.hashsploit.clank.server.medius.serializers.AccountUpdateStatsResponse;
import net.hashsploit.clank.server.medius.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusAccountUpdateStatsHandler extends MediusPacketHandler {
	
	private AccountUpdateStatsRequest reqPacket;
	private AccountUpdateStatsResponse respPacket;

	
	public MediusAccountUpdateStatsHandler() {
		super(MediusMessageType.AccountUpdateStats, MediusMessageType.AccountUpdateStatsResponse);
	}
	
	@Override 
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new AccountUpdateStatsRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {
		byte[] callbackStatus = Utils.intToBytesLittle(0);

		respPacket = new AccountUpdateStatsResponse(reqPacket.getMessageID(), callbackStatus);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}
	
}
