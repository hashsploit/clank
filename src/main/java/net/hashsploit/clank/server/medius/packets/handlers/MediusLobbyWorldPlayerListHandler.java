package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusConnectionType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.medius.packets.serializers.LobbyWorldPlayerListRequest;
import net.hashsploit.clank.server.medius.packets.serializers.LobbyWorldPlayerListResponse;

public class MediusLobbyWorldPlayerListHandler extends MediusPacketHandler {

	private LobbyWorldPlayerListRequest reqPacket;
	private LobbyWorldPlayerListResponse respPacket;
	
	public MediusLobbyWorldPlayerListHandler() {
		super(MediusPacketType.AccountLogin, MediusPacketType.AccountLoginResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new LobbyWorldPlayerListRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.MediusSuccess;
		MediusPlayerStatus playerStatus = MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD;
		int accountId = 21;
		String accountName = "Aeq";
		byte[] stats = new byte[MediusConstants.ACCOUNTSTATS_MAXLEN.getValue()];
		MediusConnectionType connectionType = MediusConnectionType.ETHERNET;
		boolean endOfList = true;
		
		
		respPacket = new LobbyWorldPlayerListResponse(reqPacket.getMessageId(), callbackStatus, playerStatus, accountId, accountName, stats, connectionType, endOfList);
		
		return respPacket;
	}


}