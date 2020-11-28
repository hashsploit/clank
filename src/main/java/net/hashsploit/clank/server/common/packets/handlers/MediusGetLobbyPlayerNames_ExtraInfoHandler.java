package net.hashsploit.clank.server.common.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RTMessageId;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.objects.MediusPlayerOnlineState;
import net.hashsploit.clank.server.common.objects.MediusPlayerStatus;
import net.hashsploit.clank.utils.Utils;

public class MediusGetLobbyPlayerNames_ExtraInfoHandler extends MediusPacketHandler {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];;
	private byte[] lobbyWorldID = new byte[4];
	
	
	public MediusGetLobbyPlayerNames_ExtraInfoHandler() {
		super(MediusMessageType.GetLobbyPlayerNames_ExtraInfo, MediusMessageType.GetLobbyPlayerNames_ExtraInfoResponse);
	}
	@Override
	public void read(MediusMessage mm) {
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());

		buf.get(messageID);
		buf.get(new byte[3]);
		buf.get(lobbyWorldID);		
	}
	
	@Override
	public MediusMessage write(MediusClient client) {
		// RESPONSE
		
		byte[] callbackStatus = Utils.intToBytesLittle(0);
		byte[] accountID = Utils.intToBytesLittle(101);
		byte[] accountName = Utils.buildByteArrayFromString("Smily", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
		MediusPlayerOnlineState onlineState = new MediusPlayerOnlineState(MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD, 0, 0, "Aquatos v2", "Aquatos v2");
		byte[] connectState = Utils.intToBytesLittle(onlineState.getConnectionStatus().getValue());
		byte[] gameWorldID = Utils.intToBytesLittle(0);
		byte[] lobbyName = Utils.buildByteArrayFromString("CY00000000-00", MediusConstants.WORLDNAME_MAXLEN.getValue());
		byte[] gameName = Utils.buildByteArrayFromString("", MediusConstants.WORLDNAME_MAXLEN.getValue());
		byte[] endOfList = Utils.hexStringToByteArray("01000000");

		logger.finest("Message ID: " + Utils.bytesToHex(messageID));
		logger.finest("Account ID: " + Utils.bytesToHex(accountID));
		logger.finest("AccountName: " + Utils.bytesToHex(accountName));
		logger.finest("ConnectState: " + Utils.bytesToHex(connectState));
		logger.finest("lobbyWorldID: " + Utils.bytesToHex(lobbyWorldID));
		logger.finest("gameWorldID: " + Utils.bytesToHex(gameWorldID));
		logger.finest("lobbyName: " + Utils.bytesToHex(lobbyName));
		logger.finest("gameName: " + Utils.bytesToHex(gameName));
		logger.finest("endOfList: " + Utils.bytesToHex(endOfList));
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
			outputStream.write(callbackStatus);
			outputStream.write(accountID);
			outputStream.write(accountName);
			outputStream.write(connectState);
			outputStream.write(lobbyWorldID);
			outputStream.write(gameWorldID);
			outputStream.write(lobbyName);
			outputStream.write(gameName);
			outputStream.write(endOfList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new MediusMessage(responseType, outputStream.toByteArray());
	}
	


}
