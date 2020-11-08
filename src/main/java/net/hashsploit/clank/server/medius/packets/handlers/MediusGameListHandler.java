package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.GameHostType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.server.medius.packets.serializers.GameListRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameListResponse;

public class MediusGameListHandler extends MediusPacketHandler {

	private GameListRequest reqPacket;
	private GameListResponse respPacket;
	
	public MediusGameListHandler() {
		super(MediusPacketType.AccountLogin, MediusPacketType.AccountLoginResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new GameListRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.MediusSuccess;
		int mediusWorldId = 1;
		String gameName = "AEQLITUDE";
		MediusWorldStatus worldStatus = MediusWorldStatus.WORLD_STAGING;
		GameHostType gameHostType = GameHostType.HOST_CLIENT_SERVER_AUX_UDP;
		int playerCount = 3;
		boolean endOfList = true;
		
		respPacket = new GameListResponse(reqPacket.getMessageId(), callbackStatus, mediusWorldId, gameName, worldStatus, gameHostType, playerCount, endOfList);
		
		return respPacket;
	}

}
