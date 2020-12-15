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
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.server.common.packets.serializers.PlayerInfoRequest;
import net.hashsploit.clank.server.common.packets.serializers.PlayerInfoResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusPlayerInfoHandler extends MediusPacketHandler {
	
	private PlayerInfoRequest reqPacket;
	private PlayerInfoResponse respPacket;
	
    public MediusPlayerInfoHandler() {
        super(MediusMessageType.PlayerInfo, MediusMessageType.PlayerInfoResponse);
    }
    
    @Override
    public void read(MediusMessage mm) {
		reqPacket = new PlayerInfoRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
    }
    
    @Override
    public void write(MediusClient client) { 
    	
    	byte[] callbackStatus = Utils.intToBytes(MediusCallbackStatus.SUCCESS.getValue());
    	byte[] accountName = Utils.buildByteArrayFromString("Smily", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
       	byte[] appID = Utils.hexStringToByteArray("bc290000");
       	byte[] playerStatus = Utils.intToBytesLittle(3);
       	byte[] connectionClass = Utils.intToBytesLittle(1);
       	byte[] stats = Utils.buildByteArrayFromString("", MediusConstants.ACCOUNTSTATS_MAXLEN.getValue());
       	
       	respPacket = new PlayerInfoResponse(reqPacket.getMessageID(), callbackStatus, appID, accountName, playerStatus, connectionClass, stats);

       	client.sendMediusMessage(respPacket);
    }

}
