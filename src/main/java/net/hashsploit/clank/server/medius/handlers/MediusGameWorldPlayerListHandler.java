package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.Player;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.GameWorldPlayerListRequest;
import net.hashsploit.clank.server.medius.serializers.GameWorldPlayerListResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGameWorldPlayerListHandler extends MediusPacketHandler {
	
	private GameWorldPlayerListRequest reqPacket;
	private GameWorldPlayerListResponse respPacket;

    public MediusGameWorldPlayerListHandler() {
        super(MediusMessageType.GameWorldPlayerList,MediusMessageType.GameWorldPlayerListResponse);
    }
    
    @Override
    public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new GameWorldPlayerListRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
    }
    
    @Override
    public List<MediusMessage> write(MediusClient client) {

    	int worldIdRequested = Utils.bytesToIntLittle(reqPacket.getWorldID());
    	
    	MediusLobbyServer server = (MediusLobbyServer) client.getServer();
    	
    	List<MediusMessage> messagesToWrite = new ArrayList<MediusMessage>();
		HashSet<Player> playersInWorld = server.getGameWorldPlayers(worldIdRequested);
		
		Iterator<Player> iterator = playersInWorld.iterator();
		
		for (int i = 0; i < playersInWorld.size(); i++) {
			Player player = iterator.next();
			
	    	byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue());
	    	byte[] accountID = Utils.intToBytesLittle(player.getAccountId());
	    	byte[] accountName = Utils.buildByteArrayFromString(player.getUsername(), MediusConstants.ACCOUNTNAME_MAXLEN.value);
	    	byte[] stats = Utils.buildByteArrayFromString("", MediusConstants.ACCOUNTSTATS_MAXLEN.value);
	    	byte[] connectionClass = Utils.intToBytesLittle(1);
			
			byte[] endOfList;
			if (i == playersInWorld.size()-1) {
				endOfList = Utils.hexStringToByteArray("01000000");
			}
			else {
				endOfList = Utils.hexStringToByteArray("00000000");
			}
			GameWorldPlayerListResponse response = new GameWorldPlayerListResponse(reqPacket.getMessageID(), callbackStatus, accountID, accountName, stats, connectionClass, endOfList);
			messagesToWrite.add(response);
		}
		
		if (messagesToWrite.size() == 0) {
			byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.NO_RESULT.getValue());
	    	byte[] accountID = Utils.intToBytesLittle(0);
	    	byte[] accountName = Utils.buildByteArrayFromString("", MediusConstants.ACCOUNTNAME_MAXLEN.value);
	    	byte[] stats = Utils.buildByteArrayFromString("", MediusConstants.ACCOUNTSTATS_MAXLEN.value);
	    	byte[] connectionClass = Utils.intToBytesLittle(0);
			byte[] endOfList = Utils.hexStringToByteArray("01000000");
			GameWorldPlayerListResponse response = new GameWorldPlayerListResponse(reqPacket.getMessageID(), callbackStatus, accountID, accountName, stats, connectionClass, endOfList);
			client.sendMediusMessage(response);
			messagesToWrite.add(response);
		}
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		
		for (MediusMessage m : messagesToWrite) {
			response.add(m);
		}
		
		response.add(respPacket);
		return response;
    }

}
