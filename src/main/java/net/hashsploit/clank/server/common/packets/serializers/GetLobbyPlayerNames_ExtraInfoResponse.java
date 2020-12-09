package net.hashsploit.clank.server.common.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class GetLobbyPlayerNames_ExtraInfoResponse extends MediusMessage {

	byte[] messageID;
	byte[] callbackStatus;
	byte[] accountID;
	byte[] accountName;
	byte[] playerStatus;
	byte[] lobbyWorldID;
	byte[] gameWorldID;
	byte[] lobbyName;
	byte[] gameName;
	byte[] endOfList;

	public GetLobbyPlayerNames_ExtraInfoResponse(byte[] messageID, byte[] callbackStatus, byte[] accountID, byte[] accountName,
			byte[] playerStatus, byte[] lobbyWorldID, byte[] gameWorldID, byte[] lobbyName, byte[] gameName, byte[] endOfList) {
		super(MediusMessageType.GetLobbyPlayerNames_ExtraInfoResponse);
		
		this.messageID = messageID;
		this.callbackStatus = callbackStatus;
		this.accountID = accountID;
		this.accountName = accountName;
		this.playerStatus = playerStatus;
		this.lobbyWorldID = lobbyWorldID;
		this.gameWorldID = gameWorldID;
		this.lobbyName = lobbyName;
		this.gameName = gameName;
		this.endOfList = endOfList;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));//padding
			outputStream.write(callbackStatus);	
			outputStream.write(accountID);
			outputStream.write(accountName);
			outputStream.write(playerStatus);
			outputStream.write(lobbyWorldID);
			outputStream.write(gameWorldID);
			outputStream.write(lobbyName);
			outputStream.write(gameName);
			outputStream.write(endOfList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
	
	
	public String toString() {
		return "GetLobbyPlayerNames_ExtraInfoResponse: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus) + '\n' + 
				"accountID: " + Utils.bytesToHex(accountID) + '\n' + 
				"accountName: " + Utils.bytesToHex(accountName) + '\n' + 
				"playerStatus: " + Utils.bytesToHex(playerStatus) + '\n' + 
				"lobbyWorldID: " + Utils.bytesToHex(lobbyWorldID) + '\n' + 
				"gameWorldID: " + Utils.bytesToHex(gameWorldID) + '\n' + 
				"lobbyName: " + Utils.bytesToHex(lobbyName) + '\n' + 
				"gameName: " + Utils.bytesToHex(gameName) + '\n' + 
				"endOfList: " + Utils.bytesToHex(endOfList);
	}
	
	
	
}
