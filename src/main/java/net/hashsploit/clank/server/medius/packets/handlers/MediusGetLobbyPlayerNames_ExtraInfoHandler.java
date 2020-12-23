package net.hashsploit.clank.server.medius.packets.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.Player;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.packets.serializers.GetLobbyPlayerNames_ExtraInfoRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GetLobbyPlayerNames_ExtraInfoResponse;
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
		
		MediusLobbyServer server = (MediusLobbyServer) client.getServer();
		logger.info(server.playersToString());
		
		List<MediusMessage> messagesToWrite = new ArrayList<MediusMessage>();
		List<Player> playersInWorld = server.getLobbyWorldPlayers(worldId);
		for (int i = 0; i < playersInWorld.size(); i++) {
			Player player = playersInWorld.get(i);
			
			logger.info("Player in the world: " + player.toString());
			byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue());
			byte[] accountID = Utils.intToBytesLittle(player.getAccountId());
			byte[] accountName = Utils.buildByteArrayFromString(player.getUsername(), MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
			byte[] playerStatus = Utils.intToBytesLittle(player.getStatus().getValue());
			byte[] gameWorldID = Utils.intToBytesLittle(player.getGameWorldId());
			byte[] lobbyName = Utils.buildByteArrayFromString(server.getChannelById(Utils.bytesToIntLittle(reqPacket.getLobbyWorldId())).getName(), MediusConstants.WORLDNAME_MAXLEN.getValue());
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
			byte[] lobbyName = Utils.buildByteArrayFromString(server.getChannelById(Utils.bytesToIntLittle(reqPacket.getLobbyWorldId())).getName(), MediusConstants.WORLDNAME_MAXLEN.getValue());
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
