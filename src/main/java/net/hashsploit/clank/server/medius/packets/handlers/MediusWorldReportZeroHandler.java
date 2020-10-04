package net.hashsploit.clank.server.medius.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.server.medius.packets.serializers.WorldReportZeroRequest;
import net.hashsploit.clank.utils.Utils;

public class MediusWorldReportZeroHandler extends MediusPacket {
	
	private WorldReportZeroRequest reqPacket;	
	
	public MediusWorldReportZeroHandler() {
		super(MediusPacketType.WorldReport0, null);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new WorldReportZeroRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public MediusMessage write(MediusClient client) {
		return null;
	}

}
