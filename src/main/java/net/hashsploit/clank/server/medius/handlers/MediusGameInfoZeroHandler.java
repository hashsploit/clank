package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.MediusGame;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.medius.serializers.GameInfoResponseZero;
import net.hashsploit.clank.server.medius.serializers.GameInfoZeroRequest;

public class MediusGameInfoZeroHandler extends MediusPacketHandler {

	private GameInfoZeroRequest reqPacket;
	private GameInfoResponseZero respPacket;

	public MediusGameInfoZeroHandler() {
		super(MediusMessageType.GameInfo0, MediusMessageType.GameInfoResponse0);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new GameInfoZeroRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;

		MediusLobbyServer server = (MediusLobbyServer) client.getServer();
		List<MediusMessage> response = new ArrayList<MediusMessage>();

		MediusGame game = server.getGame(reqPacket.getWorldId());
		CreateGameOneRequest req = game.getReqPacket();

		respPacket = new GameInfoResponseZero(reqPacket.getMessageID(), callbackStatus, req.getAppId(), req.getMinPlayers(), req.getMaxPlayers(), req.getGameLevel(), req.getPlayerSkillLevel(), game.getPlayerCount(), game.getStats(),
				req.getGameName(), req.getRulesSet(), req.getGenField1(), req.getGenField2(), req.getGenField3(), game.getWorldStatus(), req.getGameHostType());
		
		response.add(respPacket);
		
		return response;
	}

}
