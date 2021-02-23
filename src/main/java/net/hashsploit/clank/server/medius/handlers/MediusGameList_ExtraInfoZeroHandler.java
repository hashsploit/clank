package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.MediusGame;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusGameHostType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusWorldSecurityLevelType;
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.server.medius.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.medius.serializers.GameList_ExtraInfoZeroRequest;
import net.hashsploit.clank.server.medius.serializers.GameList_ExtraInfoZeroResponse;

public class MediusGameList_ExtraInfoZeroHandler extends MediusPacketHandler {

	private GameList_ExtraInfoZeroRequest reqPacket;
	private GameList_ExtraInfoZeroResponse respPacket;

	public MediusGameList_ExtraInfoZeroHandler() {
		super(MediusMessageType.GameList_ExtraInfo0, MediusMessageType.GameList_ExtraInfoResponse0);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new GameList_ExtraInfoZeroRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {

		MediusLobbyServer server = (MediusLobbyServer) client.getServer();
		List<MediusGame> games = server.getGames();
		List<MediusMessage> response = new ArrayList<MediusMessage>();

		final byte[] messageId = reqPacket.getMessageId();
		int mediusWorldId = 0;
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.NO_RESULT;
		short playerCount = 0;
		short minPlayers = 0;
		short maxPlayers = 0;
		
		// FIXME: hardcoded
		int gameLevel = client.getPlayer().getChatWorldId();
		int playerSkillLevel = 4;
		int rulesSet = 0;
		int genericField1 = 0;
		int genericField2 = 0;
		int genericField3 = 0;
		MediusWorldSecurityLevelType worldSecurityLevelType = MediusWorldSecurityLevelType.WORLD_SECURITY_NONE;
		MediusWorldStatus worldStatus = MediusWorldStatus.WORLD_CLOSED;
		MediusGameHostType gameHostType = MediusGameHostType.HOST_CLIENT_SERVER_AUX_UDP;
		String gameName = "";
		byte[] gameStats = new byte[MediusConstants.GAMESTATS_MAXLEN.value];
		
		boolean endOfList = true;
		
		if (games.size() > 0) {
			for (int i=0; i<games.size(); i++) {
				final MediusGame game = games.get(i);
				final CreateGameOneRequest gameRequested = game.getReqPacket();
				
				mediusWorldId = game.getWorldId();
				callbackStatus = MediusCallbackStatus.SUCCESS;
				playerCount = (short) game.getPlayerCount();
				minPlayers = (short) gameRequested.getMinPlayers();
				maxPlayers = (short) gameRequested.getMaxPlayers();
				gameLevel = (short) gameRequested.getGameLevel();
				
				endOfList = i == games.size() - 1;
				
				respPacket = new GameList_ExtraInfoZeroResponse(messageId, mediusWorldId, callbackStatus, playerCount, minPlayers, maxPlayers, gameLevel, playerSkillLevel, rulesSet, genericField1, genericField2, genericField3,
						worldSecurityLevelType, worldStatus, gameHostType, gameName, gameStats, endOfList);
				client.sendMediusMessage(respPacket);
			}
		} else {
			respPacket = new GameList_ExtraInfoZeroResponse(messageId, mediusWorldId, callbackStatus, playerCount, minPlayers, maxPlayers, gameLevel, playerSkillLevel, rulesSet, genericField1, genericField2, genericField3, worldSecurityLevelType, worldStatus, gameHostType, gameName, gameStats, endOfList);
			client.sendMediusMessage(respPacket);
		}
		

		response.add(respPacket);
		return response;

	}

}
