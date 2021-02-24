package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusConnectionType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.medius.serializers.LobbyWorldPlayerListRequest;
import net.hashsploit.clank.server.medius.serializers.LobbyWorldPlayerListResponse;

public class MediusLobbyWorldPlayerListHandler extends MediusPacketHandler {

	private LobbyWorldPlayerListRequest reqPacket;
	private LobbyWorldPlayerListResponse respPacket;
	
	public MediusLobbyWorldPlayerListHandler() {
		super(MediusMessageType.AccountLogin, MediusMessageType.AccountLoginResponse);
	}
	
	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new LobbyWorldPlayerListRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
		MediusPlayerStatus playerStatus = MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD;
		int accountId = 21;
		String accountName = "Aeq";
		byte[] stats = new byte[MediusConstants.ACCOUNTSTATS_MAXLEN.value];
		MediusConnectionType connectionType = MediusConnectionType.ETHERNET;
		boolean endOfList = true;
		
		
		respPacket = new LobbyWorldPlayerListResponse(reqPacket.getMessageId(), callbackStatus, playerStatus, accountId, accountName, stats, connectionType, endOfList);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}


}