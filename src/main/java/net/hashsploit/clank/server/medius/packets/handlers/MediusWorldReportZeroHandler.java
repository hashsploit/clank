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
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.server.medius.packets.serializers.WorldReportZeroRequest;
import net.hashsploit.clank.utils.Utils;

public class MediusWorldReportZeroHandler extends MediusPacketHandler {
	
	private WorldReportZeroRequest reqPacket;	
	
	public MediusWorldReportZeroHandler() {
		super(MediusMessageType.WorldReport0, null);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new WorldReportZeroRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public void write(MediusClient client) {
		// Update the logichandler with the world
		MediusLobbyServer server = (MediusLobbyServer) client.getServer();
		
		int worldId = Utils.bytesToIntLittle(reqPacket.getWorldID());
		int worldStatus = Utils.bytesToIntLittle(reqPacket.getWorldStatus());
		if (worldStatus == 2) {
			server.updateDmeWorldStatus(worldId, MediusWorldStatus.WORLD_ACTIVE);
		}
		
	}

}
