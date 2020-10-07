package net.hashsploit.clank.server.medius.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class GameWorldPlayerListResponse extends MediusPacket {

	private byte[] messageID;
	private byte[] callbackStatus;
	private byte[] accountID;
	private byte[] accountName;
	private byte[] stats;
	private byte[] connectionClass;
	private byte[] endOfList;
	
	public GameWorldPlayerListResponse(byte[] messageID, byte[] callbackStatus, byte[] accountID, byte[] accountName, byte[] stats, byte[] connectionClass, byte[] endOfList) {
		super(MediusPacketType.GameWorldPlayerListResponse);
		this.messageID = messageID;
		this.callbackStatus = callbackStatus;
		this.accountID = accountID;
		this.accountName = accountName;
		this.stats = stats;
		this.connectionClass = connectionClass;
		this.endOfList = endOfList;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
			outputStream.write(callbackStatus);	
			outputStream.write(accountID);
			outputStream.write(accountName);
			outputStream.write(stats);
			outputStream.write(connectionClass);
			outputStream.write(endOfList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
	
	public String toString() {
		return "GameWorldPlayerListResponse: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus) + '\n' + 
				"accountID: " + Utils.bytesToHex(accountID) + '\n' + 
				"accountName: " + Utils.bytesToHex(accountName) + '\n' + 
				"stats: " + Utils.bytesToHex(stats) + '\n' + 
				"connectionClass: " + Utils.bytesToHex(connectionClass) + '\n' + 
				"endOfList: " + Utils.bytesToHex(endOfList);
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

	public synchronized byte[] getAccountID() {
		return accountID;
	}

	public synchronized void setAccountID(byte[] accountID) {
		this.accountID = accountID;
	}

	public synchronized byte[] getAccountName() {
		return accountName;
	}

	public synchronized void setAccountName(byte[] accountName) {
		this.accountName = accountName;
	}

	public synchronized byte[] getStats() {
		return stats;
	}

	public synchronized void setStats(byte[] stats) {
		this.stats = stats;
	}

	public synchronized byte[] getConnectionClass() {
		return connectionClass;
	}

	public synchronized void setConnectionClass(byte[] connectionClass) {
		this.connectionClass = connectionClass;
	}

	public synchronized byte[] getEndOfList() {
		return endOfList;
	}

	public synchronized void setEndOfList(byte[] endOfList) {
		this.endOfList = endOfList;
	}


	
	
}
