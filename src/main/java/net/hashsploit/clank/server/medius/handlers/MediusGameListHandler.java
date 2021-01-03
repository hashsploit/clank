package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.GameHostType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.server.medius.serializers.GameListRequest;
import net.hashsploit.clank.server.medius.serializers.GameListResponse;

public class MediusGameListHandler extends MediusPacketHandler {

	private GameListRequest reqPacket;
	private GameListResponse respPacket;
	
	public MediusGameListHandler() {
		super(MediusMessageType.AccountLogin, MediusMessageType.AccountLoginResponse);
	}
	
	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new GameListRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
		int mediusWorldId = 1;
		String gameName = "AEQLITUDE";
		MediusWorldStatus worldStatus = MediusWorldStatus.WORLD_STAGING;
		GameHostType gameHostType = GameHostType.HOST_CLIENT_SERVER_AUX_UDP;
		int playerCount = 3;
		boolean endOfList = true;
		
		respPacket = new GameListResponse(reqPacket.getMessageId(), callbackStatus, mediusWorldId, gameName, worldStatus, gameHostType, playerCount, endOfList);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}

}
