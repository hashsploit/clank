package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.server.common.packets.serializers.GameWorldPlayerListRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameWorldPlayerListResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGameWorldPlayerListHandler extends MediusPacketHandler {
	
	private GameWorldPlayerListRequest reqPacket;
	private GameWorldPlayerListResponse respPacket;

    public MediusGameWorldPlayerListHandler() {
        super(MediusMessageType.GameWorldPlayerList,MediusMessageType.GameWorldPlayerListResponse);
    }
    
    @Override
    public void read(MediusPacket mm) {
		reqPacket = new GameWorldPlayerListRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
    }
    
    @Override
    public MediusPacket write(MediusClient client) {
    	byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue());
    	byte[] accountID = Utils.intToBytesLittle(5);
    	byte[] accountName = Utils.buildByteArrayFromString("Smily", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
    	byte[] stats = Utils.buildByteArrayFromString("", MediusConstants.ACCOUNTSTATS_MAXLEN.getValue());
    	byte[] connectionClass = Utils.intToBytesLittle(1);
    	byte[] endOfList = Utils.hexStringToByteArray("01000000");
    	
		respPacket = new GameWorldPlayerListResponse(reqPacket.getMessageID(), callbackStatus, accountID, accountName, stats, connectionClass, endOfList);
		
		return respPacket;
    }

}
