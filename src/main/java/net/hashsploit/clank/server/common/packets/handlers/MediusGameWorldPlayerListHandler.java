package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.Player;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
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
    public void read(MediusMessage mm) {
		reqPacket = new GameWorldPlayerListRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
    }
    
    @Override
    public MediusMessage write(MediusClient client) {

    	int worldIdRequested = Utils.bytesToIntLittle(reqPacket.getWorldID());
//    	
//    	for (Player player: client.getServer().getLogicHandler().getGameWorldPlayers(worldIdRequested)) {
//    		
//    	
//    	}
    	
    	byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue());
    	byte[] accountID = Utils.intToBytesLittle(Clank.getInstance().getDatabase().getAccountId("Smily"));
    	byte[] accountName = Utils.buildByteArrayFromString("Smily", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
    	byte[] stats = Utils.buildByteArrayFromString("", MediusConstants.ACCOUNTSTATS_MAXLEN.getValue());
    	byte[] connectionClass = Utils.intToBytesLittle(1);
    	byte[] endOfList = Utils.hexStringToByteArray("01000000");
    	
		respPacket = new GameWorldPlayerListResponse(reqPacket.getMessageID(), callbackStatus, accountID, accountName, stats, connectionClass, endOfList);
		
		
		
		
		
		return respPacket;
    }

}
