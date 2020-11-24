package net.hashsploit.clank.server.common.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class AccountUpdateStatsResponse extends MediusPacket {
	
	private byte[] messageID;
	private byte[] callbackStatus;

	public AccountUpdateStatsResponse(byte[] messageID, byte[] callbackStatus) {
		super(MediusPacketType.AccountUpdateStatsResponse);
		this.messageID = messageID;
		this.callbackStatus = callbackStatus;
	}
	
	@Override
	public byte[] getPayload() {
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));//padding
			outputStream.write(callbackStatus);// empty 7 byte array
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();

	}
	
	public String toString() {
		return "AccountUpdateStatsResponse: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus);
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

	
}
