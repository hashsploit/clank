package net.hashsploit.clank.server.medius.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class AccountGetIdResponse extends MediusMessage {

	private byte[] messageID;
	private byte[] accountID;
	private byte[] callbackStatus;
	
	public AccountGetIdResponse(byte[] messageID, byte[] accountID, byte[] callbackStatus) {
		super(MediusMessageType.AccountGetIdResponse);
		
		this.messageID = messageID;
		this.accountID = accountID;
		this.callbackStatus = callbackStatus;
	}
	
	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(accountID); 
			outputStream.write(callbackStatus); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
	
	
	public String toString() {
		return "AccountGetIDResponse: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"accountID: " + Utils.bytesToHex(accountID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus);
	}

}
