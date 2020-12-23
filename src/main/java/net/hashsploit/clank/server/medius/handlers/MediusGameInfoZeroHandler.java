package net.hashsploit.clank.server.medius.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.MediusGame;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.medius.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGameInfoZeroHandler extends MediusPacketHandler {

	private GameInfoZeroRequest reqPacket;
	private GameInfoZeroResponse respPacket;
	
	public MediusGameInfoZeroHandler() {
		super(MediusMessageType.GameInfo0, MediusMessageType.GameInfoResponse0);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new GameInfoZeroRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public void write(MediusClient client) {		
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));
		
		MediusLobbyServer server = (MediusLobbyServer) client.getServer();

		MediusGame game = server.getGame(Utils.bytesToIntLittle(reqPacket.getWorldID()));
		CreateGameOneRequest req = game.getReqPacket();
		
		respPacket = new GameInfoZeroResponse(reqPacket.getMessageID(), callbackStatus, req.getAppID(), req.getMinPlayers(), req.getMaxPlayers(), req.getGameLevel(), 
				req.getPlayerSkillLevel(), game.getPlayerCount(), game.getStats(), req.getGameName(), req.getRulesSet(), req.getGenField1(), req.getGenField2(), req.getGenField3(),
				game.getWorldStatusBytes(), req.getGameHostType());
		
		client.sendMediusMessage(respPacket);
	}

}
