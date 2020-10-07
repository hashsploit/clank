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
import net.hashsploit.clank.server.medius.packets.serializers.ChannelList_ExtraInfoOneRequest;
import net.hashsploit.clank.server.medius.packets.serializers.ChannelList_ExtraInfoOneResponse;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusChannelList_ExtraInfoOneHandler extends MediusPacketHandler {
	
	private ChannelList_ExtraInfoOneRequest reqPacket;
	private ChannelList_ExtraInfoOneResponse respPacket;
	
    public MediusChannelList_ExtraInfoOneHandler() {
        super(MediusPacketType.ChannelList_ExtraInfo1, MediusPacketType.ChannelList_ExtraInfoResponse);
    }
    
    @Override
    public void read(MediusPacket mm) {
		reqPacket = new ChannelList_ExtraInfoOneRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
    }
    
    @Override
    public MediusPacket write(MediusClient client) {
    	byte[] callbackStatus = Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue());
    	byte[] mediusWorldID = Utils.hexStringToByteArray("45070000");
    	byte[] playerCount = Utils.shortToBytesLittle((short) 1);
    	byte[] maxPlayers = Utils.shortToBytesLittle((short) 224);
    	byte[] worldSecurityLevelType = Utils.intToBytesLittle(0);
    	byte[] genericField1 = Utils.intToBytesLittle(1);
    	byte[] genericField2 = Utils.intToBytesLittle(1);
    	byte[] genericField3 = Utils.intToBytesLittle(0);
    	byte[] genericField4 = Utils.intToBytesLittle(0);
    	byte[] genericFieldFilter = Utils.intToBytesLittle(32);
    	byte[] lobbyName = Utils.buildByteArrayFromString("CY00000000-00", MediusConstants.LOBBYNAME_MAXLEN.getValue());		
    	byte[] endOfList = Utils.hexStringToByteArray("01000000");
    	
		respPacket = new ChannelList_ExtraInfoOneResponse(reqPacket.getMessageID(), callbackStatus, mediusWorldID, playerCount, maxPlayers, worldSecurityLevelType, 
				genericField1, genericField2, genericField3, genericField4, genericFieldFilter, lobbyName, endOfList);
		
		return respPacket;    
	}
}
