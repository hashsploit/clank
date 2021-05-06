package net.hashsploit.clank.server.medius.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusChatMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class ChatFwdMessageResponse extends MediusMessage {

	private final byte[] messageID;
	private final byte[] accountName;
	private final byte[] message;
	private final int sourceAccountId;
	private final MediusChatMessageType messageType;

	public ChatFwdMessageResponse(byte[] messageID, int sourceAccountId, byte[] accountName, byte[] message, MediusChatMessageType messageType) {
		super(MediusMessageType.ChatFwdMessage);
		this.messageID = messageID;
		this.sourceAccountId = sourceAccountId;
		this.accountName = accountName;
		this.message = message;
		this.messageType = messageType;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
			outputStream.write(Utils.intToBytesLittle(sourceAccountId));
			outputStream.write(accountName); // MediusConstants.USERNAME_MAXLEN; (32)
			outputStream.write(Utils.intToBytesLittle(messageType.getValue()));
			outputStream.write(message); // 64 len
		} catch (IOException e) {
			e.printStackTrace();
		}

		return outputStream.toByteArray();
	}

	@Override
	public String toString() {
		return "ChatFwdMessageResponse: \n" + "messageID: " + Utils.bytesToHex(messageID) + '\n' + "accountName: " + Utils.bytesToHex(accountName) + '\n' + "sourceAccountId: " + sourceAccountId + '\n' + "message: " + Utils.bytesToHex(message);
	}

}
