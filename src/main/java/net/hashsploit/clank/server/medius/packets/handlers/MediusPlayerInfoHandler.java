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
import net.hashsploit.clank.server.medius.packets.serializers.PlayerInfoRequest;
import net.hashsploit.clank.server.medius.packets.serializers.PlayerInfoResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusPlayerInfoHandler extends MediusPacketHandler {
	
	private PlayerInfoRequest reqPacket;
	private PlayerInfoResponse respPacket;
	
    public MediusPlayerInfoHandler() {
        super(MediusPacketType.PlayerInfo, MediusPacketType.PlayerInfoResponse);
    }
    
    @Override
    public void read(MediusPacket mm) {
		reqPacket = new PlayerInfoRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
    }
    
    @Override
    public MediusPacket write(MediusClient client) { 
    	
    	byte[] callbackStatus = Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue());
    	byte[] accountName = Utils.buildByteArrayFromString("Account Name", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
       	byte[] appID = Utils.intToBytesLittle(1);
       	byte[] playerStatus = Utils.intToBytesLittle(3);
       	byte[] connectionClass = Utils.intToBytesLittle(1);
       	byte[] stats = Utils.buildByteArrayFromString("", MediusConstants.ACCOUNTSTATS_MAXLEN.getValue());
       	
       	respPacket = new PlayerInfoResponse(reqPacket.getMessageID(), callbackStatus, appID, accountName, playerStatus, connectionClass, stats);

       	return respPacket;
    }

}
