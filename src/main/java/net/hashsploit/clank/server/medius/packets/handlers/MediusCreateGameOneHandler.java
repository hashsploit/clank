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
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.packets.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.medius.packets.serializers.CreateGameResponse;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusCreateGameOneHandler extends MediusPacket {
	
	private CreateGameOneRequest reqPacket;
	private CreateGameResponse respPacket;
	
    public MediusCreateGameOneHandler() {
        super(MediusPacketType.CreateGame1,MediusPacketType.CreateGameResponse);
    }
    
    @Override
    public void read(MediusMessage mm) {
		reqPacket = new CreateGameOneRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
    }
    
    @Override
    public MediusMessage write(MediusClient client) {
    	byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.MediusSuccess.getValue());
    	byte[] newWorldID = Utils.intToBytesLittle(123);

    	respPacket = new CreateGameResponse(reqPacket.getMessageID(), callbackStatus, newWorldID);
    	
    	return respPacket;
    }

}
