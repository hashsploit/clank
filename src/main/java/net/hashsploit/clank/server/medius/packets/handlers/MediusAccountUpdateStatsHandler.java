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
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.packets.serializers.AccountUpdateStatsRequest;
import net.hashsploit.clank.server.medius.packets.serializers.AccountUpdateStatsResponse;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusAccountUpdateStatsHandler extends MediusPacket {
	
	private AccountUpdateStatsRequest reqPacket;
	private AccountUpdateStatsResponse respPacket;

	
	public MediusAccountUpdateStatsHandler() {
		super(MediusPacketType.AccountUpdateStats, MediusPacketType.AccountUpdateStatsResponse);
	}
	
	@Override 
	public void read(MediusMessage mm) {
		reqPacket = new AccountUpdateStatsRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusMessage write(MediusClient client) {
		byte[] callbackStatus = Utils.intToBytesLittle(0);

		respPacket = new AccountUpdateStatsResponse(reqPacket.getMessageID(), callbackStatus);
		
		return respPacket;	}
	
}
