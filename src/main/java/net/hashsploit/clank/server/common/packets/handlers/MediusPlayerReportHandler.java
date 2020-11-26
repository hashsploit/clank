package net.hashsploit.clank.server.common.packets.handlers;

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
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.server.common.packets.serializers.PlayerReportRequest;
import net.hashsploit.clank.utils.Utils;

public class MediusPlayerReportHandler extends MediusPacketHandler {
	
	private PlayerReportRequest reqPacket;
	
    public MediusPlayerReportHandler() {
        super(MediusMessageType.PlayerReport, null);
    }
    
    @Override
    public void read(MediusPacket mm) {
		reqPacket = new PlayerReportRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
    }
    
    @Override
    public MediusPacket write(MediusClient client) { 
    	return null;
    }

}
