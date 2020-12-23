package net.hashsploit.clank.server.medius.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class ServerSessionBeginResponse extends MediusMessage {

	private byte[] messageId;
	private byte confirmation;
	private byte[] connectInfo;
	

	public ServerSessionBeginResponse(byte[] messageId, byte confirmation, byte[] connectInfo) {
		super(MediusMessageType.MediusServerSessionBeginResponse);
		this.messageId = messageId;
		this.confirmation = confirmation;
		this.connectInfo = connectInfo;
	}
	
	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			outputStream.write(messageId);
			outputStream.write(confirmation);
			outputStream.write(Utils.hexStringToByteArray("0000"));
			outputStream.write(connectInfo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
	
	@Override
	public String toString() {
		return "ServerSessionBeginResponse: \n" +
			"messageId: " + Utils.bytesToHex(messageId) + '\n' +
			"confirmation: " + Utils.byteToHex(confirmation) + '\n' +
			"connectInfo: " + Utils.bytesToHex(connectInfo);
	}

	public byte[] getMessageId() {
		return messageId;
	}

	public byte getConfirmation() {
		return confirmation;
	}

	public byte[] getConnectInfo() {
		return connectInfo;
	}
	
}
