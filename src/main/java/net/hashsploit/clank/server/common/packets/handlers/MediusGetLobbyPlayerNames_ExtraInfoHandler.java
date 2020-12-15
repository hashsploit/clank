package net.hashsploit.clank.server.common.packets.handlers;

import java.util.ArrayList;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.Player;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.GetLobbyPlayerNames_ExtraInfoRequest;
import net.hashsploit.clank.server.common.packets.serializers.GetLobbyPlayerNames_ExtraInfoResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGetLobbyPlayerNames_ExtraInfoHandler extends MediusPacketHandler {

	private GetLobbyPlayerNames_ExtraInfoRequest reqPacket;
	private GetLobbyPlayerNames_ExtraInfoResponse respPacket;
	
	public MediusGetLobbyPlayerNames_ExtraInfoHandler() {
		super(MediusMessageType.GetLobbyPlayerNames_ExtraInfo, MediusMessageType.GetLobbyPlayerNames_ExtraInfoResponse);
	}
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new GetLobbyPlayerNames_ExtraInfoRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public void write(MediusClient client) {
		// RESPONSE

		int worldId = Utils.bytesToIntLittle(reqPacket.getLobbyWorldId());
		logger.info("Searching for players in worldid: " + Integer.toString(worldId));
		
		logger.info(client.getServer().getLogicHandler().playersToString());
		
		ArrayList<MediusMessage> messagesToWrite = new ArrayList<MediusMessage>();
		ArrayList<Player> playersInWorld = client.getServer().getLogicHandler().getLobbyWorldPlayers(worldId);
		for (int i = 0; i < playersInWorld.size(); i++) {
			Player player = playersInWorld.get(i);
			
			logger.info("Player in the world: " + player.toString());
			byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue());
			byte[] accountID = Utils.intToBytesLittle(player.getAccountId());
			byte[] accountName = Utils.buildByteArrayFromString(player.getUsername(), MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
			byte[] playerStatus = Utils.intToBytesLittle(player.getStatus().getValue());
			byte[] gameWorldID = Utils.intToBytesLittle(player.getGameWorldId());
			byte[] lobbyName = Utils.buildByteArrayFromString(client.getServer().getLogicHandler().getChannelById(Utils.bytesToIntLittle(reqPacket.getLobbyWorldId())).getName(), MediusConstants.WORLDNAME_MAXLEN.getValue());
			byte[] gameName = Utils.buildByteArrayFromString("", MediusConstants.WORLDNAME_MAXLEN.getValue());
			
			byte[] endOfList;
			if (i == playersInWorld.size()-1) {
				endOfList = Utils.hexStringToByteArray("01000000");
			}
			else {
				endOfList = Utils.hexStringToByteArray("00000000");
			}
			GetLobbyPlayerNames_ExtraInfoResponse response = new GetLobbyPlayerNames_ExtraInfoResponse(
					reqPacket.getMessageID(), 
					callbackStatus,
					accountID,
					accountName,
					playerStatus,
					reqPacket.getLobbyWorldId(),
					gameWorldID,
					lobbyName,
					gameName,
					endOfList
					);
			messagesToWrite.add(response);
		}
		
		if (messagesToWrite.size() == 0) {
			logger.info("MediusGetLobbyPlayerNames_ExtraInfoHandler: NO players found!");
			byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.NO_RESULT.getValue());
			byte[] accountID = Utils.intToBytesLittle(0);
			byte[] accountName = Utils.buildByteArrayFromString("", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
			byte[] playerStatus = Utils.intToBytesLittle(0);
			byte[] gameWorldID = Utils.intToBytesLittle(0);
			byte[] lobbyName = Utils.buildByteArrayFromString(client.getServer().getLogicHandler().getChannelById(Utils.bytesToIntLittle(reqPacket.getLobbyWorldId())).getName(), MediusConstants.WORLDNAME_MAXLEN.getValue());
			byte[] gameName = Utils.buildByteArrayFromString("", MediusConstants.WORLDNAME_MAXLEN.getValue());
			byte[] endOfList = Utils.hexStringToByteArray("01000000");
			GetLobbyPlayerNames_ExtraInfoResponse response = new GetLobbyPlayerNames_ExtraInfoResponse(
					reqPacket.getMessageID(), 
					callbackStatus,
					accountID,
					accountName,
					playerStatus,
					reqPacket.getLobbyWorldId(),
					gameWorldID,
					lobbyName,
					gameName,
					endOfList
					);
			client.sendMediusMessage(response);
		}
		else {
			for (MediusMessage m: messagesToWrite) {
				client.sendMediusMessage(m);
			}
		}
	}
	


}
