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
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusLobbyServer;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.common.packets.serializers.CreateGameResponse;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusCreateGameOneHandler extends MediusPacketHandler {
	
	private CreateGameOneRequest reqPacket;
	private CreateGameResponse respPacket;
	
    public MediusCreateGameOneHandler() {
        super(MediusMessageType.CreateGame1,MediusMessageType.CreateGameResponse);
    }
    
    @Override
    public void read(MediusMessage mm) {
		reqPacket = new CreateGameOneRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
    }
    
    @Override
    public void write(MediusClient client) {
		MediusLobbyServer server = (MediusLobbyServer) client.getServer();

    	byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue());
    	byte[] newWorldID = Utils.intToBytesLittle(server.getNewGameId(reqPacket));

    	respPacket = new CreateGameResponse(reqPacket.getMessageID(), callbackStatus, newWorldID);
		client.sendMediusMessage(respPacket);
    }

}
