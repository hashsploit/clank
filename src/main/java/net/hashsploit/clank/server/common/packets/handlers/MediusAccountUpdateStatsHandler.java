package net.hashsploit.clank.server.common.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RTMessageId;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.AccountUpdateStatsRequest;
import net.hashsploit.clank.server.common.packets.serializers.AccountUpdateStatsResponse;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusAccountUpdateStatsHandler extends MediusPacketHandler {
	
	private AccountUpdateStatsRequest reqPacket;
	private AccountUpdateStatsResponse respPacket;

	
	public MediusAccountUpdateStatsHandler() {
		super(MediusMessageType.AccountUpdateStats, MediusMessageType.AccountUpdateStatsResponse);
	}
	
	@Override 
	public void read(MediusMessage mm) {
		reqPacket = new AccountUpdateStatsRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public void write(MediusClient client) {
		byte[] callbackStatus = Utils.intToBytesLittle(0);

		respPacket = new AccountUpdateStatsResponse(reqPacket.getMessageID(), callbackStatus);
		
		client.sendMediusMessage(respPacket);
	}
	
}
