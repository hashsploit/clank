package net.hashsploit.clank.server.common.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class ChannelInfoResponse extends MediusPacket {

	private byte[] messageID;
	private byte[] callbackStatus;
	private byte[] lobbyName;
	private byte[] activePlayerCount;
	private byte[] maxPlayers;
	
	public ChannelInfoResponse(byte[] messageID, byte[] callbackStatus, byte[] lobbyName, byte[] activePlayerCount, byte[] maxPlayers) {
		super(MediusPacketType.ChannelInfoResponse);
		this.messageID = messageID;
		this.callbackStatus = callbackStatus;
		this.lobbyName = lobbyName;
		this.activePlayerCount = activePlayerCount;
		this.maxPlayers = maxPlayers;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
			outputStream.write(callbackStatus);
			outputStream.write(lobbyName);
			outputStream.write(activePlayerCount);
			outputStream.write(maxPlayers);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
	
	
	public String toString() {
		return "ChannelInfoResponse: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus) + '\n' + 
				"lobbyName: " + Utils.bytesToHex(lobbyName) + '\n' + 
				"activePlayerCount: " + Utils.bytesToHex(activePlayerCount) + '\n' + 
				"maxPlayers: " + Utils.bytesToHex(maxPlayers);
	}
	

	public synchronized byte[] getMessageID() {
		return messageID;
	}

	public synchronized void setMessageID(byte[] messageID) {
		this.messageID = messageID;
	}

	public synchronized byte[] getCallbackStatus() {
		return callbackStatus;
	}

	public synchronized void setCallbackStatus(byte[] callbackStatus) {
		this.callbackStatus = callbackStatus;
	}

	public synchronized byte[] getLobbyName() {
		return lobbyName;
	}

	public synchronized void setLobbyName(byte[] lobbyName) {
		this.lobbyName = lobbyName;
	}

	public synchronized byte[] getActivePlayerCount() {
		return activePlayerCount;
	}

	public synchronized void setActivePlayerCount(byte[] activePlayerCount) {
		this.activePlayerCount = activePlayerCount;
	}

	public synchronized byte[] getMaxPlayers() {
		return maxPlayers;
	}

	public synchronized void setMaxPlayers(byte[] maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
}
