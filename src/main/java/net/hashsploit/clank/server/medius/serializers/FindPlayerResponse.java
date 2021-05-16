package net.hashsploit.clank.server.medius.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class FindPlayerResponse extends MediusMessage {

	private byte[] messageID;
	private byte[] callbackStatus;
	private byte[] applicatonId;
	private byte[] applicationName;
	private byte[] applicationType;
	private byte[] worldId;
	private byte[] accountId;
	private byte[] accountName;
	private byte[] endOfList;
	
	public FindPlayerResponse(byte[] messageID, byte[] callbackStatus, byte[] applicatonId,
			byte[] applicationName, byte[] applicationType, byte[] worldId,
			byte[] accountId, byte[] accountName, byte[] endOfList) {
		super(MediusMessageType.FindPlayerResponse);
		
		this.messageID = messageID;
		this.callbackStatus = callbackStatus;
		this.applicatonId = applicatonId;
		this.applicationName = applicationName;
		this.applicationType = applicationType;
		this.worldId = worldId;
		this.accountId = accountId;
		this.accountName = accountName;
		this.endOfList = endOfList;
	}
	
	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(callbackStatus); 
			outputStream.write(applicatonId); 
			outputStream.write(applicationName); 
			outputStream.write(applicationType); 
			outputStream.write(worldId); 
			outputStream.write(accountId); 
			outputStream.write(accountName); 
			outputStream.write(endOfList); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
	
	
	public String toString() {
		return "FindPlayerResponse: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus) + '\n' + 
				"applicatonId: " + Utils.bytesToHex(applicatonId) + '\n' + 
				"applicationName: " + Utils.bytesToHex(applicationName) + '\n' + 
				"applicationType: " + Utils.bytesToHex(applicationType) + '\n' + 
				"worldId: " + Utils.bytesToHex(worldId) + '\n' + 
				"accountId: " + Utils.bytesToHex(accountId) + '\n' + 
				"accountId: " + Utils.bytesToHex(accountId) + '\n' + 
				"accountName: " + Utils.bytesToHex(accountName) + '\n' + 
				"endOfList: " + Utils.bytesToHex(endOfList);
	}

}
