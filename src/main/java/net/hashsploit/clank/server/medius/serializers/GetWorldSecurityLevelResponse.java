package net.hashsploit.clank.server.medius.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class GetWorldSecurityLevelResponse extends MediusMessage {

	private byte[] messageID;
	private byte[] callbackStatus;
	private byte[] worldId;
	private byte[] appType;
	private byte[] securityLevel;
	
	public GetWorldSecurityLevelResponse(byte[] messageID, byte[] callbackStatus, byte[] worldId, byte[] appType, byte[] securityLevel) {
		super(MediusMessageType.GetWorldSecurityLevelResponse);
		this.messageID = messageID;
		this.callbackStatus = callbackStatus;
		this.worldId = worldId;
		this.appType = appType;
		this.securityLevel = securityLevel;
	}
	
	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(callbackStatus); 
			outputStream.write(worldId); 
			outputStream.write(appType); 
			outputStream.write(securityLevel); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
	
	
	public String toString() {
		return "GetWorldSecurityLevelResponse: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus) + '\n' + 
				"worldId: " + Utils.bytesToHex(worldId) + '\n' + 
				"appType: " + Utils.bytesToHex(appType) + '\n' + 
				"securityLevel: " + Utils.bytesToHex(securityLevel);
	}

	
	
}
