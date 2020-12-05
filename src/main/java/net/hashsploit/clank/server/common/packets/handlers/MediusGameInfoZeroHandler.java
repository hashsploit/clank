package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusGame;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroResponse;
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
	public MediusMessage write(MediusClient client) {		
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));

		MediusGame game = client.getServer().getLogicHandler().getGame(Utils.bytesToIntLittle(reqPacket.getWorldID()));
		CreateGameOneRequest req = game.getReqPacket();
		
		respPacket = new GameInfoZeroResponse(reqPacket.getMessageID(), callbackStatus, req.getAppID(), req.getMinPlayers(), req.getMaxPlayers(), req.getGameLevel(), 
				req.getPlayerSkillLevel(), game.getPlayerCount(), game.getStats(), req.getGameName(), req.getRulesSet(), req.getGenField1(), req.getGenField2(), req.getGenField3(),
				game.getWorldStatus(), req.getGameHostType());
		
		return respPacket;
	}

}
