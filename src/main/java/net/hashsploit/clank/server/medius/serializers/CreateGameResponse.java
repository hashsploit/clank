package net.hashsploit.clank.server.medius.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class CreateGameResponse extends MediusMessage {

	private byte[] messageID;
	private byte[] callbackStatus;
	private byte[] newWorldID;
	
	public CreateGameResponse(byte[] messageID, byte[] callbackStatus, byte[] newWorldID) {
		super(MediusMessageType.CreateGameResponse);
		this.messageID = messageID;
		this.callbackStatus = callbackStatus;
		this.newWorldID = newWorldID;
	}
	
	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
			outputStream.write(callbackStatus);	
			outputStream.write(newWorldID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
	
	
	public String toString() {
		return "CreateGameResponse: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus) + '\n' + 
				"newWorldID: " + Utils.bytesToHex(newWorldID);
	}
	
	
	
}
