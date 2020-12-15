package net.hashsploit.clank.server.common.packets.handlers;

import java.util.ArrayList;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.MediusGame;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.objects.GameHostType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.objects.MediusWorldStatus;
import net.hashsploit.clank.server.common.packets.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameList_ExtraInfoZeroRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameList_ExtraInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGameList_ExtraInfoZeroHandler extends MediusPacketHandler {
	
	private GameList_ExtraInfoZeroRequest reqPacket;
	private GameList_ExtraInfoZeroResponse respPacket;
	
	public MediusGameList_ExtraInfoZeroHandler() {
		super(MediusMessageType.GameList_ExtraInfo0,MediusMessageType.GameList_ExtraInfoResponse0);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new GameList_ExtraInfoZeroRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public void write(MediusClient client) {

		
		ArrayList<MediusGame> games = client.getServer().getLogicHandler().getGames();
		
//		byte[] mediusWorldID = Utils.hexStringToByteArray("65eb0000");
		byte[] callbackStatus;
		if (games.size() == 0) {
			callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.NO_RESULT.getValue());
			
			byte[] playerCount = Utils.shortToBytesLittle((short) 1);
			byte[] minPlayers = Utils.shortToBytesLittle((short) 0);
			byte[] maxPlayers = Utils.shortToBytesLittle((short) 8);
			
			// FIXME: bad
			byte[] gameLevel = Utils.intToBytesLittle(client.getServer().getLogicHandler().getChannel().getId()); // channel id
			
			byte[] playerSkillLevel = Utils.hexStringToByteArray("00000004");
			byte[] rulesSet = Utils.intToBytesLittle(0);
			byte[] genericField1 = Utils.intToBytesLittle(28);// Generic field 1 (Location Id (city id; aquatos))
			byte[] genericField2 = Utils.intToBytesLittle(0);
			byte[] genericField3 = Utils.hexStringToByteArray("0800e411");
			byte[] worldStatus = Utils.intToBytesLittle(MediusWorldStatus.WORLD_STAGING.getValue());
			byte[] gameHostType = Utils.intToBytesLittle(GameHostType.HOST_CLIENT_SERVER_AUX_UDP.getValue());
			//byte[] gameName = Utils.hexStringToByteArray("31763120662e6f20646f782020202020202030303030303032383030303000000000000000000000000000000000000000000000000000000000000000000000");
			byte[] gameName = Utils.buildByteArrayFromString("1v1 f.o dox       000000280000",MediusConstants.GAMENAME_MAXLEN.getValue());
			byte[] gameStats = Utils.buildByteArrayFromString("", MediusConstants.GAMESTATS_MAXLEN.getValue());
			respPacket = new GameList_ExtraInfoZeroResponse(reqPacket.getMessageID(), Utils.intToBytes(0), callbackStatus, playerCount, 
					minPlayers, maxPlayers,
					gameLevel, playerSkillLevel, rulesSet, genericField1, genericField2, genericField3, worldStatus, Utils.intToBytes(0),
					gameHostType, gameName, gameStats, Utils.hexStringToByteArray("01000000"));
			client.sendMediusMessage(respPacket);
			
		}
		
		else {
			
			for (int i = 0; i < games.size(); i++) {
				callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue());
				MediusGame game = games.get(i);
				CreateGameOneRequest req = game.getReqPacket();
				
				short playerCount = (short) Utils.bytesToIntLittle(game.getPlayerCount());
				short minPlayers = (short) Utils.bytesToIntLittle(req.getMinPlayers());
				short maxPlayers = (short) Utils.bytesToIntLittle(req.getMaxPlayers());		
				byte[] worldSecurityLevelType = Utils.hexStringToByteArray("00000000");
				
				byte[] endOfList;
				if (i == games.size()-1) {
					endOfList = Utils.hexStringToByteArray("01000000");
				}
				else {
					endOfList = Utils.hexStringToByteArray("00000000");
				}
	
				respPacket = new GameList_ExtraInfoZeroResponse(reqPacket.getMessageID(), game.getWorldId(), callbackStatus, Utils.shortToBytesLittle(playerCount), 
						Utils.shortToBytesLittle(minPlayers), Utils.shortToBytesLittle(maxPlayers),
						req.getGameLevel(), req.getPlayerSkillLevel(), req.getRulesSet(), req.getGenField1(), req.getGenField2(), req.getGenField3(), worldSecurityLevelType, game.getWorldStatusBytes(),
						req.getGameHostType(), req.getGameName(), game.getStats(), endOfList);
				client.sendMediusMessage(respPacket);
			}
		}

		
	}

}
