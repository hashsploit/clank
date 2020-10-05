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
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.server.medius.packets.serializers.GameWorldPlayerListRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameWorldPlayerListResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGameWorldPlayerListHandler extends MediusPacketHandler {
	
	private GameWorldPlayerListRequest reqPacket;
	private GameWorldPlayerListResponse respPacket;

    public MediusGameWorldPlayerListHandler() {
        super(MediusPacketType.GameWorldPlayerList,MediusPacketType.GameWorldPlayerListResponse);
    }
    
    @Override
    public void read(MediusPacket mm) {
		reqPacket = new GameWorldPlayerListRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
    }
    
    @Override
    public MediusPacket write(MediusClient client) {
    	byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.MediusSuccess.getValue());
    	byte[] accountID = Utils.intToBytesLittle(5);
    	byte[] accountName = Utils.buildByteArrayFromString("Smily", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
    	byte[] stats = Utils.buildByteArrayFromString("", MediusConstants.ACCOUNTSTATS_MAXLEN.getValue());
    	byte[] connectionClass = Utils.intToBytesLittle(1);
    	byte[] endOfList = Utils.hexStringToByteArray("01000000");
    	
		respPacket = new GameWorldPlayerListResponse(reqPacket.getMessageID(), callbackStatus, accountID, accountName, stats, connectionClass, endOfList);
		
		return respPacket;
    }

}
