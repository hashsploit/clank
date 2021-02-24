package net.hashsploit.clank.server.medius.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class PlayerInfoResponse extends MediusMessage {

	private byte[] messageID;
	private byte[] callbackStatus;
	private byte[] appID;
	private byte[] accountName;
	private byte[] playerStatus;
	private byte[] connectionClass;
	private byte[] stats;
	

	public PlayerInfoResponse(byte[] messageID, byte[] callbackStatus, byte[] appID, byte[] accountName, byte[] playerStatus, byte[] connectionClass, byte[] stats) {
		super(MediusMessageType.PlayerInfoResponse);
		this.messageID = messageID;
		this.callbackStatus = callbackStatus;
		this.appID = appID;
		this.accountName = accountName;
		this.playerStatus = playerStatus;
		this.connectionClass = connectionClass;
		this.stats = stats;
	}
	
	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(callbackStatus);			
			outputStream.write(accountName);
			outputStream.write(appID);
			outputStream.write(playerStatus);
			outputStream.write(connectionClass);
			outputStream.write(stats);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
	
	
	public String toString() {
		return "PlayerInfoResponse: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus) + '\n' + 
				"accountName: " + Utils.bytesToHex(accountName) + '\n' + 
				"appID: " + Utils.bytesToHex(appID) + '\n' + 
				"playerStatus: " + Utils.bytesToHex(playerStatus) + '\n' + 
				"connectionClass: " + Utils.bytesToHex(connectionClass) + '\n' + 
				"stats: " + Utils.bytesToHex(stats);
	}

	public synchronized byte[] getAccountName() {
		return accountName;
	}

	public synchronized void setAccountName(byte[] accountName) {
		this.accountName = accountName;
	}

	public synchronized byte[] getPlayerStatus() {
		return playerStatus;
	}

	public synchronized void setPlayerStatus(byte[] playerStatus) {
		this.playerStatus = playerStatus;
	}

	public synchronized byte[] getConnectionClass() {
		return connectionClass;
	}

	public synchronized void setConnectionClass(byte[] connectionClass) {
		this.connectionClass = connectionClass;
	}
	
	
}
