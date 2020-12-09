package net.hashsploit.clank.server.common.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.Player;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RTMessageId;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.objects.MediusPlayerOnlineState;
import net.hashsploit.clank.server.common.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.common.packets.serializers.GetLobbyPlayerNames_ExtraInfoRequest;
import net.hashsploit.clank.server.common.packets.serializers.GetLobbyPlayerNames_ExtraInfoResponse;
import net.hashsploit.clank.server.common.packets.serializers.LobbyWorldPlayerListRequest;
import net.hashsploit.clank.server.common.packets.serializers.LobbyWorldPlayerListResponse;
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
	public MediusMessage write(MediusClient client) {
		// RESPONSE

		int worldId = Utils.bytesToIntLittle(reqPacket.getLobbyWorldID());
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
			byte[] lobbyName = Utils.buildByteArrayFromString(client.getServer().getLogicHandler().getChannelLobbyName(Utils.bytesToIntLittle(reqPacket.getLobbyWorldID())), MediusConstants.WORLDNAME_MAXLEN.getValue());
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
					reqPacket.getLobbyWorldID(),
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
			byte[] lobbyName = Utils.buildByteArrayFromString(client.getServer().getLogicHandler().getChannelLobbyName(Utils.bytesToIntLittle(reqPacket.getLobbyWorldID())), MediusConstants.WORLDNAME_MAXLEN.getValue());
			byte[] gameName = Utils.buildByteArrayFromString("", MediusConstants.WORLDNAME_MAXLEN.getValue());
			byte[] endOfList = Utils.hexStringToByteArray("01000000");
			GetLobbyPlayerNames_ExtraInfoResponse response = new GetLobbyPlayerNames_ExtraInfoResponse(
					reqPacket.getMessageID(), 
					callbackStatus,
					accountID,
					accountName,
					playerStatus,
					reqPacket.getLobbyWorldID(),
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
//			byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.NO_RESULT.getValue());
//			byte[] accountID = Utils.intToBytesLittle(0);
//			byte[] accountName = Utils.buildByteArrayFromString("", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
//			byte[] connectState = Utils.intToBytesLittle(0);
//			byte[] gameWorldID = Utils.intToBytesLittle(0);
//			byte[] lobbyName = Utils.buildByteArrayFromString("CY00000000-00", MediusConstants.WORLDNAME_MAXLEN.getValue());
//			byte[] gameName = Utils.buildByteArrayFromString("", MediusConstants.WORLDNAME_MAXLEN.getValue());
//			byte[] endOfList = Utils.hexStringToByteArray("01000000");
//	
//			logger.finest("Message ID: " + Utils.bytesToHex(reqPacket.getMessageID()));
//			logger.finest("Account ID: " + Utils.bytesToHex(accountID));
//			logger.finest("AccountName: " + Utils.bytesToHex(accountName));
//			logger.finest("ConnectState: " + Utils.bytesToHex(connectState));
//			logger.finest("lobbyWorldID: " + Utils.bytesToHex(reqPacket.getLobbyWorldID()));
//			logger.finest("gameWorldID: " + Utils.bytesToHex(gameWorldID));
//			logger.finest("lobbyName: " + Utils.bytesToHex(lobbyName));
//			logger.finest("gameName: " + Utils.bytesToHex(gameName));
//			logger.finest("endOfList: " + Utils.bytesToHex(endOfList));
//			
//			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			try {
//				outputStream.write(reqPacket.getMessageID());
//				outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
//				outputStream.write(callbackStatus);
//				outputStream.write(accountID);
//				outputStream.write(accountName);
//				outputStream.write(connectState);
//				outputStream.write(reqPacket.getLobbyWorldID());
//				outputStream.write(gameWorldID);
//				outputStream.write(lobbyName);
//				outputStream.write(gameName);
//				outputStream.write(endOfList);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	
//			client.sendMediusMessage(new MediusMessage(responseType, outputStream.toByteArray()));

		
		
		
		
		return null;
		
//		byte[] callbackStatus = Utils.intToBytesLittle(0);
//		byte[] accountID = Utils.intToBytesLittle(101);
//		byte[] accountName = Utils.buildByteArrayFromString("Smily", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
//		MediusPlayerOnlineState onlineState = new MediusPlayerOnlineState(MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD, 0, 0, "Aquatos v2", "Aquatos v2");
//		byte[] connectState = Utils.intToBytesLittle(onlineState.getConnectionStatus().getValue());
//		byte[] gameWorldID = Utils.intToBytesLittle(0);
//		byte[] lobbyName = Utils.buildByteArrayFromString("CY00000000-00", MediusConstants.WORLDNAME_MAXLEN.getValue());
//		byte[] gameName = Utils.buildByteArrayFromString("", MediusConstants.WORLDNAME_MAXLEN.getValue());
//		byte[] endOfList = Utils.hexStringToByteArray("01000000");
//
//		logger.finest("Message ID: " + Utils.bytesToHex(messageID));
//		logger.finest("Account ID: " + Utils.bytesToHex(accountID));
//		logger.finest("AccountName: " + Utils.bytesToHex(accountName));
//		logger.finest("ConnectState: " + Utils.bytesToHex(connectState));
//		logger.finest("lobbyWorldID: " + Utils.bytesToHex(lobbyWorldID));
//		logger.finest("gameWorldID: " + Utils.bytesToHex(gameWorldID));
//		logger.finest("lobbyName: " + Utils.bytesToHex(lobbyName));
//		logger.finest("gameName: " + Utils.bytesToHex(gameName));
//		logger.finest("endOfList: " + Utils.bytesToHex(endOfList));
//		
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		try {
//			outputStream.write(messageID);
//			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
//			outputStream.write(callbackStatus);
//			outputStream.write(accountID);
//			outputStream.write(accountName);
//			outputStream.write(connectState);
//			outputStream.write(lobbyWorldID);
//			outputStream.write(gameWorldID);
//			outputStream.write(lobbyName);
//			outputStream.write(gameName);
//			outputStream.write(endOfList);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return new MediusMessage(responseType, outputStream.toByteArray());
	}
	


}
