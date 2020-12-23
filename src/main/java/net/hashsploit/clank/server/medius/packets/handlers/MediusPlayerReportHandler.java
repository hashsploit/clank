package net.hashsploit.clank.server.medius.packets.handlers;

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
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.server.medius.packets.serializers.PlayerReportRequest;
import net.hashsploit.clank.utils.Utils;

public class MediusPlayerReportHandler extends MediusPacketHandler {
	
	private PlayerReportRequest reqPacket;
	
    public MediusPlayerReportHandler() {
        super(MediusMessageType.PlayerReport, null);
    }
    
    @Override
    public void read(MediusMessage mm) {
		reqPacket = new PlayerReportRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
    }
    
    @Override
    public void write(MediusClient client) { 
    }

}
