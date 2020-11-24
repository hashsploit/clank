package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.GameHostType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.server.common.objects.MediusWorldStatus;
import net.hashsploit.clank.server.common.packets.serializers.GameListRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameListResponse;

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
