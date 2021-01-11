package net.hashsploit.clank.server.medius.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class SetLocalizationParamsResponse extends MediusMessage {

	private byte[] messageId;
	private MediusCallbackStatus callbackStatus;
	

	public SetLocalizationParamsResponse(byte[] messageId, MediusCallbackStatus callbackStatus) {
		super(MediusMessageType.SetLocalizationParamsResponse);
		this.messageId = messageId;
		this.callbackStatus = callbackStatus;
	}
	
	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			outputStream.write(messageId);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(Utils.intToBytesLittle(callbackStatus.getValue()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
	
	// @Override
	// public String toString() {
	// 	return "ServerSessionBeginResponse: \n" +
	// 		"messageId: " + Utils.bytesToHex(messageId) + '\n' +
	// 		"callbackStatus: " + Utils.bytesToHex(callbackStatus.getValue());
	// }

	// public byte[] getMessageId() {
	// 	return messageId;
	// }

	// public byte getCallbackStatus() {
	// 	return callbackStatus.getValue();
	// }

	
}
