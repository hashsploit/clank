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
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new GameInfoZeroRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {		
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));
		
		MediusLobbyServer server = (MediusLobbyServer) client.getServer();
		List<MediusMessage> response = new ArrayList<MediusMessage>();

		MediusGame game = server.getGame(Utils.bytesToIntLittle(reqPacket.getWorldID()));
		if (game == null) {
			byte[] appID = Utils.hexStringToByteArray("00000000");
			byte[] minPlayers = Utils.hexStringToByteArray("00000000");
			byte[] maxPlayers = Utils.hexStringToByteArray("00000000");
			byte[] gameLevel = Utils.hexStringToByteArray("00000000");
			byte[] gameName = new byte[MediusConstants.GAMENAME_MAXLEN.getValue()];
			byte[] playerCount = new byte[MediusConstants.GAMEPASSWORD_MAXLEN.getValue()];
			byte[] playerSkillLevel =Utils.hexStringToByteArray("00000000");
			byte[] rulesSet = Utils.hexStringToByteArray("00000000");
			byte[] genField1 = Utils.hexStringToByteArray("00000000");
			byte[] genField2 = Utils.hexStringToByteArray("00000000");
			byte[] genField3 = Utils.hexStringToByteArray("00000000");
			byte[] stats = Utils.hexStringToByteArray("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
			byte[] worldStatus = Utils.hexStringToByteArray("00000000");
			byte[] gameHostType = Utils.hexStringToByteArray("00000000");
			respPacket = new GameInfoZeroResponse(reqPacket.getMessageID(), Utils.intToBytesLittle((MediusCallbackStatus.NO_RESULT.getValue())), appID, minPlayers, maxPlayers, gameLevel, 
					playerSkillLevel, playerCount, stats, gameName, rulesSet, genField1, genField2, genField3,
					worldStatus, gameHostType);
			response.add(respPacket);
		}
		else { // Game exists
			CreateGameOneRequest req = game.getReqPacket();
			
			respPacket = new GameInfoZeroResponse(reqPacket.getMessageID(), callbackStatus, req.getAppID(), req.getMinPlayers(), req.getMaxPlayers(), req.getGameLevel(), 
					req.getPlayerSkillLevel(), game.getPlayerCount(), game.getStats(), req.getGameName(), req.getRulesSet(), req.getGenField1(), req.getGenField2(), req.getGenField3(),
					game.getWorldStatusBytes(), req.getGameHostType());
			response.add(respPacket);
		}
		
		return response;
	}

}
